package cn.edu.zucc.pojo;

import javax.persistence.Id;

/**
 * @PackageName cn.edu.zucc.pojo
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/2/24 16:53
 **/
public class Category {
  @Id
  private String id;

  private String name;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
