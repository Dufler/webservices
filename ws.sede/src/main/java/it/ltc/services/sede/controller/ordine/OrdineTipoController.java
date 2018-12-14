package it.ltc.services.sede.controller.ordine;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.shared.ordine.TipoOrdineLegacyDAOImpl;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.ITipoOrdineDao;
import it.ltc.model.shared.json.cliente.TipoOrdineJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.exception.CustomException;

@Controller
@RequestMapping("/ordine/tipo")
public class OrdineTipoController extends RestController {
	
public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("TipoOrdineController");
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<TipoOrdineJSON>> trovaTutti(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Trovo tutti i tipi di carico per la commessa.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		ITipoOrdineDao dao = getDao(user, commessa);
		List<TipoOrdineJSON> entities = dao.trovaTutti();
		HttpStatus status = entities.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<TipoOrdineJSON>> response = new ResponseEntity<List<TipoOrdineJSON>>(entities, status);
		return response;
	}
	
	private ITipoOrdineDao getDao(Utente user, String risorsaCommessa) {
		ITipoOrdineDao dao;
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null) {
			String persistenceUnitName = commessa.getNomeRisorsa();
			dao = new TipoOrdineLegacyDAOImpl(persistenceUnitName); //FIXME : Farne uno per il nuovo quando sarà necessario.
		} else {
			throw new CustomException("E' necessario specificare una commessa valida nell'header della richiesta.");
		}
		return dao;
 	}

}
