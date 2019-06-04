package it.ltc.services.sede.controller.prodotto;

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
import it.ltc.model.shared.dao.ICassaStandardDao;
import it.ltc.model.shared.json.cliente.CassaStandardJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.prodotto.CassaStandardDAOFactory;
import it.ltc.services.sede.validation.prodotto.CassaStandardValidator;

@Controller
@RequestMapping("/cassastandard")
public class CassaStandardController extends RestController {
	
	private static final Logger logger = Logger.getLogger(CassaStandardController.class);
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	@Autowired
	private CassaStandardDAOFactory factory;
	
	@Autowired
	private CassaStandardValidator validator;
	
	@InitBinder("cassaStandardJSON")
	protected void initProdottoBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CassaStandardJSON> inserisci(@Valid @RequestBody CassaStandardJSON cassa, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di salvataggio nuova cassa standard: " + cassa + " da: " + user.getUsername());
		ICassaStandardDao dao = factory.getDao(user, risorsaCommessa);
		CassaStandardJSON entity = dao.salva(cassa);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		logger.info("Creata nuova cassa standard: " + entity.toString());
		ResponseEntity<CassaStandardJSON> response = new ResponseEntity<CassaStandardJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CassaStandardJSON> modifica(@Valid @RequestBody CassaStandardJSON cassa, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di salvataggio cassa standard: " + cassa + " da: " + user.getUsername());		
		ICassaStandardDao dao = factory.getDao(user, risorsaCommessa);
		CassaStandardJSON entity = dao.salva(cassa);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		logger.info("Modificata la cassa standard: " + cassa.toString());
		ResponseEntity<CassaStandardJSON> response = new ResponseEntity<CassaStandardJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CassaStandardJSON> dismetti(@RequestBody CassaStandardJSON cassa, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di eliminazione cassa standard: " + cassa + " da: " + user.getUsername());
		ICassaStandardDao dao = factory.getDao(user, risorsaCommessa);
		CassaStandardJSON entity = dao.elimina(cassa);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		logger.info("Eliminata cassa standard: " + cassa.toString());
		ResponseEntity<CassaStandardJSON> response = new ResponseEntity<CassaStandardJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CassaStandardJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di elenco casse standard da: " + user.getUsername());		
		ICassaStandardDao dao = factory.getDao(user, risorsaCommessa);
		List<CassaStandardJSON> prodotti = dao.trovaTutte();
		HttpStatus status = prodotti.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Casse standard trovate: " + prodotti.size());
		ResponseEntity<List<CassaStandardJSON>> response = new ResponseEntity<List<CassaStandardJSON>>(prodotti, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{codice}")
	public ResponseEntity<CassaStandardJSON> trovaDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa, @PathVariable(value="codice") String codiceCassa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di dettaglio cassa standard con codice " + codiceCassa + " da: " + user.getUsername());		
		ICassaStandardDao dao = factory.getDao(user, risorsaCommessa);
		CassaStandardJSON cassa = dao.trovaDaCodiceCassa(codiceCassa);
		HttpStatus status = cassa == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<CassaStandardJSON> response = new ResponseEntity<CassaStandardJSON>(cassa, status);
		return response;
	}

}
