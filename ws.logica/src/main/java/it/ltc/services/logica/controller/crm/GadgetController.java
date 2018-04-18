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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.centrale.Gadget;
import it.ltc.services.logica.data.crm.GadgetDAO;
import it.ltc.services.logica.model.crm.FiltroTesto;
import it.ltc.services.logica.validation.crm.FiltroTestoValidator;
import it.ltc.services.logica.validation.crm.GadgetValidator;

@Controller
@RequestMapping("/crm/gadget")
public class GadgetController {

	private static final Logger logger = Logger.getLogger("GadgetController");
	
	@Autowired
	private GadgetDAO dao;
	
	@Autowired
	private GadgetValidator validatorGadget;
	
	@Autowired
	private FiltroTestoValidator validatorFiltro;
	
	@InitBinder("gadget")
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validatorGadget);
	}
	
	@InitBinder("filtroTesto")
	protected void initFiltroBinder(WebDataBinder binder) {
	    binder.setValidator(validatorFiltro);
	}
	
	public GadgetController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Gadget>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i gadget.");
		List<Gadget> entities = dao.trovaTutti();
		ResponseEntity<List<Gadget>> response = new ResponseEntity<List<Gadget>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<Gadget>> trovaDaNome(@Valid @RequestBody FiltroTesto filtro, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i gadget con un dato nome.");
		List<Gadget> entities = dao.trovaDaNome(filtro.getTesto());
		ResponseEntity<List<Gadget>> response = new ResponseEntity<List<Gadget>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Gadget> inserisci(@Valid @RequestBody Gadget gadget, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo gadget.");
		Gadget entity = dao.inserisci(gadget);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Gadget> response = new ResponseEntity<Gadget>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Gadget> modifica(@Valid @RequestBody Gadget gadget, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un gadget.");
		Gadget entity = dao.aggiorna(gadget);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Gadget> response = new ResponseEntity<Gadget>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Gadget> elimina(@RequestBody Gadget gadget, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un gadget.");
		Gadget entity = dao.elimina(gadget);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Gadget> response = new ResponseEntity<Gadget>(entity, status);
		return response;
	}
	
}
