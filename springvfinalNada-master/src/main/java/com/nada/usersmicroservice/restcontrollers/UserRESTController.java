package com.nada.usersmicroservice.restcontrollers;
import java.util.List;
import java.util.Optional;
import com.nada.usersmicroservice.repos.UserRepository;
import com.nada.usersmicroservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nada.usersmicroservice.entities.Role;
import com.nada.usersmicroservice.entities.User;
import com.nada.usersmicroservice.service.UserService;

@RequestMapping("")
@RestController
@CrossOrigin(origins = "")
public class UserRESTController {
	@Autowired
	UserRepository userRep;
	@Autowired
	UserService userService;

	@Autowired
	MailService mailService;

	@RequestMapping(path = "/all",method = RequestMethod.GET)
	public List<User> getAllUsers() {
		return userRep.findAll();
	}


	@RequestMapping(path = "/{id}",method = RequestMethod.GET)
	public Optional<User> getUserById(@PathVariable("id") Long id) {
		return userRep.findById(id);
	}

	@RequestMapping(path="/getByusername/{username}",method = RequestMethod.GET)
	public User getUserByUsername(@PathVariable("username") String username) {
		return userService.findUserByUsername(username);
	}

	@RequestMapping(path="/addUser",method=RequestMethod.POST)
	public User saveUser(@RequestBody User user) {
		return userService.saveUser(user);
	}


	@RequestMapping(path="/deleteUser/{id}",method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable("id") Long id)
	{

		userService.deleteUserById(id);
	}




	@RequestMapping(path = "activateUser/{username}/{code}", method = RequestMethod.GET)
	public String activateUser(@PathVariable String username,@PathVariable String code ) {

		System.out.println("user activated: " + code);
		User user =userService.activateUser(username, code);
		if (user!=null)
			return "Your account has been verified ";
		else
			return "Your account has not been verified";

	}



	@RequestMapping(path="/changePassword/{id}/{oldPass}/{newPass}",method = RequestMethod.PUT)
	public User ChangePassword(@PathVariable("oldPass") String oldPass,@PathVariable("newPass")
	String newPass,@PathVariable("id") Long id) {
		System.out.println("old = "+oldPass + " new = "+newPass);
		return  userService.ChangePassword(oldPass,newPass,id);
	}

	@RequestMapping(path="/allRoles",method=RequestMethod.GET)
	public List<Role> getAllRoles() {
		return userService.findAllRoles();
	}
	@RequestMapping(path="role/{id}",method=RequestMethod.GET)
	public Role findRoleById(@PathVariable Long id) {
		return userService.findRoleById(id);
	}
	@RequestMapping(path="/removeRole/{id}",method=RequestMethod.POST)
	public User removeRole(@PathVariable Long id,@RequestBody Role r)
	{
		return  userService.removeRoleFromUser(id,r);
	}

	@RequestMapping(path="/addRole/{username}/{rolename}",method=RequestMethod.POST)
	public User addRoleToUser(@PathVariable String username ,@PathVariable String rolename ) {
		return userService.addRoleToUser(username, rolename);
	}


}