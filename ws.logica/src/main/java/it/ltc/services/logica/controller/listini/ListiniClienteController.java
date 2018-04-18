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

import it.ltc.database.model.centrale.ListinoCommessa;
import it.ltc.services.logica.data.listini.clienti.ListiniClienteDAO;
import it.ltc.services.logica.validation.listini.ListinoClienteValidator;

@Controller
@RequestMapping("/listinocliente")
public class ListiniClienteController {
	
	private static final Logger logger = Logger.getLogger("ListiniClienteController");
	
	@Autowired
	private ListiniClienteDAO dao;
	
	@Autowired
	private ListinoClienteValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - inserire controlli su autenticazione e permessi.
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<ListinoCommessa>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte i listini dei clienti.");
		List<ListinoCommessa> listini = dao.trovaTutti();
		ResponseEntity<List<ListinoCommessa>> response = new ResponseEntity<List<ListinoCommessa>>(listini, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<ListinoCommessa> trovaListino(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo il listino.");
		ListinoCommessa listino = dao.trova(id);
		HttpStatus status = listino != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ListinoCommessa> response = new ResponseEntity<ListinoCommessa>(listino, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ListinoCommessa> inserisci(@Valid @RequestBody ListinoCommessa listino, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo listino.");
		ListinoCommessa entity = dao.inserisci(listino);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCommessa> response = new ResponseEntity<ListinoCommessa>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<ListinoCommessa> modifica(@Valid @RequestBody ListinoCommessa listino, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un listino.");
		ListinoCommessa entity = dao.aggiorna(listino);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCommessa> response = new ResponseEntity<ListinoCommessa>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<ListinoCommessa> elimina(@Valid @RequestBody ListinoCommessa listino, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un listino.");
		ListinoCommessa entity = dao.elimina(listino);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCommessa> response = new ResponseEntity<ListinoCommessa>(entity, status);
		return response;
	}

}
