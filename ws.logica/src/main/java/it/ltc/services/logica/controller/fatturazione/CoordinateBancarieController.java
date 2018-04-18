package it.ltc.services.logica.controller.fatturazione;

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

import it.ltc.database.model.centrale.CoordinateBancarie;
import it.ltc.services.logica.data.fatturazione.CoordinateBancarieDAO;
import it.ltc.services.logica.validation.fatturazione.CoordinateBancarieValidator;

@Controller
@RequestMapping("/coordinatebancarie")
public class CoordinateBancarieController {

	private static final Logger logger = Logger.getLogger("CoordinateBancarieController");

	@Autowired
	private CoordinateBancarieDAO dao;

	@Autowired
	private CoordinateBancarieValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	public CoordinateBancarieController() {}

	// TODO - check sui permessi

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CoordinateBancarie>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti le coordinate bancarie.");
		List<CoordinateBancarie> contrassegni = dao.trovaTutti();
		ResponseEntity<List<CoordinateBancarie>> response = new ResponseEntity<List<CoordinateBancarie>>(contrassegni, HttpStatus.OK);
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CoordinateBancarie> inserisci(@Valid @RequestBody CoordinateBancarie contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di una nuova entity.");
		CoordinateBancarie entity = dao.inserisci(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CoordinateBancarie> response = new ResponseEntity<CoordinateBancarie>(entity, status);
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CoordinateBancarie> modifica(@Valid @RequestBody CoordinateBancarie contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di una entity.");
		CoordinateBancarie entity = dao.aggiorna(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CoordinateBancarie> response = new ResponseEntity<CoordinateBancarie>(entity, status);
		return response;
	}

	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CoordinateBancarie> elimina(@Valid @RequestBody CoordinateBancarie contrassegno, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di una entity.");
		CoordinateBancarie entity = dao.elimina(contrassegno);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CoordinateBancarie> response = new ResponseEntity<CoordinateBancarie>(entity, status);
		return response;
	}

}
