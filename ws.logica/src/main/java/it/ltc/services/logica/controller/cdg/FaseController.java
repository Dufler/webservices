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

import it.ltc.database.model.centrale.CdgFase;
import it.ltc.services.logica.data.cdg.FaseDAO;
import it.ltc.services.logica.validation.cdg.CdgFaseValidator;

@Controller
@RequestMapping("/cdg/fase")
public class FaseController {
	
	private static final Logger logger = Logger.getLogger("FaseController");
	
	@Autowired
	private FaseDAO dao;
	
	@Autowired
	private CdgFaseValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public FaseController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgFase>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le fasi del controllo di gestione.");
		List<CdgFase> entities = dao.trovaTutte();
		ResponseEntity<List<CdgFase>> response = new ResponseEntity<List<CdgFase>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgFase> inserisci(@Valid @RequestBody CdgFase contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova fase del controllo di gestione.");
		CdgFase entity = dao.inserisci(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgFase> response = new ResponseEntity<CdgFase>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgFase> modifica(@Valid @RequestBody CdgFase contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una fase del controllo di gestione.");
		CdgFase entity = dao.aggiorna(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgFase> response = new ResponseEntity<CdgFase>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgFase> elimina(@Valid @RequestBody CdgFase contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una fase del controllo di gestione.");
		CdgFase entity = dao.elimina(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgFase> response = new ResponseEntity<CdgFase>(entity, status);
		return response;
	}

}
