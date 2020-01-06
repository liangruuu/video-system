package cn.edu.zucc.vo;


/**
 * 返回观众与视频的关系的实体
 *
 * @author liangruuu
 */
public class UserVideo {
  private boolean userLikeVideo;
  private boolean userCollectVideo;
  private boolean userFollowed;

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

  public boolean isUserFollowed() {
    return userFollowed;
  }

  public void setUserFollowed(boolean userFollowed) {
    this.userFollowed = userFollowed;
  }
}
