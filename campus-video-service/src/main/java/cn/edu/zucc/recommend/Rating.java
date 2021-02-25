package cn.edu.zucc.recommend;

import java.io.Serializable;

/**
 * @PackageName cn.edu.zucc.recommend
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/12 15:07
 **/

public class Rating implements Serializable {
  private int userId;
  private int videoId;
  private int rating;

  public static Rating parseRating(String str) {
    str = str.replace("\"", "");
    String[] strArr = str.split(",");
    int userId = Integer.parseInt(strArr[0]);
    int videoId = Integer.parseInt(strArr[1]);
    int rating = Integer.parseInt(strArr[2]);

    return new Rating(userId, videoId, rating);
  }

  public Rating(int userId, int videoId, int rating) {
    this.userId = userId;
    this.videoId = videoId;
    this.rating = rating;
  }

  public int getUserId() {
    return userId;
  }

  public int getVideoId() {
    return videoId;
  }

  public int getRating() {
    return rating;
  }
}
