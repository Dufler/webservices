package it.ltc.services.sede.controller.carico;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.legacy.PakiTestaTipoDao;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.ITipoCaricoDao;
import it.ltc.model.shared.json.cliente.TipoCaricoJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.exception.CustomException;

@Controller
@RequestMapping("/tipocarico")
public class TipoCaricoController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("TipoCaricoController");
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<TipoCaricoJSON>> trovaTutti(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Trovo tutti i riepiloghi d'evento della sede.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		ITipoCaricoDao dao = getDao(user, commessa);
		List<TipoCaricoJSON> entities = dao.trovaTutti();
		HttpStatus status = entities.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<TipoCaricoJSON>> response = new ResponseEntity<List<TipoCaricoJSON>>(entities, status);
		return response;
	}
	
	private ITipoCaricoDao getDao(Utente user, String risorsaCommessa) {
		ITipoCaricoDao dao;
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null) {
			String persistenceUnitName = commessa.getNomeRisorsa();
			dao = new PakiTestaTipoDao(persistenceUnitName); //FIXME : Farne uno per il nuovo quando sarà necessario.
		} else {
			throw new CustomException("E' necessario specificare una commessa valida nell'header della richiesta.");
		}
		return dao;
 	}
}
