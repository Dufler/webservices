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

import it.ltc.database.model.centrale.AziendaBrand;
import it.ltc.services.logica.data.crm.AssociazioneAziendaBrandDAO;
import it.ltc.services.logica.validation.crm.AssociazioneAziendaBrandValidator;

@Controller
@RequestMapping("/crm/associazioneaziendabrand")
public class AssociazioneAziendaBrandController {
	
	private static final Logger logger = Logger.getLogger("AssociazioneAziendaBrandController");
	
	@Autowired
	private AssociazioneAziendaBrandDAO dao;
	
	@Autowired
	private AssociazioneAziendaBrandValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public AssociazioneAziendaBrandController() {}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<AziendaBrand>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le associazioni fra aziende e brand.");
		List<AziendaBrand> entities = dao.trovaTutti();
		ResponseEntity<List<AziendaBrand>> response = new ResponseEntity<List<AziendaBrand>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<AziendaBrand> inserisci(@Valid @RequestBody AziendaBrand associazione, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova associazione fra un'azienda e un brand.");
		AziendaBrand entity = dao.inserisci(associazione);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<AziendaBrand> response = new ResponseEntity<AziendaBrand>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<AziendaBrand> elimina(@Valid @RequestBody AziendaBrand associazione, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un'associazione fra un azienda e un brand.");
		AziendaBrand entity = dao.elimina(associazione);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<AziendaBrand> response = new ResponseEntity<AziendaBrand>(entity, status);
		return response;
	}

}
