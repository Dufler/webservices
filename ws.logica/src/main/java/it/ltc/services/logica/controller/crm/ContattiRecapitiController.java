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

import it.ltc.database.model.centrale.CrmContattoRecapiti;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.logica.data.crm.ContattoRecapitiDAO;
import it.ltc.services.logica.validation.crm.ContattoRecapitoValidator;

@Controller
@RequestMapping("/crm/recapito")
public class ContattiRecapitiController extends RestController {
	
	private static final Logger logger = Logger.getLogger("ContattiRecapitiController");
	
	@Autowired
	private ContattoRecapitiDAO dao;
	
	@Autowired
	private ContattoRecapitoValidator validatorRecapito;
	
	@InitBinder("crmContattoRecapiti")
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validatorRecapito);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/contatto/{id}")
	public ResponseEntity<List<CrmContattoRecapiti>> trovaDaContatto(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo tutti i recapiti afferenti al contatto specificato.");
		List<CrmContattoRecapiti> entities = dao.trovaTuttiDaContatto(id);
		ResponseEntity<List<CrmContattoRecapiti>> response = new ResponseEntity<List<CrmContattoRecapiti>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CrmContattoRecapiti> inserisci(@Valid @RequestBody CrmContattoRecapiti recapito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo recapito per il contatto.");
		CrmContattoRecapiti entity = dao.inserisci(recapito);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CrmContattoRecapiti> response = new ResponseEntity<CrmContattoRecapiti>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CrmContattoRecapiti> modifica(@Valid @RequestBody CrmContattoRecapiti contatto, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un recapito per il contatto.");
		CrmContattoRecapiti entity = dao.aggiorna(contatto);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CrmContattoRecapiti> response = new ResponseEntity<CrmContattoRecapiti>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CrmContattoRecapiti> elimina(@RequestBody CrmContattoRecapiti contatto, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un recapito per il contatto.");
		CrmContattoRecapiti entity = dao.elimina(contatto);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CrmContattoRecapiti> response = new ResponseEntity<CrmContattoRecapiti>(entity, status);
		return response;
	}

}
