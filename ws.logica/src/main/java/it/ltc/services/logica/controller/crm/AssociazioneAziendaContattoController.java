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

import it.ltc.database.model.centrale.AziendaContatti;
import it.ltc.services.logica.data.crm.AssociazioneAziendaContattiDAO;
import it.ltc.services.logica.validation.crm.AssociazioneAziendaContattiValidator;


@Controller
@RequestMapping("/crm/associazioneaziendacontatto")
public class AssociazioneAziendaContattoController {
	
	private static final Logger logger = Logger.getLogger("AssociazioneAziendaContattoController");
	
	@Autowired
	private AssociazioneAziendaContattiDAO dao;
	
	@Autowired
	private AssociazioneAziendaContattiValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public AssociazioneAziendaContattoController() {}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<AziendaContatti>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le associazioni fra aziende e contatti.");
		List<AziendaContatti> entities = dao.trovaTutti();
		ResponseEntity<List<AziendaContatti>> response = new ResponseEntity<List<AziendaContatti>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<AziendaContatti> inserisci(@Valid @RequestBody AziendaContatti associazione, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova associazione fra un'azienda e un contatto.");
		AziendaContatti entity = dao.inserisci(associazione);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<AziendaContatti> response = new ResponseEntity<AziendaContatti>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<AziendaContatti> elimina(@Valid @RequestBody AziendaContatti associazione, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un'associazione fra un azienda e un contatto.");
		AziendaContatti entity = dao.elimina(associazione);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<AziendaContatti> response = new ResponseEntity<AziendaContatti>(entity, status);
		return response;
	}
	
}
