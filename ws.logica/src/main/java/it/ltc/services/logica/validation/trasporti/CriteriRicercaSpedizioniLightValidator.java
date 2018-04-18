package it.ltc.services.logica.validation.trasporti;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.logica.model.trasporti.CriteriRicercaSpedizioniLight;

@Component
public class CriteriRicercaSpedizioniLightValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = CriteriRicercaSpedizioniLight.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		CriteriRicercaSpedizioniLight criteri = (CriteriRicercaSpedizioniLight) target;
		
		Date inizio = criteri.getInizio();
		Date fine = criteri.getFine();
		if (inizio != null && fine != null && inizio.after(fine))
			errors.reject("date.intersecanti", "La data di inizio non puo' essere successiva alla data di fine.");
		
		//TODO - Inserire il resto dei controlli.

	}

}
