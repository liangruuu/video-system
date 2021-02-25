package cn.edu.zucc.pojo;

import javax.persistence.Id;

/**
 * @PackageName cn.edu.zucc.pojo
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/6 10:03
 **/
public class Feature {
  @Id
  private String id;

  private String userId;

  private String videoId;

  private String categoryId;

  private Integer score;

  private Integer collectStatus;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getVideoId() {
    return videoId;
  }

  public void setVideoId(String videoId) {
    this.videoId = videoId;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public Integer getCollectStatus() {
    return collectStatus;
  }

  public void setCollectStatus(Integer collectStatus) {
    this.collectStatus = collectStatus;
  }
}
