package it.ltc.services.sede.controller.ordine;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import it.ltc.model.shared.dao.ISpedizioneDao;
import it.ltc.model.shared.json.interno.ordine.DeliveryJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.permission.Permessi;
import it.ltc.services.sede.data.ordine.FactoryDaoSpedizioni;
import it.ltc.services.sede.validation.ordine.DeliveryValidator;


@Controller
@RequestMapping("/delivery")
public class DeliveryController extends RestController {
	
	private static final Logger logger = Logger.getLogger(DeliveryController.class);
	
	public static final int ID_CRUD_ORDINI = Permessi.UFFICIO_USCITE.getID();
	
	@Autowired
	private DeliveryValidator validatorDelivery;
	
	@InitBinder("deliveryJSON")
	protected void initBinderDatiSpedizione(WebDataBinder binder) {
	    binder.setValidator(validatorDelivery);
	}
	
	@Autowired
	private FactoryDaoSpedizioni factory;
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<DeliveryJSON>> lista(@RequestBody DeliveryJSON filtro, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di elenco delivery, utente: " + user.getUsername());
		ISpedizioneDao dao = factory.getDao(user, commessa);
		List<DeliveryJSON> deliveries = dao.trovaDelivery(filtro);
		HttpStatus status = deliveries.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Spedizioni trovate: " + deliveries.size());
		ResponseEntity<List<DeliveryJSON>> response = new ResponseEntity<List<DeliveryJSON>>(deliveries, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{id}")
	public ResponseEntity<DeliveryJSON> dettaglioDaID(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="id") Integer id) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di dettagli delivery, utente: " + user.getUsername());
		ISpedizioneDao dao = factory.getDao(user, commessa);
		DeliveryJSON delivery = dao.trovaDettagliDelivery(id);
		HttpStatus status = delivery == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<DeliveryJSON> response = new ResponseEntity<DeliveryJSON>(delivery, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<DeliveryJSON> inserisci(@Valid @RequestBody DeliveryJSON delivery, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		logger.info("Inserimento di una nuova delivery, utente: " + user.getUsername());		
		ISpedizioneDao dao = factory.getDao(user, commessa);
		delivery = dao.inserisciDelivery(delivery);
		HttpStatus status = delivery != null ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
		ResponseEntity<DeliveryJSON> response = new ResponseEntity<DeliveryJSON>(delivery, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<DeliveryJSON> aggiorna(@Valid @RequestBody DeliveryJSON delivery, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_CRUD_ORDINI);
		logger.info("Aggiornamento della delivery, utente: " + user.getUsername());		
		ISpedizioneDao dao = factory.getDao(user, commessa);
		delivery = dao.aggiornaDelivery(delivery);
		HttpStatus status = delivery != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<DeliveryJSON> response = new ResponseEntity<DeliveryJSON>(delivery, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<DeliveryJSON> eliminaSpedizione(@Valid @RequestBody DeliveryJSON delivery, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di eliminazione delivery, utente: " + user.getUsername());
		ISpedizioneDao dao = factory.getDao(user, commessa);
		delivery = dao.eliminaDelivery(delivery);
		HttpStatus status = delivery == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<DeliveryJSON> response = new ResponseEntity<DeliveryJSON>(delivery, status);
		return response;
	}

}
