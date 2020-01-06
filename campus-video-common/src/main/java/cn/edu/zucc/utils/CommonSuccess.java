package cn.edu.zucc.utils;

/**
 * @author liangruuu
 */
public class CommonSuccess {

  private String msg;
  private Object obj;

  public CommonSuccess(String msg) {
    this.msg = msg;
  }

  public CommonSuccess(String msg, Object obj) {
    this.msg = msg;
    this.obj = obj;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Object getObj() {
    return obj;
  }

  public void setObj(Object obj) {
    this.obj = obj;
  }
}
