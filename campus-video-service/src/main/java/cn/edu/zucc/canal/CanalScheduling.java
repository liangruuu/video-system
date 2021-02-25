package cn.edu.zucc.canal;

import cn.edu.zucc.mapper.MyVideosMapper;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PackageName cn.edu.zucc.canal
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/1 15:00
 **/
@Component
public class CanalScheduling implements Runnable, ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Resource
  private CanalConnector canalConnector;

  @Autowired
  private MyVideosMapper videosMapper;

  @Autowired
  private RestHighLevelClient restHighLevelClient;

  @Override
  // 消息消费，每隔100ms开启线程监听数据库binlog的数据是否变化
  @Scheduled(fixedDelay = 100)
  public void run() {
    // 批次Id
    long batchId = -1;
    try {
      // 一次拉取1000条数据
      int batchSize = 1000;
      // 从canal拿取1000条消息之后不需要返回ack确认帧，自己自定义消息处理方式
      Message message = canalConnector.getWithoutAck(batchSize);

      batchId = message.getId();
      // 若数据库中字段条目发生改变，则entries就是发生改变的数据，size就是有多少条数据发生变化
      List<CanalEntry.Entry> entries = message.getEntries();
      if (batchId != -1 && entries.size() > 0) {
        entries.forEach(entry -> {
          // binlog的消息是以rowdata形式传递出来的话就解析处理这条消息
          if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
            publishCanalEvent(entry);
          }
        });
      }
      // 自定义处理完成之后就返回ack帧说明消息已被处理
      canalConnector.ack(batchId);
    } catch (Exception e) {
      e.printStackTrace();
      canalConnector.rollback(batchId);
    }
  }

  /**
   * 解析处理函数
   *
   * @param entry
   */
  private void publishCanalEvent(CanalEntry.Entry entry) {
    // EventType是个枚举类，定义了数据库中的增删改查等操作
    CanalEntry.EventType eventType = entry.getHeader().getEventType();
    // 明确数据库，对应项目中的campus_video
    String database = entry.getHeader().getSchemaName();
    // 明确数据表，对应项目中的videos和category表
    String table = entry.getHeader().getTableName();
    // 这条记录发生的变化，因为一个sql语句可能影响多条数据，比如不带where条件，所以是一个list
    CanalEntry.RowChange change = null;
    try {
      change = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
      return;
    }
    change.getRowDatasList().forEach(rowData -> {
      // Column表示表中的某个字段，比如id，name字段
      List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
      String primaryKey = "id";
      CanalEntry.Column idColumn = columns.stream().filter(column -> column.getIsKey()
          && primaryKey.equals(column.getName())).findFirst().orElse(null);

      Map<String, Object> dataMap = parseColumnsToMap(columns);
      try {
        indexES(dataMap, database, table);
      } catch (IOException e) {
        e.printStackTrace();
      }

    });
  }

  Map<String, Object> parseColumnsToMap(List<CanalEntry.Column> columns) {
    Map<String, Object> jsonMap = new HashMap<>();
    columns.forEach(column -> {
      if (column == null) {
        return;
      }
      jsonMap.put(column.getName(), column.getValue());
    });
    return jsonMap;
  }

  private void indexES(Map<String, Object> dataMap, String database, String table) throws IOException {
    System.out.println(dataMap + database + table);

    if (!StringUtils.equals("campus_video", database)) {
      return;
    }
    List<Map<String, Object>> result = new ArrayList<>();
    // 根据发生变更的表来判断查询条件
    if (StringUtils.equals("videos", table)) {
      result = videosMapper.buildESQuery((String) dataMap.get("id"), null);
    } else if (StringUtils.equals("category", table)) {
      result = videosMapper.buildESQuery(null, new Integer((String) dataMap.get("id")));
    } else {
      return;
    }

    for (Map<String, Object> map : result) {
      IndexRequest indexRequest = new IndexRequest("video", "_doc");
      indexRequest.id(String.valueOf(map.get("id")));
      indexRequest.source(map);
      restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
