package cn.edu.zucc.controller;

import cn.edu.zucc.pojo.Users;
import cn.edu.zucc.service.UserService;
import cn.edu.zucc.utils.BusinessException;
import cn.edu.zucc.utils.CommonSuccess;
import cn.edu.zucc.utils.EmBusinessError;
import cn.edu.zucc.utils.MyJSONResult;
import cn.edu.zucc.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@RestController
@Api(value = "用户业务操作的接口", tags = {"用户业务操作的controller"})
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @ApiOperation(value = "用户上传头像", notes = "用户上传头像的接口")
  @PostMapping("/uploadavatar")
  public MyJSONResult uploadAvatar(String userId, @RequestParam("file") MultipartFile[] files) throws Exception {
    // 文件保存路径
    String fileSpace = "E:/code/GraduationProject/resources";
    // 保存到数据库中的相对路径
    String uploadPathDB = "/" + userId + "/avatar";

    FileOutputStream fileOutputStream = null;
    InputStream inputStream = null;
    try {
      if (files != null && files.length > 0) {
        String fileName = files[0].getOriginalFilename();
        if (StringUtils.isNotBlank(fileName)) {
          // 文件最终要上传的路径
          String finalAvatarPath = fileSpace + uploadPathDB + "/" + fileName;
          // 设置数据库保存文件路径
          uploadPathDB += ("/" + fileName);
          File outFile = new File(finalAvatarPath);
          if (!outFile.getParentFile().isDirectory()) {
            // 创建父文件夹
            outFile.getParentFile().mkdirs();
          }
          fileOutputStream = new FileOutputStream(outFile);
          inputStream = files[0].getInputStream();
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
    Users user = new Users();
    user.setId(userId);
    user.setAvatar(uploadPathDB);
    userService.updateUserInfo(user);

    return MyJSONResult.create(new CommonSuccess("上传头像成功", uploadPathDB));
  }

  @ApiOperation(value = "查询用户信息", notes = "查询用户信息的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query"),
      @ApiImplicitParam(name = "fanId", value = "粉丝id", required = true, dataType = "String", paramType = "query"),
  })
  @PostMapping("/query")
  public MyJSONResult queryUserInfo(String userId, String fanId) throws Exception {
    if (StringUtils.isBlank(userId)) {
      throw new BusinessException(EmBusinessError.PARAMS_ERROR);
    }
    Users userInfo = userService.queryUserInfo(userId);
    UsersVO usersVO = new UsersVO();
    BeanUtils.copyProperties(userInfo, usersVO);
    usersVO.setFollowed(userService.queryIsFollow(userId, fanId));

    return MyJSONResult.create(usersVO);
  }


  @ApiOperation(value = "粉丝进行关注", notes = "粉丝进行关注的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "fanId", value = "粉丝id", required = true, dataType = "String", paramType = "form"),
  })
  @PostMapping(value = "/focus")
  public MyJSONResult focus(String userId, String fanId) throws Exception {
    if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
      throw new BusinessException(EmBusinessError.PARAMS_ERROR);
    }
    userService.saveUserFanRelation(userId, fanId);
    return MyJSONResult.create(new CommonSuccess("关注成功~"));
  }

  @ApiOperation(value = "粉丝取消关注", notes = "粉丝取消关注的接口")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "fanId", value = "粉丝id", required = true, dataType = "String", paramType = "form"),
  })
  @PostMapping(value = "/unfocus")
  public MyJSONResult unFocus(String userId, String fanId) throws Exception {
    if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
      throw new BusinessException(EmBusinessError.PARAMS_ERROR);
    }
    userService.deleteUserFanRelation(userId, fanId);
    return MyJSONResult.create(new CommonSuccess("取消关注成功~"));
  }
}
