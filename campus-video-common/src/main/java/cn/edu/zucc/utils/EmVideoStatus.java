package cn.edu.zucc.utils;

/**
 * @author liangruuu
 */
public enum EmVideoStatus {

  // 视频状态枚举类
  SUCCESS(1, "发布成功"),
  FORBID(2, "禁止播放, 管理员操作");

  private Integer statusCode;

  private String statusMsg;

  EmVideoStatus(Integer statusCode, String statusMsg) {
    this.statusCode = statusCode;
    this.statusMsg = statusMsg;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusMsg() {
    return statusMsg;
  }

  public void setStatusMsg(String statusMsg) {
    this.statusMsg = statusMsg;
  }}
