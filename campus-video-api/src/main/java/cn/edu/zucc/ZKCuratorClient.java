package cn.edu.zucc;

import cn.edu.zucc.service.BgmService;
import cn.edu.zucc.utils.JsonUtils;
import cn.edu.zucc.utils.em.EmBgmOperatorType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Configuration
public class ZKCuratorClient {
  // zk客户端
  private CuratorFramework client = null;
  final static Logger log = LoggerFactory.getLogger(ZKCuratorClient.class);

  @Autowired
  private BgmService bgmService;

  private static final String ZOOKEEPER_SERVER = "101.133.167.46:2181";

  public void init() {
    if (client == null) {
      // 重连策略
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
      // 创建zk客户端
      client = CuratorFrameworkFactory.builder().connectString(ZOOKEEPER_SERVER)
          .sessionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("admin").build();
      // 启动客户端
      client.start();

      try {
        addChildWatch("/bgm");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void addChildWatch(String nodePath) throws Exception {
    final PathChildrenCache cache = new PathChildrenCache(client, nodePath, true);
    cache.start();
    cache.getListenable().addListener(new PathChildrenCacheListener() {
      @Override
      public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
          throws Exception {
        if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
          log.info("监听到事件CHILD_ADDED");

          // 1. 从数据库查询bgm对象，获取路径path
          String path = event.getData().getPath();
          String operatorObjStr = new String(event.getData().getData(), StandardCharsets.UTF_8);
          Map<String, String> map = JsonUtils.jsonToPojo(operatorObjStr, Map.class);
          String operatorType = map.get("operatorType");
          // 1.1 bgm所在的相对路径
          String bgmPath = map.get("path");

          // 2. 定义保存到本地的bgm路径
          String filePath = "E:\\code\\GraduationProject\\resources" + bgmPath;
          // 3. 定义下载的路径（播放url）
          String[] arrPath = bgmPath.split("\\\\");    // windows
          String finalPath = "";
          // 3.1 处理url的斜杠以及编码
          for (String s : arrPath) {
            if (StringUtils.isNotBlank(s)) {
              finalPath += "/";
              finalPath += URLEncoder.encode(s, "UTF-8");
            }
          }
          String bgmUrl = "http://192.168.43.253:2333/admin" + finalPath;

          if (operatorType.equals(EmBgmOperatorType.ADD.getType())) {
            // 下载bgm到spingboot服务器
            URL url = new URL(bgmUrl);
            File file = new File(filePath);
            FileUtils.copyURLToFile(url, file);
            client.delete().forPath(path);
          } else if (operatorType.equals(EmBgmOperatorType.DELETE.getType())) {
            File file = new File(filePath);
            FileUtils.forceDelete(file);
            client.delete().forPath(path);
          }
        }
      }
    });
  }
}
