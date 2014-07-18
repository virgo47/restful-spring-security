package com.github.virgo47.respsec.main.security;

import com.github.virgo47.respsec.main.User;
import com.github.virgo47.respsec.main.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUseDetailsService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	/**
	 * This will be called from
	 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider#retrieveUser(java.lang.String, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)}
	 * when {@link AuthenticationServiceImpl#authenticate(java.lang.String, java.lang.String)} calls
	 * {@link AuthenticationManager#authenticate(org.springframework.security.core.Authentication)}. Easy.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(" *** MyUseDetailService.loadUserByUsername");
		User user = userDao.getByLogin(username);
		if (user == null) {
			throw new UsernameNotFoundException("User " + username + " not found");
		}
		return new UserContext(user);
	}
}
