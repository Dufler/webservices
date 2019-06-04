package it.ltc.services.custom.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import org.jboss.logging.Logger;

import it.ltc.database.dao.utente.CommessaUtentiDao;
import it.ltc.database.dao.utente.UtenteUtentiDao;
import it.ltc.database.model.utente.CommessaUtenti;
import it.ltc.database.model.utente.UtenteUtenti;
import sun.misc.BASE64Decoder;

/**
 * Classe singleton che si occupa di validare le richieste ai web services.
 * @author Damiano
 *
 */
@SuppressWarnings("restriction")
public class LoginController {
	
	private static final Logger logger = Logger.getLogger("LoginController");
	
	private final BASE64Decoder decoder;
	private final UtenteUtentiDao userDao;
	private final CommessaUtentiDao commesseDao;

	public LoginController() {
		decoder = new BASE64Decoder();
		userDao = new UtenteUtentiDao();
		commesseDao = new CommessaUtentiDao();
	}

	/**
	 * Controlla che esista un utente con il dato username e password.
	 * Nel caso in cui l'utente non esista o la password sia errata viene restituito <code>null</code>.
	 * @param username l'username dell'utente.
	 * @param password la password dell'utente.
	 * @return l'utente e le sue info se l'autenticazione riesce, <code>null</code> altrimenti.
	 */
	public UtenteUtenti getUserByUsernameAndPassword(String username, String password) {
		UtenteUtenti user;
		//Controllo che mi sia stato fornito un'username valido e una password (anche vuota, per assurdo)
		if (username != null && !username.isEmpty() && password != null) {
			user = userDao.getUserByUsername(username, false, false);
			//Se ho trovato l'utente ne controllo la correttazza della password.
			if (user != null) {
				String hashedPassword = getHash(password);
		        String storedPassword = user.getPassword();
		        //Controllo se la password coincide, nel caso in cui sia errata restituisco null.
		        if (!storedPassword.equals(hashedPassword))
		        	user = null;
			}
		} else {
			user = null;
		}
		return user;
	}
	
	/**
	 * Restituisce l'utente validando la stringa di autenticazione.
	 * Nel caso in cui l'utente non esista, la password sia errata o la stringa sia malformata viene restituito <code>null</code>.
	 * @param authenticationString la stringa contenuta nell'header della richiesta http.
	 * @return l'utente se l'autenticazione è andata a buon fine, <code>null</code> altrimenti.
	 */
	public UtenteUtenti getUserByAuthenticationString(String authenticationString) {
		return getUserByAuthenticationString(authenticationString, false);
	}
	
	/**
	 * Restituisce l'utente validando la stringa di autenticazione.
	 * Nel caso in cui l'utente non esista, la password sia errata o la stringa sia malformata viene restituito <code>null</code>.
	 * E' possibile indicare se ricaricare i dettagli dell'utenza come i permessi, commesse etc.
	 * @param authenticationString
	 * @param refresh indica se ricaricare i dettagli dell'utenza.
	 * @return l'utente se l'autenticazione è andata a buon fine, <code>null</code> altrimenti.
	 */
	public UtenteUtenti getUserByAuthenticationString(String authenticationString, boolean refresh) {
		UtenteUtenti user;
		String decodedAuth = "";
		// Header is in the format "Basic 5tyc0uiDat4"
		// We need to extract data before decoding it back to original string
		String[] authParts = authenticationString.split("\\s+");
		String authInfo = authParts[1];
		// Decode the data back to original string
		byte[] bytes = null;
		try {
			//bytes = new BASE64Decoder().decodeBuffer(authInfo);
			bytes = decoder.decodeBuffer(authInfo);
		} catch (IOException e) {
			logger.error("Errore durante la decodifica delle info di auth. Stringa ricevuta: '" + authenticationString + "'", e);
		}
		decodedAuth = new String(bytes);
		String[] auth = decodedAuth.split(":");
		if (auth.length == 2) {
			String username = auth[0];
			String password = auth[1];
			user = userDao.getUserByUsername(username, true, refresh);
			String hashedPassword = getHash(password);
			if (user != null && !user.getPassword().equals(hashedPassword)) {
				logger.warn("Richiesta di login tramite basic auth non valida. Stringa ricevuta: '" + authenticationString + "', username: '" + username + "', password: '" + password + "'");
				user = null;
			}
		} else {
			logger.warn("Richiesta di login tramite basic auth non conforme. Stringa ricevuta: '" + authenticationString + "'");
			user = null;
		}
		return user;
	}
	
	/**
	 * Esegue dei controlli e restituisce la commessa da utilizzare in base all'utente e la risorsa che ha richiesto.
	 * La risorsa richiesta può anche essere <code>null</code> se l'utente gestisce una sola commessa.
	 * @param user l'utente
	 * @param risorsaCommessa
	 * @return
	 */
	public CommessaUtenti getCommessaByUserAndResource(UtenteUtenti user, String risorsaCommessa) {
		Set<Integer> commesse = user.getCommesse();
		CommessaUtenti commessa;
		//Se ne ha solo una gestibile allora gli restituisco quella altrimenti vado a trovarne quella che mi ha specificato.
		if (commesse.size() == 1) {
			commessa = commesseDao.trovaDaID(commesse.iterator().next());
		} else if (risorsaCommessa != null) {
			commessa = commesseDao.trovaDaRisorsa(risorsaCommessa);
			//Se ho trovato una commessa ma non è contenuta tra quelle gestibili dall'utente la rimuovo
			if (commessa != null && !commesse.contains(commessa.getId()))
				commessa = null;
		} else {
			//Se non ha specificato la commessa da gestire quando ne ha più di una possibile segnalo il problema restituendo null.
			commessa = null;
		}
		return commessa;
	}
	
	/**
	 * Restituisce la commessa da utilizzare in base alla risorsa specificata senza eseguire controlli.
	 */
	public CommessaUtenti getCommessaByResource(String risorsaCommessa) {
		CommessaUtenti commessa = commesseDao.trovaDaRisorsa(risorsaCommessa);
		return commessa;
	}

	/**
	 * Ottiene l'hash della stringa tramite l'algoritmo SHA-256.
	 * @param s la stringa da codificare.
	 * @return l'hash della stringa.
	 */
	private String getHash(String s) {
		String hash;
		// null Check
		if (s == null)
			s = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(s.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			hash = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			hash = null;
			logger.error(e.getMessage(), e);
		}
		return hash;
	}

}
