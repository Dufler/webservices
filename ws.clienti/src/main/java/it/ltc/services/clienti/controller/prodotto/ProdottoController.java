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

import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.IProdottoDao;
import it.ltc.model.shared.json.cliente.ProdottoJSON;
import it.ltc.services.clienti.data.prodotto.ProdottoDAOFactory;
import it.ltc.services.clienti.validation.ProdottoValidator;
import it.ltc.services.custom.controller.RestController;

@Controller
@RequestMapping("/prodotto")
public class ProdottoController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("ProdottoController");
	
	@Autowired
	private ProdottoDAOFactory factory;
	
	@Autowired
	private ProdottoValidator validator;
	
	@InitBinder
	protected void initCaricoBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ProdottoJSON> inserisci(@Valid @RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di inserimento prodotto dall'utente: " + user.getUsername());
		IProdottoDao dao = factory.getDao(user, risorsaCommessa);
		ProdottoJSON entity = dao.inserisci(prodotto);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<ProdottoJSON> response = new ResponseEntity<ProdottoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<ProdottoJSON> cerca(@RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		String sku = prodotto != null ? prodotto.getChiaveCliente() : "";
		logger.info("Nuova richiesta di ricerca prodotto tramite SKU '" + sku + " dall'utente: " + user.getUsername());
		IProdottoDao dao = factory.getDao(user, risorsaCommessa);
		ProdottoJSON entity = dao.trovaPerSKU(sku);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ProdottoJSON> response = new ResponseEntity<ProdottoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<ProdottoJSON> modifica(@Valid @RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di modifica prodotto dall'utente: " + user.getUsername());
		IProdottoDao dao = factory.getDao(user, risorsaCommessa);
		ProdottoJSON entity = dao.aggiorna(prodotto);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ProdottoJSON> response = new ResponseEntity<ProdottoJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<ProdottoJSON> dismetti(@RequestBody ProdottoJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		String sku = prodotto != null ? prodotto.getChiaveCliente() : "";
		logger.info("Nuova richiesta di dismissione prodotto SKU '" + sku + " dall'utente: " + user.getUsername());
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

}
