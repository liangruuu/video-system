package cn.edu.zucc.vo;


/**
 * 返回观众与视频的关系的实体
 *
 * @author liangruuu
 */
public class UserVideo {
  private boolean userLikeVideo;
  private boolean userCollectVideo;

  public boolean isUserLikeVideo() {
    return userLikeVideo;
  }

  public void setUserLikeVideo(boolean userLikeVideo) {
    this.userLikeVideo = userLikeVideo;
  }

  public boolean isUserCollectVideo() {
    return userCollectVideo;
  }

  public void setUserCollectVideo(boolean userCollectVideo) {
    this.userCollectVideo = userCollectVideo;
  }
}
