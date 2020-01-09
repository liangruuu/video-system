package cn.edu.zucc.service;

import cn.edu.zucc.mapper.*;
import cn.edu.zucc.pojo.*;
import cn.edu.zucc.utils.PageResult;
import cn.edu.zucc.vo.VideosVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author liangruuu
 */
@Service
public class VideoServiceImpl implements VideoService {

  @Autowired
  private VideosMapper videosMapper;
  @Autowired
  private MyVideosMapper myVideosMapper;
  @Autowired
  private SearchRecordsMapper searchRecordsMapper;
  @Autowired
  private UsersLikeVideosMapper usersLikeVideosMapper;
  @Autowired
  private UsersCollectVideosMapper usersCollectVideosMapper;
  @Autowired
  private UsersMapper usersMapper;
  @Autowired
  private ReportVideosMapper reportVideosMapper;

  private Sid sid = new Sid();

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public String saveVideo(Videos video) {
    // 对video生成唯一主键
    String id = sid.nextShort();
    video.setId(id);
    videosMapper.insertSelective(video);
    return id;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void updateVideoCover(String videoId, String coverPath) {
    Videos video = new Videos();
    video.setId(videoId);
    video.setCoverPath(coverPath);
    videosMapper.updateByPrimaryKeySelective(video);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public PageResult getVideoList(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {

    // 若isSaveRecord不为空且为1则保存热搜词
    if (isSaveRecord != null && isSaveRecord == 1) {
      SearchRecords records = new SearchRecords();
      String recordId = sid.nextShort();
      records.setId(recordId);
      records.setContent(video.getVideoName());
      searchRecordsMapper.insert(records);
    }

    PageHelper.startPage(page, pageSize);
    List<VideosVO> list = myVideosMapper.queryVideoList(video.getVideoName(), video.getUserId());
    PageInfo<VideosVO> pageList = new PageInfo<>(list);
    PageResult pageResult = new PageResult();
    pageResult.setPage(page);
    pageResult.setTotal(pageList.getPages());
    pageResult.setRows(list);
    pageResult.setRecords(pageList.getTotal());

    return pageResult;
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public List<String> getHotRecords() {
    return searchRecordsMapper.getHotRecords();
  }


  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void userLikeVideo(String userId, String videoId, String createUserId) {
    // 1. 保存用户和视频的点赞关联表
    String likeId = sid.nextShort();
    UsersLikeVideos ulv = new UsersLikeVideos();
    ulv.setId(likeId);
    ulv.setUserId(userId);
    ulv.setVideoId(videoId);
    usersLikeVideosMapper.insert(ulv);

    // 2. 视频点赞数+1
    myVideosMapper.addVideoLikeCount(videoId);

    // 3. 用户受喜欢数量的累加
    usersMapper.addSumLikeCounts(createUserId);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void userUnLikeVideo(String userId, String videoId, String createUserId) {
    // 1. 删除用户和视频的点赞关联表
    Example example = new Example(UsersLikeVideos.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo("userId", userId);
    criteria.andEqualTo("videoId", videoId);
    usersLikeVideosMapper.deleteByExample(example);

    // 2. 视频点赞数-1
    myVideosMapper.reduceVideoLikeCount(videoId);

    // 3. 用户受喜欢数量的累减
    usersMapper.reduceSumLikeCounts(createUserId);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void userCollectVideo(String userId, String videoId, String createUserId) {
    // 1. 保存用户和视频的收藏关联表
    String likeId = sid.nextShort();
    UsersCollectVideos ucv = new UsersCollectVideos();
    ucv.setId(likeId);
    ucv.setUserId(userId);
    ucv.setVideoId(videoId);
    usersCollectVideosMapper.insert(ucv);

    // 2. 视频收藏数+1
    myVideosMapper.addVideoCollectCount(videoId);

    // 3. 用户视频被收藏数的累加
    usersMapper.addSumCollectCounts(createUserId);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void userUnCollectVideo(String userId, String videoId, String createUserId) {
    // 1. 删除用户和视频的收藏关联表
    Example example = new Example(UsersCollectVideos.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo("userId", userId);
    criteria.andEqualTo("videoId", videoId);
    usersCollectVideosMapper.deleteByExample(example);
    // 2. 视频点赞数-1
    myVideosMapper.reduceVideoCollectCount(videoId);

    // 3. 用户受喜欢数量的累减
    usersMapper.reduceSumCollectCounts(createUserId);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public boolean isUserLikeVideo(String userId, String videoId) {
    Example example = new Example(UsersLikeVideos.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo("userId", userId);
    criteria.andEqualTo("videoId", videoId);

    List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);
    return list != null && list.size() > 0;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public boolean isUserCollectVideo(String userId, String videoId) {
    Example example = new Example(UsersCollectVideos.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andEqualTo("userId", userId);
    criteria.andEqualTo("videoId", videoId);

    List<UsersCollectVideos> list = usersCollectVideosMapper.selectByExample(example);
    return list != null && list.size() > 0;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public PageResult queryMyCollectVideos(String userId, Integer page, Integer pageSize) {
    PageHelper.startPage(page, pageSize);
    List<VideosVO> list = myVideosMapper.queryCollectVideos(userId);
    PageInfo<VideosVO> pageList = new PageInfo<>(list);

    PageResult pageResult = new PageResult();
    pageResult.setTotal(pageList.getPages());
    pageResult.setRows(list);
    pageResult.setRecords(pageList.getTotal());
    pageResult.setPage(page);

    return pageResult;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void addVideoBrowseCounts(String videoId) {
    myVideosMapper.addVideoBrowseCounts(videoId);
  }

  @Override
  public void reportVideo(ReportVideos reportVideo) {
    String rvId = sid.nextShort();
    reportVideo.setId(rvId);
    reportVideo.setCreateTime(new Date());

    reportVideosMapper.insert(reportVideo);
  }
}
