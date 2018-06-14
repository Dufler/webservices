package it.ltc.services.sede.validation.carico;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.services.sede.model.carico.ProdottoCaricoJSON;

@Component
public class ProdottoCaricoValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = ProdottoCaricoJSON.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		ProdottoCaricoJSON prodotto = (ProdottoCaricoJSON) obj;
		
		int carico = prodotto.getCarico();
		if (carico < 1)
			errors.reject("carico", "Va indicato un carico valido.");
		
		int collo = prodotto.getCollo();
		if (collo < 1)
			errors.reject("collo", "Va indicato un collo valido.");
		
		int articolo = prodotto.getProdotto();
		if (articolo < 1)
			errors.reject("articolo", "Va indicato un articolo valido.");
		
		int quantita = prodotto.getQuantita();
		if (quantita < 1)
			errors.reject("quantita", "La quantita deve essere almeno 1.");
		
		String seriale = prodotto.getSeriale();
		if (seriale != null && quantita != 1)
			errors.reject("seriale", "La quantita deve essere esattamente 1 quando si indica un seriale.");

	}

}
