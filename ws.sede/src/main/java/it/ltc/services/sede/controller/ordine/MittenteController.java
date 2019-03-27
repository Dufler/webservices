package it.ltc.services.sede.controller.ordine;

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

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IMittenteDao;
import it.ltc.model.shared.json.cliente.IndirizzoJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.permission.Permessi;
import it.ltc.services.sede.data.ordine.FactoryDaoMittenti;
import it.ltc.services.sede.validation.carico.IndirizzoValidator;

@Controller
@RequestMapping("/ordine/mittente")
public class MittenteController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	public static final int ID_CRUD_ORDINI = Permessi.UFFICIO_USCITE.getID();
	
	private static final Logger logger = Logger.getLogger("MittenteController");
	
	@Autowired
	private FactoryDaoMittenti factory;
	
	@Autowired
	private IndirizzoValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<IndirizzoJSON> inserisci(@Valid @RequestBody IndirizzoJSON mittente, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di inserimento mittente: " + mittente + " Utente: " + user.getUsername());
		IMittenteDao dao = factory.getDao(user, commessa);
		IndirizzoJSON entity = dao.inserisci(mittente);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		logger.info("Creato nuovo mittente: " + mittente);
		ResponseEntity<IndirizzoJSON> response = new ResponseEntity<IndirizzoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<IndirizzoJSON> modifica(@Valid @RequestBody IndirizzoJSON mittente, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di modifica mittente: " + mittente  + " Utente: " + user.getUsername());
		IMittenteDao dao = factory.getDao(user, commessa);
		IndirizzoJSON entity = dao.aggiorna(mittente);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		logger.info("Modificato il mittente: " + mittente.toString());
		ResponseEntity<IndirizzoJSON> response = new ResponseEntity<IndirizzoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<IndirizzoJSON> elimina(@RequestBody IndirizzoJSON mittente, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di eliminazione mittente: " + mittente  + " Utente: " + user.getUsername());
		IMittenteDao dao = factory.getDao(user, commessa);
		IndirizzoJSON entity = dao.elimina(mittente);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		logger.info("Eliminato mittente: " + mittente.toString());
		ResponseEntity<IndirizzoJSON> response = new ResponseEntity<IndirizzoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value = "/cerca")
	public ResponseEntity<List<IndirizzoJSON>> cerca(@RequestBody IndirizzoJSON filtro, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di ricerca mittenti"  + " Utente: " + user.getUsername());		
		IMittenteDao dao = factory.getDao(user, commessa);
		List<IndirizzoJSON> mittenti = dao.cerca(filtro);
		HttpStatus status = mittenti.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Mittenti trovati: " + mittenti.size());
		ResponseEntity<List<IndirizzoJSON>> response = new ResponseEntity<List<IndirizzoJSON>>(mittenti, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<IndirizzoJSON> trovaDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer id) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di mittente da ID"  + " Utente: " + user.getUsername());		
		IMittenteDao dao = factory.getDao(user, commessa);
		IndirizzoJSON mittente = dao.trovaPerID(id);
		HttpStatus status = mittente == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<IndirizzoJSON> response = new ResponseEntity<IndirizzoJSON>(mittente, status);
		return response;
	}

}
