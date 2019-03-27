package it.ltc.services.clienti.controller.magazzino;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.ltc.database.model.utente.UtenteUtenti;
import it.ltc.model.shared.json.cliente.InfoProdotto;
import it.ltc.services.clienti.data.magazzino.SaldiMagazzinoDAO;
import it.ltc.services.clienti.data.magazzino.SaldiMagazzinoFactory;
import it.ltc.services.custom.controller.RestController;

@Controller
@RequestMapping("/giacenza")
public class ControllerMagazzino extends RestController {
	
	public static final int ID_PERMESSO_WEB_SERVICE = 2;
	
	private static final Logger logger = Logger.getLogger("ControllerMagazzino");
	
	@Autowired
	private SaldiMagazzinoFactory factory;
	
	public ControllerMagazzino() {}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json", value="/tutti")
	public ResponseEntity<List<InfoProdotto>> getGiacenza(@RequestHeader("authorization") String authenticationString, @RequestHeader(value="commessa", required=false) String commessa) {
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di sincronizzazione dell'intero magazzino dall'utente: '" + user.getUsername() + "'");
		HttpStatus status;
		List<InfoProdotto> info;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			SaldiMagazzinoDAO dao = factory.getDao(user, commessa);
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
		UtenteUtenti user = checkCredentialsAndPermission(authenticationString, ID_PERMESSO_WEB_SERVICE);
		logger.info("Nuova richiesta di sincronizzazione magazzino per il magazzino '" + magazzino + "' dall'utente: '" + user.getUsername() + "'");
		HttpStatus status;
		List<InfoProdotto> info;
		if (user != null && user.isAllowedTo(ID_PERMESSO_WEB_SERVICE)) {
			SaldiMagazzinoDAO dao = factory.getDao(user, commessa);
			info = dao.getDisponibilitaPerCodiceMagazzinoCliente(magazzino);
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

}
