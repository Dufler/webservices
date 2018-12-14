package it.ltc.services.sede.data.ordine;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.CondizioneWhere;
import it.ltc.database.dao.CondizioneWhere.Condizione;
import it.ltc.database.dao.CondizioneWhere.Operatore;
import it.ltc.database.dao.legacy.MittentiOrdineDao;
import it.ltc.database.model.legacy.MittentiOrdine;
import it.ltc.model.shared.dao.IMittenteDao;
import it.ltc.model.shared.json.cliente.IndirizzoJSON;
import it.ltc.services.custom.exception.CustomException;

public class MittenteLegacyDAOImpl extends MittentiOrdineDao implements IMittenteDao {

	public MittenteLegacyDAOImpl(String persistenceUnit, String utente) {
		super(persistenceUnit);
		setUtente(utente);
	}

	@Override
	public IndirizzoJSON trovaPerID(int id) {
		MittentiOrdine entity = findByID(id);
		return deserializza(entity);
	}

	@Override
	public List<IndirizzoJSON> cerca(IndirizzoJSON filtro) {
		List<IndirizzoJSON> indirizzi = new LinkedList<>();
		List<CondizioneWhere> conditions = new LinkedList<>();
		conditions.add(new CondizioneWhere("ragioneSociale", filtro.getRagioneSociale(), Operatore.LIKE, Condizione.AND));
		List<MittentiOrdine> entities = findAll(conditions, 100);
		for (MittentiOrdine entity : entities) {
			IndirizzoJSON indirizzo = deserializza(entity);
			indirizzi.add(indirizzo);
		}
		return indirizzi;
	}

	@Override
	public IndirizzoJSON inserisci(IndirizzoJSON destinatario) {
		// Eseguo un controllo sulla ragione sociale
		String ragioneSociale = destinatario.getRagioneSociale();
		// Eseguo una ricerca per vedere se è già presente.
		// Se ho trovato corrispondenza lo restituisco, altrimenti lo inserisco
		MittentiOrdine entity = trovaIndirizzo(ragioneSociale, destinatario.getCap(), destinatario.getIndirizzo(), destinatario.getLocalita(), destinatario.getNazione());
		if (entity == null) {
			entity = new MittentiOrdine();
			entity.setCap(destinatario.getCap());
			entity.setEmail(destinatario.getEmail());
			entity.setIndirizzo(destinatario.getIndirizzo());
			entity.setLocalita(destinatario.getLocalita());
			entity.setNazione(destinatario.getNazione());
			entity.setProvincia(destinatario.getProvincia());
			entity.setRagioneSociale(ragioneSociale);
			entity.setTelefono(destinatario.getTelefono());
			entity = inserisci(entity);
			if (entity == null)
				throw new CustomException("Impossibile inserire l'indirizzo indicato per il mittente. Contattare il reparto IT.");
		}
		return deserializza(entity);
	}

	@Override
	public IndirizzoJSON aggiorna(IndirizzoJSON destinatario) {
		MittentiOrdine esistente = trovaDaID(destinatario.getId());
		if (esistente == null)  throw new CustomException("L'ID indicato per il mittente non esiste. (" + destinatario.getId() + ")");
		MittentiOrdine entity = serializza(destinatario, esistente);
		entity = aggiorna(entity);
		return deserializza(entity);
	}

	@Override
	public IndirizzoJSON elimina(IndirizzoJSON destinatario) {
		MittentiOrdine esistente = trovaDaID(destinatario.getId());
		if (esistente == null)  throw new CustomException("L'ID indicato per il mittente non esiste. (" + destinatario.getId() + ")");
		MittentiOrdine entity = elimina(esistente);
		return deserializza(entity);
	}
	
	protected MittentiOrdine serializza(IndirizzoJSON json, MittentiOrdine destinatario) {
		if (json != null) {
			destinatario.setCap(json.getCap());
			destinatario.setCodice(json.getCodice());
			destinatario.setEmail(json.getEmail());
			destinatario.setIdMittente(json.getId());
			destinatario.setIndirizzo(json.getIndirizzo());
			destinatario.setLocalita(json.getLocalita());
			destinatario.setNazione(json.getNazione());
			destinatario.setProvincia(json.getProvincia());
			destinatario.setRagioneSociale(json.getRagioneSociale());
			destinatario.setTelefono(json.getTelefono());
		} else {
			destinatario = null;
		}
		return destinatario;
	}
	
	protected IndirizzoJSON deserializza(MittentiOrdine destinatario) {
		IndirizzoJSON json;
		if (destinatario != null) {
			json = new IndirizzoJSON();
			json.setCap(destinatario.getCap());
			json.setCodice(destinatario.getCodice());
			json.setEmail(destinatario.getEmail());
			json.setId(destinatario.getIdMittente());
			json.setIndirizzo(destinatario.getIndirizzo());
			json.setLocalita(destinatario.getLocalita());
			json.setNazione(destinatario.getNazione());
			json.setProvincia(destinatario.getProvincia());
			json.setRagioneSociale(destinatario.getRagioneSociale());
			json.setTelefono(destinatario.getTelefono());
		} else {
			json = null;
		}
		return json;
	}

}
