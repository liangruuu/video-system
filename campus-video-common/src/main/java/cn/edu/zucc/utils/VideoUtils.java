package cn.edu.zucc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class VideoUtils {

  private String ffmpeg;
  private final String TEMP_FILE_PATH = "E:/code/GraduationProject/resources/temp.mp4";

  public VideoUtils(String ffmpeg) {
    this.ffmpeg = ffmpeg;
  }

  // 先清除音轨
  public void clearTrack(String videoInputPath) throws IOException {
    List<String> command = new ArrayList<>();
    command.add(ffmpeg);
    command.add("-y");
    command.add("-i");
    command.add(videoInputPath);
    command.add("-c:v");
    command.add("copy");
    command.add("-an");
    command.add(TEMP_FILE_PATH);

    ProcessBuilder builder = new ProcessBuilder(command);
    Process process = builder.start();

    InputStream errorStream = process.getErrorStream();
    InputStreamReader inputStream = new InputStreamReader(errorStream);
    BufferedReader reader = new BufferedReader(inputStream);

    String line = "";
    while ((line = reader.readLine()) != null) {
    }
    reader.close();
    errorStream.close();
    inputStream.close();
  }

  public void convert(String videoInputPath, String mp3inputPath, double seconds, String outputPath) throws Exception {
    // ffmpeg -i input.mp4 output.avi
    // 先清除音轨
    this.clearTrack(videoInputPath);

    List<String> command = new ArrayList<>();
    command.add(ffmpeg);
    // 再合并音频和视频
    command.add("-i");
    command.add(TEMP_FILE_PATH);

    command.add("-i");
    command.add(mp3inputPath);

    command.add("-t");
    command.add(String.valueOf(seconds));

    command.add("-y");
    command.add(outputPath);

    ProcessBuilder builder = new ProcessBuilder(command);
    Process process = builder.start();

    InputStream errorStream = process.getErrorStream();
    InputStreamReader inputStream = new InputStreamReader(errorStream);
    BufferedReader reader = new BufferedReader(inputStream);

    String line = "";
    while ((line = reader.readLine()) != null) {
    }
    reader.close();
    errorStream.close();
    inputStream.close();
  }

  public void fetchCover(String videoInputPath, String coverOutputPath) throws IOException {
    //ffmpeg.exe -ss 00:00:01 -y -i test.mp4 -vframes 1 new.jpg
    List<String> command = new ArrayList<>();
    command.add(ffmpeg);
    command.add("-ss");
    command.add("00:00:01");

    command.add("-y");
    command.add("-i");
    command.add(videoInputPath);

    command.add("-vframes");
    command.add("1");

    command.add(coverOutputPath);

    ProcessBuilder builder = new ProcessBuilder(command);
    Process process = builder.start();

    InputStream errorStream = process.getErrorStream();
    InputStreamReader inputStream = new InputStreamReader(errorStream);
    BufferedReader reader = new BufferedReader(inputStream);

    String line = "";
    while ((line = reader.readLine()) != null) {
    }
    reader.close();
    errorStream.close();
    inputStream.close();
  }


  public static void main(String[] args) {
//    VideoUtils videoUtils = new VideoUtils("E:/ffmpeg/ffmpeg/bin/ffmpeg.exe");
//    try {
//      videoUtils.convert("E:/ffmpeg/ffmpeg/bin/test.mp4", "E:/ffmpeg/ffmpeg/bin/You're Gonna Go Far, Kid (Clean Album Version).mp3", 4, "E:/ffmpeg/ffmpeg/bin/new.avi");
//      videoUtils.fetchCover("E:/ffmpeg/ffmpeg/bin/test.mp4", "E:/ffmpeg/ffmpeg/bin/new.jpg");
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
  }
}
