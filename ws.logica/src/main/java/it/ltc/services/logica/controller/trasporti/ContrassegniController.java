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

import it.ltc.database.dao.common.TipoContrassegnoDao;
import it.ltc.database.dao.common.VersioneTabellaDao;
import it.ltc.database.model.centrale.SpedizioneContrassegno;
import it.ltc.database.model.centrale.SpedizioneContrassegnoTipo;
import it.ltc.database.model.centrale.VersioneTabella;
import it.ltc.services.logica.data.trasporti.ContrassegniDAO;
import it.ltc.services.logica.model.trasporti.CriteriUltimaModifica;
import it.ltc.services.logica.validation.trasporti.ContrassegnoValidator;
import it.ltc.services.logica.validation.trasporti.CriteriUltimaModificaValidator;

@Controller
@RequestMapping("/contrassegno")
public class ContrassegniController {

	private static final Logger logger = Logger.getLogger("ContrassegniController");
	
	private final TipoContrassegnoDao daoTipiContrassegno;
	private final VersioneTabellaDao daoVersione;
	
	@Autowired
	private ContrassegniDAO dao;
	
	@Autowired
	private CriteriUltimaModificaValidator validatorCriteriModifica;
	
	@Autowired
	private ContrassegnoValidator validator;
	
	@InitBinder("spedizioneContrassegno")
	protected void initContrassegnoBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@InitBinder("criteriUltimaModifica")
	protected void initCriteriModificaBinder(WebDataBinder binder) {
	    binder.setValidator(validatorCriteriModifica);
	}
	
	public ContrassegniController() {
		daoVersione = VersioneTabellaDao.getInstance();
		daoTipiContrassegno = TipoContrassegnoDao.getInstance();
	}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/ultimamodifica")
	public ResponseEntity<List<SpedizioneContrassegno>> trovaRecenti(@Valid @RequestBody CriteriUltimaModifica criteri, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i contrassegni modificati recentemente.");
		VersioneTabella versioneTabellaSpedizioni = daoVersione.findByCodice("spedizione_contrassegno");
		Date dataVersione = versioneTabellaSpedizioni.getDataVersione();
		boolean reset = dataVersione.after(criteri.getDataUltimaModifica());
		List<SpedizioneContrassegno> contrassegni = reset ? dao.trovaTutti() : dao.trovaDaUltimaModifica(criteri);
		if (reset)
			logger.info("La data richiesta " + criteri.getDataUltimaModifica() + " è successiva alla data versione tabella " + dataVersione);
		HttpStatus status = reset ? HttpStatus.NON_AUTHORITATIVE_INFORMATION : HttpStatus.OK;
		status = contrassegni.isEmpty() ? HttpStatus.NO_CONTENT : status;
		ResponseEntity<List<SpedizioneContrassegno>> response = new ResponseEntity<List<SpedizioneContrassegno>>(contrassegni, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<SpedizioneContrassegno>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i contrassegni.");
		List<SpedizioneContrassegno> contrassegni = dao.trovaTutti();
		ResponseEntity<List<SpedizioneContrassegno>> response = new ResponseEntity<List<SpedizioneContrassegno>>(contrassegni, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/tipi")
	public ResponseEntity<List<SpedizioneContrassegnoTipo>> trovaTipiContrassegno(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i tipi di contrassegno.");
		List<SpedizioneContrassegnoTipo> tipi = daoTipiContrassegno.findAll();
		ResponseEntity<List<SpedizioneContrassegnoTipo>> response = new ResponseEntity<List<SpedizioneContrassegnoTipo>>(tipi, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<SpedizioneContrassegno> inserisci(@Valid @RequestBody SpedizioneContrassegno contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova entity.");
		SpedizioneContrassegno entity = dao.inserisci(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<SpedizioneContrassegno> response = new ResponseEntity<SpedizioneContrassegno>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<SpedizioneContrassegno> modifica(@Valid @RequestBody SpedizioneContrassegno contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una entity.");
		SpedizioneContrassegno entity = dao.aggiorna(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<SpedizioneContrassegno> response = new ResponseEntity<SpedizioneContrassegno>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<SpedizioneContrassegno> elimina(@Valid @RequestBody SpedizioneContrassegno contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una entity.");
		SpedizioneContrassegno entity = dao.elimina(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<SpedizioneContrassegno> response = new ResponseEntity<SpedizioneContrassegno>(entity, status);
		return response;
	}
	
}
