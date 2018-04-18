package it.ltc.services.logica.validation.trasporti;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.logica.model.trasporti.CriteriUltimaModifica;

@Component
public class CriteriUltimaModificaValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = CriteriUltimaModifica.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		CriteriUltimaModifica criteri = (CriteriUltimaModifica) target;
		
		//Data - Assolutamente necessaria
		Date data = criteri.getDataUltimaModifica();
		if (data == null)
			errors.reject("data.valida", "Bisogna inserire una data valida per l'ultima modifica.");
		
	}

}
