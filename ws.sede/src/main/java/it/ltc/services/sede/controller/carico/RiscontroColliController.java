package it.ltc.services.sede.controller.carico;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.exception.CustomException;
import it.ltc.services.sede.data.carico.RiscontroColliDAO;
import it.ltc.services.sede.data.carico.RiscontroColliLegacyDAOImpl;
import it.ltc.services.sede.model.carico.ColloCaricoJSON;
import it.ltc.services.sede.validation.carico.ColloCaricoValidator;

@Controller
@RequestMapping("/carico/collo")
public class RiscontroColliController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("RiscontroColliController");
	
	@Autowired
	private ColloCaricoValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ColloCaricoJSON> inserisci(@Valid @RequestBody ColloCaricoJSON collo, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Inserimento di un nuovo collo nel carico.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		RiscontroColliDAO dao = getDao(user, commessa);
		collo = dao.nuovoCollo(collo);
		HttpStatus status = collo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ColloCaricoJSON> response = new ResponseEntity<ColloCaricoJSON>(collo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<ColloCaricoJSON> aggiorna(@Valid @RequestBody ColloCaricoJSON collo, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Modifica di un collo nel carico.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		RiscontroColliDAO dao = getDao(user, commessa);
		collo = dao.aggiornaCollo(collo);
		HttpStatus status = collo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ColloCaricoJSON> response = new ResponseEntity<ColloCaricoJSON>(collo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<ColloCaricoJSON> elimina(@RequestBody ColloCaricoJSON collo, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Eliminazione di un collo nel carico.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		RiscontroColliDAO dao = getDao(user, commessa);
		collo = dao.eliminaCollo(collo);
		HttpStatus status = collo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ColloCaricoJSON> response = new ResponseEntity<ColloCaricoJSON>(collo, status);
		return response;
	}
	
	private RiscontroColliDAO getDao(Utente user, String risorsaCommessa) {
		RiscontroColliDAO dao;
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null) {
			String persistenceUnitName = commessa.getNomeRisorsa();
			dao = new RiscontroColliLegacyDAOImpl(persistenceUnitName);
		} else {
			throw new CustomException("E' necessario specificare una commessa valida nell'header della richiesta.");
		}
		return dao;
 	}

}
