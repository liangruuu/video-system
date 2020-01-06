package cn.edu.zucc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;

@ApiModel(value = "用户对象")
public class UsersVO {
  @Id
  private String id;

  private String userToken;

  private String username;

  private String password;

  private String avatar;

  private String nickname;

  private Long fansCounts;

  private Long followCounts;

  private Long likeCounts;

  private Long collectCounts;

  private boolean followed;

  /**
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(String id) {
    this.id = id;
  }

  public String getUserToken() {
    return userToken;
  }

  public void setUserToken(String userToken) {
    this.userToken = userToken;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return avatar
   */
  public String getAvatar() {
    return avatar;
  }

  /**
   * @param avatar
   */
  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  /**
   * @return nickname
   */
  public String getNickname() {
    return nickname;
  }

  /**
   * @param nickname
   */
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  /**
   * @return fans_counts
   */
  public Long getFansCounts() {
    return fansCounts;
  }

  /**
   * @param fansCounts
   */
  public void setFansCounts(Long fansCounts) {
    this.fansCounts = fansCounts;
  }

  /**
   * @return follow_counts
   */
  public Long getFollowCounts() {
    return followCounts;
  }

  /**
   * @param followCounts
   */
  public void setFollowCounts(Long followCounts) {
    this.followCounts = followCounts;
  }

  /**
   * @return like
   */
  public Long getLikeCounts() {
    return likeCounts;
  }

  /**
   * @param likeCounts
   */
  public void setLikeCounts(Long likeCounts) {
    this.likeCounts = likeCounts;
  }

  public Long getCollectCounts() {
    return collectCounts;
  }

  public void setCollectCounts(Long collectCounts) {
    this.collectCounts = collectCounts;
  }

  public boolean isFollowed() {
    return followed;
  }

  public void setFollowed(boolean followed) {
    this.followed = followed;
  }
}