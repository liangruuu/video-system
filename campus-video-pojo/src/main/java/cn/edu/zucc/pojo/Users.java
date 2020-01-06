package cn.edu.zucc.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;

@ApiModel(value = "用户对象")
public class Users {
  @Id
  @ApiModelProperty(hidden = true)
  private String id;

  @ApiModelProperty(value = "用户名", name = "username", example = "liangruuu", required = true)
  private String username;

  @ApiModelProperty(value = "密码", name = "password", example = "123456", required = true)
  private String password;

  @ApiModelProperty(hidden = true)
  private String avatar;

  @ApiModelProperty(hidden = true)
  private String nickname;

  @ApiModelProperty(hidden = true)
  @Column(name = "fans_counts")
  private Long fansCounts;

  @ApiModelProperty(hidden = true)
  @Column(name = "follow_counts")
  private Long followCounts;

  @ApiModelProperty(hidden = true)
  @Column(name = "like_counts")
  private Long likeCounts;

  @ApiModelProperty(hidden = true)
  @Column(name = "like_counts")
  private Long collectCounts;

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
}