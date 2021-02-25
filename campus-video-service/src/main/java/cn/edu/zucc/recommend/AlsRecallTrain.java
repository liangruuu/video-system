package cn.edu.zucc.recommend;

import cn.edu.zucc.utils.FileUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

//ALS召回算法的训练
@Component
public class AlsRecallTrain implements Serializable {

  public void trainAlsModel() throws IOException {
    String TRAIN_FILE_PATH = "E:/code/GraduationProject/code/traindata/als/behavior.csv";

    //初始化spark运行环境
    SparkSession spark = SparkSession.builder().master("local").appName("CampusVideo").getOrCreate();

    // 在这个RDD内定义了由换行符分割的list string，每个list元素就是csv文件的一行数据
    JavaRDD<String> csvFile = spark.read().textFile(TRAIN_FILE_PATH).toJavaRDD();

    JavaRDD<Rating> ratingJavaRDD = csvFile.map(new Function<String, Rating>() {
      @Override
      public Rating call(String v1) throws Exception {
        return Rating.parseRating(v1);
      }
    });

    // Dataset等同于mysql中的一张表，结构遵从Rating类
    Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD, Rating.class);

    //将所有的rating数据分成82份
    Dataset<Row>[] splits = rating.randomSplit(new double[]{0.8, 0.2});

    Dataset<Row> trainingData = splits[0];
    Dataset<Row> testingData = splits[1];


    //过拟合：增大数据规模，减少RANK,增大正则化的系数
    //欠拟合：增加rank，减少正则化系数
    ALS als = new ALS().setMaxIter(10).setRank(5).setRegParam(0.01).
        setUserCol("userId").setItemCol("videoId").setRatingCol("rating");

    //模型训练
    ALSModel alsModel = als.fit(trainingData);

    //模型评测
    Dataset<Row> predictions = alsModel.transform(testingData);

    //rmse 均方根误差，预测值与真实值的偏差的平方除以观测次数，开个根号
    RegressionEvaluator evaluator = new RegressionEvaluator().setMetricName("rmse")
        .setLabelCol("rating").setPredictionCol("prediction");
    double rmse = evaluator.evaluate(predictions);
    System.out.println("rmse = " + rmse);

    File alsFile = new File("E:/code/GraduationProject/code/alsmodel");
    if (alsFile.exists()) {
      FileUtils.delFile(alsFile);
    }

    alsModel.save("E:/code/GraduationProject/code/alsmodel");
  }

  public static void main(String[] args) throws IOException {
    String TRAIN_FILE_PATH = "E:/code/GraduationProject/code/traindata/als/behavior.csv";

    //初始化spark运行环境
    SparkSession spark = SparkSession.builder().master("local").appName("CampusVideo").getOrCreate();

    // 在这个RDD内定义了由换行符分割的list string，每个list元素就是csv文件的一行数据
    JavaRDD<String> csvFile = spark.read().textFile(TRAIN_FILE_PATH).toJavaRDD();

    JavaRDD<Rating> ratingJavaRDD = csvFile.map(new Function<String, Rating>() {
      @Override
      public Rating call(String v1) throws Exception {
        return Rating.parseRating(v1);
      }
    });

    // Dataset等同于mysql中的一张表，结构遵从Rating类
    Dataset<Row> rating = spark.createDataFrame(ratingJavaRDD, Rating.class);

    //将所有的rating数据分成82份
    Dataset<Row>[] splits = rating.randomSplit(new double[]{0.8, 0.2});

    Dataset<Row> trainingData = splits[0];
    Dataset<Row> testingData = splits[1];


    //过拟合：增大数据规模，减少RANK,增大正则化的系数
    //欠拟合：增加rank，减少正则化系数
    ALS als = new ALS().setMaxIter(10).setRank(5).setRegParam(0.01).
        setUserCol("userId").setItemCol("videoId").setRatingCol("rating");

    //模型训练
    ALSModel alsModel = als.fit(trainingData);

    //模型评测
    Dataset<Row> predictions = alsModel.transform(testingData);

    //rmse 均方根误差，预测值与真实值的偏差的平方除以观测次数，开个根号
    RegressionEvaluator evaluator = new RegressionEvaluator().setMetricName("rmse")
        .setLabelCol("rating").setPredictionCol("prediction");
    double rmse = evaluator.evaluate(predictions);
    System.out.println("rmse = " + rmse);

    File alsFile = new File("E:/code/GraduationProject/code/alsmodel");
    if (alsFile.exists()) {
      FileUtils.delFile(alsFile);
    }

    alsModel.save("E:/code/GraduationProject/code/alsmodel");
  }
}

