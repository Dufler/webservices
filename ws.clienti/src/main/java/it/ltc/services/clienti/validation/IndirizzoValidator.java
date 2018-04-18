package it.ltc.services.clienti.validation;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.dao.common.NazioneDao;
import it.ltc.database.dao.common.ProvinciaDao;
import it.ltc.database.model.centrale.Nazione;
import it.ltc.database.model.centrale.Provincia;
import it.ltc.services.clienti.model.prodotto.IndirizzoJSON;

/**
 * Il validatore esegue i seguenti controlli:
 * - presenza di un valore nei campi necessari.
 * - esistenza della provincia.
 * - esistenza della nazione.
 * - lunghezza del cap in base alla nazione.
 * - correttezza dell'indirizzo email, se indicato.
 * @author Damiano
 *
 */
@Component
public class IndirizzoValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("IndirizzoValidator");
	
	public static final String REGEX_EMAIL = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
	public static final String REGEX_CAP_ITA = "^\\d{5}";
	
	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = IndirizzoJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		IndirizzoJSON indirizzo = (IndirizzoJSON) target;
		logger.info("Avvio la validazione per l'indirizzo: " + indirizzo);
		
		//Controllo dei campi necessari
		String ragioneSociale = indirizzo.getRagioneSociale();
		if (ragioneSociale == null || ragioneSociale.isEmpty())
			errors.reject("ragioneSociale.necessaria", "E' necessario specificare una ragione sociale per l'indirizzo.");
		
		String via = indirizzo.getIndirizzo();
		if (via == null || via.isEmpty())
			errors.reject("indirizzo.necessario", "E' necessario specificare una via per l'indirizzo.");
		
		String localita = indirizzo.getLocalita();
		if (localita == null || localita.isEmpty())
			errors.reject("localita.necessaria", "E' necessario specificare una localita' per l'indirizzo.");
		
		String provincia = indirizzo.getProvincia();
		if (provincia == null || provincia.isEmpty())
			errors.reject("provincia.necessaria", "E' necessario specificare una provincia per l'indirizzo.");
		else {
			Provincia p = ProvinciaDao.getInstance().findBySigla(provincia);
			if (p == null)
				errors.reject("provincia.valida", "E' necessario specificare una provincia esistente per l'indirizzo.");
		}
		
		String nazione = indirizzo.getNazione();
		Nazione n;
		if (nazione == null || nazione.isEmpty()) {
			errors.reject("nazione.necessaria", "E' necessario specificare una nazione per l'indirizzo.");
			n = null;
		} else {
			n = NazioneDao.getInstance().findByCodiceISO3(nazione);
			if (n == null)
				errors.reject("nazione.valida", "E' necessario specificare una nazione esistente per l'indirizzo.");
		}
		
		String telefono = indirizzo.getTelefono();
		if (telefono != null && telefono.length() > 20)
			errors.reject("telefono.lunghezza", "Il numero telefonico specificato e' troppo lungo (MAX 20 Caratteri).");
		
		String email = indirizzo.getEmail();
		if (email != null) {
			if (!email.matches(REGEX_EMAIL))
				errors.reject("email.valida", "L'indirizzo email indicato non e' valido.");
			if (email.length() > 100)
				errors.reject("email.lunghezza", "L'indirizzo email specificato e' troppo lungo (MAX 100 Caratteri).");
		}
		
		String cap = indirizzo.getCap();
		if (cap == null || cap.isEmpty())
			errors.reject("cap.necessario", "E' necessario specificare un cap per l'indirizzo.");
		else if (n != null) {
			//Controllo del CAP in base alla nazione.
			boolean capValido;
			switch (nazione) {
				case "ITA" : capValido = cap.matches(REGEX_CAP_ITA); break;
				default : capValido = true;
			}
			if (!capValido)
				errors.reject("cap.valido", "E' necessario specificare un cap valido per l'indirizzo.");
		}
	}

}
