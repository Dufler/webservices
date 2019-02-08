package it.ltc.services.sede.controller.ordine;

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

import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.IOrdineDao;
import it.ltc.model.shared.json.interno.OperatoreOrdine;
import it.ltc.model.shared.json.interno.OrdineStato;
import it.ltc.model.shared.json.interno.OrdineTestata;
import it.ltc.model.shared.json.interno.RisultatoAssegnazioneOrdine;
import it.ltc.model.shared.json.interno.RisultatoFinalizzazioneOrdine;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.permission.Permessi;
import it.ltc.services.sede.data.ordine.FactoryDaoOrdini;
import it.ltc.services.sede.validation.ordine.OrdineTestataValidator;

@Controller
@RequestMapping("/ordine")
public class OrdineController extends RestController {
	
	private static final Logger logger = Logger.getLogger("OrdineController");

	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	public static final int ID_CRUD_ORDINI = Permessi.UFFICIO_USCITE.getID();
	
	@Autowired
	private FactoryDaoOrdini factory;
	
	@Autowired
	private OrdineTestataValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<OrdineTestata>> lista(@RequestBody OrdineTestata filtro, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di elenco ordini, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		List<OrdineTestata> ordini = dao.trovaCorrispondenti(filtro);
		HttpStatus status = ordini.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Ordini trovati: " + ordini.size());
		ResponseEntity<List<OrdineTestata>> response = new ResponseEntity<List<OrdineTestata>>(ordini, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/finalizza/{id}")
	public ResponseEntity<RisultatoFinalizzazioneOrdine> finalizza(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di assegnazione ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		RisultatoFinalizzazioneOrdine risultato = dao.finalizza(idOrdine);
		HttpStatus status = risultato != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<RisultatoFinalizzazioneOrdine> response = new ResponseEntity<RisultatoFinalizzazioneOrdine>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/assegna/{id}")
	public ResponseEntity<RisultatoAssegnazioneOrdine> assegna(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di assegnazione ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		RisultatoAssegnazioneOrdine assegnazione = dao.assegna(idOrdine);
		HttpStatus status = assegnazione == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<RisultatoAssegnazioneOrdine> response = new ResponseEntity<RisultatoAssegnazioneOrdine>(assegnazione, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/recuperaassegnazione/{id}")
	public ResponseEntity<RisultatoAssegnazioneOrdine> recuperaAssegnazione(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di recupero precedente assegnazione ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		RisultatoAssegnazioneOrdine assegnazione = dao.recuperaAssegnazione(idOrdine);
		HttpStatus status = assegnazione == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<RisultatoAssegnazioneOrdine> response = new ResponseEntity<RisultatoAssegnazioneOrdine>(assegnazione, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<OrdineTestata> dettaglioDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di dettaglio ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		OrdineTestata ordine = dao.trovaPerID(idOrdine);
		HttpStatus status = ordine == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(ordine, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/stati/{id}")
	public ResponseEntity<List<OrdineStato>> statiDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di stati dell'ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		List<OrdineStato> stati = dao.trovaStati(idOrdine);
		HttpStatus status = stati.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<OrdineStato>> response = new ResponseEntity<List<OrdineStato>>(stati, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/operatori/{id}")
	public ResponseEntity<List<OperatoreOrdine>> operatoriDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di operatori al lavoro sull'ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		List<OperatoreOrdine> operatori = dao.trovaOperatori(idOrdine);
		HttpStatus status = operatori.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<OperatoreOrdine>> response = new ResponseEntity<List<OperatoreOrdine>>(operatori, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<OrdineTestata> inserisci(@Valid @RequestBody OrdineTestata ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Inserimento di un nuovo ordine.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		IOrdineDao dao = factory.getDao(user, commessa);
		ordine = dao.inserisci(ordine);
		HttpStatus status = ordine != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(ordine, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<OrdineTestata> aggiorna(@Valid @RequestBody OrdineTestata ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Aggiornamento dell'ordine.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		IOrdineDao dao = factory.getDao(user, commessa);
		ordine = dao.aggiorna(ordine);
		HttpStatus status = ordine != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(ordine, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", value="/modificastato")
	public ResponseEntity<OrdineTestata> modificaStato(@Valid @RequestBody OrdineTestata ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Modifico lo stato dell'ordine.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		IOrdineDao dao = factory.getDao(user, commessa);
		ordine = dao.modificaStato(ordine);
		HttpStatus status = ordine != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(ordine, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<OrdineTestata> elimina(@RequestBody OrdineTestata ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Eliminazione dell'ordine.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		IOrdineDao dao = factory.getDao(user, commessa);
		ordine = dao.elimina(ordine);
		HttpStatus status = ordine != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(ordine, status);
		return response;
	}
	
}
