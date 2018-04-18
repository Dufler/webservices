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

import it.ltc.database.model.centrale.Contatto;
import it.ltc.services.logica.data.crm.ContattoDAO;
import it.ltc.services.logica.model.crm.FiltroTesto;
import it.ltc.services.logica.validation.crm.ContattoValidator;
import it.ltc.services.logica.validation.crm.FiltroTestoValidator;

@Controller
@RequestMapping("/crm/contatto")
public class ContattiController {
	
	private static final Logger logger = Logger.getLogger("ContattiController");
	
	@Autowired
	private ContattoDAO dao;
	
	@Autowired
	private ContattoValidator validatorContatto;
	
	@Autowired
	private FiltroTestoValidator validatorFiltro;
	
	@InitBinder("contatto")
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validatorContatto);
	}
	
	@InitBinder("filtroTesto")
	protected void initFiltroBinder(WebDataBinder binder) {
	    binder.setValidator(validatorFiltro);
	}
	
	public ContattiController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Contatto>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i contatti.");
		List<Contatto> entities = dao.trovaTutti();
		ResponseEntity<List<Contatto>> response = new ResponseEntity<List<Contatto>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/azienda/{id}")
	public ResponseEntity<List<Contatto>> trovaDaContatto(@RequestHeader("authorization") String authenticationString, @PathVariable("id") int id) {
		logger.info("Trovo tutti i contatti afferenti alla data azienda.");
		List<Contatto> entities = dao.trovaDaAzienda(id);
		ResponseEntity<List<Contatto>> response = new ResponseEntity<List<Contatto>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<Contatto>> trovaDaNome(@Valid @RequestBody FiltroTesto filtro, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i contatti con un dato nome.");
		List<Contatto> entities = dao.trovaDaNome(filtro.getTesto());
		ResponseEntity<List<Contatto>> response = new ResponseEntity<List<Contatto>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Contatto> inserisci(@Valid @RequestBody Contatto contatto, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo contatto.");
		Contatto entity = dao.inserisci(contatto);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Contatto> response = new ResponseEntity<Contatto>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Contatto> modifica(@Valid @RequestBody Contatto contatto, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un contatto.");
		Contatto entity = dao.aggiorna(contatto);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Contatto> response = new ResponseEntity<Contatto>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Contatto> elimina(@RequestBody Contatto contatto, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un contatto.");
		Contatto entity = dao.elimina(contatto);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Contatto> response = new ResponseEntity<Contatto>(entity, status);
		return response;
	}

}
