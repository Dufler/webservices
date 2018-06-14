package it.ltc.services.logica.controller.trasporti;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.common.CodiceClienteCorriereDao;
import it.ltc.database.model.centrale.JoinCommessaCorriere;

@Controller
@RequestMapping("/codiceclientecorriere")
public class CodiciClienteController {
	
	private static final Logger logger = Logger.getLogger("CodiciClienteController");
	
	@Autowired
	@Qualifier("CodiceClienteCorriereDao")
	private CodiceClienteCorriereDao dao;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<JoinCommessaCorriere>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i codici cliente per i corrieri.");
		List<JoinCommessaCorriere> lista = dao.trovaTutti();
		ResponseEntity<List<JoinCommessaCorriere>> response = new ResponseEntity<List<JoinCommessaCorriere>>(lista, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<JoinCommessaCorriere> inserisci(@Valid @RequestBody JoinCommessaCorriere codice, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo codice cliente per il corriere.");
		JoinCommessaCorriere entity = dao.inserisci(codice);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<JoinCommessaCorriere> response = new ResponseEntity<JoinCommessaCorriere>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<JoinCommessaCorriere> modifica(@Valid @RequestBody JoinCommessaCorriere codice, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un codice cliente per il corriere.");
		JoinCommessaCorriere entity = dao.aggiorna(codice);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<JoinCommessaCorriere> response = new ResponseEntity<JoinCommessaCorriere>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<JoinCommessaCorriere> elimina(@RequestBody JoinCommessaCorriere codice, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un codice cliente per il corriere.");
		JoinCommessaCorriere entity = dao.elimina(codice);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<JoinCommessaCorriere> response = new ResponseEntity<JoinCommessaCorriere>(entity, status);
		return response;
	}

}
