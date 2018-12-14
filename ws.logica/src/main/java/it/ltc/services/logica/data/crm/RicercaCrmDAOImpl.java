package it.ltc.services.logica.data.crm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.model.centrale.Azienda;
import it.ltc.database.model.centrale.AziendaNote;
import it.ltc.database.model.centrale.Contatto;
import it.ltc.database.model.centrale.CrmTagCategoriaMerceologica;
import it.ltc.database.model.centrale.CrmTagServiziRichiesti;
import it.ltc.services.logica.model.crm.RisultatoRicercaCrm;

@Repository
public class RicercaCrmDAOImpl implements RicercaCrmDAO {
	
	@Autowired
	private AziendaDAO daoAziende;
	
	@Autowired
	private AziendeNoteDAO daoNote;
	
	@Autowired
	private ContattoDAO daoContatti;
	
	@Autowired
	private TagServiziDAO daoTagServizi;
	
	@Autowired
	private TagCategorieDAO daoTagCategorie;

	@Override
	public RisultatoRicercaCrm trovaParolaChiave(String parola) {
		Set<Azienda> aziende = new HashSet<>();
		aziende.addAll(daoAziende.trovaDaNome(parola));
		List<CrmTagServiziRichiesti> tagServizi = daoTagServizi.trovaDaTag(parola);
		for (CrmTagServiziRichiesti tag : tagServizi) {
			aziende.add(daoAziende.trova(tag.getAzienda()));
		}
		List<CrmTagCategoriaMerceologica> tagCategorie = daoTagCategorie.trovaDaTag(parola);
		for (CrmTagCategoriaMerceologica tag : tagCategorie) {
			aziende.add(daoAziende.trova(tag.getAzienda()));
		}
		List<Azienda> listaAziende = new LinkedList<>();
		for (Azienda azienda : aziende) {
			listaAziende.add(azienda);
		}
		List<AziendaNote> note = daoNote.trovaDaParola(parola);
		List<Contatto> contatti = daoContatti.trovaDaNome(parola);
		RisultatoRicercaCrm risultati = new RisultatoRicercaCrm();
		risultati.setAziende(listaAziende);
		risultati.setContatti(contatti);
		risultati.setNote(note);
		return risultati;
	}

}
