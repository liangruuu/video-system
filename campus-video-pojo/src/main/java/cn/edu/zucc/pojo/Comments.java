package cn.edu.zucc.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author liangruuu
 */
public class Comments {
  @Id
  private String id;

  @Column(name = "father_comment_id")
  private String fatherCommentId;

  @Column(name = "video_id")
  private String videoId;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "to_user_id")
  private String toUserId;

  @Column(name = "create_time")
  private Date createTime;

  private String comment;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFatherCommentId() {
    return fatherCommentId;
  }

  public void setFatherCommentId(String fatherCommentId) {
    this.fatherCommentId = fatherCommentId;
  }

  public String getVideoId() {
    return videoId;
  }

  public void setVideoId(String videoId) {
    this.videoId = videoId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getToUserId() {
    return toUserId;
  }

  public void setToUserId(String toUserId) {
    this.toUserId = toUserId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}