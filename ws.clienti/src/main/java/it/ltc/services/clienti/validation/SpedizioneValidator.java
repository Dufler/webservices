package it.ltc.services.clienti.validation;

import java.util.Date;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.cliente.ContrassegnoJSON;
import it.ltc.model.shared.json.cliente.DocumentoJSON;
import it.ltc.model.shared.json.cliente.SpedizioneJSON;

@Component
public class SpedizioneValidator implements Validator {
	
	private static final Logger logger = Logger.getLogger("SpedizioneValidator");
	
	public enum CodiceCorriere { BRT, TNT, GLS, DHL, UPS, LTC, FED, ALTRO }
	
	public enum ServizioCorriere { DEF, AER, O10, O12, PRI }

	@Autowired
	private ContrassegnoValidator validatoreContrassegno;

	@Autowired
	private DocumentoValidator validatoreDocumento;

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = SpedizioneJSON.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		SpedizioneJSON ordine = (SpedizioneJSON) target;
		logger.info("Avvio la validazione per i dati della spedizione: " + ordine);
		
		//Controllo che ci sia almeno un riferimento
		if (ordine.getRiferimenti() == null || ordine.getRiferimenti().isEmpty())
			errors.reject("riferimenti.necessari", "E' necessario indicare almeno un ordine da spedire.");

		String corriere = ordine.getCorriere();
		if (corriere == null || corriere.isEmpty())
			errors.reject("corriere.necessario", "E' necessario indicare il corriere con cui partira' la merce.");
		else try {
			CodiceCorriere codice = CodiceCorriere.valueOf(corriere);
			String codiceSpecifico = ordine.getCodiceCorriere();
			if (codice == CodiceCorriere.ALTRO && (codiceSpecifico == null || codiceSpecifico.isEmpty())) 
					errors.reject("corriere.necessario", "Indicando ALTRO come corriere E' necessario indicare il codice specifico.");
			if (codiceSpecifico != null && codiceSpecifico.length() > 30)
				errors.reject("corriere.lunghezza", "Il codice specificato per il corriere è troppo lungo. (MAX 30 caratteri)");
		} catch (Exception e) {
			String errorMessage = "E' necessario indicare un corriere valido. I possibili valori sono: ";
			for (CodiceCorriere codice : CodiceCorriere.values())
				errorMessage += codice.name() + " ";
			errors.reject("corriere.valido", errorMessage);
		}

		String servizioCorriere = ordine.getServizioCorriere();
		if (servizioCorriere == null || servizioCorriere.isEmpty())
			errors.reject("servizio.necessario",
					"E' necessario indicare il livello di servizio del corriere con cui partira' la merce.");
		else try {
			ServizioCorriere.valueOf(servizioCorriere);
		} catch (Exception e) {
			errors.reject("servizio.valido", "E' necessario indicare un livello di servizio valido. I possibili valori sono: " + ServizioCorriere.values());
		}

		// Opzionali
		String note = ordine.getNote();
		if (note != null && note.length() > 70)
			errors.reject("note.lunghezza", "La lunghezza delle note è eccessiva. (MAX 70 caratteri)");
		
		Date dataConsegna = ordine.getDataConsegna();
		if (dataConsegna != null && dataConsegna.before(new Date()))
			errors.reject("dataconsegna.valida", "La data di consegna richiesta è precedente a questo momento.");
		
		ContrassegnoJSON contrassegno = ordine.getContrassegno();
		if (contrassegno != null)
			validatoreContrassegno.validate(contrassegno, errors);

		Double valoreDoganale = ordine.getValoreDoganale();
		if (valoreDoganale != null && valoreDoganale <= 0)
			errors.reject("valoredoganale.valido", "Il valore doganale indicato non e' valido. (" + valoreDoganale + ")");

		DocumentoJSON documentoFiscale = ordine.getDocumentoFiscale();
		if (documentoFiscale != null)
			validatoreDocumento.validate(documentoFiscale, errors);

	}

}
