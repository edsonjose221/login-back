package controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.User;
import service.UserService;
import util.UserRoles;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	private PasswordEncoder passwordEncoder;
	
	public UserController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping("/admin/list/{id}")
	public ResponseEntity<Optional<Object>> listUsers(@PathVariable(value = "id") Integer id) throws Exception {
		Optional<Object> usu = userService.listUser(id);
		return new ResponseEntity<Optional<Object>>(usu, HttpStatus.OK);
	}
	
	@GetMapping("/roles")
	public ResponseEntity<UserRoles[]> userRoles() {
		UserRoles[] roles = userService.roles();
		return new ResponseEntity<UserRoles[]>(roles, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Optional<User>> userLogin(@RequestBody User user) {
		Optional<User> use = userService.login(user);
		return new ResponseEntity<Optional<User>>(use, HttpStatus.OK);
	}
	
	@PostMapping("/save")
	public ResponseEntity<User> saveUser(@RequestBody User user) throws Exception {
		user.setSenha(passwordEncoder.encode(user.getSenha()));
		User use = userService.saveUser(user);
		return new ResponseEntity<User>(use, HttpStatus.CREATED);
	}
	
	@PutMapping("/edit")
	public ResponseEntity<Optional<User>> editUser(@RequestBody User user) throws Exception {
		user.setSenha(passwordEncoder.encode(user.getSenha()));
		Optional<User> use = userService.editUser(user);
		return new ResponseEntity<Optional<User>>(use, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public void deleteUser(@PathVariable Integer id) throws Exception {
		userService.deleteUser(id);
	}
}
