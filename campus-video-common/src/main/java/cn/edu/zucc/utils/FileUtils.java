package cn.edu.zucc.utils;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @PackageName cn.edu.zucc.utils
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/6 9:08
 **/
public class FileUtils {

  public static void delFile(File file) {
    File[] files = file.listFiles();
    if (files != null && files.length != 0) {
      for (int i = 0; i < files.length; i++) {
        delFile(files[i]);
      }
    }
    file.delete();
  }

  /**
   * 写入csv文件
   *
   * @param headers  列头
   * @param data     数据内容
   * @param filePath 创建的csv文件路径
   * @throws IOException
   **/
  public static <T> void writeCsv(String[] headers, List<String[]> data, String filePath) {
    String NEW_LINE_SEPARATOR = "\r\n";
    //初始化csvformat
    CSVFormat formator = CSVFormat.DEFAULT.withSkipHeaderRecord()
        .withDelimiter(',').withRecordSeparator(NEW_LINE_SEPARATOR);
    //创建FileWriter对象
    FileWriter fileWriter = null;
    CSVPrinter printer = null;
    try {
      fileWriter = new FileWriter(filePath);
      //创建CSVPrinter对象
      printer = new CSVPrinter(fileWriter, formator);
      //如果有头信息写入列头数据
      if (headers != null) {
        printer.printRecord(headers);
      }
      if (data != null) {
        //循环写入数据
        for (String[] lineData : data) {
          printer.printRecord(lineData);
        }
      }

      fileWriter.flush();
      fileWriter.close();
      printer.close();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      System.out.println("CSV文件创建成功,文件路径:" + filePath);
    }
  }

  /**
   * 读取csv文件
   *
   * @param filePath 文件路径
   * @param headers  csv列头
   * @return CSVRecord 列表
   * @throws IOException
   **/
  public static List<CSVRecord> readCsv(String filePath, String[] headers) throws IOException {

    //创建CSVFormat
    CSVFormat formator = CSVFormat.DEFAULT.withHeader(headers);

    FileReader fileReader = new FileReader(filePath);

    //创建CSVParser对象
    CSVParser parser = new CSVParser(fileReader, formator);

    List<CSVRecord> records = parser.getRecords();

    parser.close();
    fileReader.close();

    return records;
  }

  /**
   * 获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件，所以不用递归获取
   * @param path
   * @return
   */
  public static List<String> getFiles(String path) {
    List<String> files = new ArrayList<String>();
    File file = new File(path);
    File[] tempList = file.listFiles();

    for (int i = 0; i < tempList.length; i++) {
      if (tempList[i].isFile()) {
        files.add(tempList[i].toString());
        //文件名，不包含路径
        //String fileName = tempList[i].getName();
      }
      if (tempList[i].isDirectory()) {
        //这里就不递归了，
      }
    }
    return files;
  }

//  public static void main(String[] args) {
//    System.out.println(getFiles("E:/code/GraduationProject/code/traindata"));
//  }
}
