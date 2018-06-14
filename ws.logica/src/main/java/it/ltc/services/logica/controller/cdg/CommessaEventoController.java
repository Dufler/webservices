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

import it.ltc.database.model.centrale.CdgCommessaEvento;
import it.ltc.services.logica.data.cdg.CommessaEventoDAO;
import it.ltc.services.logica.validation.cdg.CdgCommessaEventoValidator;

@Controller
@RequestMapping("/cdg/commessaevento")
public class CommessaEventoController {
	
	private static final Logger logger = Logger.getLogger("CommessaEventoController");
	
	@Autowired
	private CommessaEventoDAO dao;
	
	@Autowired
	private CdgCommessaEventoValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	public CommessaEventoController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgCommessaEvento>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti gli abbinamenti commessa-evento del controllo di gestione.");
		List<CdgCommessaEvento> entities = dao.trovaTutte();
		ResponseEntity<List<CdgCommessaEvento>> response = new ResponseEntity<List<CdgCommessaEvento>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgCommessaEvento> inserisci(@Valid @RequestBody CdgCommessaEvento contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuovo abbinamento commessa-evento del controllo di gestione.");
		CdgCommessaEvento entity = dao.inserisci(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCommessaEvento> response = new ResponseEntity<CdgCommessaEvento>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgCommessaEvento> modifica(@Valid @RequestBody CdgCommessaEvento contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un abbinamento commessa-evento del controllo di gestione.");
		CdgCommessaEvento entity = dao.aggiorna(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCommessaEvento> response = new ResponseEntity<CdgCommessaEvento>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgCommessaEvento> elimina(@Valid @RequestBody CdgCommessaEvento contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un abbinamento commessa-evento del controllo di gestione.");
		CdgCommessaEvento entity = dao.elimina(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgCommessaEvento> response = new ResponseEntity<CdgCommessaEvento>(entity, status);
		return response;
	}

}
