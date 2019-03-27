package it.ltc.services.sede.controller.prodotto;

import java.util.List;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.json.interno.MovimentoProdotto;
import it.ltc.model.shared.json.interno.SaldoProdotto;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.prodotto.SaldiMovimentiDAO;
import it.ltc.services.sede.data.prodotto.SaldiMovimentiDAOFactory;

@Controller
@RequestMapping("/movimenti")
public class SaldiMovimentiController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("SaldiMovimentiController");
	
	@Autowired
	private SaldiMovimentiDAOFactory factory;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<List<MovimentoProdotto>> trovaMovimentiDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa, @PathVariable(value="id") Integer idProdotto) {
		logger.info("Nuova richiesta di movimenti prodotto");
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		SaldiMovimentiDAO dao = factory.getDao(user, risorsaCommessa);
		List<MovimentoProdotto> movimenti = dao.trovaMovimenti(idProdotto);
		HttpStatus status = movimenti.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<MovimentoProdotto>> response = new ResponseEntity<List<MovimentoProdotto>>(movimenti, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/saldo/{id}")
	public ResponseEntity<List<SaldoProdotto>> trovaSaldiDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa, @PathVariable(value="id") Integer idProdotto) {
		logger.info("Nuova richiesta di saldi prodotto");
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		SaldiMovimentiDAO dao = factory.getDao(user, risorsaCommessa);
		List<SaldoProdotto> saldi = dao.trovaSaldi(idProdotto);
		HttpStatus status = saldi.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<SaldoProdotto>> response = new ResponseEntity<List<SaldoProdotto>>(saldi, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<MovimentoProdotto> inserisci(@Valid @RequestBody MovimentoProdotto movimento, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di inserimento movimento: " + movimento + ", Utente: " + user.getUsername());
		SaldiMovimentiDAO dao = factory.getDao(user, risorsaCommessa);
		MovimentoProdotto entity = dao.inserisci(movimento);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<MovimentoProdotto> response = new ResponseEntity<MovimentoProdotto>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<MovimentoProdotto> elimina(@Valid @RequestBody MovimentoProdotto movimento, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String risorsaCommessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di eliminazione movimento: " + movimento + ", Utente: " + user.getUsername());
		SaldiMovimentiDAO dao = factory.getDao(user, risorsaCommessa);
		MovimentoProdotto entity = dao.elimina(movimento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<MovimentoProdotto> response = new ResponseEntity<MovimentoProdotto>(entity, status);
		return response;
	}

}
