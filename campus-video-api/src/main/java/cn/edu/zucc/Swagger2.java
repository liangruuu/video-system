package cn.edu.zucc;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
        .apis(RequestHandlerSelectors.basePackage("cn.edu.zucc.controller"))
        .paths(PathSelectors.any()).build();
  }

  public ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("使用Swagger2构建视频分享平台API")
        .contact(new Contact("liangruuu", "127.0.0.1:8081", "1024311844@qq.com"))
        .description("描述信息")
        .version("1.0.0").build();
  }
}
