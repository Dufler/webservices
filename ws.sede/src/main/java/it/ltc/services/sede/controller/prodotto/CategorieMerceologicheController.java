package it.ltc.services.sede.controller.prodotto;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.shared.prodotti.CategoriaMerceologicaLegacyDaoImpl;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.model.shared.dao.ICategoriaMerceologicaDao;
import it.ltc.model.shared.json.interno.CategoriaMerceologicaJSON;
import it.ltc.services.custom.controller.RestController;
import it.ltc.services.custom.exception.CustomException;

@Controller
@RequestMapping("/categoriamerceologica")
public class CategorieMerceologicheController extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("CategorieMerceologicheController");
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<CategoriaMerceologicaJSON>> trovaTutti(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Trovo tutti i tipi di carico per la commessa.");
		Utente user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		ICategoriaMerceologicaDao dao = getDao(user, commessa);
		List<CategoriaMerceologicaJSON> entities = dao.trovaTutte();
		HttpStatus status = entities.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		ResponseEntity<List<CategoriaMerceologicaJSON>> response = new ResponseEntity<List<CategoriaMerceologicaJSON>>(entities, status);
		return response;
	}
	
	private ICategoriaMerceologicaDao getDao(Utente user, String risorsaCommessa) {
		ICategoriaMerceologicaDao dao;
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null) {
			String persistenceUnitName = commessa.getNomeRisorsa();
			dao = new CategoriaMerceologicaLegacyDaoImpl(persistenceUnitName); //FIXME : Farne uno per il nuovo quando sarà necessario.
		} else {
			throw new CustomException("E' necessario specificare una commessa valida nell'header della richiesta.");
		}
		return dao;
 	}

}
