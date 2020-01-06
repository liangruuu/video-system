package cn.edu.zucc.mapper;

import cn.edu.zucc.pojo.SearchRecords;
import cn.edu.zucc.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
  public List<String> getHotRecords();
}