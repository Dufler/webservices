package it.ltc.services.sede.controller.carico;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
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

import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.IFornitoreDao;
import it.ltc.model.shared.json.cliente.FornitoreJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.fornitore.FornitoreDAOFactory;
import it.ltc.services.sede.validation.carico.FornitoreValidator;

@Controller
@RequestMapping("/fornitore")
public class FornitoreController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("FornitoreController");
	
	@Autowired
	private FornitoreDAOFactory factory;
	
	@Autowired
	private FornitoreValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<FornitoreJSON> inserisci(@Valid @RequestBody FornitoreJSON fornitore, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di inserimento fornitore: " + fornitore);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		IFornitoreDao dao = factory.getDao(user, commessa);
		FornitoreJSON entity = dao.inserisci(fornitore);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		logger.info("Creato nuovo fornitore: " + fornitore);
		ResponseEntity<FornitoreJSON> response = new ResponseEntity<FornitoreJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<FornitoreJSON> modifica(@Valid @RequestBody FornitoreJSON fornitore, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica fornitore: " + fornitore);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		IFornitoreDao dao = factory.getDao(user, commessa);
		FornitoreJSON entity = dao.aggiorna(fornitore);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		logger.info("Modificato il fornitore: " + fornitore.toString());
		ResponseEntity<FornitoreJSON> response = new ResponseEntity<FornitoreJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<FornitoreJSON> elimina(@RequestBody FornitoreJSON fornitore, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di eliminazione fornitore: " + fornitore);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		IFornitoreDao dao = factory.getDao(user, commessa);
		FornitoreJSON entity = dao.elimina(fornitore);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		logger.info("Eliminato fornitore: " + fornitore.toString());
		ResponseEntity<FornitoreJSON> response = new ResponseEntity<FornitoreJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<FornitoreJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di elenco fornitori");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		IFornitoreDao dao = factory.getDao(user, commessa);
		List<FornitoreJSON> fornitori = dao.trovaTutti();
		HttpStatus status = fornitori.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Fornitori trovati: " + fornitori.size());
		ResponseEntity<List<FornitoreJSON>> response = new ResponseEntity<List<FornitoreJSON>>(fornitori, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<FornitoreJSON> trovaDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer id) {
		logger.info("Nuova richiesta di elenco fornitori");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		IFornitoreDao dao = factory.getDao(user, commessa);
		FornitoreJSON fornitore = dao.trovaDaID(id);
		HttpStatus status = fornitore == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<FornitoreJSON> response = new ResponseEntity<FornitoreJSON>(fornitore, status);
		return response;
	}

}
