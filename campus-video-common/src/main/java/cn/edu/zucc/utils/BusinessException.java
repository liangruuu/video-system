package cn.edu.zucc.utils;


public class BusinessException extends Exception {
  private CommonErr commonErr;

  public BusinessException(EmBusinessError emBusinessError) {
    super();
    this.commonErr = new CommonErr(emBusinessError);
  }

  public CommonErr getCommonErr() {
    return commonErr;
  }

  public void setCommonErr(CommonErr commonErr) {
    this.commonErr = commonErr;
  }
}
