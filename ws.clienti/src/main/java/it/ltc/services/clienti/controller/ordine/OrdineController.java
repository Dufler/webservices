package it.ltc.services.clienti.controller.ordine;

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
import it.ltc.model.shared.json.cliente.OrdineImballatoJSON;
import it.ltc.model.shared.json.cliente.OrdineJSON;
import it.ltc.model.shared.json.cliente.SpedizioneJSON;
import it.ltc.model.shared.json.cliente.UscitaDettaglioJSON;
import it.ltc.model.shared.json.cliente.UscitaJSON;
import it.ltc.services.clienti.data.ordine.OrdineDAO;
import it.ltc.services.clienti.data.ordine.OrdineDAOFactory;
import it.ltc.services.clienti.validation.OrdineValidator;
import it.ltc.services.clienti.validation.SpedizioneValidator;
import it.ltc.services.clienti.validation.UscitaDettaglioValidator;
import it.ltc.services.clienti.validation.UscitaValidator;
import it.ltc.services.custom.controller.RestController;

@Controller
@RequestMapping("/ordine")
public class OrdineController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("OrdineController");
	
	@Autowired
	private OrdineDAOFactory factory;
	
	@Autowired
	private OrdineValidator ordineValidator;
	
	@Autowired
	private UscitaValidator uscitaValidator;
	
	@Autowired
	private UscitaDettaglioValidator prodottoValidator;
	
	@Autowired
	private SpedizioneValidator spedizioneValidator;
	
	@InitBinder("ordineJSON")
	protected void initOrdineBinder(WebDataBinder binder) {
	    binder.setValidator(ordineValidator);
	}
	
	@InitBinder("uscitaJSON")
	protected void initUscitaBinder(WebDataBinder binder) {
	    binder.setValidator(uscitaValidator);
	}
	
	@InitBinder("uscitaDettaglioJSON")
	protected void initUscitaDettaglioBinder(WebDataBinder binder) {
	    binder.setValidator(prodottoValidator);
	}
	
	@InitBinder("spedizioneJSON")
	protected void initSpedizioneBinder(WebDataBinder binder) {
	    binder.setValidator(spedizioneValidator);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<OrdineJSON> inserisci(@Valid @RequestBody OrdineJSON ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di inserimento ordine dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		OrdineJSON entity = dao.inserisci(ordine);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineJSON> response = new ResponseEntity<OrdineJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/dettaglio")
	public ResponseEntity<UscitaDettaglioJSON> inserisciDettaglio(@Valid @RequestBody UscitaDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di inserimento di un dettaglio dell'ordine dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		UscitaDettaglioJSON entity = dao.inserisciDettaglio(dettaglio);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<UscitaDettaglioJSON> response = new ResponseEntity<UscitaDettaglioJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<UscitaJSON> modifica(@Valid @RequestBody UscitaJSON ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di modifica della testata dell'ordine dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		UscitaJSON entity = dao.aggiorna(ordine);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<UscitaJSON> response = new ResponseEntity<UscitaJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", value="/dettaglio")
	public ResponseEntity<UscitaDettaglioJSON> modificaDettaglio(@Valid @RequestBody UscitaDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di modifica di un dettaglio dell'ordine dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		UscitaDettaglioJSON entity = dao.aggiornaDettaglio(dettaglio);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<UscitaDettaglioJSON> response = new ResponseEntity<UscitaDettaglioJSON>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<UscitaJSON> elimina(@RequestBody UscitaJSON ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di eliminazione ordine: " + ordine + " dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		UscitaJSON entity = dao.elimina(ordine);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<UscitaJSON> response = new ResponseEntity<UscitaJSON>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value="/dettaglio")
	public ResponseEntity<UscitaDettaglioJSON> eliminaDettaglio(@RequestBody UscitaDettaglioJSON dettaglio, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di eliminazione dettaglio ordine: " + dettaglio + "dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		UscitaDettaglioJSON entity = dao.eliminaDettaglio(dettaglio);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<UscitaDettaglioJSON> response = new ResponseEntity<UscitaDettaglioJSON>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<UscitaJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di elenco ordini dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		List<UscitaJSON> carichi = dao.trovaTutti();
		HttpStatus status = carichi.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Carichi trovati: " + carichi.size());
		ResponseEntity<List<UscitaJSON>> response = new ResponseEntity<List<UscitaJSON>>(carichi, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="{id}")
	public ResponseEntity<OrdineJSON> dettaglioDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer id) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di dettaglio ordine ID: " + id + " dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		OrdineJSON carico = dao.trovaDaID(id);
		HttpStatus status = carico == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<OrdineJSON> response = new ResponseEntity<OrdineJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<OrdineJSON> dettaglioDaRiferimento(@RequestBody UscitaJSON ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		String riferimento = ordine != null ? ordine.getRiferimentoOrdine() : null;
		logger.info("Nuova richiesta di ricerca ordine tramite riferimento: '"  + riferimento + " dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		OrdineJSON carico = dao.trovaDaRiferimento(riferimento, true);
		HttpStatus status = carico == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<OrdineJSON> response = new ResponseEntity<OrdineJSON>(carico, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/assegna")
	public ResponseEntity<Void> assegnaOrdinePerRiferimento(@RequestBody UscitaJSON ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		String riferimento = ordine != null ? ordine.getRiferimentoOrdine() : null;
		logger.info("Nuova richiesta di assegnazione ordine tramite riferimento: '"  + riferimento + " dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		boolean assegna = dao.assegna(riferimento);
		HttpStatus status = assegna ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/assegna/{id}")
	public ResponseEntity<Void> assegnaOrdinePerID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") int id) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di assegnazione ordine tramite ID: " + id + " dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		boolean assegna = dao.assegna(id);
		HttpStatus status = assegna ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/imballo")
	public ResponseEntity<OrdineImballatoJSON> ottieniImballoOrdineDaRiferimento(@RequestBody UscitaJSON ordine, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		String riferimento = ordine != null ? ordine.getRiferimentoOrdine() : null;
		logger.info("Nuova richiesta di dettagli dell'imballo tramite riferimento '" + riferimento + " dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		OrdineImballatoJSON imballo = dao.ottieniDettagliImballo(riferimento);
		HttpStatus status = imballo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineImballatoJSON> response = new ResponseEntity<OrdineImballatoJSON>(imballo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/imballo/{id}")
	public ResponseEntity<OrdineImballatoJSON>  ottieniImballoOrdineDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") int id) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di dettagli dell'imballo tramite ID: " + id + " dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		OrdineImballatoJSON imballo = dao.ottieniDettagliImballo(id);
		HttpStatus status = imballo != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<OrdineImballatoJSON> response = new ResponseEntity<OrdineImballatoJSON>(imballo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/spedisci")
	public ResponseEntity<Void> spedisci(@Valid @RequestBody SpedizioneJSON ingresso, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di spedizione dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		boolean assegna = dao.spedisci(ingresso);
		HttpStatus status = assegna ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "text/plain", value="/ddtraw/{id}")
	public ResponseEntity<String> getDocumentoRawDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") int id) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di recupero documento di trasporto grezzo tramite ID: " + id + " dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		SpedizioneJSON info = dao.getDocumentoDiTrasporto(id);
		String documento = info != null ? new String(info.getDocumentoFiscale().getDocumentoBase64()) : null;
		HttpStatus status = documento != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<String> response = new ResponseEntity<String>(documento, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/ddt/{id}")
	public ResponseEntity<SpedizioneJSON> getDocumentoDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") int id) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di recupero documento di trasporto grezzo tramite ID: " + id + " dall'utente: " + user.getUsername());
		OrdineDAO<?,?> dao = factory.getDao(user, commessa);
		SpedizioneJSON documento = dao.getDocumentoDiTrasporto(id);
		HttpStatus status = documento != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<SpedizioneJSON> response = new ResponseEntity<SpedizioneJSON>(documento, status);
		return response;
	}

}
