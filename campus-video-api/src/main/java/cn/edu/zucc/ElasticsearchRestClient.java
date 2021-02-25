package cn.edu.zucc;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @PackageName cn.edu.zucc
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/2/25 12:39
 **/
@Configuration
public class ElasticsearchRestClient {
  @Value("${elasticsearch.ip}")
  String ipAddress;

  @Bean(name = "highLevelClient")
  public RestHighLevelClient highLevelClient() {
    String[] address = ipAddress.split(":");
    String ip = address[0];
    Integer port = Integer.valueOf(address[1]);
    HttpHost httpHost = new HttpHost(ip, port, "http");

    return new RestHighLevelClient(RestClient.builder(new HttpHost[]{httpHost}));
  }
}
