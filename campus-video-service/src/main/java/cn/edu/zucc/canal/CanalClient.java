package cn.edu.zucc.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @PackageName cn.edu.zucc.canal
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/1 13:33
 **/
@Component
public class CanalClient implements DisposableBean {
  private CanalConnector canalConnector;

  @Bean
  public CanalConnector getCanalConnector() {

    canalConnector = CanalConnectors.newClusterConnector(Lists.newArrayList(
        new InetSocketAddress("192.168.31.233", 11111)), "example", "canal", "canal");

    canalConnector.connect();

    // 指定filter，格式(database).{table}
    // 订阅数据库中的所有表
    canalConnector.subscribe();
    // 回滚寻找上一次中断的位置
    canalConnector.rollback();

    return canalConnector;
  }

  @Override
  public void destroy() throws Exception {
    if(canalConnector != null){
      canalConnector.disconnect();
    }
  }
}
