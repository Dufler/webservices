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
import it.ltc.model.shared.dao.ICaricoDettaglioDao;
import it.ltc.model.shared.json.interno.CaricoDettaglio;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.permission.Permessi;
import it.ltc.services.sede.data.carico.FactoryDaoCarichiDettagli;

@Controller
@RequestMapping("/carico/dettaglio")
public class CaricoDettaglioController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	public static final int ID_CRUD_CARICHI = Permessi.UFFICIO_INGRESSI.getID();
	
	private static final Logger logger = Logger.getLogger("CaricoController");
	
	@Autowired
	private FactoryDaoCarichiDettagli factory;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<List<CaricoDettaglio>> dettagliDaIDCarico(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idCarico) {
		logger.info("Nuova richiesta di dettaglio carico");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		ICaricoDettaglioDao dao = factory.getDao(user, commessa);
		List<CaricoDettaglio> carico = dao.trovaDettagli(idCarico);
		HttpStatus status = carico.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<CaricoDettaglio>> response = new ResponseEntity<List<CaricoDettaglio>>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CaricoDettaglio> inserisci(@Valid @RequestBody CaricoDettaglio carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Inserimento di un nuovo riepilogo d'evento.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_CRUD_CARICHI);
		ICaricoDettaglioDao dao = factory.getDao(user, commessa);
		carico = dao.inserisci(carico);
		HttpStatus status = carico != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoDettaglio> response = new ResponseEntity<CaricoDettaglio>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CaricoDettaglio> aggiorna(@Valid @RequestBody CaricoDettaglio carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Inserimento di un nuovo riepilogo d'evento.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_CRUD_CARICHI);
		ICaricoDettaglioDao dao = factory.getDao(user, commessa);
		carico = dao.aggiorna(carico);
		HttpStatus status = carico != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoDettaglio> response = new ResponseEntity<CaricoDettaglio>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CaricoDettaglio> elimina(@RequestBody CaricoDettaglio carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Inserimento di un nuovo riepilogo d'evento.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_CRUD_CARICHI);
		ICaricoDettaglioDao dao = factory.getDao(user, commessa);
		carico = dao.elimina(carico);
		HttpStatus status = carico != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<CaricoDettaglio> response = new ResponseEntity<CaricoDettaglio>(carico, status);
		return response;
	}

}
