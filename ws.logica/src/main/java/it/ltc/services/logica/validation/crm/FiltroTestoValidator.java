package it.ltc.services.logica.validation.crm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.logica.model.crm.FiltroTesto;

@Component
public class FiltroTestoValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = FiltroTesto.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
//		FiltroTesto filtro = (FiltroTesto) obj;
		//Momentaneamente disabilitato perchè troppo restrittivo.
//		String testo = filtro.getTesto();
//		if (testo == null || testo.isEmpty())
//			errors.reject("testo", "Il testo di ricerca non può essere lasciato vuoto.");

	}

}
