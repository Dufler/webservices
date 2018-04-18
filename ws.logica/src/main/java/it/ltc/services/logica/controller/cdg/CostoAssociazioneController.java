package it.ltc.services.logica.controller.cdg;

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

import it.ltc.database.model.centrale.CdgCostoAssociazione;
import it.ltc.services.logica.data.cdg.CostoAssociazioneDAO;
import it.ltc.services.logica.validation.cdg.CdgCostoAssociazioneValidator;

@Controller
@RequestMapping("/cdg/costoassociazione")
public class CostoAssociazioneController {
	
	private static final Logger logger = Logger.getLogger("CostoAssociazioneController");
	
	@Autowired
	private CostoAssociazioneDAO dao;
	
	@Autowired
	private CdgCostoAssociazioneValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public CostoAssociazioneController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgCostoAssociazione>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte i costi delle associazioni del controllo di gestione.");
		List<CdgCostoAssociazione> entities = dao.trovaTutte();
		ResponseEntity<List<CdgCostoAssociazione>> response = new ResponseEntity<List<CdgCostoAssociazione>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgCostoAssociazione> inserisci(@Valid @RequestBody CdgCostoAssociazione contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo costo di associazione del controllo di gestione.");
		CdgCostoAssociazione entity = dao.inserisci(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostoAssociazione> response = new ResponseEntity<CdgCostoAssociazione>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgCostoAssociazione> modifica(@Valid @RequestBody CdgCostoAssociazione contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un costo di associazione del controllo di gestione.");
		CdgCostoAssociazione entity = dao.aggiorna(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostoAssociazione> response = new ResponseEntity<CdgCostoAssociazione>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgCostoAssociazione> elimina(@Valid @RequestBody CdgCostoAssociazione contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un costo di associazione del controllo di gestione.");
		CdgCostoAssociazione entity = dao.elimina(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCostoAssociazione> response = new ResponseEntity<CdgCostoAssociazione>(entity, status);
		return response;
	}

}
