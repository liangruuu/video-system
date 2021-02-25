package cn.edu.zucc.utils;

import cn.edu.zucc.utils.em.EmBusinessError;

public class CommonErr {

  // 错误码
  private Integer errCode;

  // 错误描述
  private String errMsg;

  public CommonErr(Integer errCode, String errMsg) {
    this.errCode = errCode;
    this.errMsg = errMsg;
  }

  public CommonErr(EmBusinessError emBusinessError){
    this.errCode = emBusinessError.getErrCode();
    this.errMsg = emBusinessError.getErrMsg();
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
