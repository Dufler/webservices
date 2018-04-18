package it.ltc.services.clienti.controller.fornitore;

import java.util.List;

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

import it.ltc.database.dao.common.LoginController;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.services.clienti.data.fornitore.FornitoreDAO;
import it.ltc.services.clienti.data.fornitore.FornitoreDAOImpl;
import it.ltc.services.clienti.data.fornitore.FornitoreLegacyDAOImpl;
import it.ltc.services.clienti.model.prodotto.FornitoreJSON;
import it.ltc.services.clienti.validation.FornitoreValidator;

@Controller
@RequestMapping("/fornitore")
public class FornitoreController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("Fornitore");
	
	private final LoginController loginManager;
	
	@Autowired
	private FornitoreValidator validator;
	
	@InitBinder
	protected void initCaricoBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public FornitoreController() {
		loginManager = LoginController.getInstance();
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<FornitoreJSON> inserisci(@Valid @RequestBody FornitoreJSON fornitore, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di inserimento fornitore: " + fornitore);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		FornitoreJSON entity;
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			FornitoreDAO<?> dao = getDao(user, commessa);
			entity = dao.inserisci(fornitore);
			status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
			logger.info("Creato nuovo fornitore: " + fornitore);
		} else {
			entity = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<FornitoreJSON> response = new ResponseEntity<FornitoreJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<FornitoreJSON> modifica(@Valid @RequestBody FornitoreJSON fornitore, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica fornitore: " + fornitore);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		FornitoreJSON entity;
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			FornitoreDAO<?> dao = getDao(user, commessa);
			entity = dao.aggiorna(fornitore);
			status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Modificato il fornitore: " + fornitore.toString());
		} else {
			entity = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di aggiornamento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<FornitoreJSON> response = new ResponseEntity<FornitoreJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<FornitoreJSON> elimina(@RequestBody FornitoreJSON fornitore, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di eliminazione fornitore: " + fornitore);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		FornitoreJSON entity;
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			FornitoreDAO<?> dao = getDao(user, commessa);
			entity = dao.elimina(fornitore);
			status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Eliminato fornitore: " + fornitore.toString());
		} else {
			entity = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di eliminazione fornitore fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<FornitoreJSON> response = new ResponseEntity<FornitoreJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<FornitoreJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di elenco fornitori");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		List<FornitoreJSON> fornitori;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			FornitoreDAO<?> dao = getDao(user, commessa);
			fornitori = dao.trovaTutti();
			status = fornitori.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Prodotti trovati: " + fornitori.size());
		} else {
			fornitori = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<List<FornitoreJSON>> response = new ResponseEntity<List<FornitoreJSON>>(fornitori, status);
		return response;
	}
	
	private FornitoreDAO<?> getDao(Utente user, String risorsaCommessa) {
		FornitoreDAO<?> dao;
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null) {
			String persistenceUnitName = commessa.getNomeRisorsa();
			dao = commessa.isLegacy() ? new FornitoreLegacyDAOImpl(persistenceUnitName) : new FornitoreDAOImpl(persistenceUnitName);
		} else {
			dao = null;
		}
		return dao;
 	}

}
