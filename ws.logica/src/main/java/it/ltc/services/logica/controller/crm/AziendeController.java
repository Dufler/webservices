package it.ltc.services.logica.controller.crm;

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

import it.ltc.database.model.centrale.Azienda;
import it.ltc.database.model.centrale.Indirizzo;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.logica.data.crm.AziendaDAO;
import it.ltc.services.logica.model.crm.FiltroTesto;
import it.ltc.services.logica.validation.crm.AziendaValidator;
import it.ltc.services.logica.validation.crm.FiltroTestoValidator;
import it.ltc.services.logica.validation.trasporti.IndirizzoValidator;

@Controller
@RequestMapping("/crm/azienda")
public class AziendeController extends RestController {
	
	private static final Logger logger = Logger.getLogger("AziendeController");
	
	@Autowired
	private AziendaDAO dao;
	
	@Autowired
	private AziendaValidator validatorAzienda;
	
	@Autowired
	private FiltroTestoValidator validatorFiltro;
	
	@Autowired
	private IndirizzoValidator validatorIndirizzo;
	
	@InitBinder("azienda")
	protected void initAziendaBinder(WebDataBinder binder) {
	    binder.setValidator(validatorAzienda);
	}
	
	@InitBinder("filtroTesto")
	protected void initFiltroBinder(WebDataBinder binder) {
	    binder.setValidator(validatorFiltro);
	}
	
	@InitBinder("indirizzo")
	protected void initIndirizzoBinder(WebDataBinder binder) {
	    binder.setValidator(validatorIndirizzo);
	}
	
	public AziendeController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Azienda>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le aziende.");
		List<Azienda> entities = dao.trovaTutte();
		ResponseEntity<List<Azienda>> response = new ResponseEntity<List<Azienda>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/contatto/{id}")
	public ResponseEntity<List<Azienda>> trovaDaContatto(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo tutte le aziende afferenti al dato contatto.");
		List<Azienda> entities = dao.trovaDaContatto(id);
		ResponseEntity<List<Azienda>> response = new ResponseEntity<List<Azienda>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/brand/{id}")
	public ResponseEntity<List<Azienda>> trovaDaBrand(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo tutte le aziende afferenti al dato brand.");
		List<Azienda> entities = dao.trovaDaBrand(id);
		ResponseEntity<List<Azienda>> response = new ResponseEntity<List<Azienda>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/indirizzo/{id}")
	public ResponseEntity<Indirizzo> trovaIndirizzo(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo l'indirizzo dell'azienda specificata.");
		Indirizzo entity = dao.trovaIndirizzo(id);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Indirizzo> response = new ResponseEntity<Indirizzo>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/indirizzo/{id}")
	public ResponseEntity<Indirizzo> salvaIndirizzo(@Valid @RequestBody Indirizzo indirizzo, @RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Salvo l'indirizzo dell'azienda specificata.");
		Indirizzo entity = dao.salvaIndirizzo(id, indirizzo);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Indirizzo> response = new ResponseEntity<Indirizzo>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<Azienda>> trovaDaNome(@Valid @RequestBody FiltroTesto filtro, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le aziende con un dato nome.");
		List<Azienda> entities = dao.trovaDaNome(filtro.getTesto());
		ResponseEntity<List<Azienda>> response = new ResponseEntity<List<Azienda>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Azienda> inserisci(@Valid @RequestBody Azienda azienda, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova azienda.");
		Azienda entity = dao.inserisci(azienda);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Azienda> response = new ResponseEntity<Azienda>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Azienda> modifica(@Valid @RequestBody Azienda azienda, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una azienda.");
		Azienda entity = dao.aggiorna(azienda);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Azienda> response = new ResponseEntity<Azienda>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Azienda> elimina(@RequestBody Azienda azienda, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una azienda.");
		Azienda entity = dao.elimina(azienda);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Azienda> response = new ResponseEntity<Azienda>(entity, status);
		return response;
	}

}
