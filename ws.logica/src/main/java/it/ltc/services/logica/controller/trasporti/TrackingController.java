package it.ltc.services.logica.controller.trasporti;

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

import it.ltc.database.model.centrale.SpedizioneLight;
import it.ltc.services.logica.data.trasporti.SpedizioneDAO;
import it.ltc.services.logica.model.trasporti.CriteriRicercaSpedizioniLight;
import it.ltc.services.logica.model.trasporti.SpedizioneCompletaJSON;
import it.ltc.services.logica.model.trasporti.TrackingJSON;
import it.ltc.services.logica.validation.trasporti.CriteriRicercaSpedizioniLightValidator;

@Controller
@RequestMapping("/tracking")
public class TrackingController {
	
	private static final Logger logger = Logger.getLogger("TrackingController");
	
	@Autowired
	private SpedizioneDAO daoSpedizioni;
	
	@Autowired
	private CriteriRicercaSpedizioniLightValidator validator;
	
	@InitBinder()
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(validator);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/eventi/{id}")
	public ResponseEntity<List<TrackingJSON>> getTracking(@RequestHeader("authorization") String authenticationString, @PathVariable("id") Integer id) {
		logger.info("Trovo il tracking della spedizione.");
		List<TrackingJSON> tracking = daoSpedizioni.getTracking(id);
		HttpStatus status = (tracking != null && !tracking.isEmpty()) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
		ResponseEntity<List<TrackingJSON>> response = new ResponseEntity<List<TrackingJSON>>(tracking, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/dettagli/{id}")
	public ResponseEntity<SpedizioneCompletaJSON> getDettagli(@RequestHeader("authorization") String authenticationString, @PathVariable("id") Integer id) {
		logger.info("Trovo la spedizione e il suo tracking.");
		SpedizioneCompletaJSON spedizione = daoSpedizioni.trovaDettagli(id);
		HttpStatus status = spedizione != null ? HttpStatus.OK : HttpStatus.NO_CONTENT;
		ResponseEntity<SpedizioneCompletaJSON> response = new ResponseEntity<SpedizioneCompletaJSON>(spedizione, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<SpedizioneLight>> cerca(@Valid @RequestBody CriteriRicercaSpedizioniLight criteri, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le spedizioni in base ai criteri specificati.");
		List<SpedizioneLight> spedizioni = daoSpedizioni.trovaSpedizioni(criteri);
		ResponseEntity<List<SpedizioneLight>> response = new ResponseEntity<List<SpedizioneLight>>(spedizioni, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/archivia/{id}")
	public ResponseEntity<Void> archivia(@RequestHeader("authorization") String authenticationString, @PathVariable("id") Integer id) {
		logger.info("Archivio la spedizione.");
		boolean archiviazione = daoSpedizioni.archiviaPerTracking(id);
		HttpStatus status = archiviazione ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/elimina/{id}")
	public ResponseEntity<Void> elimina(@RequestHeader("authorization") String authenticationString, @PathVariable("id") Integer id) {
		logger.info("Elimino la spedizione.");
		boolean eliminazione = daoSpedizioni.eliminaPerTracking(id);
		HttpStatus status = eliminazione ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}

}
