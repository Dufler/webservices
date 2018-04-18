package it.ltc.services.logica.controller.utente;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.common.LoginController;
import it.ltc.database.model.utente.Utente;

@Controller
@RequestMapping("/utente")
public class UtenteController {
	
	private static final Logger logger = Logger.getLogger("UtenteController");
	
	private final LoginController loginController;
    
    public UtenteController() {
    	loginController = LoginController.getInstance();
    }
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/login")
	public ResponseEntity<Utente> login(@RequestHeader("authorization") String authenticationString) {
		Utente user = loginController.getUserByAuthenticationString(authenticationString, true);
		HttpStatus status = user != null ? HttpStatus.OK : HttpStatus.FORBIDDEN;
		ResponseEntity<Utente> response = new ResponseEntity<Utente>(user, status);
		return response;
	}
	
	/**
	 * Questo metodo può essere usato per cambiare le info generali sull'utente e/o la sua password.
	 * Ci si aspetta in ingresso un JSON contenente le info da cambiare.
	 * Se si desira cambiare la password va impostata in chiaro dentro il campo 'nuovaPassword'.
	 * @param utente il JSON che contiene le nuove info sull'utente.
	 * @param authenticationString la stringa di autenticazione in base64.
	 * @return le nuove info appena salvate.
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", consumes = "application/json", value = "/update")
	public ResponseEntity<Utente> update(@Valid @RequestBody Utente utente, @RequestHeader("authorization") String authenticationString) {
		Utente user = loginController.getUserByAuthenticationString(authenticationString, true);
		logger.info("Richiesta di cambio password per: " + user.getUsername() + ", nuova password: " + utente.getNuovaPassword());
		HttpStatus status;
		//Se l'utente si è autenticato con successo e sta facendo la richiesta a suo nome allora procedo con l'aggiornamento.
		if (user != null && user.getUsername().equals(utente.getUsername())) {
			user = loginController.updateUserInfos(utente);
			status = user != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			status = HttpStatus.UNAUTHORIZED;
		}
		ResponseEntity<Utente> response = new ResponseEntity<Utente>(user, status);
		return response;
	}

}