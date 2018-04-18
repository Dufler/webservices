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

import it.ltc.database.model.centrale.FatturaDocumento;
import it.ltc.services.logica.data.fatturazione.DocumentiFatturazioneDAO;
import it.ltc.services.logica.validation.fatturazione.DocumentoFatturazioneValidator;

@Controller
@RequestMapping("/documentifatturazione")
public class DocumentiFatturazioneController {
	
	private static final Logger logger = Logger.getLogger("DocumentiFatturazioneController");
	
	@Autowired
	private DocumentiFatturazioneDAO dao;
	
	@Autowired
	private DocumentoFatturazioneValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - inserire controlli su autenticazione e permessi.
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<FatturaDocumento>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i documenti di fatturazione.");
		List<FatturaDocumento> documenti = dao.trovaTutti();
		ResponseEntity<List<FatturaDocumento>> response = new ResponseEntity<List<FatturaDocumento>>(documenti, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<FatturaDocumento> inserisci(@Valid @RequestBody FatturaDocumento documento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo documento.");
		FatturaDocumento entity = dao.inserisci(documento);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaDocumento> response = new ResponseEntity<FatturaDocumento>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<FatturaDocumento> modifica(@Valid @RequestBody FatturaDocumento documento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un documento.");
		FatturaDocumento entity = dao.aggiorna(documento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaDocumento> response = new ResponseEntity<FatturaDocumento>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<FatturaDocumento> elimina(@Valid @RequestBody FatturaDocumento documento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un documento.");
		FatturaDocumento entity = dao.elimina(documento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<FatturaDocumento> response = new ResponseEntity<FatturaDocumento>(entity, status);
		return response;
	}

}
