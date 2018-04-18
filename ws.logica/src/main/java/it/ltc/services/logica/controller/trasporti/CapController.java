package it.ltc.services.logica.controller.trasporti;

import java.util.Date;
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

import it.ltc.database.dao.common.VersioneTabellaDao;
import it.ltc.database.model.centrale.Cap;
import it.ltc.database.model.centrale.VersioneTabella;
import it.ltc.services.logica.data.trasporti.CapDAO;
import it.ltc.services.logica.model.trasporti.CapJSON;
import it.ltc.services.logica.model.trasporti.CriteriUltimaModifica;
import it.ltc.services.logica.validation.trasporti.CriteriUltimaModificaValidator;

@Controller
@RequestMapping("/cap")
public class CapController {
	
	private static final Logger logger = Logger.getLogger("CapController");
	
	private final VersioneTabellaDao daoVersione;
	
	@Autowired
	private CapDAO dao;
	
	@Autowired
	private CriteriUltimaModificaValidator validatorCriteriModifica;
	
	@InitBinder("criteriUltimaModifica")
	protected void initCriteriModificaBinder(WebDataBinder binder) {
	    binder.setValidator(validatorCriteriModifica);
	}
	
	public CapController() {
		daoVersione = VersioneTabellaDao.getInstance();
	}
	
	//TODO - aggiungere validatore
	//TODO - Aggiungere i controlli sui permessi per tutti i metodi
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/ultimamodifica")
	public ResponseEntity<List<CapJSON>> trovaRecenti(@Valid @RequestBody CriteriUltimaModifica criteri, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i cap modificati recentemente.");
		VersioneTabella versioneTabellaSpedizioni = daoVersione.findByCodice("cap");
		Date dataVersione = versioneTabellaSpedizioni.getDataVersione();
		boolean reset = dataVersione.after(criteri.getDataUltimaModifica());
		List<CapJSON> indirizzi = reset ? dao.findAll() : dao.trovaDaUltimaModifica(criteri);
		if (reset)
			logger.info("La data richiesta " + criteri.getDataUltimaModifica() + " è successiva alla data versione tabella " + dataVersione);
		HttpStatus status = reset ? HttpStatus.NON_AUTHORITATIVE_INFORMATION : HttpStatus.OK;
		status = indirizzi.isEmpty() ? HttpStatus.NO_CONTENT : status;
		ResponseEntity<List<CapJSON>> response = new ResponseEntity<List<CapJSON>>(indirizzi, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CapJSON>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i CAP.");
		List<CapJSON> cap = dao.findAll();
		ResponseEntity<List<CapJSON>> response = new ResponseEntity<List<CapJSON>>(cap, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<CapJSON> cerca(@RequestBody Cap cap, @RequestHeader("authorization") String authenticationString) {
		logger.info("Ricerca di un CAP tramite localita e cap.");
		String codice = cap.getId().getCap();
		String localita = cap.getId().getLocalita();
		CapJSON c = localita != null ? dao.findByCapAndTown(codice, localita) : dao.findByCap(codice);
		HttpStatus status = c != null ? HttpStatus.OK : HttpStatus.NO_CONTENT;
		ResponseEntity<CapJSON> response = new ResponseEntity<CapJSON>(c, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Void> inserisci(@Valid @RequestBody CapJSON cap, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo cap.");
		boolean insert = dao.insert(cap);
		HttpStatus status = insert ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Void> modifica(@Valid @RequestBody CapJSON cap, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un cap.");
		boolean update = dao.update(cap);
		HttpStatus status = update ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Void> elimina(@RequestBody CapJSON cap, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un cap.");
		boolean delete = dao.delete(cap);
		HttpStatus status = delete ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}

}
