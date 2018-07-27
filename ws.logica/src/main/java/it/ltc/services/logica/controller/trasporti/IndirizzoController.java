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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.common.IndirizzoDao;
import it.ltc.database.dao.common.VersioneTabellaDao;
import it.ltc.database.dao.common.model.CriteriUltimaModifica;
import it.ltc.database.model.centrale.Indirizzo;
import it.ltc.database.model.centrale.VersioneTabella;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.permission.Permessi;
import it.ltc.services.logica.validation.trasporti.CriteriUltimaModificaValidator;
import it.ltc.services.logica.validation.trasporti.IndirizzoValidator;

@Controller
@RequestMapping("/indirizzo")
public class IndirizzoController extends RestController {
	
	private static final Logger logger = Logger.getLogger("IndirizzoController");
	
	private static final int PERMESSO_LETTURA = Permessi.TRASPORTI_INDIRIZZI.getID();
	private static final int PERMESSO_GESTIONE = Permessi.TRASPORTI_INDIRIZZI_GESTIONE.getID();
	
	@Autowired
	private IndirizzoDao daoIndirizzi;
	
	@Autowired
	private CriteriUltimaModificaValidator validatorCriteriModifica;
	
	@Autowired
	private IndirizzoValidator validatorIndirizzi;
	
	@Autowired
	private VersioneTabellaDao daoVersione;
	
	@InitBinder("criteriUltimaModifica")
	protected void initCriteriModificaBinder(WebDataBinder binder) {
	    binder.setValidator(validatorCriteriModifica);
	}
	
	@InitBinder("indirizzo")
	protected void initIndirizzoBinder(WebDataBinder binder) {
	    binder.setValidator(validatorIndirizzi);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/ultimamodifica")
	public ResponseEntity<List<Indirizzo>> trovaRecenti(@Valid @RequestBody CriteriUltimaModifica criteri, @RequestHeader("authorization") String authenticationString) {
		checkCredentialsAndPermission(authenticationString, PERMESSO_LETTURA);
		logger.info("Trovo tutti gli indirizzi modificati recentemente.");
		VersioneTabella versioneTabellaSpedizioni = daoVersione.trovaDaCodice("indirizzo");
		Date dataVersione = versioneTabellaSpedizioni.getDataVersione();
		boolean reset = dataVersione.after(criteri.getDataUltimaModifica());
		List<Indirizzo> indirizzi = reset ? daoIndirizzi.trovaTutti() : daoIndirizzi.trovaDaUltimaModifica(criteri);
		if (reset)
			logger.info("La data richiesta " + criteri.getDataUltimaModifica() + " è successiva alla data versione tabella " + dataVersione);
		HttpStatus status = reset ? HttpStatus.NON_AUTHORITATIVE_INFORMATION : HttpStatus.OK;
		status = indirizzi.isEmpty() ? HttpStatus.NO_CONTENT : status;
		ResponseEntity<List<Indirizzo>> response = new ResponseEntity<List<Indirizzo>>(indirizzi, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Indirizzo> nuovoIndirizzo(@RequestHeader("authorization") String authenticationString, @Valid @RequestBody Indirizzo nuovoIndirizzo) {
		checkCredentialsAndPermission(authenticationString, PERMESSO_GESTIONE);
		Indirizzo indirizzo = daoIndirizzi.inserisci(nuovoIndirizzo);
		HttpStatus status = indirizzo != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<Indirizzo> response = new ResponseEntity<Indirizzo>(indirizzo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Indirizzo> modificaIndirizzo(@RequestHeader("authorization") String authenticationString, @Valid @RequestBody Indirizzo indirizzo) {
		checkCredentialsAndPermission(authenticationString, PERMESSO_GESTIONE);
		Indirizzo entity = daoIndirizzi.aggiorna(indirizzo);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Indirizzo> response = new ResponseEntity<Indirizzo>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Indirizzo> eliminaIndirizzo(@RequestHeader("authorization") String authenticationString, @RequestBody Indirizzo indirizzo) {
		checkCredentialsAndPermission(authenticationString, PERMESSO_GESTIONE);
		Indirizzo entity = daoIndirizzi.elimina(indirizzo);
		HttpStatus	status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Indirizzo> response = new ResponseEntity<Indirizzo>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/{id}")
	public ResponseEntity<Indirizzo> getDettagliIndirizzo(@RequestHeader("authorization") String authenticationString, @Valid @PathVariable("id") int id) {
		checkCredentialsAndPermission(authenticationString, PERMESSO_LETTURA);
		Indirizzo indirizzo = daoIndirizzi.trovaDaID(id);
		HttpStatus status = indirizzo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Indirizzo> response = new ResponseEntity<Indirizzo>(indirizzo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Indirizzo>> getTutti(@RequestHeader("authorization") String authenticationString) {
		checkCredentialsAndPermission(authenticationString, PERMESSO_LETTURA);
		List<Indirizzo> indirizzi = daoIndirizzi.trovaTutti();
		HttpStatus status = indirizzi.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<Indirizzo>> response = new ResponseEntity<List<Indirizzo>>(indirizzi, status);
		return response;
	}

}
