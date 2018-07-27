package it.ltc.services.sede.validation.carico;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.sede.model.carico.ColloCaricoJSON;

@Component
public class ColloCaricoValidator implements Validator {

	public enum StatoCollo { APERTO, CHIUSO, UBICAT } //Non è un refuso, sul vecchio abbiamo 6 caratteri.
	
	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = ColloCaricoJSON.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		ColloCaricoJSON collo = (ColloCaricoJSON) obj;
		
		int carico = collo.getCarico();
		if (carico < 1)
			errors.reject("carico", "Va indicato un carico valido.");
		
		String magazzino = collo.getMagazzino();
		if (magazzino == null || magazzino.isEmpty())
			errors.reject("magazzino", "Va indicato un magazzino valido.");
		
		String stato = collo.getStato();
		try { StatoCollo.valueOf(stato); } catch (Exception e) { errors.reject("stato", "Va indicato uno stato valido."); }

	}

}
