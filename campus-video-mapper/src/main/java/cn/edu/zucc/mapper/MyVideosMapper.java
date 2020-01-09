package cn.edu.zucc.mapper;

import cn.edu.zucc.pojo.Videos;
import cn.edu.zucc.utils.MyMapper;
import cn.edu.zucc.vo.VideosVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author liangruuu
 */
public interface MyVideosMapper extends MyMapper<Videos> {

  /**
   * @return List<Videos>
   */
  public List<VideosVO> queryVideoList(@Param("videoName") String videoName, @Param("userId") String userId);

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
   * @return List<Videos>
   */
  public List<VideosVO> queryCollectVideos(@Param("userId") String userId);

  /**
   * 增加视频点击量
   * @param videoId
   */
  public void addVideoBrowseCounts(@Param("videoId") String videoId);
}