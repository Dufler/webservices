package it.ltc.services.sede.controller.carico;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.dao.IStagioneDao;
import it.ltc.model.shared.json.interno.StagioneJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.sede.data.magazzino.StagioneDAOFactory;

@Controller
@RequestMapping("/stagione")
public class StagioneController extends RestController {

	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger(StagioneController.class);
	
	@Autowired
	private StagioneDAOFactory factory;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<StagioneJSON>> lista(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di elenco stagioni da: " + user.getUsername());
		IStagioneDao dao = factory.getDao(user, commessa);
		List<StagioneJSON> magazzini = dao.trovaTutte();
		HttpStatus status = magazzini.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		logger.info("Stagioni trovate: " + magazzini.size());
		ResponseEntity<List<StagioneJSON>> response = new ResponseEntity<List<StagioneJSON>>(magazzini, status);
		return response;
	}
}
