package it.ltc.services.sede.controller.prodotto;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.ICassaDao;
import it.ltc.model.shared.json.cliente.CassaJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.prodotto.CassaDAOFactory;
import it.ltc.services.sede.validation.prodotto.CassaValidator;

public class CassaController extends RestController {
	
	private static final Logger logger = Logger.getLogger(CassaController.class);
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	@Autowired
	private CassaDAOFactory factory;
	
	@Autowired
	private CassaValidator validator;
	
	@InitBinder("cassaJSON")
	protected void initProdottoBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CassaJSON> inserisci(@Valid @RequestBody CassaJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		logger.info("Nuova richiesta di salvataggio cassa: " + prodotto);
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		ICassaDao dao = factory.getDao(user, risorsaCommessa);
		CassaJSON entity = dao.salva(prodotto);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		logger.info("Creato nuovo prodotto: " + entity.toString());
		ResponseEntity<CassaJSON> response = new ResponseEntity<CassaJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CassaJSON> modifica(@Valid @RequestBody CassaJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		logger.info("Nuova richiesta di modifica prodotto: " + prodotto);
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		ICassaDao dao = factory.getDao(user, risorsaCommessa);
		CassaJSON entity = dao.salva(prodotto);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		logger.info("Modificato il prodotto: " + prodotto.toString());
		ResponseEntity<CassaJSON> response = new ResponseEntity<CassaJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CassaJSON> dismetti(@RequestBody CassaJSON prodotto, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		logger.info("Nuova richiesta di dismissione prodotto: " + prodotto);
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		ICassaDao dao = factory.getDao(user, risorsaCommessa);
		CassaJSON entity = dao.elimina(prodotto);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		logger.info("Dismesso prodotto: " + prodotto.toString());
		ResponseEntity<CassaJSON> response = new ResponseEntity<CassaJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CassaJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		logger.info("Nuova richiesta di elenco prodotti");
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		ICassaDao dao = factory.getDao(user, risorsaCommessa);
		List<CassaJSON> prodotti = dao.trovaTutte();
		HttpStatus status = prodotti.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Prodotti trovati: " + prodotti.size());
		ResponseEntity<List<CassaJSON>> response = new ResponseEntity<List<CassaJSON>>(prodotti, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<CassaJSON> trovaDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa, @PathVariable(value="id") Integer idProdotto) {
		logger.info("Nuova richiesta di dettaglio prodotto");
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		ICassaDao dao = factory.getDao(user, risorsaCommessa);
		CassaJSON cassa = dao.trovaDaIDCassa(idProdotto);
		HttpStatus status = cassa == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<CassaJSON> response = new ResponseEntity<CassaJSON>(cassa, status);
		return response;
	}

}
