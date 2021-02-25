package cn.edu.zucc.utils;

import java.util.Random;

/**
 * @PackageName cn.edu.zucc.utils
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/6 16:33
 **/
public class IdUtils {
  public static final int MAX_LIMIT = 100000;

  public static String createUid() {
    Random r = new Random();
    return String.valueOf(r.nextInt(MAX_LIMIT));
  }
}
