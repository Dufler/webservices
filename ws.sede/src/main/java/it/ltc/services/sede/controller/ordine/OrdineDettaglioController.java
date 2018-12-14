package it.ltc.services.sede.controller.ordine;

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
import it.ltc.model.shared.dao.IOrdineDettaglioDao;
import it.ltc.model.shared.json.interno.OrdineDettaglio;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.permission.Permessi;
import it.ltc.services.sede.data.ordine.FactoryDaoOrdineDettagli;

@Controller
@RequestMapping("/ordine/dettaglio")
public class OrdineDettaglioController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	public static final int ID_CRUD_ORDINI = Permessi.UFFICIO_USCITE.getID();
	
	private static final Logger logger = Logger.getLogger("OrdineDettaglioController");
	
	@Autowired
	private FactoryDaoOrdineDettagli factory;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<List<OrdineDettaglio>> dettagliDaIDCarico(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idCarico) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di dettaglio carico, Utente: " + user.getUsername());		
		IOrdineDettaglioDao dao = factory.getDao(user, commessa);
		List<OrdineDettaglio> carico = dao.trovaDettagli(idCarico);
		HttpStatus status = carico.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<OrdineDettaglio>> response = new ResponseEntity<List<OrdineDettaglio>>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<OrdineDettaglio> inserisci(@Valid @RequestBody OrdineDettaglio carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		logger.info("Nuova richiesta di inserimento dettaglio carico, Utente: " + user.getUsername());
		IOrdineDettaglioDao dao = factory.getDao(user, commessa);
		carico = dao.inserisci(carico);
		HttpStatus status = carico != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineDettaglio> response = new ResponseEntity<OrdineDettaglio>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<OrdineDettaglio> aggiorna(@Valid @RequestBody OrdineDettaglio carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		logger.info("Nuova richiesta di modifica dettaglio carico, Utente: " + user.getUsername());
		IOrdineDettaglioDao dao = factory.getDao(user, commessa);
		carico = dao.aggiorna(carico);
		HttpStatus status = carico != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineDettaglio> response = new ResponseEntity<OrdineDettaglio>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<OrdineDettaglio> elimina(@RequestBody OrdineDettaglio carico, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		logger.info("Nuova richiesta di eliminazione dettaglio carico, Utente: " + user.getUsername());		
		IOrdineDettaglioDao dao = factory.getDao(user, commessa);
		carico = dao.elimina(carico);
		HttpStatus status = carico != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineDettaglio> response = new ResponseEntity<OrdineDettaglio>(carico, status);
		return response;
	}

}
