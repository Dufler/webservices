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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.centrale.FatturaPreferenzeCommessa;
import it.ltc.services.logica.data.fatturazione.PreferenzeFatturazioneDAO;
import it.ltc.services.logica.validation.fatturazione.PreferenzeFatturazioneValidator;

@Controller
@RequestMapping("/preferenzefatturazione")
public class PreferenzeFatturazioneController {
	
	private static final Logger logger = Logger.getLogger("PreferenzeFatturazioneController");
	
	@Autowired
	private PreferenzeFatturazioneDAO dao;
	
	@Autowired
	private PreferenzeFatturazioneValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - inserire controlli su autenticazione e permessi.
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<FatturaPreferenzeCommessa>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le preferenze di fatturazione.");
		List<FatturaPreferenzeCommessa> ambiti = dao.trovaTutti();
		ResponseEntity<List<FatturaPreferenzeCommessa>> response = new ResponseEntity<List<FatturaPreferenzeCommessa>>(ambiti, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<FatturaPreferenzeCommessa> inserisci(@Valid @RequestBody FatturaPreferenzeCommessa ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova preferenza di fatturazione.");
		FatturaPreferenzeCommessa entity = dao.inserisci(ambito);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaPreferenzeCommessa> response = new ResponseEntity<FatturaPreferenzeCommessa>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<FatturaPreferenzeCommessa> modifica(@Valid @RequestBody FatturaPreferenzeCommessa ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una preferenza di fatturazione.");
		FatturaPreferenzeCommessa entity = dao.aggiorna(ambito);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaPreferenzeCommessa> response = new ResponseEntity<FatturaPreferenzeCommessa>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<FatturaPreferenzeCommessa> elimina(@Valid @RequestBody FatturaPreferenzeCommessa ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una preferenza di fatturazione.");
		FatturaPreferenzeCommessa entity = dao.elimina(ambito);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaPreferenzeCommessa> response = new ResponseEntity<FatturaPreferenzeCommessa>(entity, status);
		return response;
	}

}
