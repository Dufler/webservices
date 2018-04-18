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

import it.ltc.database.model.centrale.ListinoCorriere;
import it.ltc.services.logica.data.listini.corrieri.ListiniCorriereDAO;
import it.ltc.services.logica.validation.listini.ListinoCorriereValidator;

@Controller
@RequestMapping("/listinocorriere")
public class ListiniCorriereController {
	
	private static final Logger logger = Logger.getLogger("ListiniCorriereController");
	
	@Autowired
	private ListiniCorriereDAO dao;
	
	@Autowired
	private ListinoCorriereValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	//TODO - inserire controlli su autenticazione e permessi.
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<ListinoCorriere>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte i listini dei clienti.");
		List<ListinoCorriere> listini = dao.trovaTutti();
		ResponseEntity<List<ListinoCorriere>> response = new ResponseEntity<List<ListinoCorriere>>(listini, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<ListinoCorriere> trovaListino(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo il listino.");
		ListinoCorriere listino = dao.trova(id);
		HttpStatus status = listino != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ListinoCorriere> response = new ResponseEntity<ListinoCorriere>(listino, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ListinoCorriere> inserisci(@Valid @RequestBody ListinoCorriere listino, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo listino.");
		ListinoCorriere entity = dao.inserisci(listino);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCorriere> response = new ResponseEntity<ListinoCorriere>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<ListinoCorriere> modifica(@Valid @RequestBody ListinoCorriere listino, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un listino.");
		ListinoCorriere entity = dao.aggiorna(listino);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCorriere> response = new ResponseEntity<ListinoCorriere>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<ListinoCorriere> elimina(@Valid @RequestBody ListinoCorriere listino, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un listino.");
		ListinoCorriere entity = dao.elimina(listino);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<ListinoCorriere> response = new ResponseEntity<ListinoCorriere>(entity, status);
		return response;
	}

}
