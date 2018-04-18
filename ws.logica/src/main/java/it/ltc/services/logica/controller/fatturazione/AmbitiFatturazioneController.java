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

import it.ltc.database.model.centrale.FatturaAmbito;
import it.ltc.services.logica.data.fatturazione.AmbitiFatturazioneDAO;
import it.ltc.services.logica.validation.fatturazione.AmbitoFatturazioneValidator;

@Controller
@RequestMapping("/ambitifatturazione")
public class AmbitiFatturazioneController {
	
	private static final Logger logger = Logger.getLogger("AmbitiFatturazioneController");
	
	@Autowired
	private AmbitiFatturazioneDAO dao;
	
	@Autowired
	private AmbitoFatturazioneValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - inserire controlli su autenticazione e permessi.
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<FatturaAmbito>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte gli ambiti di fatturazione.");
		List<FatturaAmbito> ambiti = dao.trovaTutti();
		ResponseEntity<List<FatturaAmbito>> response = new ResponseEntity<List<FatturaAmbito>>(ambiti, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<FatturaAmbito> inserisci(@Valid @RequestBody FatturaAmbito ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo ambito.");
		FatturaAmbito entity = dao.inserisci(ambito);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaAmbito> response = new ResponseEntity<FatturaAmbito>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<FatturaAmbito> modifica(@Valid @RequestBody FatturaAmbito ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un ambito.");
		FatturaAmbito entity = dao.aggiorna(ambito);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaAmbito> response = new ResponseEntity<FatturaAmbito>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<FatturaAmbito> elimina(@Valid @RequestBody FatturaAmbito ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un ambito.");
		FatturaAmbito entity = dao.aggiorna(ambito);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaAmbito> response = new ResponseEntity<FatturaAmbito>(entity, status);
		return response;
	}

}
