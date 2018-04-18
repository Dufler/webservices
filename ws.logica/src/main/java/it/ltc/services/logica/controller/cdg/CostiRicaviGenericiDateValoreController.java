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

import it.ltc.database.model.centrale.CdgCostiRicaviGenericiDateValore;
import it.ltc.services.logica.data.cdg.CostoRicavoGenericoDateValoreDAO;
import it.ltc.services.logica.validation.cdg.CdgCostiRicaviGenericiDateValoreValidator;

@Controller
@RequestMapping("/cdg/costiricavigenericidatevalore")
public class CostiRicaviGenericiDateValoreController {
	
	private static final Logger logger = Logger.getLogger("CostiRicaviGenericiDateValoreController");
	
	@Autowired
	private CostoRicavoGenericoDateValoreDAO dao;
	
	@Autowired
	private CdgCostiRicaviGenericiDateValoreValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public CostiRicaviGenericiDateValoreController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgCostiRicaviGenericiDateValore>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i costi e ricavi generici del controllo di gestione.");
		List<CdgCostiRicaviGenericiDateValore> entities = dao.trovaTutti();
		ResponseEntity<List<CdgCostiRicaviGenericiDateValore>> response = new ResponseEntity<List<CdgCostiRicaviGenericiDateValore>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviGenericiDateValore> inserisci(@Valid @RequestBody CdgCostiRicaviGenericiDateValore crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo costo o ricavo generici del controllo di gestione.");
		CdgCostiRicaviGenericiDateValore entity = dao.inserisci(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviGenericiDateValore> response = new ResponseEntity<CdgCostiRicaviGenericiDateValore>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviGenericiDateValore> modifica(@Valid @RequestBody CdgCostiRicaviGenericiDateValore crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un costo o ricavo generici del controllo di gestione.");
		CdgCostiRicaviGenericiDateValore entity = dao.aggiorna(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviGenericiDateValore> response = new ResponseEntity<CdgCostiRicaviGenericiDateValore>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviGenericiDateValore> elimina(@Valid @RequestBody CdgCostiRicaviGenericiDateValore crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un costo o ricavo generici del controllo di gestione.");
		CdgCostiRicaviGenericiDateValore entity = dao.elimina(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviGenericiDateValore> response = new ResponseEntity<CdgCostiRicaviGenericiDateValore>(entity, status);
		return response;
	}

}
