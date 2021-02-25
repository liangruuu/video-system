package cn.edu.zucc.req;

/**
 * @PackageName cn.edu.zucc.req
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/18 23:28
 **/
public class ModifyReq {
  private String id;
  private String videoName;
  private String videoDesc;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getVideoName() {
    return videoName;
  }

  public void setVideoName(String videoName) {
    this.videoName = videoName;
  }

  public String getVideoDesc() {
    return videoDesc;
  }

  public void setVideoDesc(String videoDesc) {
    this.videoDesc = videoDesc;
  }
}
