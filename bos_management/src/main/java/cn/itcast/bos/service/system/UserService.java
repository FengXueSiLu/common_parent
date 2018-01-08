package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.User;

public interface UserService {

	User findByUsername(String username);

	List<User> list();

	void saveUser(User model, String[] roleIds);

}
