package it.ltc.services.logica.controller.utente;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.common.LoginController;
import it.ltc.database.model.utente.Utente;

@Controller
@RequestMapping("/gestioneutenti")
public class GestioneUtentiController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("GestioneUtentiController");
	
	private final LoginController loginController;
    
    public GestioneUtentiController() {
    	loginController = LoginController.getInstance();
    }
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Utente>> findAll(@RequestHeader("authorization") String authenticationString) {
    	Utente user = loginController.getUserByAuthenticationString(authenticationString, true);
		logger.info("Richiesta dell'elenco utenti da: " + user.getUsername());
		List<Utente> utenti;
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			utenti = loginController.getUtenti(true);
			status = utenti.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		} else {
			utenti = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<List<Utente>> response = new ResponseEntity<List<Utente>>(utenti, status);
		return response;
    }
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{username}")
	public ResponseEntity<Utente> findDetailsByUsername(@RequestHeader("authorization") String authenticationString, @PathVariable("username") String username) {
    	Utente user = loginController.getUserByAuthenticationString(authenticationString, true);
		logger.info("Richiesta dell'elenco utenti da: " + user.getUsername());
		Utente utente;
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			utente = loginController.getUserDetailsByUsername(username);
			status = utente != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			utente = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Utente> response = new ResponseEntity<Utente>(utente, status);
		return response;
    }
    
    /**
	 * Questo metodo può essere usato per inserire un nuovo utente a sistema.
	 * @param utente
	 * @param authenticationString
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Utente> insert(@Valid @RequestBody Utente utente, @RequestHeader("authorization") String authenticationString) {
		Utente user = loginController.getUserByAuthenticationString(authenticationString, true);
		logger.info("Richiesta di inserimento nuovo utente da: " + user.getUsername());
		Utente nuovoUtente;
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			nuovoUtente = loginController.insertNewUser(utente);
			status = user != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		} else {
			nuovoUtente = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Utente> response = new ResponseEntity<Utente>(nuovoUtente, status);
		return response;
	}
	
	/**
	 * Questo metodo può essere usato per inserire un nuovo utente a sistema.
	 * @param utente
	 * @param authenticationString
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Void> updateUserDetails(@Valid @RequestBody Utente utente, @RequestHeader("authorization") String authenticationString) {
		Utente user = loginController.getUserByAuthenticationString(authenticationString, true);
		logger.info("Richiesta di aggiornamento dettagli utente da: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			boolean result = loginController.updateUserDetails(utente);
			status = result ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}

}
