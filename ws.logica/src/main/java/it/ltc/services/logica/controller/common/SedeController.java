package it.ltc.services.logica.controller.common;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.SedeDao;
import it.ltc.database.model.centrale.Sede;

@Controller
@RequestMapping("/sede")
public class SedeController {
	
	private static final Logger logger = Logger.getLogger("SedeController");
	
	private final SedeDao dao;
    
    public SedeController() {
    	dao = new SedeDao();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Sede> listAllMembers() {
        return dao.trovaTutte();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Sede lookupMemberById(@PathVariable("id") int id) {
        return dao.trovaDaID(id);
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Sede> inserisci(@Valid @RequestBody Sede contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova entity.");
		Sede entity = dao.inserisci(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Sede> response = new ResponseEntity<Sede>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Sede> modifica(@Valid @RequestBody Sede contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una entity.");
		Sede entity = dao.aggiorna(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Sede> response = new ResponseEntity<Sede>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Sede> elimina(@Valid @RequestBody Sede contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una entity.");
		Sede entity = dao.elimina(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Sede> response = new ResponseEntity<Sede>(entity, status);
		return response;
	}

}
