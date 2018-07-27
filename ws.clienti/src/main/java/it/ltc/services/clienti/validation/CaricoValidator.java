package it.ltc.services.clienti.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.CaricoJSON;
import it.ltc.model.shared.json.cliente.DocumentoJSON;
import it.ltc.model.shared.json.cliente.IngressoDettaglioJSON;
import it.ltc.model.shared.json.cliente.IngressoJSON;

/**
 * Il validatore esegue i seguenti controlli:
 * <br>
 * <ul>
 * <li>- presenza di un valore nei campi necessari</li>
 * <li>- concordanza sui totali di prodotto dichiarati</li>
 * <li>- univocita' dei numeri di riga</li>
 * </ul>
 * <br>
 * Rimanda poi la validazione agli elementi interni tramite altri validatori.
 * @author Damiano
 *
 */
@Component
public class CaricoValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("CaricoValidator");
	
	@Autowired
	private IngressoValidator validatoreIngresso;
	
	@Autowired
	private IngressoDettaglioValidator validatoreDettagli;
	
	@Autowired
	private DocumentoValidator validatoreDocumento;

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = CaricoJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		CaricoJSON carico = (CaricoJSON) target;
		logger.info("Avvio la validazione per il carico: " + carico);
		
		//Testata
		IngressoJSON ingresso = carico.getIngresso();
		if (ingresso != null) {
			validatoreIngresso.validate(ingresso, errors);
		} else {
			errors.reject("ingresso.necessario", "E' necessario inserire le informazioni sul carico.");
		}
		
		//Dettagli
		List<IngressoDettaglioJSON> dettagli = carico.getDettagli();
		if (dettagli == null || dettagli.isEmpty()) {
			errors.reject("dettagliingresso.necessari", "E' necessario inserire le informazioni di dettaglio sul carico.");
		} else {
			Set<Integer> numeriDiRiga = new HashSet<>();
			int totale = 0;
			for (IngressoDettaglioJSON dettaglio : dettagli) {
				//Aumento il totale dei pezzi
				totale += dettaglio.getQuantitaPrevista();
				//Controllo che non siano stati inseriti numeri di riga duplicati
				Integer riga = dettaglio.getRiga();
				if (numeriDiRiga.contains(riga))
					errors.reject("riga.duplicata", "E' stato inserito un numero riga duplicato (" + riga + ")");
				else
					numeriDiRiga.add(riga);
				//Valido il resto nello specifico
				validatoreDettagli.validate(dettaglio, errors);
			}
			if (ingresso != null && totale != ingresso.getPezziStimati())
				errors.reject("totale.discordante", "Il totale dei prodotti dichiarati è diverso dal totale della lista (" + totale + " / " + ingresso.getPezziStimati() + ")");
		}
		
		//Documento
		DocumentoJSON documento = carico.getDocumento();
		if (documento == null)
			errors.reject("documento.necessario", "E' necessario inserire le informazioni sul documento dell'ingresso.");
		else
			validatoreDocumento.validate(documento, errors);
	}

}
