package it.ltc.services.clienti.validation;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.dao.common.NazioneDao;
import it.ltc.database.model.centrale.Nazione;
import it.ltc.model.shared.json.cliente.IngressoDettaglioJSON;

@Component
public class IngressoDettaglioValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("IngressoDettaglioValidator");
	
	private final NazioneDao daoNazioni;
	
	public IngressoDettaglioValidator() {
		daoNazioni = new NazioneDao();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = IngressoDettaglioJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		IngressoDettaglioJSON dettaglio = (IngressoDettaglioJSON) target;
		logger.info("Avvio la validazione per il dettaglio del carico: " + dettaglio);
		
		//Campi obbligatori
		String magazzino = dettaglio.getMagazzino();
		if (magazzino == null || magazzino.isEmpty())
			errors.reject("magazzino.necessario", "E' necessario indicare un magazzino");
		
		String sku = dettaglio.getProdotto();
		if (sku == null || sku.isEmpty())
			errors.reject("prodotto.necessario", "E' necessario indicare un prodotto");
		
		if (dettaglio.getQuantitaPrevista() < 1)
			errors.reject("prodotto.quantita", "La quantita' di prodotto indicata non e' valida (" + sku + ": " + dettaglio.getQuantitaPrevista() + ")");
		
		if (dettaglio.getRiga() < 1)
			errors.reject("riga.valida", "Il numero di riga indicato non e' valido (" + dettaglio.getRiga() + ")");
		
		//Campi facoltativi
		String note = dettaglio.getNote();
		if (note != null && note.length() > 20)
			errors.reject("note.lunghezza", "Le note inserite sono troppo lunghe. (MAX 20 caratteri)");
		
		String madeIn = dettaglio.getMadeIn();
		if (madeIn != null && !madeIn.isEmpty()) {
			Nazione n = daoNazioni.trovaDaCodiceISO3(madeIn);
			if (n == null)
				errors.reject("nazione.valida", "La nazione specificata per il madeIn non è valida. (" + madeIn + ")");
		}
	}

}
