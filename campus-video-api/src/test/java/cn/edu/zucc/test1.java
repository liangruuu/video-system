package cn.edu.zucc;

import cn.edu.zucc.mapper.RatingMapper;
import cn.edu.zucc.pojo.Rating;
import cn.edu.zucc.utils.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @PackageName PACKAGE_NAME
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/19 18:44
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class test1 {
  @Autowired
  private RatingMapper ratingMapper;

  @Test
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
  }

}
