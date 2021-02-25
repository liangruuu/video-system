package cn.edu.zucc.service;

import cn.edu.zucc.mapper.MyVideosMapper;
import cn.edu.zucc.mapper.RecommendMapper;
import cn.edu.zucc.mapper.VideosMapper;
import cn.edu.zucc.pojo.Recommend;
import cn.edu.zucc.pojo.UsersLikeVideos;
import cn.edu.zucc.pojo.Videos;
import cn.edu.zucc.vo.VideosVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @PackageName cn.edu.zucc.service
 * @ProjectName campus-vedio
 * @Description TODO
 * @Author liangruuu
 * @Date 2020/3/6 14:01
 **/
@Service
public class RecommendServiceImpl implements RecommendService {
  @Autowired
  private MyVideosMapper myVideosMapper;
  @Autowired
  private RecommendMapper recommendMapper;

  @Override
  public List<VideosVO> recall(String userId) {
    Recommend recommend = recommendMapper.selectByPrimaryKey(userId);
    if (recommend == null) {
      recommend = recommendMapper.selectByPrimaryKey("9999999");
    }
    String[] videoIdArr = recommend.getRecommend().split(",");
    List<String> videoIdList = new ArrayList<>(Arrays.asList(videoIdArr));

    List<VideosVO> videoList = new ArrayList<>();
    for (int i = 0; i < videoIdList.size(); i++) {
      videoList.add(myVideosMapper.queryRecommendVideos(videoIdList.get(i)));
    }
    return videoList;
  }
}
