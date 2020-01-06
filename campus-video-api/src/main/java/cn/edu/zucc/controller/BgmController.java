package cn.edu.zucc.controller;

import cn.edu.zucc.service.BgmService;
import cn.edu.zucc.utils.MyJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liangruuu
 */
@RestController
@Api(value = "背景音乐业务的接口", tags = "背景音乐业务的Controller")
@RequestMapping("/bgm")
public class BgmController {
  @Autowired
  BgmService bgmService;

  /**
   * 展示所有BGM信息
   * @return
   */
  @ApiOperation(value = "获取背景音乐列表", notes = "获取背景音乐列表的接口")
  @PostMapping("/list")
  public MyJSONResult showBgmList() {
    return MyJSONResult.create(bgmService.queryBgmList());
  }
}
