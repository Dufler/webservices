package it.ltc.services.custom.authentication;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import it.ltc.database.dao.common.LoginController;
import it.ltc.database.model.utente.Utente;

/**
 * Classe atta all'autenticazione delle richieste fatte dagli utenti tramite BASIC AUTH su HTTP.
 * @author Damiano
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	private static final ArrayList<Role> authorities = new ArrayList<Role>();
	
	private static final String INVALID_CREDENTIALS = "Login fallito: username o password non validi.";
	
    private final LoginController loginManager;
    
    public CustomAuthenticationProvider() {
    	loginManager = LoginController.getInstance();
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	String username = authentication.getName();
    	String password = (String) authentication.getCredentials();
    	Utente user = loginManager.getUserByUsernameAndPassword(username, password);
    	if (user == null)
    		throw new BadCredentialsException(INVALID_CREDENTIALS);
    		//throw new CustomException(INVALID_CREDENTIALS, 401);
    	return new UsernamePasswordAuthenticationToken(username, password, getAuthorities());
    }

	@Override
	public boolean supports(Class<?> c) {
		return true;
	}
	
	/**
	 * Questo metodo va cambiato per far restituire il giusto ruolo dell'utente:
	 * lato DB, tabella Utente: aggiungere la colonna 'ruolo', potrebbe contenere in chiaro una lista di ruoli separati da una virgola.
	 * lato DB, soluzione alternativa: fare tabella 'ruolo' e una join table con utente dove vengono indicati i ruoli attribuiti all'utente.
	 * qui: questo metodo deve accettare come argomento un'oggetto POJO Utente e ricavarne i ruoli, restituisce poi una Collection di Role
	 * qui, soluzione alternativa: l'oggetto POJO ruolo implementa l'interface di Role e lo soppianta del tutto.
	 * xml spring security: diversificare i path dove necessario in base ai ruoli definiti.
	 * TODO - implementare una delle due soluzioni.
	 * @return
	 */
	private static Collection<Role> getAuthorities() {
		if (authorities.isEmpty()) {
			authorities.add(new Role());			
		}
		return authorities;
	}

}
