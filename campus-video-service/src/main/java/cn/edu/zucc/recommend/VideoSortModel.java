package cn.edu.zucc.recommend;

/**
 * @PackageName cn.edu.zucc.recommend
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/6 14:22
 **/
public class VideoSortModel {
  private Integer videoId;
  private double score;

  public Integer getVideoId() {
    return videoId;
  }

  public void setVideoId(Integer videoId) {
    this.videoId = videoId;
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }
}
