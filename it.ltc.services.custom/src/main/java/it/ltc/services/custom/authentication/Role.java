package it.ltc.services.custom.authentication;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	public static final String ROLE = "ROLE_USER";
	
	Role() {}
	
	@Override
	public String getAuthority() {
		return ROLE;
	}

}
