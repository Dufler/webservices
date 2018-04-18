package it.ltc.services.logica.validation.crm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.AziendaContatti;

@Component
public class AssociazioneAziendaContattiValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = AziendaContatti.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		//AziendaContatti associazione = (AziendaContatti) obj;

	}

}
