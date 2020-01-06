package cn.edu.zucc.service;

import cn.edu.zucc.mapper.UsersFansMapper;
import cn.edu.zucc.mapper.UsersMapper;
import cn.edu.zucc.pojo.Users;
import cn.edu.zucc.pojo.UsersFans;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UsersMapper usersMapper;
  @Autowired
  UsersFansMapper usersFansMapper;

  private Sid sid = new Sid();

  @Override
  @Transactional(propagation = Propagation.SUPPORTS)
  public boolean queryUsernameIsExist(String userName) {
    Users user = new Users();
    user.setUsername(userName);

    Users result = usersMapper.selectOne(user);
    return result != null;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void saveUser(Users user) {
    String userId = sid.nextShort();
    user.setId(userId);
    usersMapper.insert(user);
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS)
  public Users queryUserForLogin(String username, String password) {
    Example userExample = new Example(Users.class);
    Example.Criteria criteria = userExample.createCriteria();
    criteria.andEqualTo("username", username);
    criteria.andEqualTo("password", password);

    return usersMapper.selectOneByExample(userExample);
  }

  @Override
  public void updateUserInfo(Users user) {
    Example userExample = new Example(Users.class);
    Example.Criteria criteria = userExample.createCriteria();
    criteria.andEqualTo("id", user.getId());
    // 选择性条件修改用户信息
    usersMapper.updateByExampleSelective(user, userExample);
  }

  @Override
  @Transactional(propagation = Propagation.SUPPORTS)
  public Users queryUserInfo(String userId) {
    Example userExample = new Example(Users.class);
    Example.Criteria criteria = userExample.createCriteria();
    criteria.andEqualTo("id", userId);

    return usersMapper.selectOneByExample(userExample);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void saveUserFanRelation(String userId, String fanId) {
    String relId = sid.nextShort();
    UsersFans usersFan = new UsersFans();
    usersFan.setId(relId);
    usersFan.setUserId(userId);
    usersFan.setFanId(fanId);

    usersFansMapper.insert(usersFan);
    usersMapper.addFansCounts(userId);
    usersMapper.addFollowCounts(fanId);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void deleteUserFanRelation(String userId, String fanId) {
    Example userExample = new Example(UsersFans.class);
    Example.Criteria criteria = userExample.createCriteria();
    criteria.andEqualTo("userId", userId);
    criteria.andEqualTo("fanId", fanId);

    usersFansMapper.deleteByExample(userExample);
    usersMapper.reduceFansCounts(userId);
    usersMapper.reduceFollowCounts(fanId);
  }

  @Override
  public boolean queryIsFollow(String userId, String fanId) {
    Example userExample = new Example(UsersFans.class);
    Example.Criteria criteria = userExample.createCriteria();
    criteria.andEqualTo("userId", userId);
    criteria.andEqualTo("fanId", fanId);

    List<UsersFans> list = usersFansMapper.selectByExample(userExample);
    return list != null && list.size() > 0;
  }
}
