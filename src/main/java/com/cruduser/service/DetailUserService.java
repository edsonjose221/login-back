package com.cruduser.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.cruduser.data.DetailUserData;
import com.cruduser.model.User;
import com.cruduser.repository.UserRepository;

@Component
public class DetailUserService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	public DetailUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmail(username);
		
		if (user.isEmpty()) {
			 throw new UsernameNotFoundException("Usuário [" + username + "] não encontrado");
		}
		return new DetailUserData(user);
	}
	 
	
}
