package it.ltc.services.sede.validation.prodotto;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.CassaStandardJSON;
import it.ltc.model.shared.json.cliente.ElementoCassaStandardJSON;

@Component
public class CassaStandardValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = CassaStandardJSON.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		CassaStandardJSON cassa = (CassaStandardJSON) arg0;

		String codice = cassa.getCodiceCassa();
		if (codice == null || codice.isEmpty())
			errors.reject("codice.necessario", "Il codice della cassa è obbligatorio.");
		
		//Controllo tutti gli elementi della cassa standard.
		if (cassa.getElementi().isEmpty()) {
			errors.reject("elementi.necessari", "La cassa standard deve avere almeno un elemento.");
		} else for (ElementoCassaStandardJSON elemento : cassa.getElementi()) {
			String taglia = elemento.getTaglia();
			if (taglia == null || taglia.isEmpty())
				errors.reject("taglia.necessaria", "La taglia è obbligatoria per tutti gli elementi nella cassa standard.");
			
			if (elemento.getQuantita() < 1)
				errors.reject("quantita.necessaria", "La quantità è obbligatoria per tutti gli elementi nella cassa standard.");
		}
	}

}
