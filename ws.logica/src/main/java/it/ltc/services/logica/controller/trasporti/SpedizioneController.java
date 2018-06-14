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
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.SpedizioneServizioDao;
import it.ltc.database.dao.common.VersioneTabellaDao;
import it.ltc.database.dao.common.model.CriteriUltimaModifica;
import it.ltc.database.model.centrale.Spedizione;
import it.ltc.database.model.centrale.SpedizioneServizio;
import it.ltc.database.model.centrale.VersioneTabella;
import it.ltc.services.logica.data.trasporti.SpedizioneDAO;
import it.ltc.services.logica.model.trasporti.CriteriFatturazione;
import it.ltc.services.logica.validation.trasporti.CriteriFatturazioneValidator;
import it.ltc.services.logica.validation.trasporti.CriteriUltimaModificaValidator;
import it.ltc.services.logica.validation.trasporti.SpedizioneValidator;

@Controller
@RequestMapping("/spedizione")
public class SpedizioneController {
	
	private static final Logger logger = Logger.getLogger("SpedizioneController");
	
	private final SpedizioneServizioDao daoServizi;
	
	private final VersioneTabellaDao daoVersione;
	
	@Autowired
	private SpedizioneDAO daoSpedizioni;
	
	@Autowired
	private CriteriFatturazioneValidator validatorCriteriFatturazione;
	
	@Autowired
	private SpedizioneValidator validatorSpedizioni;
	
	@Autowired
	private CriteriUltimaModificaValidator validatorCriteriModifica;
	
	@InitBinder("criteriFatturazione")
	protected void initCriteriFatturazioneBinder(WebDataBinder binder) {
	    binder.setValidator(validatorCriteriFatturazione);
	}
	
	@InitBinder("spedizione")
	protected void initSpedizioneBinder(WebDataBinder binder) {
	    binder.setValidator(validatorSpedizioni);
	}
	
	@InitBinder("criteriUltimaModifica")
	protected void initCriteriModificaBinder(WebDataBinder binder) {
	    binder.setValidator(validatorCriteriModifica);
	}
	
	public SpedizioneController() {
		daoServizi = new SpedizioneServizioDao();
		daoVersione = new VersioneTabellaDao();
	}
	
	//TODO - Aggiungere i controlli sui permessi per tutti i metodi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/servizio")
    public @ResponseBody List<SpedizioneServizio> getServizi() {
        return daoServizi.trovaTutti();
    }
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Spedizione>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le spedizioni.");
		List<Spedizione> spedizioni = daoSpedizioni.trovaTutte();
		ResponseEntity<List<Spedizione>> response = new ResponseEntity<List<Spedizione>>(spedizioni, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/ultimamodifica")
	public ResponseEntity<List<Spedizione>> trovaRecenti(@Valid @RequestBody CriteriUltimaModifica criteri, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le spedizioni modificate recentemente.");
		VersioneTabella versioneTabellaSpedizioni = daoVersione.trovaDaCodice("spedizione");
		Date dataVersione = versioneTabellaSpedizioni.getDataVersione();
		boolean reset = dataVersione.after(criteri.getDataUltimaModifica());
		List<Spedizione> spedizioni = reset ? daoSpedizioni.trovaTutte() : daoSpedizioni.trovaDaUltimaModifica(criteri);
		if (reset)
			logger.info("La data richiesta " + criteri.getDataUltimaModifica() + " è successiva alla data versione tabella " + dataVersione);
		HttpStatus status = reset ? HttpStatus.NON_AUTHORITATIVE_INFORMATION : HttpStatus.OK;
		status = spedizioni.isEmpty() ? HttpStatus.NO_CONTENT : status;
		ResponseEntity<List<Spedizione>> response = new ResponseEntity<List<Spedizione>>(spedizioni, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/fatturabili")
	public ResponseEntity<List<Spedizione>> trovaFatturabili(@Valid @RequestBody CriteriFatturazione criteri, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le spedizioni fatturabili in base alla commessa e le date specificate.");
		List<Spedizione> spedizioni = daoSpedizioni.trovaSpedizioniFatturabili(criteri.getIdCommessa(), criteri.getInizio(), criteri.getFine());
		ResponseEntity<List<Spedizione>> response = new ResponseEntity<List<Spedizione>>(spedizioni, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Spedizione> inserisci(@Valid @RequestBody Spedizione spedizione, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova spedizione.");
		Spedizione nuova = daoSpedizioni.inserisci(spedizione);
		HttpStatus status = nuova != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Spedizione> response = new ResponseEntity<Spedizione>(nuova, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Spedizione> modifica(@Valid @RequestBody Spedizione spedizione, @RequestHeader("authorization") String authenticationString) {
		logger.info("Aggiornamento della spedizione.");
		Spedizione nuova = daoSpedizioni.aggiorna(spedizione);
		HttpStatus status = nuova != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Spedizione> response = new ResponseEntity<Spedizione>(nuova, status);
		return response;
	}

}
