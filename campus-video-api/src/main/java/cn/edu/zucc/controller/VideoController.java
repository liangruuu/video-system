package cn.edu.zucc.controller;

import cn.edu.zucc.mapper.RecommendMapper;
import cn.edu.zucc.pojo.Bgm;
import cn.edu.zucc.pojo.Comments;
import cn.edu.zucc.pojo.ReportVideos;
import cn.edu.zucc.pojo.Videos;
import cn.edu.zucc.req.ModifyReq;
import cn.edu.zucc.req.VideoReq;
import cn.edu.zucc.service.BgmService;
import cn.edu.zucc.service.RecommendService;
import cn.edu.zucc.service.UserService;
import cn.edu.zucc.service.VideoService;
import cn.edu.zucc.utils.*;
import cn.edu.zucc.utils.em.EmBusinessError;
import cn.edu.zucc.utils.em.EmVideoStatus;
import cn.edu.zucc.vo.UserVideo;
import cn.edu.zucc.vo.VideosVO;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author liangruuu
 */
@Api(value = "视频相关业务的接口", tags = {"视频相关业务的controller"})
@RestController
@RequestMapping("/video")
public class VideoController extends BasicController {

  @Autowired
  private BgmService bgmService;
  @Autowired
  private VideoService videoService;
  @Autowired
  private UserService userService;
  @Autowired
  private RecommendService recommendService;

  private List<Object> returnList = null;

  @ApiOperation(value = "上传视频", notes = "上传视频的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "videoName", value = "视频名称", required = true, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "videoSeconds", value = "视频播放长度", required = true, dataType = "Double", paramType = "form"),
      @ApiImplicitParam(name = "videoWidth", value = "视频长度", required = true, dataType = "Integer", paramType = "form"),
      @ApiImplicitParam(name = "videoHeight", value = "视频宽度", required = true, dataType = "Integer", paramType = "form"),
      @ApiImplicitParam(name = "desc", value = "视频描述", required = false, dataType = "String", paramType = "form")
  })
  // headers表示希望接口传过来的是文件数据
  @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
  public MyJSONResult upload(String userId, Integer categoryId,
                             String bgmId, String videoName, double videoSeconds, int videoWidth, int videoHeight, String desc,
                             @ApiParam(value = "视频", required = true)
                                 MultipartFile file) throws Exception {
    // 文件保存路径
    // String fileSpace = "E:/code/GraduationProject/resources";
    // 保存到数据库中的相对路径
    String uploadPathDB = "/" + userId + "/video";
    String coverPathDB = "/" + userId + "/video";

    FileOutputStream fileOutputStream = null;
    InputStream inputStream = null;
    String finalVideoPath = "";
    try {
      if (file != null) {
        String fileName = file.getOriginalFilename();
        int index = fileName.indexOf(".mp4");
//        String fileNamePrefix = fileName.split("\\.")[0];
        String fileNamePrefix = fileName.substring(0, index);

        if (StringUtils.isNotBlank(fileName)) {
          // 文件最终要上传的路径
          finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;

          // 设置数据库保存文件路径
          uploadPathDB += ("/" + fileName);
          coverPathDB = coverPathDB + "/" + fileNamePrefix + ".jpg";

          File outFile = new File(finalVideoPath);
          if (!outFile.getParentFile().isDirectory()) {
            // 创建父文件夹
            outFile.getParentFile().mkdirs();
          }
          fileOutputStream = new FileOutputStream(outFile);
          inputStream = file.getInputStream();
          IOUtils.copy(inputStream, fileOutputStream);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new BusinessException(EmBusinessError.UPLOAD_ERROR);
    } finally {
      if (fileOutputStream != null) {
        fileOutputStream.flush();
        fileOutputStream.close();
      }
    }

    // 判断bgmId是否为空，如果不为空那就查询bgm的信息，并且合并视频，产生新的视频
    if (StringUtils.isNotBlank(bgmId)) {
      Bgm bgm = bgmService.queryBgmById(bgmId);
      String mp3InputPath = FILE_SPACE + bgm.getPath();
      VideoUtils tool = new VideoUtils(FFMPEG_EXE);
      String videoInputPath = finalVideoPath;

      uploadPathDB = "/" + userId + "/video" + "/" + UUID.randomUUID().toString() + ".mp4";
      finalVideoPath = FILE_SPACE + uploadPathDB;
      tool.convert(videoInputPath, mp3InputPath, videoSeconds, finalVideoPath);
    }

    System.out.println("uploadPathDB:" + uploadPathDB);
    System.out.println("finalVideoPath:" + finalVideoPath);

    // 对视频进行截图
    VideoUtils videoUtils = new VideoUtils(FFMPEG_EXE);
    videoUtils.fetchCover(finalVideoPath, FILE_SPACE + coverPathDB);

    //保存视频信息到数据库
    Videos video = new Videos();
    video.setBgmId(bgmId);
    video.setCategoryId(categoryId);
    video.setVideoName(videoName);
    video.setUserId(userId);
    video.setVideoDesc(desc);
    video.setVideoSeconds((float) videoSeconds);
    video.setVideoHeight(videoHeight);
    video.setVideoWeight(videoWidth);
    video.setVideoPath(uploadPathDB);
    video.setCoverPath(coverPathDB);
    video.setStatus(EmVideoStatus.SUCCESS.getStatusCode());
    video.setCreateTime(new Date());
    String videoId = videoService.saveVideo(video);

    return MyJSONResult.create(new CommonSuccess("视频上传成功", videoId));
  }

  // 因为在手机端封面信息获取不到所以用FFMEPG截图生成视频封面
  @ApiOperation(value = "上传封面", notes = "上传封面的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "form"),
  })
  @PostMapping(value = "/uploadcover", headers = "content-type=multipart/form-data")
  public MyJSONResult uploadCover(String userId, String videoId,
                                  @ApiParam(value = "视频封面", required = true) MultipartFile file) throws Exception {
    // 视频id和用户id不能为空
    if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
      throw new BusinessException(EmBusinessError.PARAMS_ERROR);
    }
    // 保存到数据库中的相对路径
    String uploadPathDB = "/" + userId + "/video";

    FileOutputStream fileOutputStream = null;
    InputStream inputStream = null;
    String finalVideoCoverPath = "";
    try {
      if (file != null) {
        String fileName = file.getOriginalFilename();
        // new.jpg

        if (StringUtils.isNotBlank(fileName)) {
          // 文件最终要上传的路径
          finalVideoCoverPath = FILE_SPACE + uploadPathDB + "/" + fileName;
          // 设置数据库保存文件路径
          uploadPathDB += ("/" + fileName);

          File outFile = new File(finalVideoCoverPath);
          if (!outFile.getParentFile().isDirectory()) {
            // 创建父文件夹
            outFile.getParentFile().mkdirs();
          }
          fileOutputStream = new FileOutputStream(outFile);
          inputStream = file.getInputStream();
          IOUtils.copy(inputStream, fileOutputStream);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new BusinessException(EmBusinessError.UPLOAD_ERROR);
    } finally {
      if (fileOutputStream != null) {
        fileOutputStream.flush();
        fileOutputStream.close();
      }
    }

    videoService.updateVideoCover(videoId, uploadPathDB);

    returnList = new ArrayList<>();
    returnList.add(uploadPathDB);
    returnList.add(userId);

    return MyJSONResult.create(new CommonSuccess("更新视频封面成功", returnList));
  }

  @ApiOperation(value = "获取视频列表", notes = "获取视频列表的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "isSaveRecord", value = "是否保存热搜词(1表示要保存, 0表示不保存或者为空)", required = true, dataType = "boolean", paramType = "query"),
      @ApiImplicitParam(name = "page", value = "获取数据库分页之后第page页的视频列表数据", required = true, dataType = "Integer", paramType = "query")
  })
  @PostMapping(value = "/showvideolist")
  public MyJSONResult showVideoList(@RequestBody VideoReq video, Integer isSaveRecord, Integer page) {
    if (page == null) {
      page = 1;
    }
    PageResult pageResult = videoService.getVideoList(video, isSaveRecord, page, BasicController.PAGE_SIZE);
    return MyJSONResult.create(pageResult);
  }

  @ApiOperation(value = "使用ES获取视频列表", notes = "使用ES获取视频列表的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "isSaveRecord", value = "是否保存热搜词(1表示要保存, 0表示不保存或者为空)", required = true, dataType = "boolean", paramType = "query"),
      @ApiImplicitParam(name = "page", value = "获取数据库分页之后第page页的视频列表数据", required = true, dataType = "Integer", paramType = "query")
  })
  @PostMapping(value = "/showvideolistes")
  public MyJSONResult showVideoListES(@RequestBody VideoReq videoReq, Integer isSaveRecord, Integer page, Integer pageSize) {
    if (page == null) {
      page = 1;
    }
    if (pageSize == null) {
      pageSize = BasicController.PAGE_SIZE;
    }
    PageResult pageResult = new PageResult();
    try {
      pageResult = videoService.getVideoListES(videoReq, isSaveRecord, page, pageSize);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return MyJSONResult.create(pageResult);
  }

  @ApiOperation(value = "获取推荐视频列表", notes = "获取推荐视频列表的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "String", paramType = "query"),
  })
  @PostMapping(value = "/recommendlist")
  public MyJSONResult showRecommendVideoList(String userId) {
    List<VideosVO> result = recommendService.recall(userId);
    return MyJSONResult.create(result, "获取推荐列表成功");
  }

  @ApiOperation(value = "获取热词列表", notes = "获取热词列表的接口")
  @PostMapping(value = "/hot")
  public MyJSONResult hotRecords() {
    return MyJSONResult.create(videoService.getHotRecords());
  }

  @ApiOperation(value = "点赞", notes = "点赞的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "path"),
      @ApiImplicitParam(name = "createUserId", value = "视频发布者Id", required = true, dataType = "String", paramType = "form")
  })
  @PostMapping(value = "/{videoId}/like")
  public MyJSONResult videoLike(String userId, @PathVariable String videoId, String createUserId) {
    videoService.userLikeVideo(userId, videoId, createUserId);
    return MyJSONResult.create(new CommonSuccess("点赞成功~"));
  }

  @ApiOperation(value = "取消点赞", notes = "取消点赞的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "path"),
      @ApiImplicitParam(name = "createUserId", value = "视频发布者Id", required = true, dataType = "String", paramType = "form")
  })
  @PostMapping(value = "/{videoId}/unlike")
  public MyJSONResult videoUnLike(String userId, @PathVariable String videoId, String createUserId) {
    videoService.userUnLikeVideo(userId, videoId, createUserId);
    return MyJSONResult.create(new CommonSuccess("取消点赞成功~"));
  }

  @ApiOperation(value = "收藏", notes = "收藏的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "path"),
      @ApiImplicitParam(name = "createUserId", value = "视频发布者Id", required = true, dataType = "String", paramType = "form")
  })
  @PostMapping(value = "/{videoId}/collect")
  public MyJSONResult videoCollect(String userId, @PathVariable String videoId, String createUserId) {
    videoService.userCollectVideo(userId, videoId, createUserId);
    return MyJSONResult.create(new CommonSuccess("收藏成功~"));
  }

  @ApiOperation(value = "取消收藏", notes = "取消收藏的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "path"),
      @ApiImplicitParam(name = "createUserId", value = "视频发布者Id", required = true, dataType = "String", paramType = "form")
  })
  @PostMapping(value = "/{videoId}/uncollect")
  public MyJSONResult videoUnCollect(String userId, @PathVariable String videoId, String createUserId) {
    videoService.userUnCollectVideo(userId, videoId, createUserId);
    return MyJSONResult.create(new CommonSuccess("取消收藏成功~"));
  }

  @ApiOperation(value = "查看个人与视频之间的关系信息(点赞，收藏...)", notes = "查看视频的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "path"),
      @ApiImplicitParam(name = "createUserId", value = "视频发布者Id", required = true, dataType = "String", paramType = "form")
  })
  @PostMapping(value = "/{videoId}")
  public MyJSONResult videoUserInfo(String userId, @PathVariable String videoId, String createUserId) throws Exception {
    if (StringUtils.isBlank(videoId) || StringUtils.isBlank(createUserId)) {
      throw new BusinessException(EmBusinessError.PARAMS_ERROR);
    }
    boolean userLikeVideo = videoService.isUserLikeVideo(userId, videoId);
    boolean userCollectVideo = videoService.isUserCollectVideo(userId, videoId);
    boolean userIsFollowed = userService.queryIsFollow(createUserId, userId);

    UserVideo userVideo = new UserVideo();
    userVideo.setUserCollectVideo(userCollectVideo);
    userVideo.setUserLikeVideo(userLikeVideo);
    userVideo.setUserFollowed(userIsFollowed);

    return MyJSONResult.create(userVideo);
  }

  @ApiOperation(value = "获取收藏视频列表", notes = "获取收藏视频列表的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "String", paramType = "query"),
      @ApiImplicitParam(name = "page", value = "获取数据库分页之后第page页的视频列表数据", required = false, dataType = "Integer", paramType = "query"),
      @ApiImplicitParam(name = "pageSize", value = "每页获取视频数", required = false, dataType = "Integer", paramType = "query")
  })
  @PostMapping(value = "/collectvideolist")
  public MyJSONResult collectVideoList(String userId, Integer page, Integer pageSize) throws BusinessException {
    if (StringUtils.isBlank(userId)) {
      throw new BusinessException(EmBusinessError.PARAMS_ERROR);
    }
    if (page == null) {
      page = 1;
    }
    if (pageSize == null) {
      pageSize = BasicController.PAGE_SIZE;
    }
    PageResult videoList = videoService.queryMyCollectVideos(userId, page, pageSize);
    return MyJSONResult.create(videoList);
  }

  @ApiOperation(value = "增加播放量", notes = "增加播放量的接口")
  @ApiImplicitParam(name = "videoId", value = "视频Id", required = true, dataType = "String", paramType = "query")
  @PostMapping(value = "/addbrowsecounts")
  public MyJSONResult addBrowseCounts(String videoId) {
    videoService.addVideoBrowseCounts(videoId);
    return MyJSONResult.create(new CommonSuccess("视频播放量+1"));
  }

  @ApiOperation(value = "举报视频", notes = "举报视频的接口")
  @PostMapping(value = "/reportvideo")
  public MyJSONResult reportVideo(@RequestBody ReportVideos reportVideo) {
    videoService.reportVideo(reportVideo);
    return MyJSONResult.create(new CommonSuccess("举报成功"));
  }

  @ApiOperation(value = "发表评论", notes = "发表评论的接口")
  @PostMapping(value = "/savecomment")
  public MyJSONResult saveComment(@RequestBody Comments comment, String fatherCommentId, String toUserId) throws BusinessException {
    comment.setFatherCommentId(fatherCommentId);
    comment.setToUserId(toUserId);

    videoService.saveComment(comment);
    return MyJSONResult.create(new CommonSuccess("保存评论成功"));
  }

  @ApiOperation(value = "获取评论列表", notes = "获取评论列表的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "videoId", value = "视频Id", required = true, dataType = "String", paramType = "query"),
      @ApiImplicitParam(name = "page", value = "获取数据库分页之后第page页的视频列表数据", required = false, dataType = "Integer", paramType = "query"),
      @ApiImplicitParam(name = "pageSize", value = "每页获取视频数", required = false, dataType = "Integer", paramType = "query")
  })
  @PostMapping(value = "/getcommentlist")
  public MyJSONResult getCommentList(String videoId, Integer page, Integer pageSize) throws BusinessException {
    if (StringUtils.isBlank(videoId)) {
      throw new BusinessException(EmBusinessError.PARAMS_ERROR);
    }
    if (page == null) {
      page = 1;
    }
    if (pageSize == null) {
      pageSize = 10;
    }
    PageResult list = videoService.getCommentList(videoId, page, pageSize);

    return MyJSONResult.create(list);
  }

  @ApiOperation(value = "新增或更新评分", notes = "新增或更新评分的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "String", paramType = "query"),
      @ApiImplicitParam(name = "videoId", value = "视频Id", required = true, dataType = "String", paramType = "query"),
      @ApiImplicitParam(name = "rating", value = "评分", required = true, dataType = "Integer", paramType = "query")
  })
  @GetMapping(value = "/rating")
  public MyJSONResult rating(String userId, String videoId, Integer rating) throws BusinessException {
    if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId) || rating == null) {
      throw new BusinessException(EmBusinessError.PARAMS_ERROR);
    }
    if (videoService.saveOrUpdaterRating(userId, videoId, rating) > 0) {
      return MyJSONResult.create(new CommonSuccess("插入或更新评分成功"));
    } else {
      return MyJSONResult.create("插入或更新评分失败");
    }
  }

  @ApiOperation(value = "获取视频评分", notes = "获取视频评分的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "String", paramType = "query"),
      @ApiImplicitParam(name = "videoId", value = "视频Id", required = true, dataType = "String", paramType = "query"),
  })
  @GetMapping(value = "getratingvalue")
  public MyJSONResult getRateValue(String userId, String videoId) {
    if (StringUtils.isBlank(userId)) {
      userId = "-1";
    }
    int rate = videoService.getRateValue(userId, videoId);
    return MyJSONResult.create(new CommonSuccess("获取评分成功", rate));
  }

  @ApiOperation(value = "获取视频分类列表", notes = "获取视频分类列表的接口")
  @GetMapping(value = "/categorylist")
  public MyJSONResult getCategoryList() {
    return MyJSONResult.create(videoService.getCategoryList(), "获取视频分类列表成功");
  }


  @ApiOperation(value = "改变视频标签列表", notes = "改变视频标签列表的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "tagListStr", value = "标签字符串", required = true, dataType = "String", paramType = "query"),
      @ApiImplicitParam(name = "videoId", value = "视频Id", required = true, dataType = "String", paramType = "query"),
  })
  @GetMapping(value = "/changetag")
  public MyJSONResult changeTag(String videoId, String tagListStr) {
    if (videoService.changeTag(videoId, tagListStr) > 0) {
      return MyJSONResult.create(new CommonSuccess("改变标签成功", tagListStr));
    }
    return MyJSONResult.create(new CommonSuccess("改变标签失败"));
  }

  @ApiOperation(value = "删除视频，以更新视频状态为3作为删除操作", notes = "删除视频的接口")
  @ApiImplicitParam(name = "videoId", value = "视频Id", required = true, dataType = "String", paramType = "query")
  @GetMapping(value = "/deletevideo")
  public MyJSONResult deleteVideo(String videoId) throws BusinessException {
    if (StringUtils.isBlank(videoId)) {
      throw new BusinessException(EmBusinessError.PARAMS_ERROR);
    }
    if (videoService.deleteVideo(videoId) > 0) {
      return MyJSONResult.create(new CommonSuccess("删除视频成功", videoId));
    } else {
      return MyJSONResult.create(new CommonErr(EmBusinessError.NO_OBJECT_FOUND));
    }
  }

  @ApiOperation(value = "更新视频基本信息", notes = "更新视频基本信息的接口")
  @PostMapping(value = "/updatevideo")
  public MyJSONResult updateVideo(@RequestBody ModifyReq modifyReq) {
    Videos videos = videoService.updateVideo(modifyReq);
    if (videos != null) {
      return MyJSONResult.create(new CommonSuccess("更新视频成功", videos));
    } else {
      return MyJSONResult.create(new CommonErr(EmBusinessError.NO_OBJECT_FOUND));
    }
  }
}
