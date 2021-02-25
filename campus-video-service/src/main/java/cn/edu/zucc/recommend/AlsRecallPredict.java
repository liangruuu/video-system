package cn.edu.zucc.recommend;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @PackageName cn.edu.zucc.Training
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/3 20:35
 **/
@Component
public class AlsRecallPredict {

  public void predictAlsData() {
    String TRAIN_FILE_PATH = "E:/code/GraduationProject/code/traindata/als/behavior.csv";
    String TRAIN_MODEL_PATH = "E:/code/GraduationProject/code/alsmodel";

    //初始化spark运行环境
    SparkSession spark = SparkSession.builder().master("local").appName("CampusVideo").getOrCreate();

    //加载模型进内存
    ALSModel alsModel = ALSModel.load(TRAIN_MODEL_PATH);

    JavaRDD<String> csvFile = spark.read().textFile(TRAIN_FILE_PATH).toJavaRDD();
    JavaRDD<Rating> ratingJavaRDD = csvFile.map(new Function<String, Rating>() {
      @Override
      public Rating call(String v1) throws Exception {
        return Rating.parseRating(v1);
      }
    });

    // DataFrame类似于数据库中的一个表
    Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD, Rating.class);
    //给用户去重且做离线的召回结果预测
    Dataset<Row> users = rating.select(alsModel.getUserCol()).distinct();
    Dataset<Row> userRecs = alsModel.recommendForAllUsers(5);

    userRecs.foreachPartition(new ForeachPartitionFunction<Row>() {
      @Override
      public void call(Iterator<Row> t) throws Exception {

        //新建数据库链接
        Connection connection = DriverManager.
            getConnection("jdbc:mysql://192.168.31.233:3306/campus_video?" +
                "user=root&password=excellent199812&useUnicode=true&characterEncoding=UTF-8");
        PreparedStatement preparedStatement = connection.
            prepareStatement("replace into recommend(id,recommend)values(?,?)");
        List<Map<String, Object>> data = new ArrayList<>();

        t.forEachRemaining(action -> {
          int userId = action.getInt(0);
          List<GenericRowWithSchema> recommendationList = action.getList(1);
          List<Integer> videoIdList = new ArrayList<Integer>();
          recommendationList.forEach(row -> {
            Integer videoId = row.getInt(0);
            videoIdList.add(videoId);
          });
          String recommendData = StringUtils.join(videoIdList, ",");
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("userId", userId);
          map.put("recommend", recommendData);
          data.add(map);
        });

        data.forEach(stringObjectMap -> {
          try {
            preparedStatement.setInt(1, (Integer) stringObjectMap.get("userId"));
            preparedStatement.setString(2, (String) stringObjectMap.get("recommend"));

            preparedStatement.addBatch();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        });
        preparedStatement.executeBatch();
        connection.close();
      }
    });
  }

  public static void main(String[] args) {
    String TRAIN_FILE_PATH = "E:/code/GraduationProject/code/traindata/als/behavior.csv";
    String TRAIN_MODEL_PATH = "E:/code/GraduationProject/code/alsmodel";

    //初始化spark运行环境
    SparkSession spark = SparkSession.builder().master("local").appName("CampusVideo").getOrCreate();

    //加载模型进内存
    ALSModel alsModel = ALSModel.load(TRAIN_MODEL_PATH);

    JavaRDD<String> csvFile = spark.read().textFile(TRAIN_FILE_PATH).toJavaRDD();
    JavaRDD<Rating> ratingJavaRDD = csvFile.map(new Function<String, Rating>() {
      @Override
      public Rating call(String v1) throws Exception {
        return Rating.parseRating(v1);
      }
    });

    // DataFrame类似于数据库中的一个表
    Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD, Rating.class);
    //给用户去重且做离线的召回结果预测
    Dataset<Row> users = rating.select(alsModel.getUserCol()).distinct();
    Dataset<Row> userRecs = alsModel.recommendForAllUsers(5);

    userRecs.foreachPartition(new ForeachPartitionFunction<Row>() {
      @Override
      public void call(Iterator<Row> t) throws Exception {

        //新建数据库链接
        Connection connection = DriverManager.
            getConnection("jdbc:mysql://192.168.31.233:3306/campus_video?" +
                "user=root&password=excellent199812&useUnicode=true&characterEncoding=UTF-8");
        PreparedStatement preparedStatement = connection.
            prepareStatement("replace into recommend(id,recommend)values(?,?)");
        List<Map<String, Object>> data = new ArrayList<>();

        t.forEachRemaining(action -> {
          int userId = action.getInt(0);
          List<GenericRowWithSchema> recommendationList = action.getList(1);
          List<Integer> videoIdList = new ArrayList<Integer>();
          recommendationList.forEach(row -> {
            Integer videoId = row.getInt(0);
            videoIdList.add(videoId);
          });
          String recommendData = StringUtils.join(videoIdList, ",");
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("userId", userId);
          map.put("recommend", recommendData);
          data.add(map);
        });

        data.forEach(stringObjectMap -> {
          try {
            preparedStatement.setInt(1, (Integer) stringObjectMap.get("userId"));
            preparedStatement.setString(2, (String) stringObjectMap.get("recommend"));

            preparedStatement.addBatch();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        });
        preparedStatement.executeBatch();
        connection.close();
      }
    });
  }
}

