package cn.edu.zucc.mapper;

import cn.edu.zucc.pojo.Videos;
import cn.edu.zucc.req.ModifyReq;
import cn.edu.zucc.utils.MyMapper;
import cn.edu.zucc.vo.VideosVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author liangruuu
 */
public interface MyVideosMapper extends MyMapper<Videos> {

  /**
   * @return List<Videos>
   */
  public List<VideosVO> queryVideoList(@Param("keyword") String keyword,
                                       @Param("userId") String userId,
                                       @Param("categoryId") Integer categoryId,
                                       @Param("tags") String tags);

  /**
   * 获取推荐视频列表
   *
   * @param videoId
   * @return
   */
  public VideosVO queryRecommendVideos(@Param("videoId") String videoId);

  /**
   * 对视频喜欢的数量进行累加
   *
   * @param videoId
   */
  public void addVideoLikeCount(@Param("videoId") String videoId);

  /**
   * 对视频喜欢的数量进行累减
   *
   * @param videoId
   */
  public void reduceVideoLikeCount(@Param("videoId") String videoId);

  /**
   * 对视频收藏的数量进行累加
   *
   * @param videoId
   */
  public void addVideoCollectCount(@Param("videoId") String videoId);

  /**
   * 对视频收藏的数量进行累减
   *
   * @param videoId
   */
  public void reduceVideoCollectCount(@Param("videoId") String videoId);

  /**
   * 查询用户收藏的视频
   *
   * @return List<Videos>
   */
  public List<VideosVO> queryCollectVideos(@Param("userId") String userId);

  /**
   * 增加视频点击量
   *
   * @param videoId
   */
  public void addVideoBrowseCounts(@Param("videoId") String videoId);

  List<Map<String, Object>> buildESQuery(@Param("videoId") String videoId, @Param("categoryId") Integer categoryId);


  /**
   * @return List<Videos>
   */
  public VideosVO queryVideoListES(@Param("videoId") String videoId);

  /**
   * 删除视频，把视频状态置为3
   *
   * @param videoId
   * @return
   */
  public int deleteVideo(@Param("videoId") String videoId);

  /**
   * 更新视频
   * @param modifyReq
   * @return
   */
  public int updateVideo(ModifyReq modifyReq);

}