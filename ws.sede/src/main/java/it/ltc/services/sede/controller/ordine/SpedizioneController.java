package it.ltc.services.sede.controller.ordine;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import it.ltc.model.shared.dao.ISpedizioneDao;
import it.ltc.model.shared.json.interno.ordine.DatiSpedizione;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.ordine.FactoryDaoSpedizioni;

@Controller
@RequestMapping("/spedizione")
public class SpedizioneController extends RestController {
	
	private static final Logger logger = Logger.getLogger(SpedizioneController.class);
	
	@Autowired
	private FactoryDaoSpedizioni factory;
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<DatiSpedizione>> lista(@RequestBody DatiSpedizione filtro, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di elenco spedizioni, utente: " + user.getUsername());
		ISpedizioneDao dao = factory.getDao(user, commessa);
		List<DatiSpedizione> ordini = dao.trovaCorrispondenti(filtro);
		HttpStatus status = ordini.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Spedizioni trovate: " + ordini.size());
		ResponseEntity<List<DatiSpedizione>> response = new ResponseEntity<List<DatiSpedizione>>(ordini, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<DatiSpedizione> dettaglioDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer idOrdine) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di dettagli spedizione, utente: " + user.getUsername());
		ISpedizioneDao dao = factory.getDao(user, commessa);
		DatiSpedizione ordine = dao.trovaPerID(idOrdine);
		HttpStatus status = ordine == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<DatiSpedizione> response = new ResponseEntity<DatiSpedizione>(ordine, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", value="/abilita")
	public ResponseEntity<DatiSpedizione> abilitaSpedizione(@Valid @RequestBody DatiSpedizione dati, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di generazione dati spedizione ordine, utente: " + user.getUsername());
		ISpedizioneDao dao = factory.getDao(user, commessa);
		DatiSpedizione risultato = dao.abilita(dati, true);
		HttpStatus status = risultato == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<DatiSpedizione> response = new ResponseEntity<DatiSpedizione>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json", value="/disabilita")
	public ResponseEntity<DatiSpedizione> disabilitaSpedizione(@Valid @RequestBody DatiSpedizione dati, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di generazione dati spedizione ordine, utente: " + user.getUsername());
		ISpedizioneDao dao = factory.getDao(user, commessa);
		DatiSpedizione risultato = dao.abilita(dati, false);
		HttpStatus status = risultato == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<DatiSpedizione> response = new ResponseEntity<DatiSpedizione>(risultato, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json", value="/elimina")
	public ResponseEntity<DatiSpedizione> eliminaSpedizione(@Valid @RequestBody DatiSpedizione dati, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di generazione dati spedizione ordine, utente: " + user.getUsername());
		ISpedizioneDao dao = factory.getDao(user, commessa);
		DatiSpedizione risultato = dao.elimina(dati);
		HttpStatus status = risultato == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<DatiSpedizione> response = new ResponseEntity<DatiSpedizione>(risultato, status);
		return response;
	}

}
