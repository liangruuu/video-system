package cn.edu.zucc.task;

import cn.edu.zucc.mapper.CategoryMapper;
import cn.edu.zucc.mapper.FeatureMapper;
import cn.edu.zucc.mapper.RatingMapper;
import cn.edu.zucc.pojo.Feature;
import cn.edu.zucc.pojo.Rating;
import cn.edu.zucc.recommend.AlsRecallPredict;
import cn.edu.zucc.recommend.AlsRecallTrain;
import cn.edu.zucc.utils.FileUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @PackageName cn.edu.zucc.Task
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/6 10:08
 **/

@Component
public class TrainScheduling {
  @Autowired
  private FeatureMapper featureMapper;
  @Autowired
  private CategoryMapper categoryMapper;
  @Autowired
  private RatingMapper ratingMapper;
  @Autowired
  private AlsRecallTrain alsRecallTrain;
  @Autowired
  private AlsRecallPredict alsRecallPredict;

//  @Scheduled(fixedDelay = 3000)
//  public void outputCsvFile() throws IOException {
//    int categoryNums = categoryMapper.selectNums();
//    int oneCodelengh = categoryNums + 2;
//    List<Feature> featureList = featureMapper.selectAll();
//
//    String fileName = new String(
//        new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".csv");
//
//    String csvFilePath = "E:/code/GraduationProject/code/traindata/" + fileName;
//
//    List<String[]> data = new ArrayList<>();
//    for (Feature feature : featureList) {
//      String[] oneCode = new String[oneCodelengh];
//      int i;
//      for (i = 0; i < categoryNums; i++) {
//        int categoryId = Integer.parseInt(feature.getCategoryId());
//        if ((categoryId - 1) % categoryNums == i) {
//          oneCode[i] = "1";
//        } else {
//          oneCode[i] = "0";
//        }
//      }
//      DecimalFormat df = new DecimalFormat("0.0");
//      oneCode[i++] = df.format((float) feature.getScore() / 3);
//      oneCode[i] = String.valueOf(feature.getCollectStatus());
//      data.add(oneCode);
//    }
//    FileUtils.writeCsv(null, data, csvFilePath);
//  }

  @Scheduled(fixedDelay = 30000)
  public void outPutRatingCsvFile() {
    String csvFilePath = "E:/code/GraduationProject/code/traindata/als/behavior.csv";
    List<Rating> ratingList = ratingMapper.selectAll();
    List<String[]> data = new ArrayList<>();
    for (Rating rating : ratingList) {
      String[] item = new String[3];
      item[0] = rating.getUserId();
      item[1] = rating.getVideoId();
      item[2] = String.valueOf(rating.getRating());

      data.add(item);
    }
    FileUtils.writeCsv(null, data, csvFilePath);
    System.out.println("每分钟导出csv文件成功");
  }

//  @Scheduled(cron = "0 0 1 * * ?")
//  public void trainAlsModel() throws IOException {
//    alsRecallTrain.trainAlsModel();
//  }
//
//  @Scheduled(cron = "0 0 2 * * ?")
//  public void predictAlsData() throws IOException {
//    alsRecallPredict.predictAlsData();
//  }
}
