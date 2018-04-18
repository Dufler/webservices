package it.ltc.services.logica.controller.cdg;

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

import it.ltc.database.model.centrale.CdgEvento;
import it.ltc.services.logica.data.cdg.EventoDAO;
import it.ltc.services.logica.validation.cdg.CdgEventoValidator;

@Controller
@RequestMapping("/cdg/evento")
public class EventoController {
	
	private static final Logger logger = Logger.getLogger("EventoController");
	
	@Autowired
	private EventoDAO dao;
	
	@Autowired
	private CdgEventoValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public EventoController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgEvento>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte gli eventi del controllo di gestione.");
		List<CdgEvento> contrassegni = dao.trovaTutti();
		ResponseEntity<List<CdgEvento>> response = new ResponseEntity<List<CdgEvento>>(contrassegni, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgEvento> inserisci(@Valid @RequestBody CdgEvento contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo evento del controllo di gestione.");
		CdgEvento entity = dao.inserisci(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgEvento> response = new ResponseEntity<CdgEvento>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgEvento> modifica(@Valid @RequestBody CdgEvento contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un evento del controllo di gestione.");
		CdgEvento entity = dao.aggiorna(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgEvento> response = new ResponseEntity<CdgEvento>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgEvento> elimina(@Valid @RequestBody CdgEvento contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un evento del controllo di gestione.");
		CdgEvento entity = dao.elimina(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgEvento> response = new ResponseEntity<CdgEvento>(entity, status);
		return response;
	}

}
