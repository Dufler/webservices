package it.ltc.services.logica.controller.common;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.ltc.database.dao.common.PermessoDao;
import it.ltc.database.model.utente.Permesso;

@Controller
@RequestMapping("/permesso")
public class PermessoController {
	
	private static final Logger logger = Logger.getLogger("PermessoController");
	
	private final PermessoDao dao;
    
    public PermessoController() {
    	dao = PermessoDao.getInstance();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Permesso> lista() {
        return dao.trovaTutti();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Permesso trova(@PathVariable("id") int id) {
        return dao.trovaDaID(id);
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Permesso> inserisci(@Valid @RequestBody Permesso permesso, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo permesso.");
		Permesso entity = dao.inserisci(permesso);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Permesso> response = new ResponseEntity<Permesso>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Permesso> modifica(@Valid @RequestBody Permesso permesso, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un permesso.");
		Permesso entity = dao.aggiorna(permesso);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Permesso> response = new ResponseEntity<Permesso>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Permesso> elimina(@Valid @RequestBody Permesso permesso, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un permesso.");
		Permesso entity = dao.elimina(permesso);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Permesso> response = new ResponseEntity<Permesso>(entity, status);
		return response;
	}

}
