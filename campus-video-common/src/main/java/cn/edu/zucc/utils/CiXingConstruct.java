package cn.edu.zucc.utils;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PackageName cn.edu.zucc.utils
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/2/25 19:17
 **/
@Component
public class CiXingConstruct {
  private static Map<Integer, List<String>> categoryWorkMap = new HashMap<>();

  // 构造分词函数识别器
  @PostConstruct
  public void init() {
    categoryWorkMap.put(1, new ArrayList<>());
    categoryWorkMap.put(2, new ArrayList<>());

    categoryWorkMap.get(1).add("学校");
    categoryWorkMap.get(1).add("辅导班");
    categoryWorkMap.get(1).add("考研");

    categoryWorkMap.get(2).add("校园");
    categoryWorkMap.get(2).add("社团");
  }

  public static Map<Integer, List<String>> getCategoryWorkMap() {
    return categoryWorkMap;
  }
}
