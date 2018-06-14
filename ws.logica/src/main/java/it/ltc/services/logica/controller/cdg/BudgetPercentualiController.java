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

import it.ltc.database.model.centrale.CdgBudgetPercentualiCosto;
import it.ltc.services.logica.data.cdg.BudgetPercentualiDAO;
import it.ltc.services.logica.validation.cdg.CdgBudgetPercentualiCostoValidator;

@Controller
@RequestMapping("/cdg/budgetpercentuali")
public class BudgetPercentualiController {
	
	private static final Logger logger = Logger.getLogger("BudgetPercentualiController");
	
	@Autowired
	private BudgetPercentualiDAO dao;
	
	@Autowired
	private CdgBudgetPercentualiCostoValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public BudgetPercentualiController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgBudgetPercentualiCosto>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le percentuali di costo dei budget.");
		List<CdgBudgetPercentualiCosto> entities = dao.trovaTutti();
		ResponseEntity<List<CdgBudgetPercentualiCosto>> response = new ResponseEntity<List<CdgBudgetPercentualiCosto>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgBudgetPercentualiCosto> inserisci(@Valid @RequestBody CdgBudgetPercentualiCosto crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una percentuale di costo di budget.");
		CdgBudgetPercentualiCosto entity = dao.inserisci(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgBudgetPercentualiCosto> response = new ResponseEntity<CdgBudgetPercentualiCosto>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgBudgetPercentualiCosto> modifica(@Valid @RequestBody CdgBudgetPercentualiCosto crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una percentuale di costo di budget.");
		CdgBudgetPercentualiCosto entity = dao.aggiorna(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgBudgetPercentualiCosto> response = new ResponseEntity<CdgBudgetPercentualiCosto>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgBudgetPercentualiCosto> elimina(@Valid @RequestBody CdgBudgetPercentualiCosto crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una percentuale di costo di budget.");
		CdgBudgetPercentualiCosto entity = dao.elimina(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgBudgetPercentualiCosto> response = new ResponseEntity<CdgBudgetPercentualiCosto>(entity, status);
		return response;
	}

}
