package it.ltc.services.sede.controller.operatore;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.common.LoginController;
import it.ltc.database.model.sede.Attivita;
import it.ltc.database.model.utente.Utente;
import it.ltc.services.sede.data.operatore.AttivitaDAO;
import it.ltc.services.sede.validation.operatore.AttivitaValidator;

@Controller
@RequestMapping("/attivita")
public class AttivitaController {
	
	private static final Logger logger = Logger.getLogger("AttivitaController");
	
	private final LoginController loginManager;
	
	@Autowired
	private AttivitaDAO dao;
	
	@Autowired
	private AttivitaValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public AttivitaController() {
		loginManager = LoginController.getInstance();
	}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/cerca")
	public ResponseEntity<List<Attivita>> trovaPerCommessaEData(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le attività per l'operatore indicato.");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		List<Attivita> entities = dao.trovaTutteDaOperatore(user.getUsername());
		HttpStatus status = entities.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<Attivita>> response = new ResponseEntity<List<Attivita>>(entities, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Attivita>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le attività della sede.");
		//TODO : Sarà necessario un permesso particolare per vederle tutte.
		List<Attivita> entities = dao.trovaTutte();
		HttpStatus status = entities.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<Attivita>> response = new ResponseEntity<List<Attivita>>(entities, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Attivita> inserisci(@Valid @RequestBody Attivita evento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova attività.");
		Attivita entity = dao.inserisci(evento);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Attivita> response = new ResponseEntity<Attivita>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Attivita> modifica(@Valid @RequestBody Attivita evento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un'attività.");
		Attivita entity = dao.aggiorna(evento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Attivita> response = new ResponseEntity<Attivita>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Attivita> elimina(@Valid @RequestBody Attivita evento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un'attività.");
		Attivita entity = dao.elimina(evento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Attivita> response = new ResponseEntity<Attivita>(entity, status);
		return response;
	}
	
}
