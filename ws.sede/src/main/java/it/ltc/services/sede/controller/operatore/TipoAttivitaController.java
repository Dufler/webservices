package it.ltc.services.sede.controller.operatore;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.sede.AttivitaTipo;
import it.ltc.services.sede.data.operatore.TipoAttivitaDAO;

@Controller
@RequestMapping("/tipoattivita")
public class TipoAttivitaController {
	
	private static final Logger logger = Logger.getLogger("TipoAttivitaController");
	
	@Autowired
	private TipoAttivitaDAO dao;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<AttivitaTipo>> trovaTutte(@RequestHeader("authorization") String authenticationString) {
		logger.info("Trovo tutte le attività della sede.");
		List<AttivitaTipo> entities = dao.trovaTutti();
		ResponseEntity<List<AttivitaTipo>> response = new ResponseEntity<List<AttivitaTipo>>(entities, HttpStatus.OK);
		return response;
	}

}
