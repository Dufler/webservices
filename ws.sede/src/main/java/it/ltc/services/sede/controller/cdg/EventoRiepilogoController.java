package it.ltc.services.sede.controller.cdg;

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

import it.ltc.database.model.sede.CdgEventoRiepilogo;
import it.ltc.services.sede.data.cdg.EventoRiepilogoDAO;
import it.ltc.services.sede.model.cdg.FiltroEventoRiepilogo;
import it.ltc.services.sede.validation.cdg.EventoRiepilogoValidator;
import it.ltc.services.sede.validation.cdg.FiltroEventoRiepilogoValidator;

@Controller
@RequestMapping("/cdg/eventoriepilogo")
public class EventoRiepilogoController {
	
	private static final Logger logger = Logger.getLogger("EventoRiepilogoController");
	
	@Autowired
	private EventoRiepilogoDAO dao;
	
	@Autowired
	private EventoRiepilogoValidator validatorEvento;
	
	@Autowired
	private FiltroEventoRiepilogoValidator validatorFiltro;
	
	@InitBinder("cdgEventoRiepilogoJSON")
	protected void initEventoBinder(WebDataBinder binder) {
	    binder.setValidator(validatorEvento);
	}
	
	@InitBinder("filtroEventoRiepilogo")
	protected void initFiltroBinder(WebDataBinder binder) {
	    binder.setValidator(validatorFiltro);
	}
	
	public EventoRiepilogoController() {}
	
	//TODO - check sui permessi
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/cerca")
	public ResponseEntity<List<CdgEventoRiepilogo>> trovaPerCommessaEData(@Valid @RequestBody FiltroEventoRiepilogo filtro, @RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i riepiloghi d'evento della sede che corrispondo al filtro specificato.");
		List<CdgEventoRiepilogo> entities = dao.trovaPerCommessaEData(filtro);
		ResponseEntity<List<CdgEventoRiepilogo>> response = new ResponseEntity<List<CdgEventoRiepilogo>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CdgEventoRiepilogo>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutti i riepiloghi d'evento della sede.");
		List<CdgEventoRiepilogo> entities = dao.trovaTutti();
		ResponseEntity<List<CdgEventoRiepilogo>> response = new ResponseEntity<List<CdgEventoRiepilogo>>(entities, HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<CdgEventoRiepilogo> inserisci(@Valid @RequestBody CdgEventoRiepilogo evento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Inserimento di un nuovo riepilogo d'evento.");
		CdgEventoRiepilogo entity = dao.inserisci(evento);
		HttpStatus status = entity != null ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgEventoRiepilogo> response = new ResponseEntity<CdgEventoRiepilogo>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<CdgEventoRiepilogo> modifica(@Valid @RequestBody CdgEventoRiepilogo evento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Modifica di un riepilogo d'evento.");
		CdgEventoRiepilogo entity = dao.aggiorna(evento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgEventoRiepilogo> response = new ResponseEntity<CdgEventoRiepilogo>(entity, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<CdgEventoRiepilogo> elimina(@Valid @RequestBody CdgEventoRiepilogo evento, @RequestHeader("authorization") String authenticationString) {
		logger.info("Eliminazione di un riepilogo d'evento.");
		CdgEventoRiepilogo entity = dao.elimina(evento);
		HttpStatus status = entity != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<CdgEventoRiepilogo> response = new ResponseEntity<CdgEventoRiepilogo>(entity, status);
		return response;
	}

}
