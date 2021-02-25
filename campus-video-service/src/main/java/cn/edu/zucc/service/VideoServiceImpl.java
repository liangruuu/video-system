package cn.edu.zucc.service;

import cn.edu.zucc.mapper.*;
import cn.edu.zucc.pojo.*;
import cn.edu.zucc.req.ModifyReq;
import cn.edu.zucc.req.VideoReq;
import cn.edu.zucc.utils.*;
import cn.edu.zucc.vo.CommentsVO;
import cn.edu.zucc.vo.VideosVO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.*;

/**
 * @author liangruuu
 */
@Service
public class VideoServiceImpl implements VideoService {

  @Autowired
  private VideosMapper videosMapper;
  @Autowired
  private MyVideosMapper myVideosMapper;
  @Autowired
  private SearchRecordsMapper searchRecordsMapper;
  @Autowired
  private UsersLikeVideosMapper usersLikeVideosMapper;
  @Autowired
  private UsersCollectVideosMapper usersCollectVideosMapper;
  @Autowired
  private UsersMapper usersMapper;
  @Autowired
  private ReportVideosMapper reportVideosMapper;
  @Autowired
  private CommentsMapper commentsMapper;
  @Autowired
  private MyCommentsMapper myCommentsMapper;
  @Autowired
  private RatingMapper ratingMapper;
  @Autowired
  private CategoryMapper categoryMapper;
  @Autowired
  private RestHighLevelClient highLevelClient;

  private Sid sid = new Sid();

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public String saveVideo(Videos video) {
    // 对video生成唯一主键
//    String id = sid.nextShort();
    String id = IdUtils.createUid();
    video.setId(id);
    videosMapper.insertSelective(video);
    return id;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void updateVideoCover(String videoId, String coverPath) {
    Videos video = new Videos();
    video.setId(videoId);
    video.setCoverPath(coverPath);
    videosMapper.updateByPrimaryKeySelective(video);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public PageResult getVideoList(VideoReq videoReq, Integer isSaveRecord, Integer page, Integer pageSize) {

    // 若isSaveRecord不为空且为1则保存热搜词
    if (isSaveRecord != null && isSaveRecord == 1) {
      SearchRecords records = new SearchRecords();
//      String recordId = sid.nextShort();
      String recordId = IdUtils.createUid();
      records.setId(recordId);
      records.setContent(videoReq.getKeyword());
      searchRecordsMapper.insert(records);
    }

    PageHelper.startPage(page, pageSize);
    List<VideosVO> list = myVideosMapper.
        queryVideoList(videoReq.getKeyword(), videoReq.getUserId(), videoReq.getCategoryId(), videoReq.getTags());
    PageInfo<VideosVO> pageList = new PageInfo<>(list);
    PageResult pageResult = new PageResult();
    pageResult.setPage(page);
    pageResult.setTotal(pageList.getPages());
    pageResult.setRows(list);
    pageResult.setRecords(pageList.getTotal());

    return pageResult;
  }

  @Override
  public PageResult getVideoListES(VideoReq videoReq, Integer isSaveRecord, Integer page, Integer pageSize) throws IOException {
    String userId = videoReq.getUserId();
    String tags = videoReq.getTags();
    String keyword = videoReq.getKeyword();
    Integer categoryId = videoReq.getCategoryId();

    Map<String, Object> result = new HashMap<>();

    // 若isSaveRecord不为空且为1则保存热搜词
    if (isSaveRecord != null && isSaveRecord == 1 && !StringUtils.isBlank(keyword)) {
      SearchRecords records = new SearchRecords();
//      String recordId = sid.nextShort();
      String recordId = IdUtils.createUid();
      records.setId(recordId);
      records.setContent(keyword);
      searchRecordsMapper.insert(records);
    }

    /**
     * 第三版本
     */
    JSONObject jsonRequestObj = new JSONObject();

    jsonRequestObj.put("_source", "*");

    // 创建分页参数
    jsonRequestObj.put("from", (page - 1) * pageSize);
    jsonRequestObj.put("size", pageSize);

    // 构建query
    Map<String, Object> cixingMap = analyzeCategoryKeyword(keyword);
    // 适用词性分析，影响召回策略
    boolean isAffectFilter = true;
    boolean isAffectOrder = true;
    jsonRequestObj.put("query", new JSONObject());
    // 构建fuction_score
    jsonRequestObj.getJSONObject("query").put("function_score", new JSONObject());
    // 构建fuction_score里的query
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("query", new JSONObject());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").put("bool", new JSONObject());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query")
        .getJSONObject("bool").put("must", new JSONArray());

    int queryIndex = 0;
    int filterQueryIndex = 0;

    //    "bool": {
    //      "should": [
    //      {"match": {"video_name": {"query": "","boost": 0.1}}},
    //      {"term": {"category_id": {"value": "4","boost": 0}}},
    //      {"term": {"tags": {"value": "历史"}}}
    //        ]
    //    }
    if (cixingMap.keySet().size() > 0 && isAffectFilter) {
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query")
          .getJSONObject("bool").getJSONArray("must").add(new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).put("bool", new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").put("should", new JSONArray());

      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").add(new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .put("match", new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .getJSONObject("match").put("video_name", new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .getJSONObject("match").getJSONObject("video_name").put("query", keyword);
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .getJSONObject("match").getJSONObject("video_name").put("boost", 0.1);

      for (String key : cixingMap.keySet()) {
        filterQueryIndex++;
        Integer cixingCategoryId = (Integer) cixingMap.get(key);
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
            .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").add(new JSONObject());

        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
            .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
            .put("term", new JSONObject());

        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
            .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
            .getJSONObject("term").put("category_id", new JSONObject());

        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
            .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
            .getJSONObject("term").getJSONObject("category_id").put("value", cixingCategoryId);

        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
            .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
            .getJSONObject("term").getJSONObject("category_id").put("boost", 0);
      }
      queryIndex++;

      filterQueryIndex++;

    } else if (keyword != "") {
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query")
          .getJSONObject("bool").getJSONArray("must").add(new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).put("bool", new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").put("should", new JSONArray());

      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").add(new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .put("match", new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .getJSONObject("match").put("video_name", new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .getJSONObject("match").getJSONObject("video_name").put("query", keyword);
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .getJSONObject("match").getJSONObject("video_name").put("boost", 0.1);

      filterQueryIndex++;
      // 构建tag部分
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").add(new JSONObject());

      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .put("term", new JSONObject());

      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .getJSONObject("term").put("tags", new JSONObject());

      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
          .getJSONObject("term").getJSONObject("tags").put("value", tags);


//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query")
//          .getJSONObject("bool").getJSONArray("must").add(new JSONObject());
//
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
//          .getJSONArray("must").getJSONObject(queryIndex).put("match", new JSONObject());
//
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
//          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("match").put("video_name", new JSONObject());
//
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
//          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("match").getJSONObject("video_name").put("query", keyword);
//
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
//          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("match").getJSONObject("video_name").put("boost", 0.1);

      queryIndex++;
    }

    // 构建tag部分
//    if (tags != "") {
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query")
//          .getJSONObject("bool").getJSONArray("must").add(new JSONObject());
//
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
//          .getJSONArray("must").getJSONObject(queryIndex).put("bool", new JSONObject());
//
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
//          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").put("should", new JSONArray());
//
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
//          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").add(new JSONObject());
//
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
//          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
//          .put("term", new JSONObject());
//
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
//          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
//          .getJSONObject("term").put("tags", new JSONObject());
//
//      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
//          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
//          .getJSONObject("term").getJSONObject("tags").put("value", tags);
//    }

    // 构建第二个query条件
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
        .getJSONArray("must").add(new JSONObject());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
        .getJSONArray("must").getJSONObject(queryIndex).put("term", new JSONObject());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
        .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("term").put("status", 1);

    // 构建第三个query条件
    if (categoryId != null) {
      queryIndex++;
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").add(new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).put("term", new JSONObject());
      jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
          .getJSONArray("must").getJSONObject(queryIndex).getJSONObject("term").put("category_id", categoryId);
    }


    // 构建functions部分
    int functionIndex = 0;
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("functions", new JSONArray());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("field_value_factor", new JSONObject());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
        .put("field", "like_counts");
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
        .put("modifier", "log2p");
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
        .put("factor", 0.2);
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight", 1);

    functionIndex++;
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("field_value_factor", new JSONObject());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
        .put("field", "collect_counts");
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
        .put("modifier", "log2p");
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
        .put("factor", 0.2);
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight", 0.5);

    functionIndex++;
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("field_value_factor", new JSONObject());
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
        .put("field", "collect_counts");
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
        .put("modifier", "log2p");
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
        .put("factor", 0.2);
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight", 0.2);

    if (cixingMap.keySet().size() > 0 && isAffectOrder) {
      for (String key : cixingMap.keySet()) {
        functionIndex++;
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("filter", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("filter")
            .put("term", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("filter")
            .getJSONObject("term").put("category_id", cixingMap.get(key));
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight", 0.2);
      }
    }

    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("score_mode", "sum");
    jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("boost_mode", "sum");


    //排序字段
    jsonRequestObj.put("sort", new JSONArray());
    jsonRequestObj.getJSONArray("sort").add(new JSONObject());
    jsonRequestObj.getJSONArray("sort").getJSONObject(0).put("_score", new JSONObject());
    jsonRequestObj.getJSONArray("sort").getJSONObject(0).getJSONObject("_score").put("order", "desc");

    //聚合字段
    jsonRequestObj.put("aggs", new JSONObject());
    jsonRequestObj.getJSONObject("aggs").put("group_by_tags", new JSONObject());
    jsonRequestObj.getJSONObject("aggs").getJSONObject("group_by_tags").put("terms", new JSONObject());
    jsonRequestObj.getJSONObject("aggs").getJSONObject("group_by_tags").getJSONObject("terms").put("field", "tags");

    Request request = new Request("GET", "/video/_search");
    String reqJson = jsonRequestObj.toJSONString();
    request.setJsonEntity(reqJson);
    Response response = highLevelClient.getLowLevelClient().performRequest(request);
    String responseStr = EntityUtils.toString(response.getEntity());
    System.out.println(responseStr);
    JSONObject jsonObject = JSONObject.parseObject(responseStr);
    JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
    int totalRecordCounts = jsonObject.getJSONObject("hits").getJSONObject("total").getInteger("value");
    List<VideosVO> videoList = new ArrayList<>();
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject jsonObj = jsonArray.getJSONObject(i);
      String id = jsonObj.get("_id").toString();
      VideosVO video = myVideosMapper.queryVideoListES(id);
      videoList.add(video);
    }

    PageResult pageResult = new PageResult();
    pageResult.setTotal((totalRecordCounts + pageSize - 1) / pageSize);
    pageResult.setRows(videoList);
    pageResult.setRecords(totalRecordCounts);
    pageResult.setPage(page);

    /**
     * 返回tags列表
     */
//    List<Map> tagsList = new ArrayList<>();
//    JSONArray tagsJsonArray = jsonObject.getJSONObject("aggregations").getJSONObject("group_by_tags").getJSONArray("buckets");
//    for(int i = 0; i < tagsJsonArray.size();i++){
//      JSONObject jsonObj = tagsJsonArray.getJSONObject(i);
//      Map<String,Object> tagMap = new HashMap<>();
//      tagMap.put("tags",jsonObj.getString("key"));
//      tagMap.put("num",jsonObj.getInteger("doc_count"));
//      tagsList.add(tagMap);
//    }
//
//    result.put("tagList",tagsList);

//    result.put("videoList",videoList);

    return pageResult;
  }

  //构造分词函数识别器
  private Map<String, Object> analyzeCategoryKeyword(String keyword) throws IOException {
    Map<String, Object> res = new HashMap<>();

    if (keyword == null) {
      return res;
    }

    // 先获取分词结果
    //    {
    //      "tokens" : [
    //      {
    //        "token" : "梦幻岛",
    //          "start_offset" : 0,
    //          "end_offset" : 3,
    //          "type" : "CN_WORD",
    //          "position" : 0
    //      },
    //      {
    //        "token" : "梦幻",
    //          "start_offset" : 0,
    //          "end_offset" : 2,
    //          "type" : "CN_WORD",
    //          "position" : 1
    //      }
    //  ]
    //    }
    Request request = new Request("GET", "/video/_analyze");
    request.setJsonEntity("{" + "  \"field\": \"video_name\"," + "  \"text\":\"" + keyword + "\"\n" + "}");
    Response response = highLevelClient.getLowLevelClient().performRequest(request);
    String responseStr = EntityUtils.toString(response.getEntity());
    JSONObject jsonObject = JSONObject.parseObject(responseStr);
    JSONArray jsonArray = jsonObject.getJSONArray("tokens");
    for (int i = 0; i < jsonArray.size(); i++) {
      String token = jsonArray.getJSONObject(i).getString("token");
      Integer categoryId = getCategoryIdByToken(token);
      if (categoryId != null) {
        res.put(token, categoryId);
      }
    }
    return res;
  }

  /**
   * 比如token为计算机的话，返回key值就是categoryId=1
   *
   * @param token
   * @return
   */
  private Integer getCategoryIdByToken(String token) {
    Map<Integer, List<String>> categoryWorkMap = CiXingConstruct.getCategoryWorkMap();
    for (Integer key : categoryWorkMap.keySet()) {
      List<String> tokenList = categoryWorkMap.get(key);
      if (tokenList.contains(token)) {
        return key;
      }
    }
    return null;
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public List<String> getHotRecords() {
    return searchRecordsMapper.getHotRecords();
  }


  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void userLikeVideo(String userId, String videoId, String createUserId) {
    // 1. 保存用户和视频的点赞关联表
//    String likeId = sid.nextShort();
    String likeId = IdUtils.createUid();
    UsersLikeVideos ulv = new UsersLikeVideos();
    ulv.setId(likeId);
    ulv.setUserId(userId);
    ulv.setVideoId(videoId);
    usersLikeVideosMapper.insert(ulv);

    // 2. 视频点赞数+1
    myVideosMapper.addVideoLikeCount(videoId);

    // 3. 用户受喜欢数量的累加
    usersMapper.addSumLikeCounts(createUserId);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void userUnLikeVideo(String userId, String videoId, String createUserId) {
    // 1. 删除用户和视频的点赞关联表
    Example example = new Example(UsersLikeVideos.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo("userId", userId);
    criteria.andEqualTo("videoId", videoId);
    usersLikeVideosMapper.deleteByExample(example);

    // 2. 视频点赞数-1
    myVideosMapper.reduceVideoLikeCount(videoId);

    // 3. 用户受喜欢数量的累减
    usersMapper.reduceSumLikeCounts(createUserId);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void userCollectVideo(String userId, String videoId, String createUserId) {
    // 1. 保存用户和视频的收藏关联表
//    String likeId = sid.nextShort();
    String likeId = IdUtils.createUid();
    UsersCollectVideos ucv = new UsersCollectVideos();
    ucv.setId(likeId);
    ucv.setUserId(userId);
    ucv.setVideoId(videoId);
    usersCollectVideosMapper.insert(ucv);

    // 2. 视频收藏数+1
    myVideosMapper.addVideoCollectCount(videoId);

    // 3. 用户视频被收藏数的累加
    usersMapper.addSumCollectCounts(createUserId);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void userUnCollectVideo(String userId, String videoId, String createUserId) {
    // 1. 删除用户和视频的收藏关联表
    Example example = new Example(UsersCollectVideos.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo("userId", userId);
    criteria.andEqualTo("videoId", videoId);
    usersCollectVideosMapper.deleteByExample(example);
    // 2. 视频点赞数-1
    myVideosMapper.reduceVideoCollectCount(videoId);

    // 3. 用户受喜欢数量的累减
    usersMapper.reduceSumCollectCounts(createUserId);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public boolean isUserLikeVideo(String userId, String videoId) {
    Example example = new Example(UsersLikeVideos.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo("userId", userId);
    criteria.andEqualTo("videoId", videoId);

    List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);
    return list != null && list.size() > 0;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public boolean isUserCollectVideo(String userId, String videoId) {
    Example example = new Example(UsersCollectVideos.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo("userId", userId);
    criteria.andEqualTo("videoId", videoId);

    List<UsersCollectVideos> list = usersCollectVideosMapper.selectByExample(example);
    return list != null && list.size() > 0;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public PageResult queryMyCollectVideos(String userId, Integer page, Integer pageSize) {
    PageHelper.startPage(page, pageSize);
    List<VideosVO> list = myVideosMapper.queryCollectVideos(userId);
    PageInfo<VideosVO> pageList = new PageInfo<>(list);

    PageResult pageResult = new PageResult();
    pageResult.setTotal(pageList.getPages());
    pageResult.setRows(list);
    pageResult.setRecords(pageList.getTotal());
    pageResult.setPage(page);

    return pageResult;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void addVideoBrowseCounts(String videoId) {
    myVideosMapper.addVideoBrowseCounts(videoId);
  }

  @Override
  public void reportVideo(ReportVideos reportVideo) {
//    String rvId = sid.nextShort();
    String rvId = IdUtils.createUid();
    reportVideo.setId(rvId);
    reportVideo.setCreateTime(new Date());

    reportVideosMapper.insert(reportVideo);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void saveComment(Comments comment) {
//    String cid = sid.nextShort();
    String cid = IdUtils.createUid();
    comment.setId(cid);
    comment.setCreateTime(new Date());

    commentsMapper.insert(comment);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public PageResult getCommentList(String videoId, Integer page, Integer pageSize) {
    PageHelper.startPage(page, pageSize);

    List<CommentsVO> list = myCommentsMapper.queryComment(videoId);

    for (CommentsVO c : list) {
      String timeAgoStr = TimeAgoUtils.format(c.getCreateTime());
      c.setTimeAgoStr(timeAgoStr);
    }
    PageInfo<CommentsVO> pageList = new PageInfo<>(list);

    PageResult cid = new PageResult();
    cid.setTotal(pageList.getPages());
    cid.setPage(page);
    cid.setRecords(pageList.getTotal());
    cid.setRows(list);

    return cid;
  }

  @Override
  public int saveOrUpdaterRating(String userId, String videoId, Integer rating) {
    return ratingMapper.saveOrUpdaterRating(userId, videoId, rating);
  }

  @Override
  public int getRateValue(String userId, String videoId) {
    int result = 0;
    Rating rating = ratingMapper.getRateValue(userId, videoId);
    if (rating != null) {
      result = rating.getRating();
    }
    return result;
  }

  @Override
  public List<Category> getCategoryList() {
    return categoryMapper.selectAll();
  }

  @Override
  public int changeTag(String videoId, String tagListStr) {
    Videos video = new Videos();
    video.setId(videoId);
    video.setTags(tagListStr);
    return videosMapper.updateByPrimaryKeySelective(video);
  }

  @Override
  public int deleteVideo(String videoId) {
    Videos video = videosMapper.selectByPrimaryKey(videoId);
    if (video == null) {
      return -1;
    }
    return myVideosMapper.deleteVideo(videoId);
  }

  @Override
  public Videos updateVideo(ModifyReq modifyReq) {
    if (myVideosMapper.selectByPrimaryKey(modifyReq.getId()) == null) {
      return null;
    }
    myVideosMapper.updateVideo(modifyReq);
    return myVideosMapper.selectByPrimaryKey(modifyReq.getId());
  }
}
