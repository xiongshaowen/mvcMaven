package cn.ybzy.mvcproject.dao;

import java.sql.Connection;
import java.util.List;

import cn.ybzy.mvcproject.model.User;

public class UserDaoImpl extends BaseDao<User> implements UserDao {

	@Override
	public int save(User user) {
		String sql = "INSERT INTO `t_users`(`username`,`password`,`phone_no`,`address`,`reg_date`)VALUES(?,?,?,?,?);";
		return super.update(sql, user.getUsername(), user.getPassword(), user.getPhoneNo(), user.getAddress(),
				user.getRegDate());
	}

	@Override
	public int deleteUserById(int id) {
		String sql = "DELETE FROM `t_users` WHERE `id`=?;";
		return super.update(sql, id);
	}

	@Override
	public int updateUserById(User user) {
		String sql = "UPDATE `t_users` SET `username`=?,`password`=?,`phone_no`=?,`address`=? WHERE `id`=?;";
		return super.update(sql, user.getUsername(), user.getPassword(), user.getPhoneNo(), user.getAddress(),
				user.getId());
	}

	@Override
	public User get(int id) {
		String sql = "SELECT `id`,`username`,`password`,`phone_no` phoneNo,`address`,`reg_date` regDate FROM `t_users` WHERE `id`=?;";
		return super.get(sql, id);
	}

	@Override
	public User get(Connection conn, int id) {
		String sql = "SELECT `id`,`username`,`password`,`phone_no` phoneNo,`address`,`reg_date` regDate FROM `t_users` WHERE `id`=?;";
		return super.get(conn, sql, id);
	}

	@Override
	public List<User> getListAll() {
		String sql = "SELECT `id`,`username`,`password`,`phone_no` phoneNo,`address`,`reg_date` regDate FROM `t_users`;";
		return super.getList(sql);
	}

	@Override
	public long getCountByName(String username) {
		String sql = "SELECT COUNT(`id`) FROM `t_users` WHERE `username`=?;";
		return (long) super.getValue(sql, username);
	}

	@Override
	public List<User> query(String username, String address, String phoneNo) {
		String sql = "SELECT `id`,`username`,`password`,`phone_no` phoneNo,`address`,`reg_date` regDate FROM `t_users` WHERE 1=1";
		if(username!=null && !"".equals(username)) {
			sql = sql + " AND username like '%"+username+"%'";  //
		}
		if(address!=null && !"".equals(address)) {
			sql = sql + " AND address like '%"+address+"%'";
		}
		if(phoneNo!=null && !"".equals(phoneNo)) {
			sql = sql + " AND phone_no like '%"+phoneNo+"%'";
		}
		//System.out.println(sql);
		return super.getList(sql);
	}

	@Override
	public User getUserByUp(String username, String password) {
		String sql = "SELECT `id`,`username`,`password`,`phone_no` phoneNo,`address`,`reg_date` regDate FROM t_users WHERE username=? AND password=?";
		return super.get(sql, username,password);
	}

}
