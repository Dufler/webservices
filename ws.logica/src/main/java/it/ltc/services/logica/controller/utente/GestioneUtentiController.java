package it.ltc.services.logica.controller.utente;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.centrale.Utente;
import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.permission.Permessi;
import it.ltc.services.logica.data.utente.UserController;

@Controller
@RequestMapping("/gestioneutenti")
public class GestioneUtentiController extends RestController {
	
	public static final int PERMESSO_LETTURA = Permessi.ADMIN.getID();
	public static final int PERMESSO_GESTIONE = Permessi.ADMIN_GESTIONE_UTENTE.getID();
	
	private static final Logger logger = Logger.getLogger("GestioneUtentiController");
	
	@Autowired
	private UserController daoUtenti;
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Utente>> findAll(@RequestHeader("authorization") String authenticationString) {
    	UtenteUtenti user = checkCredentialsAndPermission(authenticationString, PERMESSO_LETTURA);
		logger.info("Richiesta dell'elenco utenti da: " + user.getUsername());
		List<Utente> utenti = daoUtenti.trovaTutti(true);
		HttpStatus status = utenti.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<Utente>> response = new ResponseEntity<List<Utente>>(utenti, status);
		return response;
    }
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{username}")
	public ResponseEntity<Utente> findDetailsByUsername(@RequestHeader("authorization") String authenticationString, @PathVariable("username") String username) {
    	UtenteUtenti user = checkCredentialsAndPermission(authenticationString, PERMESSO_LETTURA);
		logger.info("Richiesta dettagli utenti da: " + user.getUsername());
		Utente utente = daoUtenti.getUserByUsername(username, true, true);
		HttpStatus status = utente != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Utente> response = new ResponseEntity<Utente>(utente, status);
		return response;
    }
    
    /**
	 * Questo metodo può essere usato per inserire un nuovo utente a sistema.
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Utente> insert(@Valid @RequestBody Utente utente, @RequestHeader("authorization") String authenticationString) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, PERMESSO_GESTIONE);
		logger.info("Richiesta di inserimento nuovo utente da: " + user.getUsername());
		Utente nuovoUtente = daoUtenti.inserisciNuovoUtente(utente);
		HttpStatus status = nuovoUtente != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<Utente> response = new ResponseEntity<Utente>(nuovoUtente, status);
		return response;
	}
	
	/**
	 * Questo metodo può essere usato per modificare permessi, sedi, commesse e features di un utente già presente a sistema.
	 */
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Utente> updateUserDetails(@Valid @RequestBody Utente utente, @RequestHeader("authorization") String authenticationString) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, PERMESSO_GESTIONE);
		logger.info("Richiesta di aggiornamento dettagli utente da: " + user.getUsername());
		Utente utenteAggiornato = daoUtenti.aggiornaUtente(utente);
		HttpStatus status = utenteAggiornato != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Utente> response = new ResponseEntity<Utente>(utenteAggiornato, status);
		return response;
	}

}
