package it.ltc.services.custom.controller;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.services.custom.exception.CustomException;

public abstract class RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger(RestController.class);
	
	private static final String INVALID_CREDENTIALS = "Autenticazione fallita";
	private static final String NO_PERMISSIONS = "Permessi insufficienti";
	
	@Autowired
	protected LoginController loginManager;
	
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
