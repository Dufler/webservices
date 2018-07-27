package it.ltc.services.clienti.data.indirizzo;

import it.ltc.database.dao.Dao;
import it.ltc.database.dao.legacy.DestinatariDao;
import it.ltc.database.dao.legacy.MittentiOrdineDao;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.MittentiOrdine;
import it.ltc.model.shared.json.cliente.IndirizzoJSON;
import it.ltc.services.custom.exception.CustomException;

public class IndirizziLegacyDAOImpl extends Dao implements IndirizziLegacyDAO {
	
	protected final DestinatariDao daoDestinatari;
	protected final MittentiOrdineDao daoMittenti;

	public IndirizziLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
		daoDestinatari = new DestinatariDao(persistenceUnit);
		daoMittenti = new MittentiOrdineDao(persistenceUnit);
	}
	
	@Override
	public MittentiOrdine trovaMittente(int idMittente) {
		MittentiOrdine mittente = daoMittenti.trovaDaID(idMittente);
		return mittente;
	}

	@Override
	public MittentiOrdine ottieniMittente(IndirizzoJSON mittente) {
		// Eseguo una ricerca per vedere se è già presente.
		// Se ho trovato corrispondenza lo restituisco, altrimenti lo inserisco
		MittentiOrdine entity = daoMittenti.trovaIndirizzo(mittente.getRagioneSociale(), mittente.getCap(), mittente.getIndirizzo(), mittente.getLocalita(), mittente.getNazione());
		if (entity == null) {
			entity = new MittentiOrdine();
			entity.setCap(mittente.getCap());
			entity.setEmail(mittente.getEmail());
			entity.setIndirizzo(mittente.getIndirizzo());
			entity.setLocalita(mittente.getLocalita());
			entity.setNazione(mittente.getNazione());
			entity.setProvincia(mittente.getProvincia());
			entity.setRagioneSociale(mittente.getRagioneSociale());
			entity.setTelefono(mittente.getTelefono());
			entity = daoMittenti.inserisci(entity);
		}
		return entity;
	}
	
	@Override
	public Destinatari trovaDestinatario(int idDestinatario) {
		Destinatari destinatario = daoDestinatari.trovaDaID(idDestinatario);
		return destinatario;
	}

	@Override
	public Destinatari ottieniDestinatario(IndirizzoJSON destinatario) {
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
		Destinatari entity = daoDestinatari.trovaIndirizzo(ragioneSociale1, destinatario.getCap(), destinatario.getIndirizzo(), destinatario.getLocalita(), destinatario.getNazione());
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
			entity = daoDestinatari.inserisci(entity);
			if (entity == null)
				throw new CustomException("Impossibile inserire l'indirizzo indicato per il destinatario. Contattare il reparto IT.");
		}
		return entity;
	}

}
