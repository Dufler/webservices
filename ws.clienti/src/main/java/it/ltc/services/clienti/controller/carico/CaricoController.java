package it.ltc.services.clienti.controller.carico;

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

import it.ltc.database.dao.common.LoginController;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.services.clienti.data.carico.CaricoDAO;
import it.ltc.services.clienti.data.carico.CaricoDAOImpl;
import it.ltc.services.clienti.data.carico.CaricoLegacyDAOImpl;
import it.ltc.services.clienti.model.prodotto.CaricoJSON;
import it.ltc.services.clienti.model.prodotto.IngressoDettaglioJSON;
import it.ltc.services.clienti.model.prodotto.IngressoJSON;
import it.ltc.services.clienti.model.prodotto.ModificaCaricoJSON;
import it.ltc.services.clienti.validation.CaricoValidator;
import it.ltc.services.clienti.validation.IngressoDettaglioValidator;
import it.ltc.services.clienti.validation.ModificaCaricoValidator;
import it.ltc.services.custom.exception.CustomException;

@Controller
@RequestMapping("/ingresso")
public class CaricoController {
	
	public static final int ID_TEST = 1;
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("Carico");
	
	private final LoginController loginManager;
	
	@Autowired
	private CaricoValidator caricoValidator;
	
	@Autowired
	private IngressoDettaglioValidator dettaglioValidator;
	
	@Autowired
	private ModificaCaricoValidator modificheValidator;
	
	@InitBinder("caricoJSON")
	protected void initCaricoBinder(WebDataBinder binder) {
	    binder.setValidator(caricoValidator);
	}
	
	@InitBinder("ingressoDettaglioJSON")
	protected void initDettaglioBinder(WebDataBinder binder) {
	    binder.setValidator(dettaglioValidator);
	}
	
	@InitBinder("modificaCaricoJSON")
	protected void initModificheBinder(WebDataBinder binder) {
	    binder.setValidator(modificheValidator);
	}
	
	public CaricoController() {
		loginManager = LoginController.getInstance();
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Void> inserisci(@Valid @RequestBody CaricoJSON carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di inserimento carico: " + carico);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			boolean insert = dao.inserisci(carico);
			status = insert ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
			if (insert)
				logger.info("Creato nuovo carico: " + carico);
			else
				logger.error("Impossibile salvare il nuovo carico.");
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/dettaglio")
	public ResponseEntity<Void> inserisciDettaglio(@Valid @RequestBody IngressoDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di inserimento dettaglio di carico: " + dettaglio);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			dao.inserisciDettaglio(dettaglio);
			status = HttpStatus.CREATED;
			logger.info("Creato nuovo dettaglio carico: " + dettaglio);
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento dettaglio carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Void> modifica(@Valid @RequestBody IngressoJSON carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica carico: " + carico);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			boolean successo = dao.aggiorna(carico);
			status = successo ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Modificato il carico: " + carico.toString());
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di aggiornamento carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", value="/dettaglio")
	public ResponseEntity<Void> modificaDettaglio(@Valid @RequestBody IngressoDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica dettaglio carico: " + dettaglio);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			boolean successo = dao.aggiornaDettaglio(dettaglio);
			status = successo ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Modificato il carico: " + dettaglio.toString());
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di aggiornamento dettaglio carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Void> elimina(@RequestBody IngressoJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di eliminazione carico: " + ingresso);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			boolean successo = dao.elimina(ingresso);
			status = successo ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Eliminato carico: " + ingresso.toString());
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di eliminazione carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value="/dettaglio")
	public ResponseEntity<Void> eliminaDettaglio(@RequestBody IngressoDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di eliminazione dettaglio carico: " + dettaglio);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			boolean successo = dao.eliminaDettaglio(dettaglio);
			status = successo ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Eliminato dettaglio carico: " + dettaglio.toString());
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di eliminazione dettaglio carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<IngressoJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di elenco carichi");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		List<IngressoJSON> carichi;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			carichi = dao.trovaTutti();
			status = carichi.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Carichi trovati: " + carichi.size());
		} else {
			carichi = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di elenco carichi fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<List<IngressoJSON>> response = new ResponseEntity<List<IngressoJSON>>(carichi, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<CaricoJSON> dettaglioDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idCarico) {
		logger.info("Nuova richiesta di dettaglio carico");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		CaricoJSON carico;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && idCarico != null) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			carico = dao.trovaDaID(idCarico, true);
			status = (carico == null || carico.getIngresso() == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Carico trovato: " + carico);
		} else {
			carico = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di dettagli carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (idCarico == null) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo del carico errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<CaricoJSON> dettaglioDaRiferimento(@RequestBody IngressoJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di ricerca carico tramite riferimento");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		CaricoJSON carico;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && ingresso != null) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			carico = dao.trovaDaRiferimento(ingresso.getRiferimentoCliente(), true);
			status = (carico == null || carico.getIngresso() == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Carico trovato: " + carico);
		} else {
			carico = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di dettagli carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (ingresso == null) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo del carico errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/semplice/{id}")
	public ResponseEntity<CaricoJSON> dettaglioSempliceDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idCarico) {
		logger.info("Nuova richiesta di dettaglio carico");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		CaricoJSON carico;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && idCarico != null) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			carico = dao.trovaDaID(idCarico, false);
			status = (carico == null || carico.getIngresso() == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Carico trovato: " + carico);
		} else {
			carico = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di dettagli carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (idCarico == null) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo del carico errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/semplice/cerca")
	public ResponseEntity<CaricoJSON> dettaglioSempliceDaRiferimento(@RequestBody IngressoJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di ricerca carico tramite riferimento");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		CaricoJSON carico;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && ingresso != null) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			carico = dao.trovaDaRiferimento(ingresso.getRiferimentoCliente(), false);
			status = (carico == null || carico.getIngresso() == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Carico trovato: " + carico);
		} else {
			carico = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di dettagli carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (ingresso == null) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo del carico errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/modificacaricoditest")
	public ResponseEntity<CaricoJSON> modificaCaricoDiTest(@Valid @RequestBody ModificaCaricoJSON modifiche, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica per un carico di test.");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		CaricoJSON carico;
		if (user != null && user.isAllowedTo(ID_TEST) && modifiche != null) {
			CaricoDAO<?,?> dao = getDao(user, commessa);
			boolean generazioneSeriali = dao.modificaCaricoDiTest(modifiche);
			if (generazioneSeriali) {
				carico = dao.trovaDaID(modifiche.getId(), true);
				status = (carico == null || carico.getIngresso() == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
				logger.info("Carico trovato: " + carico);
			} else {
				carico = null;
				status = HttpStatus.BAD_REQUEST;
			}
		} else {
			carico = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di dettagli carico fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (modifiche.getId() < 1) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo del carico errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	private CaricoDAO<?,?> getDao(Utente user, String risorsaCommessa) {
		CaricoDAO<?,?> dao;
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null) {
			String persistenceUnitName = commessa.getNomeRisorsa();
			dao = commessa.isLegacy() ? new CaricoLegacyDAOImpl(persistenceUnitName) : new CaricoDAOImpl(persistenceUnitName);
		} else {
			throw new CustomException("E' necessario specificare una commessa valida nell'header della richiesta.");
		}
		return dao;
 	}

}
