package it.ltc.services.sede.controller.carico;

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

import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.json.cliente.CaricoJSON;
import it.ltc.model.shared.json.cliente.IngressoJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.carico.CaricoDAO;
import it.ltc.services.sede.data.carico.FactoryDaoCarico;

@Controller
@RequestMapping("/_carico")
public class _CaricoController extends RestController {

	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("_CaricoController");
	
	@Autowired
	private FactoryDaoCarico factory;
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<IngressoJSON>> lista(@RequestBody IngressoJSON filtro, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di elenco carichi");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		CaricoDAO dao = factory.getDao(user, commessa);
		List<IngressoJSON> carichi = dao.trovaTutti(filtro);
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
		CaricoDAO dao = factory.getDao(user, commessa);
		CaricoJSON carico = dao.trovaDaID(idCarico, true);
		HttpStatus status = (carico == null || carico.getIngresso() == null) ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CaricoJSON> inserisci(@Valid @RequestBody CaricoJSON carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Inserimento di un nuovo riepilogo d'evento.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		CaricoDAO dao = factory.getDao(user, commessa);
		carico = dao.nuovoCarico(carico);
		HttpStatus status = carico != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CaricoJSON> aggiorna(@Valid @RequestBody CaricoJSON carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Inserimento di un nuovo riepilogo d'evento.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		CaricoDAO dao = factory.getDao(user, commessa);
		carico = dao.aggiornaCarico(carico);
		HttpStatus status = carico != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CaricoJSON> elimina(@RequestBody CaricoJSON carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Inserimento di un nuovo riepilogo d'evento.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		CaricoDAO dao = factory.getDao(user, commessa);
		carico = dao.eliminaCarico(carico);
		HttpStatus status = carico != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoJSON> response = new ResponseEntity<CaricoJSON>(carico, status);
		return response;
	}
	
}
