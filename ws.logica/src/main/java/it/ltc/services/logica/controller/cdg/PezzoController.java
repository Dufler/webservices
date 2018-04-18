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

import it.ltc.database.model.centrale.CdgPezzo;
import it.ltc.services.logica.data.cdg.PezzoDAO;
import it.ltc.services.logica.validation.cdg.CdgPezzoValidator;

@Controller
@RequestMapping("/cdg/pezzo")
public class PezzoController {
	
	private static final Logger logger = Logger.getLogger("PezzoController");
	
	@Autowired
	private PezzoDAO dao;
	
	@Autowired
	private CdgPezzoValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public PezzoController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgPezzo>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti gli abbinamenti commessa e categoria merceologica del controllo di gestione.");
		List<CdgPezzo> entities = dao.trovaTutte();
		ResponseEntity<List<CdgPezzo>> response = new ResponseEntity<List<CdgPezzo>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgPezzo> inserisci(@Valid @RequestBody CdgPezzo contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo pezzo del controllo di gestione.");
		CdgPezzo entity = dao.inserisci(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgPezzo> response = new ResponseEntity<CdgPezzo>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgPezzo> modifica(@Valid @RequestBody CdgPezzo contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un pezzo del controllo di gestione.");
		CdgPezzo entity = dao.aggiorna(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgPezzo> response = new ResponseEntity<CdgPezzo>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgPezzo> elimina(@RequestBody CdgPezzo contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un pezzo del controllo di gestione.");
		CdgPezzo entity = dao.elimina(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgPezzo> response = new ResponseEntity<CdgPezzo>(entity, status);
		return response;
	}

}
