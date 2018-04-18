package it.ltc.services.logica.validation.trasporti;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.dao.common.CommessaDao;
import it.ltc.database.model.centrale.Commessa;
import it.ltc.services.logica.model.trasporti.CriteriFatturazione;

@Component
public class CriteriFatturazioneValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean support = CriteriFatturazione.class.equals(clazz);
		return support;
	}

	@Override
	public void validate(Object target, Errors errors) {
		CriteriFatturazione criteri = (CriteriFatturazione) target;
		
		Commessa commessa = CommessaDao.getInstance().trovaDaID(criteri.getIdCommessa());
		if (commessa == null)
			errors.reject("commessa.valida", "Bisogna selezionare una commessa valida.");
		Date now = new Date();
		Date inizio = criteri.getInizio();
		if (inizio == null)
			errors.reject("inizio.necessario", "Bisogna selezionare una data d'inizio.");
		else if (inizio.after(now))
			errors.reject("inizio.necessario", "La data d'inizio indicata non e' valida. (" + inizio.toString() + ")");
		Date fine = criteri.getFine();
		if (fine == null)
			errors.reject("fine.necessaria", "Bisogna selezionare una data di fine.");
		else if (fine.after(now))
			errors.reject("inizio.necessario", "La data di fine indicata non e' valida. (" + inizio.toString() + ")");
		if (inizio != null && fine != null && inizio.after(fine))
			errors.reject("date.valide", "La data di fine indicata e' precedente a quella di inizio.");
	}

}
