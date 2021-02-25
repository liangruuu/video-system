package cn.edu.zucc.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author liangruuu
 */
public class Videos {
  @Id
  private String id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "bgm_id")
  private String bgmId;

  @Column(name = "video_name")
  private String videoName;

  @Column(name = "video_desc")
  private String videoDesc;

  @Column(name = "video_path")
  private String videoPath;

  @Column(name = "video_seconds")
  private Float videoSeconds;

  @Column(name = "video_weight")
  private Integer videoWeight;

  @Column(name = "video_height")
  private Integer videoHeight;

  @Column(name = "cover_path")
  private String coverPath;

  @Column(name = "like_counts")
  private Long likeCounts;

  @Column(name = "browse_counts")
  private Long browseCounts;

  @Column(name = "collect_counts")
  private Long collectCounts;

  /**
   * 视频状态：
   * 1.发布成功
   * 2.禁止播放，管理员操作
   */
  private Integer status;

  @Column(name = "create_time")
  private Date createTime;

  @Column(name = "update_time")
  private Date updateTime;

  @Column(name = "category_id")
  private Integer categoryId;

  @Column(name = "tags")
  private String tags;

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

  public Long getCollectCounts() {
    return collectCounts;
  }

  public void setCollectCounts(Long collectCounts) {
    this.collectCounts = collectCounts;
  }

  /**
   * @param browseCounts
   */
  public void setBrowseCounts(Long browseCounts) {
    this.browseCounts = browseCounts;
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

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}