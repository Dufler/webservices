package it.ltc.services.clienti.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.DocumentoJSON;
import it.ltc.model.shared.json.cliente.OrdineJSON;
import it.ltc.model.shared.json.cliente.UscitaDettaglioJSON;
import it.ltc.model.shared.json.cliente.UscitaJSON;

@Component
public class OrdineValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("OrdineValidator");
	
	@Autowired
	private UscitaValidator validatoreUscita;
	
	@Autowired
	private UscitaDettaglioValidator validatoreProdotti;
	
	@Autowired
	private DocumentoValidator validatoreDocumento;

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = OrdineJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		OrdineJSON ordine = (OrdineJSON) target;
		logger.info("Avvio la validazione per l'ordine: " + ordine);
		
		//Testata
		UscitaJSON uscita = ordine.getOrdine();
		if (uscita == null)
			errors.reject("ordine.necessario", "E' necessario indicare le informazioni sull'ordine.");
		else
			validatoreUscita.validate(uscita, errors);
		
		//Righe
		List<UscitaDettaglioJSON> prodotti = ordine.getDettagli();
		if (prodotti == null || prodotti.isEmpty()) {
			errors.reject("prodotti.necessari", "E' necessario indicare dei prodotti da spedire.");
		} else {
			Set<Integer> numeriDiRiga = new HashSet<>();
			int totale = 0;
			for (UscitaDettaglioJSON prodotto : prodotti) {
				validatoreProdotti.validate(prodotto, errors);
				//Aumento il totale dei pezzi
				totale += prodotto.getQuantitaOrdinata();
				//Controllo che non siano state inserite righe duplicate o con valori inferiori a 1.
				Integer riga = prodotto.getRiga();
				if (riga < 1)
					errors.reject("riga.valida", "E' stato inserito un numero riga non valido. (" + riga + ")");
				else if (numeriDiRiga.contains(riga))
					errors.reject("riga.duplicata", "E' stato inserito un numero riga duplicato (" + riga + ")");
				else
					numeriDiRiga.add(riga);
			}
			if (uscita != null && uscita.getPezziOrdinati() != totale) {
				errors.reject("prodotti.totale", "Il totale indicato di prodotti da spedire non coincide con il dettaglio.");
			}
		}
		
		//Documento
		DocumentoJSON documento = ordine.getDocumento();
		if (documento == null)
			errors.reject("documento.necessario", "E' necessario indicare il documento dell'ordine.");
		else
			validatoreDocumento.validate(documento, errors);
	}

}
