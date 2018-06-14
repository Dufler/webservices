package it.ltc.services.sede.validation.carico;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.model.shared.json.interno.CaricoTestata;

@Component
public class CaricoTestataValidator implements Validator {
	
	public static final String regexStagione = "^(AU|IN|PR|ES|AI|PE|CO)\\d{2}"; //Qua volendo possiamo andare ad aggiungere una codifica per mese nel caso in cui servisse. (es. Gennaio -> GE etc.)


	@Override
	public boolean supports(Class<?> c) {
		boolean support = CaricoTestata.class.equals(c);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		CaricoTestata testata = (CaricoTestata) obj;
		
		//Documento
		String riferimentoDocumento = testata.getDocumentoRiferimento();
		if (riferimentoDocumento == null || riferimentoDocumento.isEmpty())
			errors.reject("riferimentoDocumento", "E' necessario un riferimento per il documento che accompagna la merce.");
		else if (riferimentoDocumento.length() > 30)
			errors.reject("riferimentoDocumento", "Il riferimento per il documento è troppo lungo. (MAX 30 Caratteri)");
		
		Date dataDocumento = testata.getDocumentoData();
		if (dataDocumento == null)
			errors.reject("dataDocumento", "E' necessario una data per il documento che accompagna la merce.");
		
		String tipoDocumento = testata.getDocumentoTipo();
		if (tipoDocumento == null || tipoDocumento.isEmpty())
			errors.reject("tipoDocumento", "E' necessario indicare un tipo di documento.");
		
		//Richiesti
		int fornitore = testata.getFornitore();
		if (fornitore < 1)
			errors.reject("fornitore", "E' necessario indicare un fornitore.");
		
		String tipo = testata.getTipo();
		if (tipo == null || tipo.isEmpty())
			errors.reject("tipo", "E' necessario un tipo per il carico.");
		
		String riferimento = testata.getRiferimento();
		if (riferimento == null || riferimento.isEmpty())
			errors.reject("riferimento", "E' necessario un riferimento per il carico.");
		
		//Opzionali
		String note = testata.getNote();
		if (note != null && note.length() > 250)
			errors.reject("note", "Le note per il carico sono troppo lunghe. (MAX 250 Caratteri)");
		
		String stagione = testata.getStagione();
		if (stagione != null && !stagione.matches(regexStagione))
			errors.reject("stagione.formato", "Il valore indicato per la stagione è errato. I possibili valori sono: AU (Autunno), IN (Inverno), PR (Primavera), ES (Estate), AI (Autunno-Inverno), PE (Primavera-Estate), CO (Continuativo) seguiti da 2 cifre indicanti l'anno. Es: AI17.");
		
	}

}
