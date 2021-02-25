package cn.edu.zucc.service;

import cn.edu.zucc.pojo.Users;
import cn.edu.zucc.pojo.UsersFans;
import cn.edu.zucc.utils.PageResult;

import java.util.List;

public interface UserService {

  /**
   * 判断用户名是否存在
   *
   * @param userName
   * @return
   */
  public boolean queryUsernameIsExist(String userName);

  /**
   * 保存用户信息(用于注册)
   *
   * @param user
   */
  public void saveUser(Users user);

  /**
   * 用户通过账号密码进行登录
   *
   * @param username
   * @param password
   * @return
   */
  public Users queryUserForLogin(String username, String password);

  /**
   * 用户信息修改
   *
   * @param user
   */
  public void updateUserInfo(Users user);

  /**
   * 查询用户信息
   * @param userId
   * @return
   */
  public Users queryUserInfo(String userId);

  /**
   * 增加用户和粉丝的关系
   * @param userId
   * @param fanId
   */
  public void saveUserFanRelation(String userId, String fanId);

  /**
   * 删除用户和粉丝的关系
   * @param userId
   * @param fanId
   */
  public void deleteUserFanRelation(String userId, String fanId);

  /**
   * 查询用户是否关注
   * @param userId
   * @param fanId
   * @return
   */
  public boolean queryIsFollow(String userId, String fanId);

  /**
   * 查询用户关注用户列表
   * @return
   */
  public PageResult showFollowUsers(String fanId, Integer page, Integer pageSize);

  /**
   * 获取关注人数
   * @param userId
   * @return
   */
  public int getFansNumber(String userId);
}
