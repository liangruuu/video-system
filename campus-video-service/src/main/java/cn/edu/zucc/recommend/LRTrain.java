package cn.edu.zucc.recommend;

import cn.edu.zucc.utils.FileUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.collection.JavaConverters;
import scala.collection.Seq;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @PackageName cn.edu.zucc.Training
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/6 8:39
 **/
public class LRTrain {
  public static void main(String[] args) throws IOException {
    String TRAIN_FILE_PATH = "E:/code/GraduationProject/code/traindata/lr";

    //初始化spark运行环境
    SparkSession spark = SparkSession.builder().master("local").appName("CampusVideo").getOrCreate();

    List<String> trainFiles = FileUtils.getFiles(TRAIN_FILE_PATH);

//    //加载特征及label训练文件
//    JavaRDD<String> csvFile = spark.read().textFile("E:/code/GraduationProject/code/feature.csv").toJavaRDD();
    //加载特征及label训练文件

    JavaRDD<String> csvFile = spark.read().
        textFile(JavaConverters.asScalaIteratorConverter(trainFiles.iterator()).asScala().toSeq()).toJavaRDD();

    //做转化
    JavaRDD<Row> rowJavaRDD = csvFile.map(new Function<String, Row>() {
      @Override
      public Row call(String v1) throws Exception {
        v1 = v1.replace("\"", "");
        String[] strArr = v1.split(",");
        return RowFactory.create(new Double(strArr[5]), Vectors.dense(Double.valueOf(strArr[0]),
            Double.valueOf(strArr[1]), Double.valueOf(strArr[2]), Double.valueOf(strArr[3]), Double.valueOf(strArr[4])));
      }
    });
    StructType schema = new StructType(
        new StructField[]{
            new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
            new StructField("features", new VectorUDT(), false, Metadata.empty())
        }
    );

    Dataset<Row> data = spark.createDataFrame(rowJavaRDD, schema);

    //分开训练和测试集
    Dataset<Row>[] dataArr = data.randomSplit(new double[]{0.8, 0.2});
    Dataset<Row> trainData = dataArr[0];
    Dataset<Row> testData = dataArr[1];

    LogisticRegression lr = new LogisticRegression().
        setMaxIter(10).setRegParam(0.3).setFamily("multinomial");

    LogisticRegressionModel lrModel = lr.fit(trainData);

    File lrFile = new File("E:/code/GraduationProject/code/lrmodel");
    if (lrFile.exists()) {
      FileUtils.delFile(lrFile);
    }

    lrModel.save("E:/code/GraduationProject/code/lrmodel");

    //测试评估
    Dataset<Row> predictions = lrModel.transform(testData);

    //评价指标
    MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator();
    double accuracy = evaluator.setMetricName("accuracy").evaluate(predictions);

    System.out.println("auc=" + accuracy);

  }
}