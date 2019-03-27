package it.ltc.services.sede.controller.operatore;

import java.util.Date;
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

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.operatore.EventiDAO;
import it.ltc.services.sede.model.operatore.EventoJSON;
import it.ltc.services.sede.validation.operatore.EventoJSONValidator;


@Controller
@RequestMapping("/eventi")
public class EventiController extends RestController {

	private static final Logger logger = Logger.getLogger("EventiController");
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private EventiDAO dao;
	
	@Autowired
	private EventoJSONValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json", value="/nuovo")
	public ResponseEntity<EventoJSON> nuovoEvento(@Valid @RequestBody EventoJSON evento, @RequestHeader("authorization") String authenticationString) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Inserisco il nuovo evento per l'utente " + user.getUsername());
		//Imposto le informazioni essenziali
		evento.setOperatore(user.getUsername());
		evento.setDataInizio(new Date());
		evento.setDataFine(null);
		//Vado in inserimento
		EventoJSON entity = dao.inserisci(evento);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<EventoJSON> response = new ResponseEntity<EventoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json", value="/aggiorna")
	public ResponseEntity<EventoJSON> aggiornaEvento(@Valid @RequestBody EventoJSON evento, @RequestHeader("authorization") String authenticationString) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Aggiorno l'evento per l'utente " + user.getUsername());
		//Imposto le informazioni essenziali
		evento.setOperatore(user.getUsername());
		evento.setDataInizio(null);
		evento.setDataFine(null);
		//Vado in inserimento
		EventoJSON entity = dao.inserisci(evento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<EventoJSON> response = new ResponseEntity<EventoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json", value="/chiudi")
	public ResponseEntity<EventoJSON> chiudiEvento(@Valid @RequestBody EventoJSON evento, @RequestHeader("authorization") String authenticationString) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Chiudo l'evento per l'utente " + user.getUsername());
		//Imposto le informazioni essenziali
		evento.setOperatore(user.getUsername());
		evento.setDataInizio(null);
		evento.setDataFine(new Date());
		//Vado in inserimento
		EventoJSON entity = dao.inserisci(evento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<EventoJSON> response = new ResponseEntity<EventoJSON>(entity, status);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<EventoJSON>> trovaEventiPerOperatoreETempo(@RequestBody EventoJSON evento, @RequestHeader("authorization") String authenticationString) {
		checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE); //FIXME : Sarà necessario un permesso particolare per vederle tutte.
		logger.info("Trovo tutte le attività della sede.");		
		List<EventoJSON> entities = dao.trovaEventi(evento.getOperatore(), evento.getDataInizio(), evento.getDataFine());
		ResponseEntity<List<EventoJSON>> response = new ResponseEntity<List<EventoJSON>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<EventoJSON> inserisci(@Valid @RequestBody EventoJSON evento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova attività.");
		EventoJSON entity = dao.inserisci(evento);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<EventoJSON> response = new ResponseEntity<EventoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<EventoJSON> modifica(@Valid @RequestBody EventoJSON evento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un'attività.");
		EventoJSON entity = dao.aggiorna(evento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<EventoJSON> response = new ResponseEntity<EventoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<EventoJSON> elimina(@Valid @RequestBody EventoJSON evento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un'attività.");
		EventoJSON entity = dao.elimina(evento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<EventoJSON> response = new ResponseEntity<EventoJSON>(entity, status);
		return response;
	}
	
}
