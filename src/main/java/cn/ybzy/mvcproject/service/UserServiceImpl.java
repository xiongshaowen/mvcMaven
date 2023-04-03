package cn.ybzy.mvcproject.service;

import java.sql.Connection;
import java.util.List;

import cn.ybzy.mvcproject.dao.FactoryDao;
import cn.ybzy.mvcproject.dao.UserDao;
import cn.ybzy.mvcproject.model.User;
import cn.ybzy.mvcproject.utils.JdbcUtils;

public class UserServiceImpl implements UserService {
	
	UserDao userDao = FactoryDao.getUserDao();
	
	@Override
	public int save(User user) {
		return userDao.save(user);
	}

	@Override
	public int deleteUserById(int id) {
		return userDao.deleteUserById(id);
	}

	@Override
	public int updateUserById(User user) {
		return userDao.updateUserById(user);
	}

	@Override
	public User get(int id) {
		return userDao.get(id);
	}

	@Override
	public User getTransation(int id) {
		Connection conn = null;
		User user = null;
		try {
			conn = JdbcUtils.getConnection();
			conn.setAutoCommit(false);//开始事务
			user = userDao.get(conn, id);
			conn.commit();
		}catch (Exception e) {
			JdbcUtils.rollbackTransation(conn);//回滚事务
		}
		return user;
	}

	@Override
	public List<User> getListAll() {
		return userDao.getListAll();
	}

	@Override
	public long getCountByName(String username) {
		return userDao.getCountByName(username);
	}

	@Override
	public List<User> query(String username, String address, String phoneNo) {
		return userDao.query(username,address,phoneNo);
	}

	@Override
	public User login(String username, String password) {
		return userDao.getUserByUp(username,password);
	}

}
