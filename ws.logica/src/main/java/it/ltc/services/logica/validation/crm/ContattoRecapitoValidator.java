package it.ltc.services.logica.validation.crm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.CrmContattoRecapiti;
import it.ltc.database.model.centrale.CrmContattoRecapiti.Tipo;

@Component
public class ContattoRecapitoValidator implements Validator {

	public static final String REGEX_TELEFONO = "^\\d+((\\.|-|\\s+)\\d+)*";
	public static final String REGEX_EMAIL = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

	
	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = CrmContattoRecapiti.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		CrmContattoRecapiti recapito = (CrmContattoRecapiti) obj;
		
		int contatto = recapito.getContatto();
		if (contatto < 1)
			errors.reject("contatto", "Bisogna indicare un contatto valido per il recapito.");
		
		String valore = recapito.getRecapito();
		if (valore == null || valore.isEmpty())
			errors.reject("recapito", "Bisogna indicare un recapito valido.");
		
		Tipo tipo = recapito.getTipo();
		switch (tipo) {
			case EMAIL : validaEmail(valore, errors); break;
			case TELEFONO : validaTelefono(valore, errors); break;
			default : break;
		}

	}
	
	private void validaEmail(String email, Errors errors) {
		if (email.length() > 250)
			errors.reject("email", "L'email è troppo lunga. (MAX 250 caratteri)");
		if (!email.matches(REGEX_EMAIL))
			errors.reject("email", "L'email non è corretta.");
	}
	
	private void validaTelefono(String telefono, Errors errors) {
		if (telefono.length() > 100)
			errors.reject("telefono", "Il numero di telefono è troppo lungo. (MAX 100 caratteri)");
		if (!telefono.matches(REGEX_TELEFONO))
			errors.reject("telefono", "Il numero di telefono non è corretto.");
	}

}
