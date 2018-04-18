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

import it.ltc.database.model.centrale.ListinoCorriereVoce;
import it.ltc.services.logica.data.listini.corrieri.VociListinoCorriereDAO;
import it.ltc.services.logica.validation.listini.VoceListinoCorriereValidator;

@Controller
@RequestMapping("/vocilistinocorriere")
public class VociListinoCorriereController {
	
	private static final Logger logger = Logger.getLogger("VociListinoCorriereController");
	
	@Autowired
	private VociListinoCorriereDAO dao;
	
	@Autowired
	private VoceListinoCorriereValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - inserire controlli su autenticazione e permessi.
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<ListinoCorriereVoce>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le voci di listino dei corrieri.");
		List<ListinoCorriereVoce> voci = dao.trovaTutte();
		ResponseEntity<List<ListinoCorriereVoce>> response = new ResponseEntity<List<ListinoCorriereVoce>>(voci, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<ListinoCorriereVoce> trovaVoce(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo la voce di listino.");
		ListinoCorriereVoce voce = dao.trova(id);
		HttpStatus status = voce != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ListinoCorriereVoce> response = new ResponseEntity<ListinoCorriereVoce>(voce, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ListinoCorriereVoce> inserisci(@Valid @RequestBody ListinoCorriereVoce voce, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova voce.");
		ListinoCorriereVoce entity = dao.inserisci(voce);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCorriereVoce> response = new ResponseEntity<ListinoCorriereVoce>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<ListinoCorriereVoce> modifica(@Valid @RequestBody ListinoCorriereVoce voce, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una voce.");
		ListinoCorriereVoce entity = dao.aggiorna(voce);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCorriereVoce> response = new ResponseEntity<ListinoCorriereVoce>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<ListinoCorriereVoce> elimina(@Valid @RequestBody ListinoCorriereVoce voce, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una voce.");
		ListinoCorriereVoce entity = dao.elimina(voce);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCorriereVoce> response = new ResponseEntity<ListinoCorriereVoce>(entity, status);
		return response;
	}

}
