package it.ltc.services.logica.validation.crm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.Azienda;

@Component
public class AziendaValidator implements Validator {
	
	public static final String REGEX_PIVA_CF = "^(\\d{11}|[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z])";
	public static final String REGEX_TELEFONO = "^\\d+((\\.|-|\\s+)\\d+)*";
	public static final String REGEX_EMAIL = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = Azienda.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Azienda azienda = (Azienda) obj;
		
		String email = azienda.getEmail();
		if (email != null && !email.isEmpty()) {
			if (!email.matches(REGEX_EMAIL))
				errors.reject("email", "L'email inserita non è valida.");
			if (email.length() > 250)
				errors.reject("email", "L'email inserita è troppo lunga. (MAX 250 caratteri)");
		}  
				
//		String piva = azienda.getPartitaIva();
//		if (piva == null || piva.isEmpty())
//			errors.reject("partitaIva", "La P.IVA/C.F. è obbligatorio.");
//		else if (!piva.matches(REGEX_PIVA_CF))
//			errors.reject("partitaIva", "La P.IVA/C.F. inserito non è valido.");
		
		String piva = azienda.getPartitaIva();
		if (piva != null && !piva.matches(REGEX_PIVA_CF))
			errors.reject("partitaIva", "La P.IVA/C.F. inserito non è valido.");
		
		String ragioneSociale = azienda.getRagioneSociale();
		if (ragioneSociale == null || ragioneSociale.isEmpty())
			errors.reject("ragioneSociale", "La ragione sociale è obbligatoria.");
		else if (ragioneSociale.length() > 250)
			errors.reject("ragioneSociale", "La ragione sociale inserita è troppo lunga. (MAX 250 caratteri)");
		
		String sitoWeb = azienda.getSitoWeb();
		if (sitoWeb != null && sitoWeb.length() > 250)
			errors.reject("sitoWeb", "L'indirizzo del sito web inserito è troppo lungo. (MAX 250 caratteri)");
		
		String telefono = azienda.getTelefono();
		if (telefono != null && !telefono.isEmpty()) {
			if (!telefono.matches(REGEX_TELEFONO))
				errors.reject("telefono", "Il numero di telefono non è valido.");
			if (telefono.length() > 100)
				errors.reject("telefono", "Il numero di telefono inserito è troppo lungo. (MAX 100 caratteri)");
		}
		
		int appetibile = azienda.getAppetibile();
		if (appetibile < 0 || appetibile > 10)
			errors.reject("appetibile", "L'indice di appetibilita' deve essere compreso fra 0 e 10. (inserito: " + appetibile + ")");
	}

}
