package it.ltc.services.sede.controller.operatore;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.services.custom.controller.RestController;

@Controller
@RequestMapping("/utente")
public class UtenteController extends RestController {
	
//	private static final Logger logger = Logger.getLogger("UtenteController");
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/login")
	public ResponseEntity<UtenteUtenti> login(@RequestHeader("authorization") String authenticationString) {
		UtenteUtenti user = loginManager.getUserByAuthenticationString(authenticationString, true);
		HttpStatus status = user != null ? HttpStatus.OK : HttpStatus.FORBIDDEN;
		ResponseEntity<UtenteUtenti> response = new ResponseEntity<UtenteUtenti>(user, status);
		return response;
	}
	
//	/**
//	 * Questo metodo può essere usato per cambiare le info generali sull'utente e/o la sua password.
//	 * Ci si aspetta in ingresso un JSON contenente le info da cambiare.
//	 * Se si desira cambiare la password va impostata in chiaro dentro il campo 'nuovaPassword'.
//	 * @param utente il JSON che contiene le nuove info sull'utente.
//	 * @param authenticationString la stringa di autenticazione in base64.
//	 * @return le nuove info appena salvate.
//	 */
//	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", consumes = "application/json", value = "/update")
//	public ResponseEntity<UtenteUtenti> update(@Valid @RequestBody UtenteUtenti utente, @RequestHeader("authorization") String authenticationString) {
//		UtenteUtenti user = loginController.getUserByAuthenticationString(authenticationString, true);
//		logger.info("Richiesta di cambio password per: " + user.getUsername() + ", nuova password: " + utente.getNuovaPassword());
//		HttpStatus status;
//		//Se l'utente si è autenticato con successo e sta facendo la richiesta a suo nome allora procedo con l'aggiornamento.
//		if (user != null && user.getUsername().equals(utente.getUsername())) {
//			user = loginController.updateUserInfos(utente);
//			status = user != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
//		} else {
//			status = HttpStatus.UNAUTHORIZED;
//		}
//		ResponseEntity<UtenteUtenti> response = new ResponseEntity<UtenteUtenti>(user, status);
//		return response;
//	}

}
