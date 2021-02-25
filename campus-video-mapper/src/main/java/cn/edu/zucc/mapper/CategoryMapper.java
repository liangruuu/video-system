package cn.edu.zucc.mapper;

import cn.edu.zucc.pojo.Category;
import cn.edu.zucc.utils.MyMapper;

public interface CategoryMapper extends MyMapper<Category> {
  public int selectNums();
}