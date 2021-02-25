package cn.edu.zucc.vo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author liangruuu
 */
public class VideosVO {
  @Id
  private String id;

  private String userId;

  private String bgmId;

  private String videoName;

  private String videoDesc;

  private String videoPath;

  private Float videoSeconds;

  private Integer videoWeight;

  private Integer videoHeight;

  private String coverPath;

  private Long likeCounts;

  private Long browseCounts;

  private Long collectCounts;

  private Integer status;

  private Date createTime;

  private Date updateTime;

  private String avatar;
  private String nickname;

  private String tags;

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

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

  /**
   * @return user_id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * @param userId
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * @return bgm_id
   */
  public String getBgmId() {
    return bgmId;
  }

  /**
   * @param bgmId
   */
  public void setBgmId(String bgmId) {
    this.bgmId = bgmId;
  }

  /**
   * @return video_name
   */
  public String getVideoName() {
    return videoName;
  }

  /**
   * @param videoName
   */
  public void setVideoName(String videoName) {
    this.videoName = videoName;
  }

  /**
   * @return video_desc
   */
  public String getVideoDesc() {
    return videoDesc;
  }

  /**
   * @param videoDesc
   */
  public void setVideoDesc(String videoDesc) {
    this.videoDesc = videoDesc;
  }

  /**
   * @return video_path
   */
  public String getVideoPath() {
    return videoPath;
  }

  /**
   * @param videoPath
   */
  public void setVideoPath(String videoPath) {
    this.videoPath = videoPath;
  }

  /**
   * @return video_seconds
   */
  public Float getVideoSeconds() {
    return videoSeconds;
  }

  /**
   * @param videoSeconds
   */
  public void setVideoSeconds(Float videoSeconds) {
    this.videoSeconds = videoSeconds;
  }

  /**
   * @return video_weight
   */
  public Integer getVideoWeight() {
    return videoWeight;
  }

  /**
   * @param videoWeight
   */
  public void setVideoWeight(Integer videoWeight) {
    this.videoWeight = videoWeight;
  }

  /**
   * @return video_height
   */
  public Integer getVideoHeight() {
    return videoHeight;
  }

  /**
   * @param videoHeight
   */
  public void setVideoHeight(Integer videoHeight) {
    this.videoHeight = videoHeight;
  }

  /**
   * @return cover_path
   */
  public String getCoverPath() {
    return coverPath;
  }

  /**
   * @param coverPath
   */
  public void setCoverPath(String coverPath) {
    this.coverPath = coverPath;
  }

  /**
   * @return like_counts
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

  /**
   * @return browse_counts
   */
  public Long getBrowseCounts() {
    return browseCounts;
  }

  /**
   * @param browseCounts
   */
  public void setBrowseCounts(Long browseCounts) {
    this.browseCounts = browseCounts;
  }

  public Long getCollectCounts() {
    return collectCounts;
  }

  public void setCollectCounts(Long collectCounts) {
    this.collectCounts = collectCounts;
  }

  /**
   * 获取视频状态：
   * 1.发布成功
   * 2.禁止播放，管理员操作
   *
   * @return status - 视频状态：
   * 1.发布成功
   * 2.禁止播放，管理员操作
   */
  public Integer getStatus() {
    return status;
  }

  /**
   * 设置视频状态：
   * 1.发布成功
   * 2.禁止播放，管理员操作
   *
   * @param status 视频状态：
   *               1.发布成功
   *               2.禁止播放，管理员操作
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return create_time
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * @param createTime
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }
}