package it.ltc.services.sede.controller.prodotto;

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

import it.ltc.database.dao.common.model.CriteriUltimaModifica;
import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.IProdottoDao;
import it.ltc.model.shared.json.cliente.ProdottoJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.prodotto.ProdottoDAOFactory;
import it.ltc.services.sede.validation.generiche.CriteriUltimaModificaValidator;
import it.ltc.services.sede.validation.prodotto.ProdottoValidator;

@Controller
@RequestMapping("/prodotto")
public class ProdottoController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("ProdottoController");
	
	@Autowired
	private ProdottoDAOFactory factory;
	
	@Autowired
	private ProdottoValidator validatorProdotto;
	
	@Autowired
	private CriteriUltimaModificaValidator validatorCriteriModifica;
	
	@InitBinder("prodottoJSON")
	protected void initProdottoBinder(WebDataBinder binder) {
	    binder.setValidator(validatorProdotto);
	}
	
	@InitBinder("criteriUltimaModifica")
	protected void initCriteriModificaBinder(WebDataBinder binder) {
	    binder.setValidator(validatorCriteriModifica);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ProdottoJSON> inserisci(@Valid @RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		logger.info("Nuova richiesta di inserimento prodotto: " + prodotto);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		IProdottoDao dao = factory.getDao(user, risorsaCommessa);
		ProdottoJSON entity = dao.inserisci(prodotto);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		logger.info("Creato nuovo prodotto: " + entity.toString());
		ResponseEntity<ProdottoJSON> response = new ResponseEntity<ProdottoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<ProdottoJSON>> cerca(@RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		logger.info("Nuova richiesta di ricerca");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		IProdottoDao dao = factory.getDao(user, risorsaCommessa);
		List<ProdottoJSON> entities = dao.trova(prodotto);
		HttpStatus status = entities != null ? HttpStatus.OK : HttpStatus.NO_CONTENT;
		ResponseEntity<List<ProdottoJSON>> response = new ResponseEntity<List<ProdottoJSON>>(entities, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<ProdottoJSON> modifica(@Valid @RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		logger.info("Nuova richiesta di modifica prodotto: " + prodotto);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		IProdottoDao dao = factory.getDao(user, risorsaCommessa);
		ProdottoJSON entity = dao.aggiorna(prodotto);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		logger.info("Modificato il prodotto: " + prodotto.toString());
		ResponseEntity<ProdottoJSON> response = new ResponseEntity<ProdottoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<ProdottoJSON> dismetti(@RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		logger.info("Nuova richiesta di dismissione prodotto: " + prodotto);
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		IProdottoDao dao = factory.getDao(user, risorsaCommessa);
		ProdottoJSON entity = dao.dismetti(prodotto);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		logger.info("Dismesso prodotto: " + prodotto.toString());
		ResponseEntity<ProdottoJSON> response = new ResponseEntity<ProdottoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<ProdottoJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		logger.info("Nuova richiesta di elenco prodotti");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		IProdottoDao dao = factory.getDao(user, risorsaCommessa);
		List<ProdottoJSON> prodotti = dao.trovaTutti();
		HttpStatus status = prodotti.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Prodotti trovati: " + prodotti.size());
		ResponseEntity<List<ProdottoJSON>> response = new ResponseEntity<List<ProdottoJSON>>(prodotti, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<ProdottoJSON> trovaDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa, @PathVariable(value="id") Integer idProdotto) {
		logger.info("Nuova richiesta di dettaglio prodotto");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		IProdottoDao dao = factory.getDao(user, risorsaCommessa);
		ProdottoJSON prodotto = dao.trovaPerID(idProdotto);
		HttpStatus status = prodotto == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<ProdottoJSON> response = new ResponseEntity<ProdottoJSON>(prodotto, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/ultimamodifica")
	public ResponseEntity<List<ProdottoJSON>> trovaRecenti(@Valid @RequestBody CriteriUltimaModifica criteri, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Trovo tutte le spedizioni modificate recentemente.");
		IProdottoDao dao = factory.getDao(user, risorsaCommessa);
		List<ProdottoJSON> prodotti = dao.trovaDaUltimaModifica(criteri.getDataUltimaModifica());
		HttpStatus status = prodotti.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<ProdottoJSON>> response = new ResponseEntity<List<ProdottoJSON>>(prodotti, status);
		return response;
	}

}
