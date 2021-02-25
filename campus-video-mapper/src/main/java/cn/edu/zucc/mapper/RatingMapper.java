package cn.edu.zucc.mapper;

import cn.edu.zucc.pojo.Bgm;
import cn.edu.zucc.pojo.Rating;
import cn.edu.zucc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface RatingMapper extends MyMapper<Rating> {
  public int saveOrUpdaterRating(@Param("userId") String userId,
                                 @Param("videoId") String videoId,
                                 @Param("rating") Integer rating);

  public Rating getRateValue(@Param("userId") String userId,
                          @Param("videoId") String videoId);
}