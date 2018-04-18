package it.ltc.services.logica.validation.crm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.model.centrale.Gadget;

@Component
public class GadgetValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = Gadget.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Gadget gadget = (Gadget) obj;
		
		String nome = gadget.getNome();
		if (nome == null || nome.isEmpty())
			errors.reject("nome", "Il nome è necessario.");
		else if (nome.length() > 250)
			errors.reject("nome", "Il nome è troppo lungo. (MAX 250 Carateri)");
		
		String descrizione = gadget.getDescrizione();
		if (descrizione != null && descrizione.length() > 250)
			errors.reject("descrizione", "La descrizione è troppo lunga. (MAX 250 Carateri)");
	}

}
