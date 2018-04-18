package it.ltc.services.logica.controller.listini;

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

import it.ltc.database.model.centrale.ListinoCommessaVoce;
import it.ltc.services.logica.data.listini.clienti.VociListinoClienteDAO;
import it.ltc.services.logica.validation.listini.VoceListinoClienteValidator;

@Controller
@RequestMapping("/vocilistinocliente")
public class VociListinoClienteController {
	
	private static final Logger logger = Logger.getLogger("VociListinoClienteController");
	
	@Autowired
	private VociListinoClienteDAO dao;
	
	@Autowired
	private VoceListinoClienteValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - inserire controlli su autenticazione e permessi.
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<ListinoCommessaVoce>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le voci di listino dei clienti.");
		List<ListinoCommessaVoce> voci = dao.trovaTutte();
		ResponseEntity<List<ListinoCommessaVoce>> response = new ResponseEntity<List<ListinoCommessaVoce>>(voci, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<ListinoCommessaVoce> trovaVoce(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo la voce di listino.");
		ListinoCommessaVoce voce = dao.trova(id);
		HttpStatus status = voce != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ListinoCommessaVoce> response = new ResponseEntity<ListinoCommessaVoce>(voce, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ListinoCommessaVoce> inserisci(@Valid @RequestBody ListinoCommessaVoce voce, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova voce.");
		ListinoCommessaVoce entity = dao.inserisci(voce);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCommessaVoce> response = new ResponseEntity<ListinoCommessaVoce>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<ListinoCommessaVoce> modifica(@Valid @RequestBody ListinoCommessaVoce voce, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una voce.");
		ListinoCommessaVoce entity = dao.aggiorna(voce);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCommessaVoce> response = new ResponseEntity<ListinoCommessaVoce>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<ListinoCommessaVoce> elimina(@Valid @RequestBody ListinoCommessaVoce voce, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una voce.");
		ListinoCommessaVoce entity = dao.elimina(voce);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCommessaVoce> response = new ResponseEntity<ListinoCommessaVoce>(entity, status);
		return response;
	}

}
