package it.ltc.services.logica.controller.utente;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.centrale.Utente;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.logica.data.utente.UserController;
import it.ltc.services.logica.model.utente.ReimpostaPassword;

@Controller
@RequestMapping("/utente")
public class UtenteController extends RestController {
	
	private static final Logger logger = Logger.getLogger("UtenteController");
	
	@Autowired
	private UserController daoUtenti;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/login")
	public ResponseEntity<UtenteUtenti> login(@RequestHeader("authorization") String authenticationString) {
		UtenteUtenti user = loginManager.getUserByAuthenticationString(authenticationString, true);
		HttpStatus status = user != null ? HttpStatus.OK : HttpStatus.FORBIDDEN;
		ResponseEntity<UtenteUtenti> response = new ResponseEntity<UtenteUtenti>(user, status);
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
		UtenteUtenti user = loginManager.getUserByAuthenticationString(authenticationString, true);
		logger.info("Richiesta di cambio password per: " + user.getUsername() + ", nuova password: " + utente.getNuovaPassword());
		HttpStatus status;
		Utente utenteNuovaPassword;
		//Se l'utente si è autenticato con successo e sta facendo la richiesta a suo nome allora procedo con l'aggiornamento.
		if (user != null && user.getUsername().equals(utente.getUsername())) {
			utenteNuovaPassword = daoUtenti.aggiornaPasswordUtente(utente.getUsername(), utente.getNuovaPassword());
			status = utenteNuovaPassword != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			utenteNuovaPassword = null;
			status = HttpStatus.UNAUTHORIZED;
		}
		ResponseEntity<Utente> response = new ResponseEntity<Utente>(utenteNuovaPassword, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PATCH, consumes = "application/json", value = "/reimpostapassword")
	public ResponseEntity<Void> reimpostaPassword(@RequestBody Utente utente) {
		logger.info("Richiesta di reimpostare la password.");
		HttpStatus status;
		if (utente != null && utente.getUsername() != null) {
			Utente user = daoUtenti.reimpostaPassword(utente.getUsername());
			status = user != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, consumes = "application/json", value = "/esisterisorsa")
	public ResponseEntity<Void> esisteRisorsa(@RequestBody ReimpostaPassword risorsa) {
		logger.info("Richiesta di verifica esistenza risorsa temporanea.");
		HttpStatus status;
		if (risorsa != null && risorsa.getRisorsa() != null && !risorsa.getRisorsa().isEmpty()) {
			Utente user = daoUtenti.trovaDaRisorsa(risorsa.getRisorsa());
			status = user != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/reimpostapassword")
	public ResponseEntity<Void> reimpostaPassword(@RequestBody ReimpostaPassword risorsa) {
		logger.info("Richiesta di reimpostare la password tramite risorsa temporanea.");
		HttpStatus status;
		if (risorsa != null && risorsa.getRisorsa() != null && !risorsa.getRisorsa().isEmpty() && risorsa.getNuovaPassword() != null) {
			Utente user = daoUtenti.reimpostaNuovaPassword(risorsa.getRisorsa(), risorsa.getNuovaPassword());
			status = user != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}

}
