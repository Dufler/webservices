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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.CommessaDao;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.services.logica.validation.common.CommessaValidator;

@Controller
@RequestMapping("/commessa")
public class CommessaController {
	
	private static final Logger logger = Logger.getLogger(CommessaController.class);
	
    private final CommessaDao dao;
    
    @Autowired
    private CommessaValidator validator;
    
    @InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
    
    public CommessaController() {
    	dao = new CommessaDao();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Commessa trovaDaID(@PathVariable("id") Integer id) {
        return dao.trovaDaID(id);
    }
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Commessa>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le commesse.");
		List<Commessa> entities = dao.trovaTutte();
		ResponseEntity<List<Commessa>> response = new ResponseEntity<List<Commessa>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Commessa> inserisci(@Valid @RequestBody Commessa commessa, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova entity.");
		Commessa entity = dao.inserisci(commessa);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Commessa> response = new ResponseEntity<Commessa>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Commessa> modifica(@Valid @RequestBody Commessa commessa, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una entity.");
		Commessa entity = dao.aggiorna(commessa);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Commessa> response = new ResponseEntity<Commessa>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Commessa> elimina(@Valid @RequestBody Commessa commessa, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una entity.");
		Commessa entity = dao.elimina(commessa);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Commessa> response = new ResponseEntity<Commessa>(entity, status);
		return response;
	}

}
