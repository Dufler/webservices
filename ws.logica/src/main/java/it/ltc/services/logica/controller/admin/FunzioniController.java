package it.ltc.services.logica.controller.admin;

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

import it.ltc.database.dao.common.FunzioneDao;
import it.ltc.database.model.centrale.Funzione;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.permission.Permessi;

@Controller
@RequestMapping("/funzione")
public class FunzioniController extends RestController {
	
	private static final Logger logger = Logger.getLogger(FunzioniController.class);
	
	private static final int PERMESSO_GESTIONE = Permessi.ADMIN.getID();
	
	private final FunzioneDao dao;
    
    public FunzioniController() {
    	dao = new FunzioneDao();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Funzione> listAllMembers() {
        return dao.trovaTutte();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Funzione lookupMemberById(@PathVariable("id") String id) {
        return dao.trovaDaNome(id);
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Funzione> inserisci(@Valid @RequestBody Funzione ambito, @RequestHeader("authorization") String authenticationString) {
    	checkCredentialsAndPermission(authenticationString, PERMESSO_GESTIONE);
    	logger.info("Inserimento di una nuova funzione.");
		Funzione entity = dao.inserisci(ambito);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Funzione> response = new ResponseEntity<Funzione>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Funzione> modifica(@Valid @RequestBody Funzione ambito, @RequestHeader("authorization") String authenticationString) {
		checkCredentialsAndPermission(authenticationString, PERMESSO_GESTIONE);
		logger.info("Modifica di una funzione.");
		Funzione entity = dao.aggiorna(ambito);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Funzione> response = new ResponseEntity<Funzione>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Funzione> elimina(@Valid @RequestBody Funzione ambito, @RequestHeader("authorization") String authenticationString) {
		checkCredentialsAndPermission(authenticationString, PERMESSO_GESTIONE);
		logger.info("Eliminazione di una funzione.");
		Funzione entity = dao.aggiorna(ambito);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Funzione> response = new ResponseEntity<Funzione>(entity, status);
		return response;
	}

}
