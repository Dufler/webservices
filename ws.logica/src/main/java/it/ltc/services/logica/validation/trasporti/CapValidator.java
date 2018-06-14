package it.ltc.services.logica.validation.trasporti;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ltc.database.dao.common.ProvinciaDao;
import it.ltc.database.dao.common.RegioneDao;
import it.ltc.database.model.centrale.Cap;
import it.ltc.database.model.centrale.Provincia;
import it.ltc.database.model.centrale.Regione;

@Component
public class CapValidator implements Validator {
	
	public static final String REGEX_CAP = "^\\d{5}";
	
	private final ProvinciaDao daoProvince;
	private final RegioneDao daoRegioni;

	public CapValidator() {
		daoProvince = new ProvinciaDao();
		daoRegioni = new RegioneDao();
	}
	
	@Override
	public boolean supports(Class<?> arg0) {
		boolean support = Cap.class.equals(arg0);
		return support;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Cap cap = (Cap) obj;
		
		String codice = cap.getCap();
		if (codice == null || !codice.matches(REGEX_CAP))
			errors.reject("cap", "Il cap specificato non è valido. (" + codice + ")");
		
		String localita = cap.getLocalita();
		if (localita == null || localita.isEmpty())
			errors.reject("localita", "E' necessario specificare una località.");
		
		String provincia = cap.getProvincia();
		if (provincia == null || provincia.length() != 2) {
			errors.reject("provincia", "E' necessario specificare una provincia. (2 Caratteri)");
		} else {
			Provincia entity = daoProvince.trovaDaSigla(provincia);
			if (entity == null)
				errors.reject("provincia", "La sigla della provincia indicata non esiste. (" + provincia + ")");
		}
		
		String regione = cap.getRegione();
		if (regione == null || regione.length() != 3) {
			errors.reject("regione", "E' necessario specificare una regione. (3 Caratteri)");
		} else {
			Regione entity = daoRegioni.trovaDaSigla(regione);
			if (entity == null)
				errors.reject("regione", "La sigla della regione indicata non esiste. (" + regione + ")");
		}
	}

}
