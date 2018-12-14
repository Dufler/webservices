package it.ltc.services.logica.data.listini.corrieri;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCorriereVoce;
import it.ltc.database.model.centrale.ListinoCorriereVoceFissa;
import it.ltc.database.model.centrale.ListinoCorriereVocePercentuale;
import it.ltc.database.model.centrale.ListinoCorriereVoceProporzionale;
import it.ltc.database.model.centrale.ListinoCorriereVoceScaglioni;
import it.ltc.database.model.centrale.ListinoCorriereVoceScaglioniPK;
import it.ltc.database.model.centrale.ListinoCorriereVoceScaglioniRipetuti;
import it.ltc.database.model.centrale.json.VoceScaglioneJSON;

@Repository
public class VociListinoCorriereDAOImpl extends CRUDDao<ListinoCorriereVoce> implements VociListinoCorriereDAO {
	
	@Autowired
	private VociListinoCorriereFissaDAO daoFissa;
	
	@Autowired
	private VociListinoCorrierePercentualeDAO daoPercentuale;
	
	@Autowired
	private VociListinoCorriereProporzionaleDAO daoProporzionale;
	
	@Autowired
	private VociListinoCorriereScaglioniRipetutiDAO daoRipetuti;
	
	@Autowired
	private VociListinoCorriereScaglioniDAO daoScaglioni;

	public VociListinoCorriereDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCorriereVoce.class);
	}

	@Override
	public List<ListinoCorriereVoce> trovaTutte() {
		List<ListinoCorriereVoce> list = findAll();
		for (ListinoCorriereVoce voce : list) {
			trovaDettagli(voce);
		}
		return list;
	}
	
	private void trovaDettagli(ListinoCorriereVoce voce) {
		switch(voce.getTipoCalcolo()) {
			case FIS : trovaDettagliVoceFissa(voce); break;
			case PER : trovaDettaglioVocePercentuale(voce); break;
			case PRO : trovaDettaglioVoceProporzionale(voce); break;
			case SCR : trovaDettaglioVoceRipetuti(voce); break;
			case SCA : trovaDettaglioVociScaglioni(voce); break;
			default : break;
		}
	}

	private void trovaDettaglioVociScaglioni(ListinoCorriereVoce voce) {
		List<ListinoCorriereVoceScaglioni> scaglioni = daoScaglioni.trovaScaglioni(voce.getId());
		List<VoceScaglioneJSON> jsons = new LinkedList<>();
		for (ListinoCorriereVoceScaglioni scaglione : scaglioni) {
			VoceScaglioneJSON json = serializza(scaglione);
			jsons.add(json);
		}
		//voce.setScaglioni(scaglioni);
		voce.setScaglioni(jsons);
	}

	private void trovaDettaglioVoceRipetuti(ListinoCorriereVoce voce) {
		ListinoCorriereVoceScaglioniRipetuti ripetuti = daoRipetuti.trova(voce.getId());
		voce.setRipetuti(ripetuti);
	}

	private void trovaDettaglioVoceProporzionale(ListinoCorriereVoce voce) {
		ListinoCorriereVoceProporzionale proporzionale = daoProporzionale.trova(voce.getId());
		voce.setProporzionale(proporzionale);
	}

	private void trovaDettaglioVocePercentuale(ListinoCorriereVoce voce) {
		ListinoCorriereVocePercentuale percentuale = daoPercentuale.trova(voce.getId());
		voce.setPercentuale(percentuale);
	}

	private void trovaDettagliVoceFissa(ListinoCorriereVoce voce) {
		ListinoCorriereVoceFissa fissa = daoFissa.trova(voce.getId());
		voce.setFissa(fissa);
	}

	@Override
	public ListinoCorriereVoce trova(int id) {
		ListinoCorriereVoce entity = findByID(id);
		trovaDettagli(entity);
		return entity;
	}

	@Override
	public ListinoCorriereVoce inserisci(ListinoCorriereVoce voce) {
		ListinoCorriereVoce entity = insert(voce);
		inserisciDettagli(entity, voce);
		return entity;
	}

	private void inserisciDettagli(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		switch(voce.getTipoCalcolo()) {
			case FIS : inserisciDettagliVoceFissa(entity, voce); break;
			case PER : inserisciDettaglioVocePercentuale(entity, voce); break;
			case PRO : inserisciDettaglioVoceProporzionale(entity, voce); break;
			case SCR : inserisciDettaglioVoceRipetuti(entity, voce); break;
			case SCA : inserisciDettaglioScaglioni(entity, voce); break;
			default : break;
		}	
	}

	private void inserisciDettaglioScaglioni(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		List<VoceScaglioneJSON> inseriti = new LinkedList<>();
		List<VoceScaglioneJSON> jsons = voce.getScaglioni();
		for (VoceScaglioneJSON json : jsons) {
			json.setIdVoce(voce.getId());
			ListinoCorriereVoceScaglioni scaglione = deserializza(json);
			ListinoCorriereVoceScaglioni dettaglio = daoScaglioni.inserisci(scaglione);
			if (dettaglio != null)
				inseriti.add(serializza(dettaglio));
		}
		entity.setScaglioni(inseriti);
	}

	private void inserisciDettaglioVoceRipetuti(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		ListinoCorriereVoceScaglioniRipetuti ripetuti = voce.getRipetuti();
		ripetuti.setIdVoce(voce.getId());
		ListinoCorriereVoceScaglioniRipetuti dettaglio = daoRipetuti.inserisci(ripetuti);
		entity.setRipetuti(dettaglio);
	}

	private void inserisciDettaglioVoceProporzionale(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		ListinoCorriereVoceProporzionale proporzionale = voce.getProporzionale();
		proporzionale.setIdVoce(voce.getId());
		ListinoCorriereVoceProporzionale dettaglio = daoProporzionale.inserisci(proporzionale);
		entity.setProporzionale(dettaglio);
	}

	private void inserisciDettaglioVocePercentuale(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		ListinoCorriereVocePercentuale percentuale = voce.getPercentuale();
		percentuale.setIdVoce(voce.getId());
		ListinoCorriereVocePercentuale dettaglio = daoPercentuale.inserisci(percentuale);
		entity.setPercentuale(dettaglio);
	}

	private void inserisciDettagliVoceFissa(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		ListinoCorriereVoceFissa fissa = voce.getFissa();
		fissa.setIdVoce(voce.getId());
		ListinoCorriereVoceFissa dettaglio = daoFissa.inserisci(fissa);
		entity.setFissa(dettaglio);
	}

	@Override
	public ListinoCorriereVoce aggiorna(ListinoCorriereVoce voce) {
		ListinoCorriereVoce entity = update(voce, voce.getId());
		aggiornaDettagli(entity, voce);
		return entity;
	}

	private void aggiornaDettagli(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		switch(voce.getTipoCalcolo()) {
			case FIS : aggiornaDettagliVoceFissa(entity, voce); break;
			case PER : aggiornaDettagliVocePercentuale(entity, voce); break;
			case PRO : aggiornaDettaglioVoceProporzionale(entity, voce); break;
			case SCR : aggiornaDettaglioVoceRipetuti(entity, voce); break;
			case SCA : aggiornaDettaglioScaglioni(entity, voce); break;
			default : break;
		}
	}

	private void aggiornaDettaglioScaglioni(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		//Elimino i scaglioni esistenti
		List<ListinoCorriereVoceScaglioni> scaglioniDaEliminare = daoScaglioni.trovaScaglioni(entity.getId());
		for (ListinoCorriereVoceScaglioni scaglione : scaglioniDaEliminare) {
			daoScaglioni.elimina(scaglione);
		}
		//Inserisco i nuovi scaglioni
		List<VoceScaglioneJSON> aggiornati = new LinkedList<>();
		List<VoceScaglioneJSON> jsons = voce.getScaglioni();
		for (VoceScaglioneJSON json : jsons) {
			json.setIdVoce(voce.getId());
			ListinoCorriereVoceScaglioni scaglione = deserializza(json);
			ListinoCorriereVoceScaglioni dettaglio = daoScaglioni.inserisci(scaglione);
			if (dettaglio != null)
				aggiornati.add(serializza(dettaglio));
		}
		entity.setScaglioni(aggiornati);
	}

	private void aggiornaDettaglioVoceRipetuti(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		ListinoCorriereVoceScaglioniRipetuti ripetuti = voce.getRipetuti();
		ListinoCorriereVoceScaglioniRipetuti dettaglio = daoRipetuti.aggiorna(ripetuti);
		entity.setRipetuti(dettaglio);
	}

	private void aggiornaDettaglioVoceProporzionale(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		ListinoCorriereVoceProporzionale proporzionale = voce.getProporzionale();
		ListinoCorriereVoceProporzionale dettaglio = daoProporzionale.aggiorna(proporzionale);
		entity.setProporzionale(dettaglio);
	}

	private void aggiornaDettagliVocePercentuale(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		ListinoCorriereVocePercentuale percentuale = voce.getPercentuale();
		ListinoCorriereVocePercentuale dettaglio = daoPercentuale.aggiorna(percentuale);
		entity.setPercentuale(dettaglio);
	}

	private void aggiornaDettagliVoceFissa(ListinoCorriereVoce entity, ListinoCorriereVoce voce) {
		ListinoCorriereVoceFissa fissa = voce.getFissa();
		ListinoCorriereVoceFissa dettaglio = daoFissa.aggiorna(fissa);
		entity.setFissa(dettaglio);
	}

	@Override
	public ListinoCorriereVoce elimina(ListinoCorriereVoce voce) {
		ListinoCorriereVoce entity = delete(voce.getId());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCorriereVoce oldEntity, ListinoCorriereVoce entity) {
		oldEntity.setDescrizione(entity.getDescrizione());
		oldEntity.setIdListino(entity.getIdListino());
		oldEntity.setIdSottoAmbito(entity.getIdSottoAmbito());
		oldEntity.setNome(entity.getNome());
		oldEntity.setStrategiaCalcolo(entity.getStrategiaCalcolo());
		oldEntity.setTipoCalcolo(entity.getTipoCalcolo());
		oldEntity.setValoreSottoAmbito(entity.getValoreSottoAmbito());
	}
	
	private VoceScaglioneJSON serializza(ListinoCorriereVoceScaglioni voce) {
		VoceScaglioneJSON json = new VoceScaglioneJSON();
		ListinoCorriereVoceScaglioniPK pk = voce.getId();
		json.setIdVoce(pk.getIdVoce());
		json.setInizio(pk.getInizio());
		json.setFine(pk.getFine());
		json.setValore(voce.getValore());
		return json;
	}
	
	private ListinoCorriereVoceScaglioni deserializza(VoceScaglioneJSON json) {
		ListinoCorriereVoceScaglioni voce = new ListinoCorriereVoceScaglioni();
		ListinoCorriereVoceScaglioniPK pk = new ListinoCorriereVoceScaglioniPK();
		pk.setIdVoce(json.getIdVoce());
		pk.setInizio(json.getInizio());
		pk.setFine(json.getFine());
		voce.setId(pk);
		voce.setValore(json.getValore());
		return voce;
	}

}
