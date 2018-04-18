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

import it.ltc.database.model.centrale.CdgPeriodico;
import it.ltc.services.logica.data.cdg.PeriodicoDAO;
import it.ltc.services.logica.validation.cdg.CdgPeriodicoValidator;

@Controller
@RequestMapping("/cdg/periodico")
public class PeriodicoController {
	
	private static final Logger logger = Logger.getLogger("PeriodicoController");
	
	@Autowired
	private PeriodicoDAO dao;
	
	@Autowired
	private CdgPeriodicoValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public PeriodicoController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgPeriodico>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte i bilanci periodici del controllo di gestione.");
		List<CdgPeriodico> entities = dao.trovaTutte();
		ResponseEntity<List<CdgPeriodico>> response = new ResponseEntity<List<CdgPeriodico>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgPeriodico> inserisci(@Valid @RequestBody CdgPeriodico contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo bilancio periodico del controllo di gestione.");
		CdgPeriodico entity = dao.inserisci(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgPeriodico> response = new ResponseEntity<CdgPeriodico>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgPeriodico> modifica(@Valid @RequestBody CdgPeriodico contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un bilancio periodico del controllo di gestione.");
		CdgPeriodico entity = dao.aggiorna(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgPeriodico> response = new ResponseEntity<CdgPeriodico>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgPeriodico> elimina(@Valid @RequestBody CdgPeriodico contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un bilancio periodico del controllo di gestione.");
		CdgPeriodico entity = dao.elimina(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgPeriodico> response = new ResponseEntity<CdgPeriodico>(entity, status);
		return response;
	}

}
