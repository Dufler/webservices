package it.ltc.services.sede.controller.carico;

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

import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.exception.CustomException;
import it.ltc.services.sede.data.carico.RiscontroProdottiDAO;
import it.ltc.services.sede.data.carico.RiscontroProdottiLegacyDAOImpl;
import it.ltc.services.sede.model.carico.ProdottoCaricoJSON;
import it.ltc.services.sede.validation.carico.ProdottoCaricoValidator;

@Controller
@RequestMapping("/carico/prodotto")
public class RiscontroProdottiController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("RiscontroProdottiController");
	
	@Autowired
	private ProdottoCaricoValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<ProdottoCaricoJSON> inserisci(@Valid @RequestBody ProdottoCaricoJSON collo, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Inserimento di un nuovo prodotto nel carico.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		RiscontroProdottiDAO dao = getDao(user, commessa);
		collo = dao.nuovoProdotto(collo);
		HttpStatus status = collo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ProdottoCaricoJSON> response = new ResponseEntity<ProdottoCaricoJSON>(collo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<ProdottoCaricoJSON> aggiorna(@Valid @RequestBody ProdottoCaricoJSON collo, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Modifica di un prodotto nel carico.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		RiscontroProdottiDAO dao = getDao(user, commessa);
		collo = dao.aggiornaProdotto(collo);
		HttpStatus status = collo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ProdottoCaricoJSON> response = new ResponseEntity<ProdottoCaricoJSON>(collo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<ProdottoCaricoJSON> elimina(@RequestBody ProdottoCaricoJSON collo, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Eliminazione di un prodotto nel carico.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		RiscontroProdottiDAO dao = getDao(user, commessa);
		collo = dao.eliminaProdotto(collo);
		HttpStatus status = collo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<ProdottoCaricoJSON> response = new ResponseEntity<ProdottoCaricoJSON>(collo, status);
		return response;
	}
	
	private RiscontroProdottiDAO getDao(Utente user, String risorsaCommessa) {
		RiscontroProdottiDAO dao;
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null) {
			String persistenceUnitName = commessa.getNomeRisorsa();
			dao = new RiscontroProdottiLegacyDAOImpl(persistenceUnitName);
		} else {
			throw new CustomException("E' necessario specificare una commessa valida nell'header della richiesta.");
		}
		return dao;
 	}

}