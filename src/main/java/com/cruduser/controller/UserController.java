package com.cruduser.controller;

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

import com.cruduser.model.User;
import com.cruduser.service.UserService;
import com.cruduser.util.UserRoles;

@RestController
@RequestMapping("/api/usuario")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	private PasswordEncoder passwordEncoder;
	
	public UserController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping("/admin/listar/{id}")
	public ResponseEntity<Optional<Object>> listarUsuario(@PathVariable(value = "id") Integer id) {
		Optional<Object> usu = userService.listarUsuarios(id);
		return new ResponseEntity<Optional<Object>>(usu, HttpStatus.OK);
	}
	
	@GetMapping("/roles")
	public ResponseEntity<UserRoles[]> roles() {
		UserRoles[] roles = userService.roles();
		return new ResponseEntity<UserRoles[]>(roles, HttpStatus.OK);
	}
	
	@PostMapping("/logado")
	public ResponseEntity<Optional<User>> usuarioLogado(@RequestBody User user) {
		Optional<User> use = userService.logado(user);
		return new ResponseEntity<Optional<User>>(use, HttpStatus.OK);
	}
		
	@PostMapping("/salvar")
	public ResponseEntity<User> salvarUsuario(@RequestBody User user) throws Exception {
		user.setSenha(passwordEncoder.encode(user.getSenha()));
		User use = userService.salvarUsuario(user);
		return new ResponseEntity<User>(use, HttpStatus.CREATED);
	}
	
	@PutMapping("/editar")
	public ResponseEntity<Optional<User>> editar(@RequestBody User user) throws Exception {
		user.setSenha(passwordEncoder.encode(user.getSenha()));
		Optional<User> use = userService.editarUsuario(user);
		return new ResponseEntity<Optional<User>>(use, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/excluir/{id}")
	public void deletarUsuario(@PathVariable Integer id) throws Exception {
		userService.delete(id);
	}
}
