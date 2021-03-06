package it.ltc.services.sede.controller.carico;

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

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.ICaricoDao;
import it.ltc.model.shared.json.interno.carico.CaricoStato;
import it.ltc.model.shared.json.interno.carico.CaricoTestata;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.permission.Permessi;
import it.ltc.services.sede.data.carico.FactoryDaoCarichi;
import it.ltc.services.sede.validation.carico.CaricoTestataValidator;

@Controller
@RequestMapping("/carico")
public class CaricoController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	public static final int ID_CRU_CARICHI = Permessi.UFFICIO_INGRESSI.getID();
	public static final int ID_D_CARICHI = Permessi.UFFICIO_INGRESSI_ELIMINA.getID();
	
	private static final Logger logger = Logger.getLogger(CaricoController.class);
	
	@Autowired
	private FactoryDaoCarichi factory;
	
	@Autowired
	private CaricoTestataValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<CaricoTestata>> lista(@RequestBody CaricoTestata filtro, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di elenco carichi, utente: " + user.getUsername());
		ICaricoDao dao = factory.getDao(user, commessa);
		List<CaricoTestata> carichi = dao.trovaCorrispondenti(filtro);
		HttpStatus status = carichi.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Carichi trovati: " + carichi.size());
		ResponseEntity<List<CaricoTestata>> response = new ResponseEntity<List<CaricoTestata>>(carichi, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<CaricoTestata> dettaglioDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idCarico) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di dettaglio carico, utente: " + user.getUsername());
		ICaricoDao dao = factory.getDao(user, commessa);
		CaricoTestata carico = dao.trovaPerID(idCarico);
		HttpStatus status = carico == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<CaricoTestata> response = new ResponseEntity<CaricoTestata>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/stati/{id}")
	public ResponseEntity<List<CaricoStato>> statiDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idCarico) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di stati del carico, utente: " + user.getUsername());
		ICaricoDao dao = factory.getDao(user, commessa);
		List<CaricoStato> stati = dao.trovaStati(idCarico);
		HttpStatus status = stati.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<CaricoStato>> response = new ResponseEntity<List<CaricoStato>>(stati, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CaricoTestata> inserisci(@Valid @RequestBody CaricoTestata carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_CRU_CARICHI);
		logger.info("Inserimento di un nuovo carico.");		
		ICaricoDao dao = factory.getDao(user, commessa);
		carico = dao.inserisci(carico);
		HttpStatus status = carico != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoTestata> response = new ResponseEntity<CaricoTestata>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CaricoTestata> aggiorna(@Valid @RequestBody CaricoTestata carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_CRU_CARICHI);
		logger.info("Aggiornamento del carico.");		
		ICaricoDao dao = factory.getDao(user, commessa);
		carico = dao.aggiorna(carico);
		HttpStatus status = carico != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoTestata> response = new ResponseEntity<CaricoTestata>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", value="/modificastato")
	public ResponseEntity<CaricoTestata> modificaStato(@Valid @RequestBody CaricoTestata carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_CRU_CARICHI);
		logger.info("Modifico lo stato del carico.");		
		ICaricoDao dao = factory.getDao(user, commessa);
		carico = dao.modificaStato(carico);
		HttpStatus status = carico != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoTestata> response = new ResponseEntity<CaricoTestata>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CaricoTestata> elimina(@RequestBody CaricoTestata carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_D_CARICHI);
		logger.info("Eliminazione del carico.");		
		ICaricoDao dao = factory.getDao(user, commessa);
		carico = dao.elimina(carico);
		HttpStatus status = carico != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoTestata> response = new ResponseEntity<CaricoTestata>(carico, status);
		return response;
	}

}
