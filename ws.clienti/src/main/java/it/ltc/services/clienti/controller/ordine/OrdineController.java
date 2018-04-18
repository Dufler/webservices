package it.ltc.services.clienti.controller.ordine;

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
import it.ltc.services.clienti.data.ordine.OrdineDAO;
import it.ltc.services.clienti.data.ordine.OrdineDAOImpl;
import it.ltc.services.clienti.data.ordine.OrdineLegacyDAOImpl;
import it.ltc.services.clienti.model.prodotto.OrdineImballatoJSON;
import it.ltc.services.clienti.model.prodotto.OrdineJSON;
import it.ltc.services.clienti.model.prodotto.SpedizioneJSON;
import it.ltc.services.clienti.model.prodotto.UscitaDettaglioJSON;
import it.ltc.services.clienti.model.prodotto.UscitaJSON;
import it.ltc.services.clienti.validation.OrdineValidator;
import it.ltc.services.clienti.validation.SpedizioneValidator;
import it.ltc.services.clienti.validation.UscitaDettaglioValidator;
import it.ltc.services.clienti.validation.UscitaValidator;

@Controller
@RequestMapping("/ordine")
public class OrdineController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("Ordine");
	
	private final LoginController loginManager;
	
	@Autowired
	private OrdineValidator ordineValidator;
	
	@Autowired
	private UscitaValidator uscitaValidator;
	
	@Autowired
	private UscitaDettaglioValidator prodottoValidator;
	
	@Autowired
	private SpedizioneValidator spedizioneValidator;
	
	@InitBinder("ordineJSON")
	protected void initOrdineBinder(WebDataBinder binder) {
	    binder.setValidator(ordineValidator);
	}
	
	@InitBinder("uscitaJSON")
	protected void initUscitaBinder(WebDataBinder binder) {
	    binder.setValidator(uscitaValidator);
	}
	
	@InitBinder("uscitaDettaglioJSON")
	protected void initUscitaDettaglioBinder(WebDataBinder binder) {
	    binder.setValidator(prodottoValidator);
	}
	
	@InitBinder("spedizioneJSON")
	protected void initSpedizioneBinder(WebDataBinder binder) {
	    binder.setValidator(spedizioneValidator);
	}
	
	public OrdineController() {
		loginManager = LoginController.getInstance();
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Void> inserisci(@Valid @RequestBody OrdineJSON ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di inserimento ordine: " + ordine);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			boolean insert = dao.inserisci(ordine);
			status = insert ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
			if (insert)
				logger.info("Creato nuovo ordine: " + ordine);
			else
				logger.error("Impossibile salvare il nuovo ordine.");
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di inserimento ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/dettaglio")
	public ResponseEntity<Void> inserisciDettaglio(@Valid @RequestBody UscitaDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di inserimento dettaglio di carico: " + dettaglio);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
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
	public ResponseEntity<Void> modifica(@Valid @RequestBody UscitaJSON ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica ordine: " + ordine);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			boolean successo = dao.aggiorna(ordine);
			status = successo ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Modificato l'ordine: " + ordine.toString());
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di aggiornamento ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", value="/dettaglio")
	public ResponseEntity<Void> modificaDettaglio(@Valid @RequestBody UscitaDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica dettaglio ordine: " + dettaglio);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			boolean successo = dao.aggiornaDettaglio(dettaglio);
			status = successo ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Modificato il dettaglio ordine: " + dettaglio.toString());
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di aggiornamento dettaglio ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Void> elimina(@RequestBody UscitaJSON ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di eliminazione ordine: " + ordine);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			boolean successo = dao.elimina(ordine);
			status = successo ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Eliminato ordine: " + ordine.toString());
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di eliminazione ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value="/dettaglio")
	public ResponseEntity<Void> eliminaDettaglio(@RequestBody UscitaDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di eliminazione dettaglio ordine: " + dettaglio);
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			boolean successo = dao.eliminaDettaglio(dettaglio);
			status = successo ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
			logger.info("Eliminato dettaglio ordine: " + dettaglio.toString());
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di eliminazione dettaglio ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<UscitaJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di elenco ordini");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		List<UscitaJSON> carichi;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			carichi = dao.trovaTutti();
			status = carichi.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Carichi trovati: " + carichi.size());
		} else {
			carichi = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di elenco ordini fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			logger.warn(message + reason);
		}
		ResponseEntity<List<UscitaJSON>> response = new ResponseEntity<List<UscitaJSON>>(carichi, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="{id}")
	public ResponseEntity<OrdineJSON> dettaglioDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idCarico) {
		logger.info("Nuova richiesta di dettaglio ordine");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		OrdineJSON carico;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && idCarico != null) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			carico = dao.trovaDaID(idCarico);
			status = carico == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Ordine trovato: " + carico);
		} else {
			carico = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di dettagli ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (idCarico == null) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo dell'ordine errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<OrdineJSON> response = new ResponseEntity<OrdineJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<OrdineJSON> dettaglioDaRiferimento(@RequestBody UscitaJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di ricerca ordine tramite riferimento");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		OrdineJSON carico;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && ingresso != null) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			carico = dao.trovaDaRiferimento(ingresso.getRiferimentoOrdine(), true);
			status = carico == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Ordine trovato: " + carico);
		} else {
			carico = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di dettagli ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (ingresso == null) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo dell'ordine errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<OrdineJSON> response = new ResponseEntity<OrdineJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/assegna")
	public ResponseEntity<Void> assegnaOrdinePerRiferimento(@RequestBody UscitaJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di assegnazione ordine tramite riferimento");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && ingresso != null) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			boolean assegna = dao.assegna(ingresso.getRiferimentoOrdine());
			status = assegna ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di assegnazione ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (ingresso == null) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo dell'ordine errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/assegna/{id}")
	public ResponseEntity<Void> assegnaOrdinePerID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") int idCarico) {
		logger.info("Nuova richiesta di assegnazione ordine tramite ID.");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && idCarico > 0) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			boolean assegna = dao.assegna(idCarico);
			status = assegna ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di assegnazione ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (idCarico <= 0) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo dell'ordine errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/imballo")
	public ResponseEntity<OrdineImballatoJSON> ottieniImballoOrdineDaRiferimento(@RequestBody UscitaJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di dettagli dell'imballo tramite riferimento");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		OrdineImballatoJSON imballo;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && ingresso != null) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			imballo = dao.ottieniDettagliImballo(ingresso.getRiferimentoOrdine());
			status = imballo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			imballo = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di dettagli dell'imballo fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (ingresso == null) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo dell'ordine errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<OrdineImballatoJSON> response = new ResponseEntity<OrdineImballatoJSON>(imballo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/imballo/{id}")
	public ResponseEntity<OrdineImballatoJSON>  ottieniImballoOrdineDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") int idOrdine) {
		logger.info("Nuova richiesta di dettagli dell'imballo tramite ID.");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		OrdineImballatoJSON imballo;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && idOrdine > 0) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			imballo = dao.ottieniDettagliImballo(idOrdine);
			status = imballo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			imballo = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di assegnazione ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (idOrdine <= 0) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo dell'ordine errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<OrdineImballatoJSON> response = new ResponseEntity<OrdineImballatoJSON>(imballo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/spedisci")
	public ResponseEntity<Void> spedisci(@Valid @RequestBody SpedizioneJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di spedizione.");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && ingresso != null) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			boolean assegna = dao.spedisci(ingresso);
			status = assegna ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di spedizione fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (ingresso == null) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Informazioni sulla spedizione mancanti.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "text/plain", value="/ddtraw/{id}")
	public ResponseEntity<String> getDocumentoRawDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") int idCarico) {
		logger.info("Nuova richiesta di recupero documento di trasporto tramite ID.");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		String documento;
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && idCarico > 0) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			SpedizioneJSON info = dao.getDocumentoDiTrasporto(idCarico);
			documento = info != null ? new String(info.getDocumentoFiscale().getDocumentoBase64()) : null;
			status = documento != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			documento = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di assegnazione ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (idCarico <= 0) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo dell'ordine errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<String> response = new ResponseEntity<String>(documento, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/ddt/{id}")
	public ResponseEntity<SpedizioneJSON> getDocumentoDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") int idCarico) {
		logger.info("Nuova richiesta di recupero documento di trasporto tramite ID.");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: " + user.getUsername());
		SpedizioneJSON documento;
		HttpStatus status;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE) && idCarico > 0) {
			OrdineDAO<?,?> dao = getDao(user, commessa);
			documento = dao.getDocumentoDiTrasporto(idCarico);
			status = documento != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		} else {
			documento = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di assegnazione ordine fallita: ";
			String reason = user != null ? "Autenticazione fallita" : "Permessi insufficienti";
			if (idCarico <= 0) {
				status = HttpStatus.BAD_REQUEST;
				reason = "Identificativo dell'ordine errato.";
			}
			logger.warn(message + reason);
		}
		ResponseEntity<SpedizioneJSON> response = new ResponseEntity<SpedizioneJSON>(documento, status);
		return response;
	}
	
	private OrdineDAO<?,?> getDao(Utente user, String risorsaCommessa) {
		OrdineDAO<?,?> dao;
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null) {
			String persistenceUnitName = commessa.getNomeRisorsa();
			dao = commessa.isLegacy() ? new OrdineLegacyDAOImpl(persistenceUnitName) : new OrdineDAOImpl(persistenceUnitName);
		} else {
			dao = null;
		}
		return dao;
 	}

}
