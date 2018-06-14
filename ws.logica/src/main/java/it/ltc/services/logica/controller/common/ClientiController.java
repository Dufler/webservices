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

import it.ltc.database.dao.common.ClienteDao;
import it.ltc.database.model.centrale.Cliente;
import it.ltc.services.logica.validation.common.ClienteValidator;

@Controller
@RequestMapping("/cliente")
public class ClientiController {
	
	private static final Logger logger = Logger.getLogger("ClientiController");
	
	@Autowired
	private ClienteDao dao;
	
	@Autowired
	private ClienteValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public ClientiController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Cliente>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i clienti.");
		List<Cliente> contrassegni = dao.trovaTutti();
		ResponseEntity<List<Cliente>> response = new ResponseEntity<List<Cliente>>(contrassegni, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Cliente> inserisci(@Valid @RequestBody Cliente contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova entity.");
		Cliente entity = dao.inserisci(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Cliente> response = new ResponseEntity<Cliente>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Cliente> modifica(@Valid @RequestBody Cliente contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una entity.");
		Cliente entity = dao.aggiorna(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Cliente> response = new ResponseEntity<Cliente>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Cliente> elimina(@Valid @RequestBody Cliente contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una entity.");
		Cliente entity = dao.elimina(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Cliente> response = new ResponseEntity<Cliente>(entity, status);
		return response;
	}

}
