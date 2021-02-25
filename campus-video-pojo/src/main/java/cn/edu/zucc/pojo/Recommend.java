package cn.edu.zucc.pojo;

import javax.persistence.Id;

/**
 * @PackageName cn.edu.zucc.pojo
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/6 13:58
 **/
public class Recommend {
  @Id
  private String id;

  private String recommend;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRecommend() {
    return recommend;
  }

  public void setRecommend(String recommend) {
    this.recommend = recommend;
  }
}
