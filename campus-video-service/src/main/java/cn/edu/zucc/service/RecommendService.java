package cn.edu.zucc.service;

import cn.edu.zucc.pojo.Videos;
import cn.edu.zucc.vo.VideosVO;

import java.util.List;

public interface RecommendService {

  public List<VideosVO> recall(String userId);
}
