package it.ltc.services.sede.controller.magazzino;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.utente.Utente;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.magazzino.FactoryDaoInventario;
import it.ltc.services.sede.data.magazzino.InventarioDao;
import it.ltc.services.sede.model.magazzino.ColloInventarioConSeriali;
import it.ltc.services.sede.model.magazzino.ControlloSeriale;

@Controller
@RequestMapping("/inventario")
public class InventarioController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("TipoCaricoController");
	
	@Autowired
	private FactoryDaoInventario factory;
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/seriali/nuovo")
	public ResponseEntity<ColloInventarioConSeriali> nuovo(@RequestBody ColloInventarioConSeriali collo, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di creazione collo inventario dall'utente: " + user.getUsername());
		InventarioDao dao = factory.getDao(user, commessa);
		collo = dao.nuovoCollo(collo);
		HttpStatus status = collo == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<ColloInventarioConSeriali> response = new ResponseEntity<ColloInventarioConSeriali>(collo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/seriali/scarico")
	public ResponseEntity<ColloInventarioConSeriali> scarico(@RequestBody ColloInventarioConSeriali collo, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di creazione collo di scarico dall'utente: " + user.getUsername());
		InventarioDao dao = factory.getDao(user, commessa);
		collo = dao.nuovoColloDiScarico(collo);
		HttpStatus status = collo == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<ColloInventarioConSeriali> response = new ResponseEntity<ColloInventarioConSeriali>(collo, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/seriali/controlloimpegno")
	public ResponseEntity<Void> controlloImpegno(@RequestBody ControlloSeriale seriale, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di creazione collo inventario dall'utente: " + user.getUsername());
		InventarioDao dao = factory.getDao(user, commessa);
		boolean check = dao.checkImpegnoSeriale(seriale);
		HttpStatus status = check ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", value="/distruggicollo")
	public ResponseEntity<Void> distruggiCollo(@RequestBody ColloInventarioConSeriali collo, @RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di distruzione collo dall'utente: " + user.getUsername());
		InventarioDao dao = factory.getDao(user, commessa);
		boolean distrutto = dao.distruggiCollo(collo);
		HttpStatus status = distrutto ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
		ResponseEntity<Void> response = new ResponseEntity<Void>(status);
		return response;
	}

}
