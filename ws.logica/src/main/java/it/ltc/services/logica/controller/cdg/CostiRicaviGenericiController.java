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

import it.ltc.database.model.centrale.CdgCostiRicaviGenerici;
import it.ltc.services.logica.data.cdg.CostoRicavoGenericoDAO;
import it.ltc.services.logica.validation.cdg.CdgCostiRicaviGenericiValidator;

@Controller
@RequestMapping("/cdg/costiricavigenerici")
public class CostiRicaviGenericiController {
	
	private static final Logger logger = Logger.getLogger("CostiRicaviGenericiController");
	
	@Autowired
	private CostoRicavoGenericoDAO dao;
	
	@Autowired
	private CdgCostiRicaviGenericiValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public CostiRicaviGenericiController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgCostiRicaviGenerici>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i costi e ricavi generici del controllo di gestione.");
		List<CdgCostiRicaviGenerici> entities = dao.trovaTutti();
		ResponseEntity<List<CdgCostiRicaviGenerici>> response = new ResponseEntity<List<CdgCostiRicaviGenerici>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviGenerici> inserisci(@Valid @RequestBody CdgCostiRicaviGenerici crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo costo o ricavo generici del controllo di gestione.");
		CdgCostiRicaviGenerici entity = dao.inserisci(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviGenerici> response = new ResponseEntity<CdgCostiRicaviGenerici>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviGenerici> modifica(@Valid @RequestBody CdgCostiRicaviGenerici crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un costo o ricavo generici del controllo di gestione.");
		CdgCostiRicaviGenerici entity = dao.aggiorna(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviGenerici> response = new ResponseEntity<CdgCostiRicaviGenerici>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgCostiRicaviGenerici> elimina(@Valid @RequestBody CdgCostiRicaviGenerici crGenerici, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un costo o ricavo generici del controllo di gestione.");
		CdgCostiRicaviGenerici entity = dao.elimina(crGenerici);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostiRicaviGenerici> response = new ResponseEntity<CdgCostiRicaviGenerici>(entity, status);
		return response;
	}

}
