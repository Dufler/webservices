package it.ltc.services.clienti.validation;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.UscitaDettaglioJSON;

@Component
public class UscitaDettaglioValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("UscitaDettaglioValidator");

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = UscitaDettaglioJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		UscitaDettaglioJSON prodotto = (UscitaDettaglioJSON) target;
		logger.info("Avvio la validazione per il dettaglio dell'ordine: " + prodotto);
		
		int riga = prodotto.getRiga();
		if (riga < 1)
			errors.reject("prodotto.riga.valida", "E' necessario indicare un numero di riga valido. (" + riga + ")");
		
		String magazzino = prodotto.getMagazzino();
		if (magazzino == null || magazzino.isEmpty())
			errors.reject("prodotto.magazzino.necessario", "E' necessario indicare da quale magazzino prelevare il pezzo.");
		
		String sku = prodotto.getProdotto();
		if (sku == null || sku.isEmpty())
			errors.reject("prodotto.sku.necessario", "E' necessario indicare la chiave identificativa del prodotto da prelevare.");
		
		if (prodotto.getQuantitaOrdinata() < 1)
			errors.reject("prodotto.quantita.valida", "E' necessario indicare per il prodotto indicato. (" + sku + ": " + prodotto.getQuantitaOrdinata() + ")");
	
		//Opzionali, il tipo è facoltativo e se presente viene validato dentro l'oggetto che si occupa di salvare l'ordine in quanto la tipizzazione risiede nel database.
		String note = prodotto.getNote();
		if (note != null && note.length() > 30)
			errors.reject("prodotto.note.lunghezza", "La lunghezza delle note è eccessiva per la riga " + prodotto.getRiga());
	}

}
