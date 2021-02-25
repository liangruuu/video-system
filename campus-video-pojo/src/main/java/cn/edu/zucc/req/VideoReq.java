package cn.edu.zucc.req;

/**
 * @PackageName cn.edu.zucc.req
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/2/25 13:42
 **/
public class VideoReq {
  private String userId;
  private String keyword;
  private String tags;
  private Integer categoryId;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

}
