package cn.edu.zucc.utils;

public class MyJSONResult {

  // 响应业务状态
  private String status;


  // 响应中的数据
  private Object data;

  public static MyJSONResult create(Object result) {
    return MyJSONResult.create(result, "success");
  }

  public static MyJSONResult create(Object result, String status) {
    MyJSONResult jsonResult = new MyJSONResult();
    jsonResult.setStatus(status);
    jsonResult.setData(result);

    return jsonResult;
  }

  private MyJSONResult() {
  }

  public String getStatus() {
    return status;
  }

  private void setStatus(String status) {
    this.status = status;
  }

  public Object getData() {
    return data;
  }

  private void setData(Object data) {
    this.data = data;
  }
}
