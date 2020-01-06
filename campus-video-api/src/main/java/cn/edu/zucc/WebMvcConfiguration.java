package cn.edu.zucc;

import cn.edu.zucc.intercepetor.MyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

  /**
   * 配置虚拟目录用来响应小程序对服务器资源的请求
   *
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/META-INF/resources/")
        .addResourceLocations("file:E:/code/GraduationProject/resources/");
  }

  // 以Bean的形式注册Intercepetor
  @Bean
  public MyInterceptor myInterceptor() {
    return new MyInterceptor();
  }

  // 把拦截器加载到拦截中心中
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 对"/user/"下的所有路径都进行拦截
    registry.addInterceptor(myInterceptor()).addPathPatterns("/user/query")
    .addPathPatterns("/video/upload");
  }
}
