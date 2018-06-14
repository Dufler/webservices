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
import it.ltc.database.dao.common.model.CriteriUltimaModifica;
import it.ltc.database.model.centrale.SpedizioneGiacenza;
import it.ltc.database.model.centrale.VersioneTabella;
import it.ltc.services.logica.data.trasporti.GiacenzeDAO;
import it.ltc.services.logica.validation.trasporti.CriteriUltimaModificaValidator;
import it.ltc.services.logica.validation.trasporti.GiacenzaValidator;

@Controller
@RequestMapping("/giacenza")
public class GiacenzeController {
	
	private static final Logger logger = Logger.getLogger("GiacenzaController");
	
	private final VersioneTabellaDao daoVersione;
	
	@Autowired
	private GiacenzeDAO dao;
	
	@Autowired
	private GiacenzaValidator validator;
	
	@Autowired
	private CriteriUltimaModificaValidator validatorCriteriModifica;
	
	@InitBinder("spedizioneGiacenza")
	protected void initGiacenzaBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@InitBinder("criteriUltimaModifica")
	protected void initCriteriModificaBinder(WebDataBinder binder) {
	    binder.setValidator(validatorCriteriModifica);
	}
	
	public GiacenzeController() {
		daoVersione = new VersioneTabellaDao();
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/ultimamodifica")
	public ResponseEntity<List<SpedizioneGiacenza>> trovaRecenti(@Valid @RequestBody CriteriUltimaModifica criteri, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le giacenze modificate recentemente.");
		VersioneTabella versioneTabellaSpedizioni = daoVersione.trovaDaCodice("spedizione_giacenza");
		Date dataVersione = versioneTabellaSpedizioni.getDataVersione();
		boolean reset = dataVersione.after(criteri.getDataUltimaModifica());
		List<SpedizioneGiacenza> spedizioni = reset ? dao.trovaTutte() : dao.trovaDaUltimaModifica(criteri);
		if (reset)
			logger.info("La data richiesta " + criteri.getDataUltimaModifica() + " è successiva alla data versione tabella " + dataVersione);
		HttpStatus status = reset ? HttpStatus.NON_AUTHORITATIVE_INFORMATION : HttpStatus.OK;
		status = spedizioni.isEmpty() ? HttpStatus.NO_CONTENT : status;
		ResponseEntity<List<SpedizioneGiacenza>> response = new ResponseEntity<List<SpedizioneGiacenza>>(spedizioni, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<SpedizioneGiacenza>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le giacenze.");
		List<SpedizioneGiacenza> giacenze = dao.trovaTutte();
		ResponseEntity<List<SpedizioneGiacenza>> response = new ResponseEntity<List<SpedizioneGiacenza>>(giacenze, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<SpedizioneGiacenza> inserisci(@Valid @RequestBody SpedizioneGiacenza giacenza, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova giacenza.");
		SpedizioneGiacenza entity = dao.inserisci(giacenza);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<SpedizioneGiacenza> response = new ResponseEntity<SpedizioneGiacenza>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<SpedizioneGiacenza> modifica(@Valid @RequestBody SpedizioneGiacenza giacenza, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una giacenza.");
		SpedizioneGiacenza entity = dao.aggiorna(giacenza);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<SpedizioneGiacenza> response = new ResponseEntity<SpedizioneGiacenza>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<SpedizioneGiacenza> elimina(@Valid @RequestBody SpedizioneGiacenza giacenza, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una giacenza.");
		SpedizioneGiacenza entity = dao.aggiorna(giacenza);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<SpedizioneGiacenza> response = new ResponseEntity<SpedizioneGiacenza>(entity, status);
		return response;
	}

}
