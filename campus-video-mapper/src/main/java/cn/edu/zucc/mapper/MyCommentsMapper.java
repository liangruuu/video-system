package cn.edu.zucc.mapper;

import cn.edu.zucc.pojo.Comments;
import cn.edu.zucc.utils.MyMapper;
import cn.edu.zucc.vo.CommentsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyCommentsMapper extends MyMapper<CommentsVO> {

  public List<CommentsVO> queryComment(@Param("videoId") String videoId);
}