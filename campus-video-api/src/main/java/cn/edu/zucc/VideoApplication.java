package cn.edu.zucc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author liangruuu
 */
@SpringBootApplication
@MapperScan("cn.edu.zucc.mapper")
@ComponentScan(basePackages = {"cn.edu.zucc", "org.n3r.idworker"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class VideoApplication {
  public static void main(String[] args) {
    SpringApplication.run(VideoApplication.class, args);
  }
}
