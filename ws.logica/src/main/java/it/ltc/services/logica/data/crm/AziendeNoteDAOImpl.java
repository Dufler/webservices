package it.ltc.services.logica.data.crm;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.dao.CondizioneWhere;
import it.ltc.database.dao.CondizioneWhere.Condizione;
import it.ltc.database.dao.CondizioneWhere.Operatore;
import it.ltc.database.model.centrale.Azienda;
import it.ltc.database.model.centrale.AziendaNote;
import it.ltc.database.model.centrale.Contatto;

@Repository
public class AziendeNoteDAOImpl extends CRUDDao<AziendaNote> implements AziendeNoteDAO {

	@Autowired
	private ContattoDAO daoContatti;
	
	@Autowired
	private AziendaDAO daoAziende;
	
	private final SimpleDateFormat sdf;
	private final HashMap<Integer, Azienda> aziende;
	private final HashMap<Integer, Contatto> contatti;
	
	public AziendeNoteDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, AziendaNote.class);
		sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
		aziende = new HashMap<>();
		contatti = new HashMap<>();
	}
	
	protected void aggiungiInfoAggiuntiveNota(AziendaNote nota) {
		if (nota != null) {
			String dataNota = nota.getDataNota() != null ? sdf.format(nota.getDataNota()) : "";
			nota.setDataFormattata(dataNota);
			Contatto contatto = trovaContatto(nota.getContatto());
			String nomeContatto = contatto != null ? contatto.toString() : "nessuno";
			nota.setNomeContatto(nomeContatto);
			Azienda azienda = trovaAzienda(nota.getAzienda());
			String nomeAzienda = azienda != null ? azienda.getRagioneSociale() : "";
			nota.setNomeAzienda(nomeAzienda);
		}
	}
	
	protected Contatto trovaContatto(Integer id) {
		if (!contatti.containsKey(id)) {
			Contatto contatto = daoContatti.trovaDaID(id == null ? 0 : id);
			contatti.put(id, contatto);
		}
		return contatti.get(id);
	}
	
	protected Azienda trovaAzienda(int id) {
		if (!aziende.containsKey(id)) {
			Azienda azienda = daoAziende.trovaDaID(id);
			aziende.put(id, azienda);
		}
		return aziende.get(id);
	}

	@Override
	public List<AziendaNote> trovaTutti() {
		List<AziendaNote> entitites = findAll();
		return entitites;
	}

	@Override
	public List<AziendaNote> trovaDaAzienda(int idAzienda) {
		List<AziendaNote> lista = findAllEqualTo("azienda", idAzienda);
		for (AziendaNote nota : lista)
			aggiungiInfoAggiuntiveNota(nota);
        return lista;
	}

	@Override
	public List<AziendaNote> trovaDaContatto(int idContatto) {
		List<AziendaNote> lista = findAllEqualTo("contatto", idContatto);
		for (AziendaNote nota : lista)
			aggiungiInfoAggiuntiveNota(nota);
        return lista;
	}
	
	@Override
	public List<AziendaNote> trovaDaParola(String parola) {
		CondizioneWhere likeNote = new CondizioneWhere("note", parola, Operatore.LIKE, Condizione.AND);
		List<CondizioneWhere> conditions = new LinkedList<>();
		conditions.add(likeNote);
		List<AziendaNote> lista = findAll(conditions, 100);
		for (AziendaNote nota : lista)
			aggiungiInfoAggiuntiveNota(nota);
        return lista;
	}

	@Override
	public AziendaNote trova(int id) {
		AziendaNote entity = findByID(id);
		aggiungiInfoAggiuntiveNota(entity);
		return entity;
	}

	@Override
	public AziendaNote inserisci(AziendaNote note) {
		AziendaNote entity = insert(note);
		aggiungiInfoAggiuntiveNota(entity);
		return entity;
	}

	@Override
	public AziendaNote aggiorna(AziendaNote note) {
		AziendaNote entity = update(note, note.getId());
		aggiungiInfoAggiuntiveNota(entity);
		return entity;
	}

	@Override
	public AziendaNote elimina(AziendaNote note) {
		AziendaNote entity = delete(note.getId());
		return entity;
	}

	@Override
	protected void updateValues(AziendaNote oldEntity, AziendaNote entity) {
		oldEntity.setAutore(entity.getAutore());
		oldEntity.setAzienda(entity.getAzienda());
		oldEntity.setContatto(entity.getContatto());
		oldEntity.setDataNota(entity.getDataNota());
		oldEntity.setNote(entity.getNote());
	}

}
