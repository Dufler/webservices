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

import it.ltc.database.model.centrale.CdgCostiRicaviGenericiFase;
import it.ltc.services.logica.data.cdg.CostoRicavoGenericoPerFaseDAO;
import it.ltc.services.logica.validation.cdg.CdgCostiRicaviGenericiFaseValidator;

@Controller
@RequestMapping("/cdg/costiricavigenericifase")
public class CostiRicaviGenericiFaseController {
	
	private static final Logger logger = Logger.getLogger("CostiRicaviGenericiFaseController");
	
	@Autowired
	private CostoRicavoGenericoPerFaseDAO dao;
	
	@Autowired
	private CdgCostiRicaviGenericiFaseValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public CostiRicaviGenericiFaseController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgCostiRicaviGenericiFase>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i costi e ricavi generici del controllo di gestione.");
		List<CdgCostiRicaviGenericiFase> entities = dao.trovaTutti();
		ResponseEntity<List<CdgCostiRicaviGenericiFase>> response = new ResponseEntity<List<CdgCostiRicaviGenericiFase>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviGenericiFase> inserisci(@Valid @RequestBody CdgCostiRicaviGenericiFase crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo costo o ricavo generici del controllo di gestione.");
		CdgCostiRicaviGenericiFase entity = dao.inserisci(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviGenericiFase> response = new ResponseEntity<CdgCostiRicaviGenericiFase>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviGenericiFase> modifica(@Valid @RequestBody CdgCostiRicaviGenericiFase crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un costo o ricavo generici del controllo di gestione.");
		CdgCostiRicaviGenericiFase entity = dao.aggiorna(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviGenericiFase> response = new ResponseEntity<CdgCostiRicaviGenericiFase>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviGenericiFase> elimina(@Valid @RequestBody CdgCostiRicaviGenericiFase crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un costo o ricavo generici del controllo di gestione.");
		CdgCostiRicaviGenericiFase entity = dao.elimina(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviGenericiFase> response = new ResponseEntity<CdgCostiRicaviGenericiFase>(entity, status);
		return response;
	}

}
