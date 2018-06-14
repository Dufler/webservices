package it.ltc.services.logica.controller.trasporti;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.common.CapDao;
import it.ltc.database.dao.common.VersioneTabellaDao;
import it.ltc.database.dao.common.model.CriteriUltimaModifica;
import it.ltc.database.model.centrale.Cap;
import it.ltc.database.model.centrale.VersioneTabella;
import it.ltc.services.logica.validation.trasporti.CapValidator;
import it.ltc.services.logica.validation.trasporti.CriteriUltimaModificaValidator;

@Controller
@RequestMapping("/cap")
public class CapController {
	
	private static final Logger logger = Logger.getLogger("CapController");
	
	private final VersioneTabellaDao daoVersione;
	
	@Autowired
	@Qualifier("CapDao")
	private CapDao dao;
	
	@Autowired
	private CapValidator validatorCap;
	
	@Autowired
	private CriteriUltimaModificaValidator validatorCriteriModifica;
	
	@InitBinder("cap")
	protected void initCapBinder(WebDataBinder binder) {
	    binder.setValidator(validatorCap);
	}
	
	@InitBinder("criteriUltimaModifica")
	protected void initCriteriModificaBinder(WebDataBinder binder) {
	    binder.setValidator(validatorCriteriModifica);
	}
	
	public CapController() {
		daoVersione = new VersioneTabellaDao();
	}
	
	//TODO - Aggiungere i controlli sui permessi per tutti i metodi
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/ultimamodifica")
	public ResponseEntity<List<Cap>> trovaRecenti(@Valid @RequestBody CriteriUltimaModifica criteri, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i cap modificati recentemente.");
		VersioneTabella versioneTabellaSpedizioni = daoVersione.trovaDaCodice("cap");
		Date dataVersione = versioneTabellaSpedizioni.getDataVersione();
		boolean reset = dataVersione.after(criteri.getDataUltimaModifica());
		List<Cap> entitites = reset ? dao.trovaTutti() : dao.trovaDaUltimaModifica(criteri);
		if (reset)
			logger.info("La data richiesta " + criteri.getDataUltimaModifica() + " è successiva alla data versione tabella " + dataVersione);
		HttpStatus status = reset ? HttpStatus.NON_AUTHORITATIVE_INFORMATION : HttpStatus.OK;
		status = entitites.isEmpty() ? HttpStatus.NO_CONTENT : status;
		ResponseEntity<List<Cap>> response = new ResponseEntity<List<Cap>>(entitites, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Cap>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i CAP.");
		List<Cap> cap = dao.trovaTutti();
		HttpStatus status = cap.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<Cap>> response = new ResponseEntity<List<Cap>>(cap, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<Cap> cerca(@RequestBody Cap cap, @RequestHeader("authorization") String authenticationString) {
		logger.info("Ricerca di un CAP tramite localita e cap.");
		String codice = cap.getCap();
		String localita = cap.getLocalita();
		Cap entity = localita != null ? dao.trovaDaCapELocalita(codice, localita) : dao.trovaDaCap(codice);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.NO_CONTENT;
		ResponseEntity<Cap> response = new ResponseEntity<Cap>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Cap> inserisci(@Valid @RequestBody Cap cap, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo cap.");
		Cap entity = dao.inserisci(cap);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<Cap> response = new ResponseEntity<Cap>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Cap> modifica(@Valid @RequestBody Cap cap, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un cap.");
		Cap entity = dao.aggiorna(cap);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Cap> response = new ResponseEntity<Cap>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Cap> elimina(@RequestBody Cap cap, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un cap.");
		Cap entity = dao.elimina(cap);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Cap> response = new ResponseEntity<Cap>(entity, status);
		return response;
	}

}
