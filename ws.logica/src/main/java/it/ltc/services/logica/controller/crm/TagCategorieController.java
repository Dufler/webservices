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

import it.ltc.database.model.centrale.CrmTagCategoriaMerceologica;
import it.ltc.services.logica.data.crm.TagCategorieDAO;
import it.ltc.services.logica.validation.crm.TagCategorieValidator;

@Controller
@RequestMapping("/crm/tagcategorie")
public class TagCategorieController {

	private static final Logger logger = Logger.getLogger("TagCategorieController");
	
	@Autowired
	private TagCategorieDAO dao;
	
	@Autowired
	private TagCategorieValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public TagCategorieController() {}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CrmTagCategoriaMerceologica>> trovaTutti(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le associazioni le aziende e i tag.");
		List<CrmTagCategoriaMerceologica> entities = dao.trovaTutti();
		ResponseEntity<List<CrmTagCategoriaMerceologica>> response = new ResponseEntity<List<CrmTagCategoriaMerceologica>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/perazienda")
	public ResponseEntity<List<CrmTagCategoriaMerceologica>> trovaDaAzienda(@RequestBody CrmTagCategoriaMerceologica tag, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le associazioni l'azienda specificata e i tag.");
		List<CrmTagCategoriaMerceologica> entities = dao.trovaDaAzienda(tag.getAzienda());
		ResponseEntity<List<CrmTagCategoriaMerceologica>> response = new ResponseEntity<List<CrmTagCategoriaMerceologica>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/pertag")
	public ResponseEntity<List<CrmTagCategoriaMerceologica>> trovaDaTag(@RequestBody CrmTagCategoriaMerceologica tag, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le associazioni l'azienda specificata e i tag.");
		List<CrmTagCategoriaMerceologica> entities = dao.trovaDaTag(tag.getTag());
		ResponseEntity<List<CrmTagCategoriaMerceologica>> response = new ResponseEntity<List<CrmTagCategoriaMerceologica>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CrmTagCategoriaMerceologica> inserisci(@Valid @RequestBody CrmTagCategoriaMerceologica tag, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova associazione fra un'azienda e un brand.");
		CrmTagCategoriaMerceologica entity = dao.inserisci(tag);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CrmTagCategoriaMerceologica> response = new ResponseEntity<CrmTagCategoriaMerceologica>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CrmTagCategoriaMerceologica> elimina(@Valid @RequestBody CrmTagCategoriaMerceologica tag, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un'associazione fra un azienda e un brand.");
		CrmTagCategoriaMerceologica entity = dao.elimina(tag);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CrmTagCategoriaMerceologica> response = new ResponseEntity<CrmTagCategoriaMerceologica>(entity, status);
		return response;
	}
	
}
