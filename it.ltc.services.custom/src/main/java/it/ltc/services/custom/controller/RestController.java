package it.ltc.services.custom.controller;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.services.custom.exception.CustomException;

public abstract class RestController {
	
	private static final Logger logger = Logger.getLogger("RestController");
	
	private static final String INVALID_CREDENTIALS = "Autenticazione fallita";
	private static final String NO_PERMISSIONS = "Permessi insufficienti";
	
	@Autowired
	protected LoginController loginManager;
	
//	public RestController() {
//		loginManager = LoginController.getInstance();
//	}
	
	protected UtenteUtenti checkCredentialsAndPermission(String authenticationString, int permessionID) {
		UtenteUtenti user = loginManager.getUserByAuthenticationString(authenticationString);
		if (user == null) {
			logger.warn(INVALID_CREDENTIALS);
			throw new CustomException(INVALID_CREDENTIALS, 401);
		} else if (!user.isAllowedTo(permessionID)) {
			logger.warn(NO_PERMISSIONS);
			throw new CustomException(NO_PERMISSIONS, 403);
		}
		return user;
	}

}
