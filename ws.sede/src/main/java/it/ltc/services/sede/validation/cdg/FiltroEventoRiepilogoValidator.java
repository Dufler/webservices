package it.ltc.services.sede.validation.cdg;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.sede.model.cdg.FiltroEventoRiepilogo;

@Component
public class FiltroEventoRiepilogoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = FiltroEventoRiepilogo.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		FiltroEventoRiepilogo filtro = (FiltroEventoRiepilogo) target;
		
		int commessa = filtro.getCommessa();
		if (commessa < 1)
			errors.reject("eventoriepilogo.commessa", "Bisogna specificare una commessa");
		
		Date inizio = filtro.getInizio();
		if (inizio == null)
			errors.reject("eventoriepilogo.inizio", "Bisogna specificare una data di inizio.");
		
		Date fine = filtro.getFine();
		if (fine == null)
			errors.reject("eventoriepilogo.fine", "Bisogna specificare una data di fine.");
		
		if (inizio != null && fine != null && inizio.after(fine))
			errors.reject("eventoriepilogo.date", "La data di fine precede quella di inizio.");
	}

}
