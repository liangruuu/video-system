package cn.edu.zucc.utils.em;

/**
 * @author liangruuu
 */
public enum EmBusinessError {

  // 通用的错误类型10000开头
  NO_OBJECT_FOUND(10001, "请求对象不存在"),
  OBJECT_IS_EXISTED(10002, "对象已经存在"),
  UNKNOWN_ERROR(10003, "未知错误"),
  UPLOAD_ERROR(10004, "上传出错"),
  PARAMS_ERROR(10005, "传递参数错误或者为空"),
  TOKEN_ERROR(10006, "Token为空或者出错");

  private Integer errCode;

  private String errMsg;

  EmBusinessError(Integer errCode, String errMsg) {
    this.errCode = errCode;
    this.errMsg = errMsg;
  }

  public Integer getErrCode() {
    return errCode;
  }

  public void setErrCode(Integer errCode) {
    this.errCode = errCode;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }
}
