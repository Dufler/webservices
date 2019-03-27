package it.ltc.services.sede.validation.prodotto;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.CassaJSON;
import it.ltc.model.shared.json.cliente.ElementoCassaJSON;

@Component
public class CassaValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = CassaJSON.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		CassaJSON cassa = (CassaJSON) arg0;
		
		String tipo = cassa.getTipo(); 
		if (tipo == null || tipo.isEmpty()) {
			errors.reject("tipocassa.necessario", "Il tipo della cassa è obbligatorio.");
		} else switch (tipo) {
			case "NO" : case "STANDARD" : case "BUNDLE" : break; //DO NOTHING!
			default : errors.reject("tipocassa.nonvalido", "Il tipo della cassa può assumere solo i valori NO, STANDARD e BUNDLE.");
		}
		
		String modello = cassa.getModello();
		if (modello != null && modello.length() > 50)
			errors.reject("modello.lunghezza", "Il valore indicato per il modello è troppo lungo. (MAX 50 caratteri)");
		
		String codice = cassa.getCodiceCassa();
		if (codice != null && codice.length() > 20)
			errors.reject("codice.lunghezza", "Il valore indicato per il codice cassa è troppo lungo. (MAX 20 caratteri)");
		
		if (cassa.getIdCassa() < 1)
			errors.reject("idcassa.necessario", "L'ID della cassa è obbligatorio.");
		
		List<ElementoCassaJSON> prodotti = cassa.getProdotti();
		if (prodotti == null || prodotti.isEmpty()) {
			errors.reject("prodotti.necessario", "E' necessario indicare i prodotti contenuti nella cassa.");
		} else for (ElementoCassaJSON prodotto : prodotti) {
			if (prodotto.getIdProdotto() < 1)
				errors.reject("idprodotto.necessario", "L'ID del prodotto è obbligatorio.");
			if (prodotto.getQuantita() < 1)
				errors.reject("quantitaprodotto.necessaria", "La quantità indicata per i prodotti contenuti nella cassa deve essere almeno 1.");
		}
		
	}

}
