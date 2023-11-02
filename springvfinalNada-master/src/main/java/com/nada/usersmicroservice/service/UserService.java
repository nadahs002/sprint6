package com.nada.usersmicroservice.service;

import com.nada.usersmicroservice.entities.Role;
import com.nada.usersmicroservice.entities.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserService {
	User saveUser(User user);
	User findUserByUsername (String username);
	Role addRole(Role role);
	// User addRoleToUser(Long id, Role r);
	List<User> findAllUsers();

	User updateUser(User user);
	void deleteUserById(Long id);
	User getUserById(Long id);

	User ChangePassword(String oldPass ,String newPass, Long id);
	List<Role> findAllRoles();

	Role findRoleById(Long id);

	User removeRoleFromUser(Long id, Role r);
	User addRoleToUser(String username, String rolename);

	User activateUser(String username , String code );


}