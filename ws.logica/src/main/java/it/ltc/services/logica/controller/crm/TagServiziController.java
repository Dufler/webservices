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

import it.ltc.database.model.centrale.CrmTagServiziRichiesti;
import it.ltc.services.logica.data.crm.TagServiziDAO;
import it.ltc.services.logica.validation.crm.TagServiziRichiestiValidator;

@Controller
@RequestMapping("/crm/tagservizi")
public class TagServiziController {
	
	private static final Logger logger = Logger.getLogger("TagServiziController");
	
	@Autowired
	private TagServiziDAO dao;
	
	@Autowired
	private TagServiziRichiestiValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public TagServiziController() {}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CrmTagServiziRichiesti>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le associazioni le aziende e i tag.");
		List<CrmTagServiziRichiesti> entities = dao.trovaTutti();
		ResponseEntity<List<CrmTagServiziRichiesti>> response = new ResponseEntity<List<CrmTagServiziRichiesti>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/perazienda")
	public ResponseEntity<List<CrmTagServiziRichiesti>> trovaDaAzienda(@RequestBody CrmTagServiziRichiesti tag, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le associazioni l'azienda specificata e i tag.");
		List<CrmTagServiziRichiesti> entities = dao.trovaDaAzienda(tag.getAzienda());
		ResponseEntity<List<CrmTagServiziRichiesti>> response = new ResponseEntity<List<CrmTagServiziRichiesti>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/pertag")
	public ResponseEntity<List<CrmTagServiziRichiesti>> trovaDaTag(@RequestBody CrmTagServiziRichiesti tag, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le associazioni l'azienda specificata e i tag.");
		List<CrmTagServiziRichiesti> entities = dao.trovaDaTag(tag.getTag());
		ResponseEntity<List<CrmTagServiziRichiesti>> response = new ResponseEntity<List<CrmTagServiziRichiesti>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CrmTagServiziRichiesti> inserisci(@Valid @RequestBody CrmTagServiziRichiesti tag, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova associazione fra un'azienda e un brand.");
		CrmTagServiziRichiesti entity = dao.inserisci(tag);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CrmTagServiziRichiesti> response = new ResponseEntity<CrmTagServiziRichiesti>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CrmTagServiziRichiesti> elimina(@Valid @RequestBody CrmTagServiziRichiesti tag, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un'associazione fra un azienda e un brand.");
		CrmTagServiziRichiesti entity = dao.elimina(tag);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CrmTagServiziRichiesti> response = new ResponseEntity<CrmTagServiziRichiesti>(entity, status);
		return response;
	}

}
