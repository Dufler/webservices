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

import it.ltc.database.model.centrale.GadgetInviati;
import it.ltc.services.logica.data.crm.GadgetInviatiDAO;
import it.ltc.services.logica.validation.crm.GadgetInviatiValidator;

@Controller
@RequestMapping("/crm/gadgetinviati")
public class GadgetInviatiController {
	
	private static final Logger logger = Logger.getLogger("GadgetInviatiController");
	
	@Autowired
	private GadgetInviatiDAO dao;
	
	@Autowired
	private GadgetInviatiValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public GadgetInviatiController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<GadgetInviati>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i gadget inviati.");
		List<GadgetInviati> entities = dao.trovaTutti();
		ResponseEntity<List<GadgetInviati>> response = new ResponseEntity<List<GadgetInviati>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/azienda/{id}")
	public ResponseEntity<List<GadgetInviati>> trovaDaAzienda(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo tutti i gadget inviati afferenti alla data azienda.");
		List<GadgetInviati> entities = dao.trovaDaAzienda(id);
		ResponseEntity<List<GadgetInviati>> response = new ResponseEntity<List<GadgetInviati>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/gadget/{id}")
	public ResponseEntity<List<GadgetInviati>> trovaDaGadget(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo tutti i gadget inviati afferenti al dato gadget.");
		List<GadgetInviati> entities = dao.trovaDaGadget(id);
		ResponseEntity<List<GadgetInviati>> response = new ResponseEntity<List<GadgetInviati>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<GadgetInviati> inserisci(@Valid @RequestBody GadgetInviati gadgetInviati, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo gadget inviato.");
		GadgetInviati entity = dao.inserisci(gadgetInviati);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<GadgetInviati> response = new ResponseEntity<GadgetInviati>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<GadgetInviati> modifica(@Valid @RequestBody GadgetInviati gadgetInviati, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un gadget inviato.");
		GadgetInviati entity = dao.aggiorna(gadgetInviati);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<GadgetInviati> response = new ResponseEntity<GadgetInviati>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<GadgetInviati> elimina(@RequestBody GadgetInviati gadgetInviati, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un gadget inviato.");
		GadgetInviati entity = dao.elimina(gadgetInviati);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<GadgetInviati> response = new ResponseEntity<GadgetInviati>(entity, status);
		return response;
	}

}
