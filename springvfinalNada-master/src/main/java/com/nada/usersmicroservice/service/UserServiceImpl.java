package com.nada.usersmicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nada.usersmicroservice.entities.Role;
import com.nada.usersmicroservice.entities.User;
import com.nada.usersmicroservice.repos.RoleRepository;
import com.nada.usersmicroservice.repos.UserRepository;

import jakarta.transaction.Transactional;


@Transactional
@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRep;
	@Autowired
	RoleRepository roleRep;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Value("${spring.mail.username}")
	private String from;
	@Autowired
	private JavaMailSender mailSender;
	//	@Override
//	public User saveUser(User user) {
//	user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//	return userRep.save(user);
//	}
	@Override
	public User saveUser(User user) {
		//Random rand = new Random();
		//String code = String.format("%04d", rand.nextInt(10000));

		String code= UUID.randomUUID().toString();



		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(user.getEmail());
		msg.setSubject("your code of the authentication is :");
		String activationURL = "http://localhost:8082/users/activateUser/" + user.getUsername() + "/" + code;

		msg.setText(     "Dear " + user.getUsername() + "," +
				"\n\n" +
				"To activate your employee account, please click on the following link: " +
				"\n\n" + activationURL +
				"\n\n");
		//msg.setText(code);
		mailSender.send(msg);
		List<Role> listOfrole=new ArrayList<>();
		listOfrole.add(roleRep.findRoleById(2L));
		user.setRoles(listOfrole);

		user.setEmail(user.getEmail());
		user.setCode(code);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRep.save(user);
	}
	/*@Override
	public User addRoleToUser(String username, String rolename) {
	User usr = userRep.findByUsername(username);
	Role r = roleRep.findByRole(rolename);
	usr.getRoles().add(r);
	return usr;
	}*/
	@Override
	public User addRoleToUser(String username, String rolename) {
		User usr = userRep.findByUsername(username);
		Role r = roleRep.findByRole(rolename);
		System.out.println(r);
		if (usr.getRoles() == null) {
			usr.setRoles(new ArrayList<>()); // Initialize the roles list
		}
		if (!usr.getRoles().contains(r)) {
			usr.getRoles().add(r);
		}
		return usr;
	}
	@Override
	public Role addRole(Role role) {
		return roleRep.save(role);
	}
	@Override
	public User findUserByUsername(String username) {
		return userRep.findByUsername(username);
	}
	@Override
	public List<User> findAllUsers() {
		return userRep.findAll();
	}
	@Override
	public User updateUser(User user) {
		User existingUser = userRep.getById(user.getUser_id());

		if (existingUser == null) {
			throw new IllegalArgumentException("user '" + user.getUsername() + "' does not exist.");
		}



		if (userRep.findByUsername(user.getUsername()) != null && !user.getUsername().equals(existingUser.getUsername()) ) {
			throw new IllegalArgumentException("Username '" + user.getUsername() + "' already exists.");
		}

		existingUser.setUsername(user.getUsername());
		existingUser.setEmail(user.getEmail());

		return userRep.save(existingUser);
	}
	@Override
	public void deleteUserById(Long id) {
		User user = userRep.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User with id " + id + " does not exist."));

		// Remove the association with roles
		user.getRoles().clear();
		userRep.deleteById(id);
	}
	@Override
	public User getUserById(Long id) {
		return userRep.getById(id);
	}
	@Override
	public User ChangePassword(String oldPass, String newPass, Long id) {
		User existingUser = userRep.getById(id);

		if (existingUser == null) {
			throw new IllegalArgumentException("user '" + existingUser.getUsername() + "' does not exist.");
		}

		// Verify the old password

		if (!bCryptPasswordEncoder.matches(oldPass, existingUser.getPassword())) {
			throw new IllegalArgumentException("Incorrect old password.");
		}


		existingUser.setPassword(bCryptPasswordEncoder.encode(newPass));

		return userRep.save(existingUser);
	}
	@Override
	public List<Role> findAllRoles() {
		return roleRep.findAll();
	}
	@Override
	public User removeRoleFromUser(Long id, Role r) {
		{
			User user = userRep.getUserById(id);
			List<Role> listOfRoles = user.getRoles();
			listOfRoles.remove(r);
			userRep.save(user);
			return user;
		}
	}
	@Override
	public Role findRoleById(Long id) {
		return roleRep.findRoleById(id);
	}
	//	@Override
//	public User activateUser(String username) {
//		User user = userRep.findByUsername(username);
//
//        if (user != null) {
//            if (user.getEnabled() == null || user.getEnabled() == false) {
//
//                user.setEnabled(true);
//                userRep.save(user);
//                return user;
//            } else {
//                return null;
//            }
//        } else {
//            return null;
//        }
//	}
	@Override
	public User activateUser(String username, String code) {
		User user=userRep.findByUsername(username);
		if(user!=null)
		{
			if(user.getEnabled()==null || user.getEnabled()==false)
			{
				if(user.getCode().equals(code)==true)
				{
					user.setEnabled(true);
					userRep.save(user);
					return user;
				}
				else
				{
					System.out.println(user.getCode());
					return null;
				}
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}


}