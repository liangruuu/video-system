package cn.edu.zucc.service;

import cn.edu.zucc.recommend.VideoSortModel;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.SparkSession;
import scala.Serializable;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @PackageName cn.edu.zucc.service
 * @ProjectName campus-vedio
 * @Description spark的所用类都需要继承Serializable接口
 * @Author liangruuu
 * @Date 2020/3/6 14:15
 **/
public class RecommendSortServiceImpl implements Serializable {
  private SparkSession spark;

  private LogisticRegressionModel lrModel;


  @PostConstruct
  public void init() {
    //加载LR模型
    spark = SparkSession.builder().master("local").appName("CampusVideo").getOrCreate();

    lrModel = LogisticRegressionModel.load("E:/code/GraduationProject/code/lrmode");
  }

  public List<Integer> sort(List<Integer> videoIdList, Integer userId) {
    //需要根据lrmode所需要5维的x，生成特征，然后调用其预测方法
    List<VideoSortModel> list = new ArrayList<>();
    for (Integer videoId : videoIdList) {
      //造的假数据，可以从数据库或缓存中拿到对应的性别，年龄，评分，价格等做特征转化生成feture向量
      Vector v = Vectors.dense(1, 0, 0, 0, 0.7, 1);
      Vector result = lrModel.predictProbability(v);
      double[] arr = result.toArray();
      double score = arr[1];
      VideoSortModel videoSortModel = new VideoSortModel();
      videoSortModel.setVideoId(videoId);
      videoSortModel.setScore(score);
      list.add(videoSortModel);
    }
    list.sort(new Comparator<VideoSortModel>() {
      @Override
      public int compare(VideoSortModel o1, VideoSortModel o2) {
        if (o1.getScore() < o2.getScore()) {
          return 1;
        } else if (o1.getScore() > o2.getScore()) {
          return -1;
        } else {
          return 0;
        }
      }
    });
    return list.stream().map(shopSortModel -> shopSortModel.getVideoId()).collect(Collectors.toList());
  }
}
