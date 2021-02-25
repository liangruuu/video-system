package cn.edu.zucc.service;

import cn.edu.zucc.pojo.Category;
import cn.edu.zucc.pojo.Comments;
import cn.edu.zucc.pojo.ReportVideos;
import cn.edu.zucc.pojo.Videos;
import cn.edu.zucc.req.ModifyReq;
import cn.edu.zucc.req.VideoReq;
import cn.edu.zucc.utils.PageResult;
import io.swagger.models.auth.In;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
  public PageResult getVideoList(VideoReq videoReq, Integer isSaveRecord, Integer page, Integer pageSize);

  /**
   * 使用ES获取视频列表
   * @param videoReq
   * @param isSaveRecord
   * @param page
   * @param pageSize
   * @return
   */
  public PageResult getVideoListES(VideoReq videoReq, Integer isSaveRecord, Integer page, Integer pageSize) throws IOException;

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
   * @param
   */
  public void reportVideo(ReportVideos video);

  /**
   * 保存评论
   * @param comment
   */
  public void saveComment(Comments comment);


  /**
   * 获取评论分页
   * @param videoId
   * @return
   */
  public PageResult getCommentList(String videoId, Integer page, Integer pageSize);

  /**
   * 更新或插入评分
   * @param userId
   * @param videoId
   * @param rating
   */
  public int saveOrUpdaterRating(String userId, String videoId, Integer rating);

  /**
   * 获取视频评分
   * @param userId
   * @param videoId
   * @return
   */
  public int getRateValue(String userId, String videoId);

  /**
   * 获取视频分类列表
   * @return
   */
  public List<Category> getCategoryList();

  /**
   * 改变视频标签列表
   * @return
   */
  public int changeTag(String videoId, String tagListStr);

  /**
   * 删除视频，把视频状态置为3
   * @param videoId
   * @return
   */
  public int deleteVideo(String videoId);

  /**
   * 更新视频信息
   * @param modifyReq
   * @return
   */
  public Videos updateVideo(ModifyReq modifyReq);
}
