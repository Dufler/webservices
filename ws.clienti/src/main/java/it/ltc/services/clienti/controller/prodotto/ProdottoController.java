package it.ltc.services.clienti.controller.prodotto;

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
import it.ltc.services.clienti.data.prodotto.ProdottoDAO;
import it.ltc.services.clienti.data.prodotto.ProdottoDAOImpl;
import it.ltc.services.clienti.data.prodotto.ProdottoLegacyDAOImpl;
import it.ltc.services.clienti.model.prodotto.ProdottoJSON;
import it.ltc.services.clienti.validation.ProdottoValidator;

@Controller
@RequestMapping("/prodotto")
public class ProdottoController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("Prodotto");
	
	private final LoginController loginManager;
	
	@Autowired
	private ProdottoValidator validator;
	
	@InitBinder
	protected void initCaricoBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public ProdottoController () {
		loginManager = LoginController.getInstance();
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Void> inserisci(@Valid @RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di inserimento");
		logger.info("Prodotto: " + prodotto);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			ProdottoDAO<?> dao = getDao(user, commessa);
			boolean insert = dao.inserisci(prodotto);
			status = insert ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
			logger.info("Creato nuovo prodotto: " + prodotto.toString());
		} else {
			prodotto = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<ProdottoJSON> cerca(@RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di ricerca");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			ProdottoDAO<?> dao = getDao(user, commessa);
			String sku = prodotto != null ? prodotto.getChiaveCliente() : "";
			prodotto = dao.trovaDaSKU(sku);
			status = prodotto != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			prodotto = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<ProdottoJSON> response = new ResponseEntity<ProdottoJSON>(prodotto, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Void> modifica(@Valid @RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica");
		logger.info("Prodotto: " + prodotto);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			ProdottoDAO<?> dao = getDao(user, commessa);
			boolean successo = dao.aggiorna(prodotto);
			status = successo ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Modificato il prodotto: " + prodotto.toString());
		} else {
			prodotto = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di aggiornamento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Void> dismetti(@RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di dismissione");
		logger.info("Prodotto: " + prodotto);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			ProdottoDAO<?> dao = getDao(user, commessa);
			boolean successo = dao.dismetti(prodotto);
			status = successo ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Dismesso prodotto: " + prodotto.toString());
		} else {
			prodotto = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<ProdottoJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di elenco prodotti");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		List<ProdottoJSON> prodotti;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			ProdottoDAO<?> dao = getDao(user, commessa);
			prodotti = dao.trovaTutti();
			status = prodotti.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Prodotti trovati: " + prodotti.size());
		} else {
			prodotti = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento prodotto fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<List<ProdottoJSON>> response = new ResponseEntity<List<ProdottoJSON>>(prodotti, status);
		return response;
	}
	
	private ProdottoDAO<?> getDao(Utente user, String risorsaCommessa) {
		ProdottoDAO<?> dao;
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null) {
			String persistenceUnitName = commessa.getNomeRisorsa();
			dao = commessa.isLegacy() ? new ProdottoLegacyDAOImpl(persistenceUnitName) : new ProdottoDAOImpl(persistenceUnitName);
		} else {
			dao = null;
		}
		return dao;
 	}

}
