package it.ltc.services.clienti.data.fornitore;

import java.util.LinkedList;
import java.util.List;

import it.ltc.database.dao.legacy.FornitoreDao;
import it.ltc.database.model.legacy.Fornitori;
import it.ltc.model.shared.dao.IFornitoreDao;
import it.ltc.model.shared.json.cliente.FornitoreJSON;
import it.ltc.model.shared.json.cliente.IndirizzoJSON;
import it.ltc.services.custom.exception.CustomException;

public class FornitoreLegacyDAOImpl extends FornitoreDao implements IFornitoreDao {

	public FornitoreLegacyDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	public FornitoreJSON trovaDaID(int idFornitore) {
		Fornitori fornitore = trovaPerID(idFornitore);
		FornitoreJSON json = fornitore != null ? serializza(fornitore) : null;
		return json;
	}
	
	@Override
	public List<FornitoreJSON> trovaTutti() {
        List<Fornitori> list = findAll();
        List<FornitoreJSON> fornitori = new LinkedList<>();
        for (Fornitori fornitore : list)
        	fornitori.add(serializza(fornitore));
		return fornitori;
	}
	
	@Override
	public FornitoreJSON inserisci(FornitoreJSON json) {
		checkRiferimentoUnicity(json);
		Fornitori fornitore = deserializza(json);
		Fornitori entity = insert(fornitore);
		FornitoreJSON inserito = entity != null ? serializza(entity) : null;
		return inserito;
	}
	
	@Override
	public FornitoreJSON aggiorna(FornitoreJSON json) {
		String riferimento = json != null ? json.getRiferimentoCliente() : ""; 
		Fornitori entity = trovaDaCodice(riferimento);
		FornitoreJSON aggiornato;
		if (entity != null) {
			Fornitori fornitore = deserializza(json);
			entity = update(fornitore, entity.getIdFornitore());
			aggiornato = entity != null ? serializza(entity) : null;
		} else throw new CustomException("Non esiste nessun fornitore con il codice di riferimento indicato.");
		return aggiornato;
	}
	
	@Override
	public FornitoreJSON elimina(FornitoreJSON json) {
		String riferimento = json != null ? json.getRiferimentoCliente() : ""; 
		Fornitori entity = trovaDaCodice(riferimento);
		FornitoreJSON eliminato;
		if (entity != null) {
			entity = delete(entity.getIdFornitore());
			eliminato = entity != null ? serializza(entity) : null;
		} else throw new CustomException("Non esiste nessun fornitore con il codice di riferimento indicato.");
		return eliminato;
	}
	
	private void checkRiferimentoUnicity(FornitoreJSON json) throws CustomException {
		if (json == null || json.getRiferimentoCliente() == null || json.getRiferimentoCliente().isEmpty())
			throw new CustomException("Nessun fornitore da inserire.");
		Fornitori fornitore = trovaDaCodice(json.getRiferimentoCliente());
		if (fornitore != null)
			throw new CustomException("E' gia' presente un fornitore con lo stesso codice.");
	}
	
	public Fornitori deserializza(FornitoreJSON json) {
		Fornitori fornitore;
		if (json != null) {
			fornitore = new Fornitori();
			fornitore.setIdFornitore(json.getId());
			fornitore.setRagSoc(json.getNome());
			fornitore.setCodiceFornitore(json.getRiferimentoCliente());
			//Non esiste il corrispondente delle note nei sistemi legacy - json.getNote();
			IndirizzoJSON indirizzo = json.getIndirizzo();
			if (indirizzo != null) {
				fornitore.setCap(indirizzo.getCap());
				fornitore.setCitta(indirizzo.getLocalita());
				fornitore.setCodnaz(indirizzo.getNazione());
				fornitore.setEMail(indirizzo.getEmail());
				fornitore.setFax(indirizzo.getTelefono());
				fornitore.setIndirizzo(indirizzo.getIndirizzo());
				fornitore.setNaz(indirizzo.getNazione());
				fornitore.setProv(indirizzo.getProvincia());
				fornitore.setTel(indirizzo.getTelefono());
			}
		} else {
			fornitore = null;
		}
		return fornitore;
	}

	public FornitoreJSON serializza(Fornitori fornitore) {
		FornitoreJSON json = new FornitoreJSON();
		if (fornitore != null) {
			json.setId(fornitore.getIdFornitore());
			json.setNome(fornitore.getRagSoc());
			json.setRiferimentoCliente(fornitore.getCodiceFornitore());
			IndirizzoJSON indirizzo = new IndirizzoJSON();
			indirizzo.setCap(fornitore.getCap());
			indirizzo.setEmail(fornitore.getEMail());
			indirizzo.setIndirizzo(fornitore.getIndirizzo());
			indirizzo.setLocalita(fornitore.getCitta());
			indirizzo.setNazione(fornitore.getCodnaz());
			indirizzo.setProvincia(fornitore.getProv());
			indirizzo.setRagioneSociale(fornitore.getRagSoc());
			indirizzo.setTelefono(fornitore.getTel());
			json.setIndirizzo(indirizzo);
		}
		return json;
	}

}
