package com.cruduser.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cruduser.data.DetailUserData;
import com.cruduser.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter  {
	
	public static final int TOKEN_EXPIRACAO = 900_000;
	public static final String TOKEN_SENHA = "6609ebd3-b3a8-45d7-a5b6-15ec122c8b25";
	
	
	private final AuthenticationManager authenticationManager;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					user.getEmail(),
					user.getSenha(),
					new ArrayList<>()
					));
		} catch (IOException e) {
			throw new RuntimeException("Falha ao autenticar usuário");
		}
	}
	
	@Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
		
		DetailUserData userData = (DetailUserData) authResult.getPrincipal();
		
		String token = JWT.create().withSubject(userData.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRACAO))
				.sign(Algorithm.HMAC512(TOKEN_SENHA));
		
//		response.addHeader("Access-Control-Allow-Origin", "**");
		response.addHeader("Authorization", JWTValidateFilter.ATRIBUTO_PREFIXO + token);
		response.getWriter().write(token);
		response.getWriter().flush();
	}
}
