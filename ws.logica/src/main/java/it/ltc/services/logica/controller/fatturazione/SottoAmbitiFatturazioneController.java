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

import it.ltc.database.model.centrale.FatturaSottoAmbito;
import it.ltc.services.logica.data.fatturazione.SottoAmbitiFatturazioneDAO;
import it.ltc.services.logica.validation.fatturazione.SottoAmbitoFatturazioneValidator;

@Controller
@RequestMapping("/sottoambitifatturazione")
public class SottoAmbitiFatturazioneController {
	
private static final Logger logger = Logger.getLogger("SottoAmbitiFatturazioneController");
	
	@Autowired
	private SottoAmbitiFatturazioneDAO dao;
	
	@Autowired
	private SottoAmbitoFatturazioneValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - inserire controlli su autenticazione e permessi.
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<FatturaSottoAmbito>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i sotto ambiti di fatturazione.");
		List<FatturaSottoAmbito> giacenze = dao.trovaTutti();
		ResponseEntity<List<FatturaSottoAmbito>> response = new ResponseEntity<List<FatturaSottoAmbito>>(giacenze, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<FatturaSottoAmbito> inserisci(@Valid @RequestBody FatturaSottoAmbito ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo sotto ambito.");
		FatturaSottoAmbito entity = dao.inserisci(ambito);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaSottoAmbito> response = new ResponseEntity<FatturaSottoAmbito>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<FatturaSottoAmbito> modifica(@Valid @RequestBody FatturaSottoAmbito ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un sotto ambito.");
		FatturaSottoAmbito entity = dao.aggiorna(ambito);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaSottoAmbito> response = new ResponseEntity<FatturaSottoAmbito>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<FatturaSottoAmbito> elimina(@Valid @RequestBody FatturaSottoAmbito ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un sotto ambito.");
		FatturaSottoAmbito entity = dao.aggiorna(ambito);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaSottoAmbito> response = new ResponseEntity<FatturaSottoAmbito>(entity, status);
		return response;
	}

}
