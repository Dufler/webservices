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

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IOrdineDao;
import it.ltc.model.shared.json.cliente.ImballoJSON;
import it.ltc.model.shared.json.interno.ordine.DatiSpedizione;
import it.ltc.model.shared.json.interno.ordine.OperatoreOrdine;
import it.ltc.model.shared.json.interno.ordine.OrdineStato;
import it.ltc.model.shared.json.interno.ordine.OrdineTestata;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoAssegnazioneOrdine;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoFinalizzazioneOrdine;
import it.ltc.model.shared.json.interno.ordine.risultato.RisultatoGenerazioneMovimenti;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.permission.Permessi;
import it.ltc.services.sede.data.ordine.FactoryDaoOrdini;
import it.ltc.services.sede.validation.ordine.DatiSpedizioneValidator;
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
	private OrdineTestataValidator validatorTestataOrdine;
	
	@Autowired
	private DatiSpedizioneValidator validatorDatiSpedizione;
	
	@InitBinder("ordineTestata")
	protected void initBinderTestataOrdine(WebDataBinder binder) {
	    binder.setValidator(validatorTestataOrdine);
	}
	
	@InitBinder("datiSpedizione")
	protected void initBinderDatiSpedizione(WebDataBinder binder) {
	    binder.setValidator(validatorDatiSpedizione);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<OrdineTestata>> lista(@RequestBody OrdineTestata filtro, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
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
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di assegnazione ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		RisultatoFinalizzazioneOrdine risultato = dao.finalizza(idOrdine);
		HttpStatus status = risultato != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<RisultatoFinalizzazioneOrdine> response = new ResponseEntity<RisultatoFinalizzazioneOrdine>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/assegna/{id}")
	public ResponseEntity<RisultatoAssegnazioneOrdine> assegna(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di assegnazione ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		RisultatoAssegnazioneOrdine assegnazione = dao.assegna(idOrdine);
		HttpStatus status = assegnazione == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<RisultatoAssegnazioneOrdine> response = new ResponseEntity<RisultatoAssegnazioneOrdine>(assegnazione, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/recuperaassegnazione/{id}")
	public ResponseEntity<RisultatoAssegnazioneOrdine> recuperaAssegnazione(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di recupero precedente assegnazione ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		RisultatoAssegnazioneOrdine assegnazione = dao.recuperaAssegnazione(idOrdine);
		HttpStatus status = assegnazione == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<RisultatoAssegnazioneOrdine> response = new ResponseEntity<RisultatoAssegnazioneOrdine>(assegnazione, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/annullaimballo/{id}")
	public ResponseEntity<OrdineTestata> annullaImballo(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di annullamento imballo ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		OrdineTestata risultato = dao.annullaImballo(idOrdine);
		HttpStatus status = risultato == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/annullaassegnazioneriposiziona/{id}")
	public ResponseEntity<OrdineTestata> annullaAssegnazioneRiposiziona(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di annullamento imballo ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		OrdineTestata risultato = dao.annullaAssegnazioneConRiposizionamento(idOrdine);
		HttpStatus status = risultato == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/annullaassegnazionenuovocarico/{id}")
	public ResponseEntity<OrdineTestata> annullaAssegnazioneNuovoCarico(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di annullamento imballo ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		OrdineTestata risultato = dao.annullaAssegnazioneConNuovoCarico(idOrdine);
		HttpStatus status = risultato == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/annullaimportazione/{id}")
	public ResponseEntity<OrdineTestata> annullaImportazione(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di annullamento imballo ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		OrdineTestata risultato = dao.annullaImportazione(idOrdine);
		HttpStatus status = risultato == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/generamovimenti/{id}")
	public ResponseEntity<RisultatoGenerazioneMovimenti> generaMovimentiUscita(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di generazione movimenti d'uscita ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		RisultatoGenerazioneMovimenti risultato = dao.generaMovimentiUscita(idOrdine);
		HttpStatus status = risultato == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<RisultatoGenerazioneMovimenti> response = new ResponseEntity<RisultatoGenerazioneMovimenti>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/trovadatispedizione/{id}")
	public ResponseEntity<DatiSpedizione> trovaDatiSpedizione(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di ricerca dati spedizione ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		DatiSpedizione risultato = dao.trovaDatiSpedizione(idOrdine);
		HttpStatus status = risultato == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<DatiSpedizione> response = new ResponseEntity<DatiSpedizione>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/generadatispedizione")
	public ResponseEntity<DatiSpedizione> generaDatiSpedizione(@Valid @RequestBody DatiSpedizione dati, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di generazione dati spedizione ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		DatiSpedizione risultato = dao.generaDatiSpedizione(dati);
		HttpStatus status = risultato == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
		ResponseEntity<DatiSpedizione> response = new ResponseEntity<DatiSpedizione>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<OrdineTestata> dettaglioDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di dettaglio ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		OrdineTestata ordine = dao.trovaPerID(idOrdine);
		HttpStatus status = ordine == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(ordine, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/stati/{id}")
	public ResponseEntity<List<OrdineStato>> statiDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di stati dell'ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		List<OrdineStato> stati = dao.trovaStati(idOrdine);
		HttpStatus status = stati.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<OrdineStato>> response = new ResponseEntity<List<OrdineStato>>(stati, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/operatori/{id}")
	public ResponseEntity<List<OperatoreOrdine>> operatoriDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di operatori al lavoro sull'ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		List<OperatoreOrdine> operatori = dao.trovaOperatori(idOrdine);
		HttpStatus status = operatori.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<OperatoreOrdine>> response = new ResponseEntity<List<OperatoreOrdine>>(operatori, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/imballi/{id}")
	public ResponseEntity<List<ImballoJSON>> imballiDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di imballi dell'ordine, utente: " + user.getUsername());
		IOrdineDao dao = factory.getDao(user, commessa);
		List<ImballoJSON> imballi = dao.ottieniDettagliImballo(idOrdine);
		HttpStatus status = imballi.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<ImballoJSON>> response = new ResponseEntity<List<ImballoJSON>>(imballi, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<OrdineTestata> inserisci(@Valid @RequestBody OrdineTestata ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Inserimento di un nuovo ordine.");
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		IOrdineDao dao = factory.getDao(user, commessa);
		ordine = dao.inserisci(ordine);
		HttpStatus status = ordine != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(ordine, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<OrdineTestata> aggiorna(@Valid @RequestBody OrdineTestata ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Aggiornamento dell'ordine.");
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		IOrdineDao dao = factory.getDao(user, commessa);
		ordine = dao.aggiorna(ordine);
		HttpStatus status = ordine != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(ordine, status);
		return response;
	}
	
//	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", value="/modificastato")
//	public ResponseEntity<OrdineTestata> modificaStato(@Valid @RequestBody OrdineTestata ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
//		logger.info("Modifico lo stato dell'ordine.");
//		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
//		IOrdineDao dao = factory.getDao(user, commessa);
//		ordine = dao.modificaStato(ordine);
//		HttpStatus status = ordine != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
//		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(ordine, status);
//		return response;
//	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<OrdineTestata> elimina(@RequestBody OrdineTestata ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Eliminazione dell'ordine.");
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		IOrdineDao dao = factory.getDao(user, commessa);
		ordine = dao.elimina(ordine);
		HttpStatus status = ordine != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineTestata> response = new ResponseEntity<OrdineTestata>(ordine, status);
		return response;
	}
	
}
