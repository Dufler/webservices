package it.ltc.services.clienti.validation;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.dao.costanti.NazioneDao;
import it.ltc.database.dao.costanti.ProvinciaDao;
import it.ltc.database.model.costanti.Nazione;
import it.ltc.database.model.costanti.Provincia;
import it.ltc.model.shared.json.cliente.IndirizzoJSON;

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
	
	private final NazioneDao daoNazioni;
	private final ProvinciaDao daoProvince;
	
	public IndirizzoValidator() {
		daoNazioni = new NazioneDao();
		daoProvince = new ProvinciaDao();
	}
	
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
		else if (ragioneSociale.length() > 50)
			errors.reject("ragioneSociale.lunghezza", "La ragione sociale indicata è troppo lunga. (MAX 50 caratteri)");
		
		String via = indirizzo.getIndirizzo();
		if (via == null || via.isEmpty())
			errors.reject("indirizzo.necessario", "E' necessario specificare una via per l'indirizzo.");
		else if (via.length() > 250)
			errors.reject("indirizzo.lunghezza", "L'indirizzo indicato è troppo lungo. (MAX 250 caratteri)");
		
		String localita = indirizzo.getLocalita();
		if (localita == null || localita.isEmpty())
			errors.reject("localita.necessaria", "E' necessario specificare una localita' per l'indirizzo.");
		else if (localita.length() > 50)
			errors.reject("localita.lunghezza", "La località indicata è troppo lunga. (MAX 50 caratteri)");
		
		String provincia = indirizzo.getProvincia();
		if (provincia == null || provincia.isEmpty())
			errors.reject("provincia.necessaria", "E' necessario specificare una provincia per l'indirizzo.");
		else {
			Provincia p = daoProvince.trovaDaSigla(provincia);
			if (p == null)
				errors.reject("provincia.valida", "E' necessario specificare una provincia esistente per l'indirizzo. (" + provincia + ")");
		}
		
		String nazione = indirizzo.getNazione();
		Nazione n;
		if (nazione == null || nazione.isEmpty()) {
			errors.reject("nazione.necessaria", "E' necessario specificare una nazione per l'indirizzo.");
			n = null;
		} else {
			n = daoNazioni.trovaDaCodiceISO3(nazione);
			if (n == null)
				errors.reject("nazione.valida", "E' necessario specificare una nazione esistente per l'indirizzo. (" + nazione + ")");
		}
		
		String cap = indirizzo.getCap();
		if (cap == null || cap.isEmpty())
			errors.reject("cap.necessario", "E' necessario specificare un cap per l'indirizzo.");
		else if (n != null) {
			//Controllo del CAP in base alla nazione.
			boolean capValido;
			switch (nazione) {
				case "ITA" : capValido = cap.matches(REGEX_CAP_ITA); break;
				default : capValido = cap.length() < 11; //10 o meno caratteri.
			}
			if (!capValido)
				errors.reject("cap.valido", "E' necessario specificare un cap valido per l'indirizzo.");
		}
		
		//Campi facoltativi
		String telefono = indirizzo.getTelefono();
		if (telefono != null && telefono.length() > 20)
			errors.reject("telefono.lunghezza", "Il numero telefonico specificato e' troppo lungo (MAX 20 Caratteri).");
		
		String email = indirizzo.getEmail();
		if (email != null) {
			if (!email.matches(REGEX_EMAIL))
				errors.reject("email.valida", "L'indirizzo email indicato non e' valido. (" + email + ")");
			if (email.length() > 100)
				errors.reject("email.lunghezza", "L'indirizzo email specificato e' troppo lungo (MAX 100 Caratteri).");
		}
	
	}

}
