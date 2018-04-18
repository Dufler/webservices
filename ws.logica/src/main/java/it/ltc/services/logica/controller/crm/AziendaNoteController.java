package it.ltc.services.logica.controller.crm;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.centrale.AziendaNote;
import it.ltc.services.logica.data.crm.AziendeNoteDAO;
import it.ltc.services.logica.model.crm.FiltroTesto;
import it.ltc.services.logica.validation.crm.AziendaNoteValidator;

@Controller
@RequestMapping("/crm/note")
public class AziendaNoteController {

	private static final Logger logger = Logger.getLogger("AziendaNoteController");
	
	@Autowired
	private AziendeNoteDAO dao;
	
	@Autowired
	private AziendaNoteValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public AziendaNoteController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<AziendaNote>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le note sulle aziende.");
		List<AziendaNote> entities = dao.trovaTutti();
		ResponseEntity<List<AziendaNote>> response = new ResponseEntity<List<AziendaNote>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/azienda/{id}")
	public ResponseEntity<List<AziendaNote>> trovaDaAzienda(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo tutte le note afferenti alla data azienda.");
		List<AziendaNote> entities = dao.trovaDaAzienda(id);
		ResponseEntity<List<AziendaNote>> response = new ResponseEntity<List<AziendaNote>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/contatto/{id}")
	public ResponseEntity<List<AziendaNote>> trovaDaContatto(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo tutte le note afferenti al dato contatto.");
		List<AziendaNote> entities = dao.trovaDaContatto(id);
		ResponseEntity<List<AziendaNote>> response = new ResponseEntity<List<AziendaNote>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<AziendaNote>> trovaDaTesto(@Valid @RequestBody FiltroTesto filtro, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le note che contengono il dato testo.");
		List<AziendaNote> entities = dao.trovaDaParola(filtro.getTesto());
		ResponseEntity<List<AziendaNote>> response = new ResponseEntity<List<AziendaNote>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<AziendaNote> inserisci(@Valid @RequestBody AziendaNote note, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuova nota.");
		AziendaNote entity = dao.inserisci(note);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<AziendaNote> response = new ResponseEntity<AziendaNote>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<AziendaNote> modifica(@Valid @RequestBody AziendaNote note, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un nota.");
		AziendaNote entity = dao.aggiorna(note);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<AziendaNote> response = new ResponseEntity<AziendaNote>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<AziendaNote> elimina(@RequestBody AziendaNote note, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un nota.");
		AziendaNote entity = dao.elimina(note);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<AziendaNote> response = new ResponseEntity<AziendaNote>(entity, status);
		return response;
	}
	
}
