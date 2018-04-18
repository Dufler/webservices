package it.ltc.services.logica.controller.common;

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

import it.ltc.database.model.centrale.Documento;
import it.ltc.services.logica.data.fatturazione.DocumentiDAO;
import it.ltc.services.logica.validation.fatturazione.DocumentoValidator;

@Controller
@RequestMapping("/documento")
public class DocumentiController {
	
	private static final Logger logger = Logger.getLogger("DocumentiController");
	
	@Autowired
	private DocumentiDAO dao;
	
	@Autowired
	private DocumentoValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - inserire controlli su autenticazione e permessi.
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Documento>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i documenti.");
		List<Documento> documenti = dao.trovaTutti();
		ResponseEntity<List<Documento>> response = new ResponseEntity<List<Documento>>(documenti, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Documento> inserisci(@Valid @RequestBody Documento ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo documento.");
		Documento entity = dao.inserisci(ambito);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Documento> response = new ResponseEntity<Documento>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Documento> modifica(@Valid @RequestBody Documento ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un documento.");
		Documento entity = dao.aggiorna(ambito);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Documento> response = new ResponseEntity<Documento>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Documento> elimina(@Valid @RequestBody Documento ambito, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un documento.");
		Documento entity = dao.aggiorna(ambito);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Documento> response = new ResponseEntity<Documento>(entity, status);
		return response;
	}

}
