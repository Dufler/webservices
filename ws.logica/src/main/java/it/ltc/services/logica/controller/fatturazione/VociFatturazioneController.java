package it.ltc.services.logica.controller.fatturazione;

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

import it.ltc.database.model.centrale.FatturaVoce;
import it.ltc.services.logica.data.fatturazione.VociDocumentiFatturazioneDAO;
import it.ltc.services.logica.model.fatturazione.ElementoFatturazioneJSON;
import it.ltc.services.logica.validation.fatturazione.VoceFatturazioneValidator;

@Controller
@RequestMapping("/vocifatturazione")
public class VociFatturazioneController {
	
	private static final Logger logger = Logger.getLogger("VociFatturazioneController");
	
	@Autowired
	private VociDocumentiFatturazioneDAO dao;
	
	@Autowired
	private VoceFatturazioneValidator validator;
	
	@InitBinder("fatturaVoce")
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - inserire controlli su autenticazione e permessi.
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/documento/{id}")
	public ResponseEntity<List<FatturaVoce>> trovaTutte(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo tutte le voci del documento di fatturazione.");
		List<FatturaVoce> voci = dao.trovaTuttePerFattura(id);
		HttpStatus status = voci != null && !voci.isEmpty() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<List<FatturaVoce>> response = new ResponseEntity<List<FatturaVoce>>(voci, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/voce/{id}")
	public ResponseEntity<FatturaVoce> refresh(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo la voce di fatturazione.");
		FatturaVoce voce = dao.trova(id);
		HttpStatus status = voce != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<FatturaVoce> response = new ResponseEntity<FatturaVoce>(voce, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<FatturaVoce> inserisci(@Valid @RequestBody FatturaVoce voce, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova voce di fatturazione.");
		FatturaVoce entity = dao.inserisci(voce);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaVoce> response = new ResponseEntity<FatturaVoce>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/salvafattura")
	public ResponseEntity<Void> salvaFattura(@RequestBody List<ElementoFatturazioneJSON> elementi, @RequestHeader("authorization") String authenticationString) {
		logger.info("Salvataggio delle voci di fatturazione e aggiornamento del dato base.");
		boolean result = dao.inserisciVoci(elementi);
		HttpStatus status = result ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<FatturaVoce> modifica(@Valid @RequestBody FatturaVoce voce, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una voce di fatturazione.");
		FatturaVoce entity = dao.aggiorna(voce);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaVoce> response = new ResponseEntity<FatturaVoce>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<FatturaVoce> elimina(@Valid @RequestBody FatturaVoce voce, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una voce di fatturazione.");
		FatturaVoce entity = dao.elimina(voce);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaVoce> response = new ResponseEntity<FatturaVoce>(entity, status);
		return response;
	}

}
