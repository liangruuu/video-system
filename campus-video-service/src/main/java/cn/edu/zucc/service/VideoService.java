package cn.edu.zucc.service;

import cn.edu.zucc.pojo.ReportVideos;
import cn.edu.zucc.pojo.Videos;
import cn.edu.zucc.utils.PageResult;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @author liangruuu
 */
public interface VideoService {

  /**
   * 向数据库保存视频
   *
   * @param video
   * @return
   */
  public String saveVideo(Videos video);

  /**
   * 修改视频封面
   *
   * @param videoId
   * @param coverPath
   * @return
   */
  public void updateVideoCover(String videoId, String coverPath);

  /**
   * 分页获取视频列表
   *
   * @param page
   * @param pageSize
   * @return
   */
  public PageResult getVideoList(Videos video, Integer isSaveRecord, Integer page, Integer pageSize);

  /**
   * 获取热搜词列表
   *
   * @return
   */
  public List<String> getHotRecords();

  /**
   * 用户点赞视频
   *
   * @param userId
   * @param videoId
   * @param createUserId
   */
  public void userLikeVideo(String userId, String videoId, String createUserId);

  /**
   * 用户取消点赞
   *
   * @param userId
   * @param videoId
   * @param createUserId
   */
  public void userUnLikeVideo(String userId, String videoId, String createUserId);

  /**
   * 用户收藏视频
   *
   * @param userId
   * @param videoId
   * @param createUserId
   */
  public void userCollectVideo(String userId, String videoId, String createUserId);

  /**
   * 用户取消收藏
   *
   * @param userId
   * @param videoId
   * @param createUserId
   */
  public void userUnCollectVideo(String userId, String videoId, String createUserId);

  /**
   * 查看用户是否已经点赞视频
   * @param userId
   * @param videoId
   * @return
   */
  public boolean isUserLikeVideo(String userId, String videoId);

  /**
   * 查看用户是否已经收藏视频
   * @param userId
   * @param videoId
   * @return
   */
  public boolean isUserCollectVideo(String userId, String videoId);

  /**
   * 查看用户已收藏视频
   * @param userId
   * @param page
   * @param pageSize
   * @return
   */
  public PageResult queryMyCollectVideos(String userId, Integer page, Integer pageSize);

  /**
   * 增加视频浏览量
   * @param videoId
   */
  public void addVideoBrowseCounts(String videoId);

  /**
   * 举报视频
   * @param videoId
   */
  public void reportVideo(ReportVideos video);
}
