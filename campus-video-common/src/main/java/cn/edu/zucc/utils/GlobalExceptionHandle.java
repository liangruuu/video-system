package cn.edu.zucc.utils;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liangruuu
 */
@ControllerAdvice(basePackages = "cn.edu.zucc.controller")
public class GlobalExceptionHandle {

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public MyJSONResult doError(HttpServletRequest servletRequest,
                              HttpServletResponse servletResponse, Exception ex) {
    return MyJSONResult.create(((BusinessException) ex).getCommonErr(), "failed");
  }
}

//if (ex instanceof BusinessException){
//    return MyJSONResult.create(((BusinessException)ex).getCommonErr(),"failed");
//    }else{
//    CommonErr commonErr=new CommonErr(EmBusinessError.UNKNOWN_ERROR);
//    return MyJSONResult.create(commonErr,"failed");
//    }
