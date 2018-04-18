package it.ltc.services.clienti.controller.magazzino;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.dao.common.LoginController;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.database.model.utente.Utente;
import it.ltc.services.clienti.data.magazzino.SaldiMagazzinoDAO;
import it.ltc.services.clienti.data.magazzino.SaldiMagazzinoDAOImpl;
import it.ltc.services.clienti.model.prodotto.InfoProdotto;

@Controller
@RequestMapping("/giacenza")
public class ControllerMagazzino {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("ControllerMagazzino");
	
	private final LoginController loginManager;
	
	public ControllerMagazzino() {
		loginManager = LoginController.getInstance();
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/tutti")
	public ResponseEntity<List<InfoProdotto>> getGiacenza(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		logger.info("Nuova richiesta di sincronizzazione dell'intero magazzino.");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: '" + user.getUsername() + "'");
		HttpStatus status;
		List<InfoProdotto> info;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			SaldiMagazzinoDAO dao = getDao(user, commessa);
			info = dao.getDisponibilita();
			status = info.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Sono state restituite " + info.size() + " info su disponibilità.");
		} else {
			info = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di sincronizzazione magazzino fallita: ";
			String reason = user != null ? "Permessi insufficienti" : "Autenticazione fallita";
			logger.warn(message + reason);
		}
		ResponseEntity<List<InfoProdotto>> response = new ResponseEntity<List<InfoProdotto>>(info, status);
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/{magazzino}/tutti")
	public ResponseEntity<List<InfoProdotto>> getGiacenzaPerMagazzino(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa, @PathVariable(value="magazzino") String magazzino) {
		logger.info("Nuova richiesta di sincronizzazione magazzino per il magazzino '" + magazzino + "'.");
		Utente user = loginManager.getUserByAuthenticationString(authenticationString);
		logger.info("Utente: '" + user.getUsername() + "'");
		HttpStatus status;
		List<InfoProdotto> info;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			SaldiMagazzinoDAO dao = getDao(user, commessa);
			info = dao.getDisponibilita(magazzino);
			status = info.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
			logger.info("Sono state restituite " + info.size() + " info su disponibilità.");
		} else {
			info = null;
			status = HttpStatus.UNAUTHORIZED;
			String message = "Richiesta di sincronizzazione magazzino fallita: ";
			String reason = user != null ? "Permessi insufficienti" : "Autenticazione fallita";
			logger.warn(message + reason);
		}
		ResponseEntity<List<InfoProdotto>> response = new ResponseEntity<List<InfoProdotto>>(info, status);
		return response;
	}

	private SaldiMagazzinoDAO getDao(Utente user, String risorsaCommessa) {
		SaldiMagazzinoDAO dao;
		Commessa commessa = loginManager.getCommessaByUserAndResource(user, risorsaCommessa);
		if (commessa != null) {
			String persistenceUnitName = commessa.getNomeRisorsa();
			dao = commessa.isLegacy() ? new SaldiMagazzinoDAOImpl(persistenceUnitName) : new SaldiMagazzinoDAOImpl(persistenceUnitName); //FIXME - Quando ci sarà sul nuovo.
		} else {
			dao = null;
		}
		return dao;
	}

}