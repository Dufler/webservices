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

import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.json.cliente.CaricoJSON;
import it.ltc.model.shared.json.cliente.IngressoDettaglioJSON;
import it.ltc.model.shared.json.cliente.IngressoJSON;
import it.ltc.model.shared.json.cliente.ModificaCaricoJSON;
import it.ltc.services.clienti.data.carico.CaricoDAO;
import it.ltc.services.clienti.data.carico.CaricoDAOFactory;
import it.ltc.services.clienti.validation.CaricoValidator;
import it.ltc.services.clienti.validation.IngressoDettaglioValidator;
import it.ltc.services.clienti.validation.ModificaCaricoValidator;
import it.ltc.services.custom.controller.RestController;

@Controller
@RequestMapping("/ingresso")
public class CaricoController extends RestController {
	
	public static final int ID_TEST = 1;
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("CaricoController");
	
	@Autowired
	private CaricoDAOFactory factory;
	
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
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CaricoJSON> inserisci(@Valid @RequestBody CaricoJSON carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di inserimento carico: " + carico);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		CaricoJSON entity = dao.inserisci(carico);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/dettaglio")
	public ResponseEntity<IngressoDettaglioJSON> inserisciDettaglio(@Valid @RequestBody IngressoDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di inserimento dettaglio di carico: " + dettaglio);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		IngressoDettaglioJSON entity = dao.inserisciDettaglio(dettaglio);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<IngressoDettaglioJSON> response = new ResponseEntity<IngressoDettaglioJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<IngressoJSON> modifica(@Valid @RequestBody IngressoJSON carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica carico: " + carico);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		IngressoJSON entity = dao.aggiorna(carico);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<IngressoJSON> response = new ResponseEntity<IngressoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", value="/dettaglio")
	public ResponseEntity<IngressoDettaglioJSON> modificaDettaglio(@Valid @RequestBody IngressoDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica dettaglio carico: " + dettaglio);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		IngressoDettaglioJSON entity = dao.aggiornaDettaglio(dettaglio);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<IngressoDettaglioJSON> response = new ResponseEntity<IngressoDettaglioJSON>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<IngressoJSON> elimina(@RequestBody IngressoJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di eliminazione carico: " + ingresso);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		IngressoJSON entity = dao.elimina(ingresso);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<IngressoJSON> response = new ResponseEntity<IngressoJSON>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value="/dettaglio")
	public ResponseEntity<IngressoDettaglioJSON> eliminaDettaglio(@RequestBody IngressoDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di eliminazione dettaglio carico: " + dettaglio);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		IngressoDettaglioJSON entity = dao.eliminaDettaglio(dettaglio);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<IngressoDettaglioJSON> response = new ResponseEntity<IngressoDettaglioJSON>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<IngressoJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di elenco carichi");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		List<IngressoJSON> carichi = dao.trovaTutti();
		HttpStatus status = carichi.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Carichi trovati: " + carichi.size());
		ResponseEntity<List<IngressoJSON>> response = new ResponseEntity<List<IngressoJSON>>(carichi, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<CaricoJSON> dettaglioDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idCarico) {
		logger.info("Nuova richiesta di dettaglio carico");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		CaricoJSON carico = dao.trovaDaID(idCarico, true);
		HttpStatus status = (carico == null || carico.getIngresso() == null) ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<CaricoJSON> dettaglioDaRiferimento(@RequestBody IngressoJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di ricerca carico tramite riferimento");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		CaricoJSON carico = dao.trovaDaRiferimento(ingresso.getRiferimentoCliente(), true);
		HttpStatus status = (carico == null || carico.getIngresso() == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/semplice/{id}")
	public ResponseEntity<CaricoJSON> dettaglioSempliceDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idCarico) {
		logger.info("Nuova richiesta di dettaglio carico");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		CaricoJSON carico = dao.trovaDaID(idCarico, false);
		HttpStatus status = (carico == null || carico.getIngresso() == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/semplice/cerca")
	public ResponseEntity<CaricoJSON> dettaglioSempliceDaRiferimento(@RequestBody IngressoJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di ricerca carico tramite riferimento");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		CaricoJSON carico = dao.trovaDaRiferimento(ingresso.getRiferimentoCliente(), false);
		HttpStatus status = (carico == null || carico.getIngresso() == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/modificacaricoditest")
	public ResponseEntity<CaricoJSON> modificaCaricoDiTest(@Valid @RequestBody ModificaCaricoJSON modifiche, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di modifica per un carico di test.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_TEST);
		logger.info("Utente: " + user.getUsername());
		HttpStatus status;
		CaricoJSON carico;
		CaricoDAO<?,?> dao = factory.getDao(user, commessa);
		boolean successo = dao.modificaCaricoDiTest(modifiche);
		if (successo) {
			carico = dao.trovaDaID(modifiche.getId(), true);
			status = (carico == null || carico.getIngresso() == null) ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Carico trovato: " + carico);
		} else {
			carico = null;
			status = HttpStatus.BAD_REQUEST;
		}
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}

}
