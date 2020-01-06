package cn.edu.zucc.mapper;

import cn.edu.zucc.pojo.Users;
import cn.edu.zucc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author liangruuu
 */
public interface UsersMapper extends MyMapper<Users> {

  /**
   * 用户受喜欢数累加
   * @param userId
   */
  public void addSumLikeCounts(@Param("userId") String userId);

  /**
   * 用户受喜欢数累减
   * @param userId
   */
  public void reduceSumLikeCounts(@Param("userId") String userId);

  /**
   * 用户视频被收藏数累加
   * @param userId
   */
  public void addSumCollectCounts(@Param("userId") String userId);

  /**
   * 用户视频被收藏数累减
   * @param userId
   */
  public void reduceSumCollectCounts(@Param("userId") String userId);

  /**
   * 增加关注数
   * @param userId
   */
  public void addFansCounts(@Param("userId") String userId);

  /**
   * 减少关注数
   * @param userId
   */
  public void reduceFansCounts(@Param("userId") String userId);

  /**
   * 增加粉丝数
   * @param userId
   */
  public void addFollowCounts(@Param("userId") String userId);

  /**
   * 减少粉丝数
   * @param userId
   */
  public void reduceFollowCounts(@Param("userId") String userId);
}