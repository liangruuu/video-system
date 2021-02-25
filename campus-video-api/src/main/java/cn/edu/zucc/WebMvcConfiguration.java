package cn.edu.zucc;

import cn.edu.zucc.intercepetor.MyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liangruuu
 */
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

  /**
   * 初始化zookeeper客户端
   *
   * @return
   */
  @Bean(initMethod = "init")
  public ZKCuratorClient zkCuratorClient() {
    return new ZKCuratorClient();
  }

  /**
   * 以Bean的形式注册Intercepetor
   *
   * @return
   */
  @Bean
  public MyInterceptor myInterceptor() {
    return new MyInterceptor();
  }

  /**
   * 以Bean的形式注册Intercepetor
   *
   * @param registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(myInterceptor()).addPathPatterns("/video/upload");
  }
}
