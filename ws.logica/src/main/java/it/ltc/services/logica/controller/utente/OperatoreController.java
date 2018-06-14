package it.ltc.services.logica.controller.utente;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.common.OperatoreDao;
import it.ltc.database.model.centrale.Operatore;

@Controller
@RequestMapping("/operatore")
public class OperatoreController {
	
	private static final Logger logger = Logger.getLogger("OperatoreController");
	
	@Autowired
	private OperatoreDao dao;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Operatore>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti gli operatori.");
		List<Operatore> list = dao.trovaTutti();
		HttpStatus status = list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<Operatore>> response = new ResponseEntity<List<Operatore>>(list, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Operatore> inserisci(@Valid @RequestBody Operatore operatore, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo operatore.");
		Operatore entity = dao.inserisci(operatore);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Operatore> response = new ResponseEntity<Operatore>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Operatore> modifica(@Valid @RequestBody Operatore operatore, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un operatore.");
		Operatore entity = dao.aggiorna(operatore);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Operatore> response = new ResponseEntity<Operatore>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Operatore> elimina(@Valid @RequestBody Operatore operatore, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un operatore.");
		Operatore entity = dao.elimina(operatore);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Operatore> response = new ResponseEntity<Operatore>(entity, status);
		return response;
	}

}
