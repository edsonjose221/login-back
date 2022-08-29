package com.cruduser.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cruduser.exception.NotFoundException;
import com.cruduser.model.User;
import com.cruduser.repository.UserRepository;
import com.cruduser.util.UserRoles;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	private User userLogin;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public Optional<User> logado(User userLogin) {
		return userRepository.findByEmail(userLogin.getEmail()).map(data -> {
			this.userLogin = data;
			return this.userLogin;
		});
	}
	
	public Optional<Object> listarUsuarios(Integer id) throws NotFoundException {
		try {
			 return userRepository.findById(id).map(dados -> {
				 if(dados.getUserRoles() == UserRoles.ADMIN) {
					 return userRepository.findAll();
				 } else {
					 return dados;
				 }
			 });
			
		 } catch (NotFoundException e) {
			 throw new NotFoundException("Usuário não encontrado");
		 }
	}
	
	public User salvarUsuario(User user) throws Exception {
		try {
			if(user.getUserRoles() == null) {
				user.setUserRoles(UserRoles.USER);
			}
			
			return userRepository.save(user);
			
		} catch (Exception e) {
			throw new Exception("Erro ao salvar o usuário");
		}
	}
	
	public Optional<User> editarUsuario(User email) throws Exception {
		return userRepository.findByEmail(email.getEmail())
			.map(data -> {
				data.setNome(email.getNome());
				data.setSenha(email.getSenha());
			
			if(email.getUserRoles() == null) {
				data.setUserRoles(UserRoles.USER);
			}
			else {
				data.setUserRoles(UserRoles.ADMIN);
			}
			
			return userRepository.save(data);
		});
	}
	
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public void delete(Integer id) throws Exception {
		try {
			userRepository.deleteById(id);
			
		} catch (Exception e) {
			throw new Exception("Erro ao deletar usuário");
		}
	}
	
	public UserRoles[] roles() {
		return UserRoles.values();
	}
}
