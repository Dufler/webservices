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

import it.ltc.database.model.centrale.CdgCostiRicaviCommesse;
import it.ltc.services.logica.data.cdg.CostoRicavoCommesseDAO;
import it.ltc.services.logica.validation.cdg.CdgCostiRicaviCommesseValidator;

@Controller
@RequestMapping("/cdg/costiricavicommesse")
public class CostiRicaviCommesseController {
	
	private static final Logger logger = Logger.getLogger("CostiRicaviCommesseController");
	
	@Autowired
	private CostoRicavoCommesseDAO dao;
	
	@Autowired
	private CdgCostiRicaviCommesseValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public CostiRicaviCommesseController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgCostiRicaviCommesse>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i costi e ricavi per commessa del controllo di gestione.");
		List<CdgCostiRicaviCommesse> entities = dao.trovaTutti();
		ResponseEntity<List<CdgCostiRicaviCommesse>> response = new ResponseEntity<List<CdgCostiRicaviCommesse>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviCommesse> inserisci(@Valid @RequestBody CdgCostiRicaviCommesse crCommesse, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo costo o ricavo per commessa del controllo di gestione.");
		CdgCostiRicaviCommesse entity = dao.inserisci(crCommesse);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviCommesse> response = new ResponseEntity<CdgCostiRicaviCommesse>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviCommesse> modifica(@Valid @RequestBody CdgCostiRicaviCommesse crCommesse, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un costo o ricavo per commessa del controllo di gestione.");
		CdgCostiRicaviCommesse entity = dao.aggiorna(crCommesse);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviCommesse> response = new ResponseEntity<CdgCostiRicaviCommesse>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviCommesse> elimina(@Valid @RequestBody CdgCostiRicaviCommesse crCommesse, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un costo o ricavo per commessa del controllo di gestione.");
		CdgCostiRicaviCommesse entity = dao.elimina(crCommesse);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviCommesse> response = new ResponseEntity<CdgCostiRicaviCommesse>(entity, status);
		return response;
	}

}
