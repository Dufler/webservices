package it.ltc.services.sede.controller.magazzino;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.IMagazzinoDao;
import it.ltc.model.shared.json.interno.MagazzinoJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.magazzino.MagazzinoDAOFactory;

@Controller
@RequestMapping("/magazzino")
public class MagazzinoController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("MagazzinoController");
	
	@Autowired
	private MagazzinoDAOFactory factory;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<MagazzinoJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di elenco magazzini");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Utente: " + user.getUsername());
		IMagazzinoDao dao = factory.getDao(user, commessa);
		List<MagazzinoJSON> magazzini = dao.trovaliTutti();
		HttpStatus status = magazzini.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Magazzini trovati: " + magazzini.size());
		ResponseEntity<List<MagazzinoJSON>> response = new ResponseEntity<List<MagazzinoJSON>>(magazzini, status);
		return response;
	}

}
