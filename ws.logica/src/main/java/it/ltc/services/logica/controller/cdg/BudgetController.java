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

import it.ltc.database.model.centrale.CdgBudget;
import it.ltc.services.logica.data.cdg.BudgetDAO;
import it.ltc.services.logica.validation.cdg.CdgBudgetValidator;

@Controller
@RequestMapping("/cdg/budget")
public class BudgetController {
	
	private static final Logger logger = Logger.getLogger("BudgetController");
	
	@Autowired
	private BudgetDAO dao;
	
	@Autowired
	private CdgBudgetValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public BudgetController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgBudget>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i budget.");
		List<CdgBudget> entities = dao.trovaTutti();
		ResponseEntity<List<CdgBudget>> response = new ResponseEntity<List<CdgBudget>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgBudget> inserisci(@Valid @RequestBody CdgBudget crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo budget.");
		CdgBudget entity = dao.inserisci(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgBudget> response = new ResponseEntity<CdgBudget>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgBudget> modifica(@Valid @RequestBody CdgBudget crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un budget.");
		CdgBudget entity = dao.aggiorna(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgBudget> response = new ResponseEntity<CdgBudget>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgBudget> elimina(@Valid @RequestBody CdgBudget crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un budget.");
		CdgBudget entity = dao.elimina(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgBudget> response = new ResponseEntity<CdgBudget>(entity, status);
		return response;
	}

}
