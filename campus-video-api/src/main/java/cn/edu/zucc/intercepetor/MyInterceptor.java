package cn.edu.zucc.intercepetor;

import cn.edu.zucc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liangruuu
 */
public class MyInterceptor implements HandlerInterceptor {

  @Autowired
  public RedisOperator redis;
  public static final String USER_REDIS_SESSION = "user-redis-session";

  /**
   * 拦截请求，在Controller调用之前
   *
   * @param request
   * @param response
   * @param handler
   * @return
   * @throws Exception
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    /**
     * 返回false，请求被拦截
     * true，请求OK
     */
    String userId = request.getHeader("userId");
    String userToken = request.getHeader("userToken");
    if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {
      System.out.println("请求拦截...");
      String uniqueToken = redis.get(USER_REDIS_SESSION + ":" + userId);
      if (StringUtils.isBlank(uniqueToken) && StringUtils.isEmpty(uniqueToken)) {
        System.out.println("Token过期,请先登录");
      } else {
        if (!uniqueToken.equals(userToken)) {
          System.out.println("账号在别的手机上登录...");
          return false;
        }
      }
    } else {
      System.out.println("请先登录");
      return false;
    }
    return true;
  }


  /**
   * 请求Controller之前,渲染视图之前
   *
   * @param request
   * @param response
   * @param handler
   * @param modelAndView
   * @throws Exception
   */
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

  }

  /**
   * @param request
   * @param response
   * @param handler
   * @param ex
   * @throws Exception
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

  }
}
