package cn.edu.zucc.utils;

import java.util.List;

/**
 * 封装分页以后的数据格式
 * @author liangruuu
 */
public class PageResult {

  private int page; // 当前页数
  private int total;  // 总页数
  private long records; // 总记录数
  private List<?> rows; // 每行显示的内容(即数据库表中的其中一行字段内容)

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public long getRecords() {
    return records;
  }

  public void setRecords(long records) {
    this.records = records;
  }

  public List<?> getRows() {
    return rows;
  }

  public void setRows(List<?> rows) {
    this.rows = rows;
  }
}
