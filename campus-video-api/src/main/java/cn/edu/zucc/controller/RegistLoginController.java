package cn.edu.zucc.controller;

import cn.edu.zucc.service.UserService;
import cn.edu.zucc.pojo.Users;
import cn.edu.zucc.utils.em.EmBusinessError;
import cn.edu.zucc.vo.UsersVO;
import cn.edu.zucc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author liangruuu
 */
@RestController
@Api(value = "用户登录注册的接口", tags = {"注册和登录的controller"})
@RequestMapping("/user")
public class RegistLoginController extends BasicController {

  @Autowired
  private UserService userService;

  private UsersVO setUsersRedisSession(Users user) {
    String uniqueToken = UUID.randomUUID().toString();
//    redis.set(USER_REDIS_SESSION + ":" + user.getId(), uniqueToken, 1000 * 60 * 30);
    UsersVO usersVO = new UsersVO();
    BeanUtils.copyProperties(user, usersVO);
    usersVO.setUserToken(uniqueToken);

    return usersVO;
  }

  @ApiOperation(value = "用户注册", notes = "用户注册的接口")
  @PostMapping("/regist")
  public MyJSONResult regist(@RequestBody Users user) throws Exception {

    //1. 判断用户名管是否存在
    boolean userNameIsExist = userService.queryUsernameIsExist(user.getUsername());

    //3. 保存用户，注册信息
    if (!userNameIsExist) {
      user.setNickname(user.getUsername());
      user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
      user.setFansCounts((long) 0);
      user.setLikeCounts((long) 0);
      user.setFollowCounts((long) 0);
      user.setCollectCounts((long) 0);
      userService.saveUser(user);
    } else {
      throw new BusinessException(EmBusinessError.OBJECT_IS_EXISTED);
    }

    user.setPassword("");
    return MyJSONResult.create(setUsersRedisSession(user));
  }

  @ApiOperation(value = "用户登录", notes = "用户登陆的接口")
  @PostMapping("/login")
  public MyJSONResult login(@RequestBody Users user) throws Exception {
    String username = user.getUsername();
    String password = user.getPassword();

    // 1.判断用户是否存在
    Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
    if (userResult == null) {
//      return MyJSONResult.create(new CommonErr(EmBusinessError.NO_OBJECT_FOUND), "failed");
      throw new BusinessException(EmBusinessError.NO_OBJECT_FOUND);
    }
    // 2. 返回信息
    userResult.setPassword("");
    return MyJSONResult.create(setUsersRedisSession(userResult));
  }

  @ApiOperation(value = "用户注销", notes = "用户注销的接口")
  @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
  @PostMapping(value = "/logout")
  public MyJSONResult logout(@RequestParam("userId") String userId) {
    redis.del(USER_REDIS_SESSION + ":" + userId);
    return MyJSONResult.create(new CommonSuccess("注销成功"));
  }
}
