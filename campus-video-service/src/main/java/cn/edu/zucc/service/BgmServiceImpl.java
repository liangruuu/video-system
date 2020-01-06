package cn.edu.zucc.service;

import cn.edu.zucc.mapper.BgmMapper;
import cn.edu.zucc.pojo.Bgm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BgmServiceImpl implements BgmService {

  @Autowired
  private BgmMapper bgmMapper;

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public List<Bgm> queryBgmList() {
    return bgmMapper.selectAll();
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  @Override
  public Bgm queryBgmById(String bgmId) {
    return bgmMapper.selectByPrimaryKey(bgmId);
  }
}
