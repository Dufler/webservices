package it.ltc.services.logica.data.utente;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import it.ltc.database.dao.common.UtenteDao;
import it.ltc.database.model.centrale.Utente;
import it.ltc.services.custom.exception.CustomException;
import it.ltc.utility.mail.Email;
import it.ltc.utility.mail.MailConfiguration;
import it.ltc.utility.mail.MailMan;

public class UserController extends UtenteDao {
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	private static final String USERNAME_SOLO_CIFRE = "^\\d+";
	private static final long durataRisorsa = 86400000; //24 ore
	private static final String indirizzoRisorseTemporanee = "http://web.services.ltc-logistics.it/areaclienti/reimpostaPassword/";
	private static final String indirizzoMail = "sysinfo@ltc-logistics.it";
	private static final String passwordMail = "ltc10183";
	private static final String indirizzoMailDestinario = "support@ltc-logistics.it";
	private static final String oggettoMail = "Reimposta la password LTC";
	
	public Utente inserisciNuovoUtente(Utente user) {
		String password = user.getNome().matches(USERNAME_SOLO_CIFRE) ? user.getNome() : "ltc" + user.getNome().toLowerCase();
		logger.info(password);
		password = getHash(password);
		user.setPassword(password);
		Utente nuovo = inserisci(user);
		return nuovo;
	}
	
	/**
	 * Aggiorna tutte le info sull'utente (nome, cognome, email, ...) e le info sui suoi permessi, features, commesse e sedi.
	 */
	public Utente aggiornaUtente(Utente user) {
		boolean sedi = updateUserSedi(user, user.getSedi());
		boolean commesse = updateUserCommesse(user, user.getCommesse());
		boolean permessi = updateUserPermessi(user, user.getPermessi());
		boolean features = updateUserFeature(user, user.getFeatures());
		user.setDataUltimaModifica(new Date());
		boolean update = aggiorna(user) != null;
		if (sedi && commesse && permessi && features && update) {
			user = getUserByUsername(user.getUsername(), true, true);
		} else {
			user = null;
		}
		return user;
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
	
	public Utente aggiornaPasswordUtente(String username, String nuovaPassword) {
		if (nuovaPassword == null  || nuovaPassword.isEmpty())
			throw new CustomException("La nuova password non è valida.");
		nuovaPassword = getHash(nuovaPassword);
		logger.info("Hash della nuova password: '" + nuovaPassword + "'");
		Utente esistente = getUserByUsername(username);
		esistente.setPassword(nuovaPassword);
		Utente update = aggiorna(esistente);
		return update;
	}
	
//	public Utente aggiornaPasswordUtente(Utente utente) {
//		String nuovaPassword = utente.getNuovaPassword();
//		if (nuovaPassword != null && !nuovaPassword.isEmpty())
//			nuovaPassword = getHash(nuovaPassword);
//		utente.setNuovaPassword(nuovaPassword);
//		logger.info("Hash della nuova password: '" + nuovaPassword + "'");
//		Utente update = aggiorna(utente);
//		return update;
//	}
	
	public Utente reimpostaPassword(String username) {
		//controllo se l'utente esiste: se lo trovo vado a impostare una risorsa temporanea e una scadenza.
		Utente user = getUserByUsername(username);
		if (user != null) {
			Date now = new Date();
			String path = getHash(now.toString());
			long expiration = now.getTime() + durataRisorsa;
			now.setTime(expiration);
			user.setRisorsaTemporanea(path);
			user.setScadenzaRisorsa(now);
			user = aggiorna(user);
			if (user != null) {
				inviaMail(user);
			}
		}
		return user;
	}
	
	private boolean inviaMail(Utente account) {
		String corpo = "Salve " + account.getUsername() + ",\n\r";
		corpo += "a questo indirizzo:\n\r";
		corpo += indirizzoRisorseTemporanee + account.getRisorsaTemporanea() + "\n\r";
		corpo += "puoi reimpostare la tua password.\n\r\n\r";
		corpo += "Cordiali saluti,\n\r";
		corpo += "L&TC";
		Set<String> destinatari = new HashSet<String>();
		destinatari.add(indirizzoMailDestinario);
		destinatari.add(account.getEmail());
		MailConfiguration config = MailConfiguration.getArubaPopConfiguration(indirizzoMail, passwordMail);
		MailMan postino = new MailMan(config);
		Email email = new Email(oggettoMail, corpo);		
		return postino.invia(destinatari, email);
	}
	
	public Utente reimpostaNuovaPassword(String risorsa, String nuovaPassword) {
		Utente user = getUserByResource(risorsa);
		if (user != null) {
			nuovaPassword = getHash(nuovaPassword);
			user.setPassword(nuovaPassword);
			user = aggiorna(user);
		}
		return user;
	}
	
	public Utente trovaDaRisorsa(String risorsa) {
		Utente user = getUserByResource(risorsa);
		return user;
	}

}
