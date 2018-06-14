package it.ltc.services.clienti.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.UscitaDettaglioJSON;

@Component
public class UscitaDettaglioValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = UscitaDettaglioJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		UscitaDettaglioJSON prodotto = (UscitaDettaglioJSON) target;
		String magazzino = prodotto.getMagazzino();
		if (magazzino == null || magazzino.isEmpty())
			errors.reject("prodotto.magazzino.necessario", "E' necessario indicare da quale magazzino prelevare il pezzo.");
		String sku = prodotto.getProdotto();
		if (sku == null || sku.isEmpty())
			errors.reject("prodotto.sku.necessario", "E' necessario indicare la chiave identificativa del prodotto da prelevare.");
		if (prodotto.getQuantitaOrdinata() < 1)
			errors.reject("prodotto.quantita.valida", "E' necessario indicare per il prodotto indicato. (" + sku + ": " + prodotto.getQuantitaOrdinata() + ")");
	
		//Opzionali
		String note = prodotto.getNote();
		if (note != null && note.length() > 30)
			errors.reject("prodotto.note.lunghezza", "La lunghezza delle note è eccessiva per la riga " + prodotto.getRiga());
	}

}
