package it.ltc.services.sede.validation.stampa;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.sede.model.stampa.RichiestaDiStampa;

@Component
public class RichiestaDiStampaValidator implements Validator {

	private static final String IP_REGEX = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
	
	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = RichiestaDiStampa.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		RichiestaDiStampa richiesta = (RichiestaDiStampa) target;
		
		//Controllo che ci sia l'IP e sia corretto.
		String ip = richiesta.getIp();
		if (ip == null || !ip.matches(IP_REGEX))
			errors.reject("richiesta.ip", "E' necessario passare un IP valido.");
		
		//Controllo che mi abbia passato qualcosa da stampare.
		String content = richiesta.getContent();
		if (content == null || content.isEmpty())
			errors.reject("richiesta.content", "E' necessario passare del contenuto da stampare.");
	}

}
