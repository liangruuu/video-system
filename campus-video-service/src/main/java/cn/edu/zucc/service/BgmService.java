package cn.edu.zucc.service;

import cn.edu.zucc.pojo.Bgm;
import java.util.List;

public interface BgmService {
  /**
   * 查询背景音乐列表
   * @return
   */
  public List<Bgm> queryBgmList();

  /**
   * 根据Id查询bgm信息
   * @param bgmId
   * @return
   */
  public Bgm queryBgmById(String bgmId);

}
