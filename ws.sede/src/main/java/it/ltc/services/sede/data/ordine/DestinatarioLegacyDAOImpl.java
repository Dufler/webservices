package it.ltc.services.sede.data.ordine;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.CondizioneWhere;
import it.ltc.database.dao.CondizioneWhere.Condizione;
import it.ltc.database.dao.CondizioneWhere.Operatore;
import it.ltc.database.dao.legacy.DestinatariDao;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.model.shared.dao.IDestinatarioDao;
import it.ltc.model.shared.json.cliente.IndirizzoJSON;
import it.ltc.services.custom.exception.CustomException;

public class DestinatarioLegacyDAOImpl extends DestinatariDao implements IDestinatarioDao {

	public DestinatarioLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}

	@Override
	public IndirizzoJSON trovaPerID(int id) {
		Destinatari entity = findByID(id);
		return deserializza(entity);
	}

	@Override
	public List<IndirizzoJSON> cerca(IndirizzoJSON filtro) {
		List<IndirizzoJSON> indirizzi = new LinkedList<>();
		List<CondizioneWhere> conditions = new LinkedList<>();
		conditions.add(new CondizioneWhere("ragSoc1", filtro.getRagioneSociale(), Operatore.LIKE, Condizione.AND));
		List<Destinatari> entities = findAll(conditions, 100);
		for (Destinatari entity : entities) {
			IndirizzoJSON indirizzo = deserializza(entity);
			indirizzi.add(indirizzo);
		}
		return indirizzi;
	}

	@Override
	public IndirizzoJSON inserisci(IndirizzoJSON destinatario) {
		// Eseguo un controllo sulla ragione sociale
		String ragioneSociale1 = destinatario.getRagioneSociale();
		String ragioneSociale2 = "";
		// Se è più lunga di 70 vado a tagliare la parte eccedente.
		if (ragioneSociale1.length() > 70) {
			ragioneSociale1 = ragioneSociale1.substring(0, 70);
		}
		// Se è più lunga di 35 la divido in 2
		if (ragioneSociale1.length() > 35) {
			ragioneSociale2 = ragioneSociale1.substring(35);
			ragioneSociale1 = ragioneSociale1.substring(0, 35);
		}
		// Eseguo una ricerca per vedere se è già presente.
		// Se ho trovato corrispondenza lo restituisco, altrimenti lo inserisco
		Destinatari entity = trovaIndirizzo(ragioneSociale1, destinatario.getCap(), destinatario.getIndirizzo(), destinatario.getLocalita(), destinatario.getNazione());
		if (entity == null) {
			entity = new Destinatari();
			entity.setCap(destinatario.getCap());
			entity.setCodIso(destinatario.getNazione());
			entity.setCodNaz(destinatario.getNazione());
			entity.setEmail(destinatario.getEmail());
			entity.setIndirizzo(destinatario.getIndirizzo());
			entity.setLocalita(destinatario.getLocalita());
			entity.setNazione(destinatario.getNazione());
			entity.setProvincia(destinatario.getProvincia());
			entity.setRagSoc1(ragioneSociale1);
			entity.setRagSoc2(ragioneSociale2);
			entity.setTel(destinatario.getTelefono());
			entity = inserisci(entity);
			if (entity == null)
				throw new CustomException("Impossibile inserire l'indirizzo indicato per il destinatario. Contattare il reparto IT.");
		}
		return deserializza(entity);
	}

	@Override
	public IndirizzoJSON aggiorna(IndirizzoJSON destinatario) {
		Destinatari esistente = trovaDaID(destinatario.getId());
		if (esistente == null)  throw new CustomException("L'ID indicato per il destinatario non esiste. (" + destinatario.getId() + ")");
		Destinatari entity = serializza(destinatario, esistente);
		entity = aggiorna(entity);
		return deserializza(entity);
	}

	@Override
	public IndirizzoJSON elimina(IndirizzoJSON destinatario) {
		Destinatari esistente = trovaDaID(destinatario.getId());
		if (esistente == null)  throw new CustomException("L'ID indicato per il destinatario non esiste. (" + destinatario.getId() + ")");
		Destinatari entity = elimina(esistente);
		return deserializza(entity);
	}
	
	protected Destinatari serializza(IndirizzoJSON json, Destinatari destinatario) {
		if (json != null) {
			destinatario.setCap(json.getCap());
			destinatario.setCodDestina(json.getCodice());
			destinatario.setCodIso(json.getNazione());
			destinatario.setCodNaz(json.getNazione());
			destinatario.setEmail(json.getEmail());
			destinatario.setIdDestina(json.getId());
			destinatario.setIndirizzo(json.getIndirizzo());
			destinatario.setLocalita(json.getLocalita());
			destinatario.setNazione(json.getNazione());
			destinatario.setProvincia(json.getProvincia());
			destinatario.setRagSoc1(json.getRagioneSociale());
			destinatario.setTel(json.getTelefono());
		} else {
			destinatario = null;
		}
		return destinatario;
	}
	
	protected IndirizzoJSON deserializza(Destinatari destinatario) {
		IndirizzoJSON json;
		if (destinatario != null) {
			json = new IndirizzoJSON();
			json.setCap(destinatario.getCap());
			json.setCodice(destinatario.getCodDestina());
			json.setEmail(destinatario.getEmail());
			json.setId(destinatario.getIdDestina());
			json.setIndirizzo(destinatario.getIndirizzo());
			json.setLocalita(destinatario.getLocalita());
			json.setNazione(destinatario.getCodIso());
			json.setProvincia(destinatario.getProvincia());
			json.setRagioneSociale(destinatario.getRagSoc1());
			json.setTelefono(destinatario.getTel());
		} else {
			json = null;
		}
		return json;
	}

}
