package it.ltc.services.logica.data.listini.clienti;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.ltc.database.dao.CRUDDao;
import it.ltc.database.model.centrale.ListinoCommessaVoce;
import it.ltc.database.model.centrale.ListinoCommessaVoceFissa;
import it.ltc.database.model.centrale.ListinoCommessaVocePercentuale;
import it.ltc.database.model.centrale.ListinoCommessaVoceProporzionale;
import it.ltc.database.model.centrale.ListinoCommessaVoceScaglioni;
import it.ltc.database.model.centrale.ListinoCommessaVoceScaglioniPK;
import it.ltc.database.model.centrale.ListinoCommessaVoceScaglioniRipetuti;
import it.ltc.database.model.centrale.json.VoceScaglioneJSON;

@Repository
public class VociListinoClienteDAOImpl extends CRUDDao<ListinoCommessaVoce> implements VociListinoClienteDAO {
	
	@Autowired
	private VociListinoClienteFissaDAO daoFissa;
	
	@Autowired
	private VociListinoClientePercentualeDAO daoPercentuale;
	
	@Autowired
	private VociListinoClienteProporzionaleDAO daoProporzionale;
	
	@Autowired
	private VociListinoClienteScaglioniRipetutiDAO daoRipetuti;
	
	@Autowired
	private VociListinoClienteScaglioniDAO daoScaglioni;

	public VociListinoClienteDAOImpl() {
		super(LOCAL_CENTRALE_PERSISTENCE_UNIT_NAME, ListinoCommessaVoce.class);
	}

	@Override
	public List<ListinoCommessaVoce> trovaTutte() {
		List<ListinoCommessaVoce> list = findAll();
		for (ListinoCommessaVoce voce : list) {
			trovaDettagli(voce);
		}
		return list;
	}
	
	private void trovaDettagli(ListinoCommessaVoce voce) {
		switch(voce.getTipoCalcolo()) {
			case FIS : trovaDettagliVoceFissa(voce); break;
			case PER : trovaDettaglioVocePercentuale(voce); break;
			case PRO : trovaDettaglioVoceProporzionale(voce); break;
			case SCR : trovaDettaglioVoceRipetuti(voce); break;
			case SCA : trovaDettaglioVociScaglioni(voce); break;
			default : break;
		}
	}

	private void trovaDettaglioVociScaglioni(ListinoCommessaVoce voce) {
		List<ListinoCommessaVoceScaglioni> scaglioni = daoScaglioni.trovaScaglioni(voce.getId());
		List<VoceScaglioneJSON> jsons = new LinkedList<>();
		for (ListinoCommessaVoceScaglioni scaglione : scaglioni) {
			VoceScaglioneJSON json = serializza(scaglione);
			jsons.add(json);
		}
		voce.setScaglioni(jsons);
	}

	private void trovaDettaglioVoceRipetuti(ListinoCommessaVoce voce) {
		ListinoCommessaVoceScaglioniRipetuti ripetuti = daoRipetuti.trova(voce.getId());
		voce.setRipetuti(ripetuti);
	}

	private void trovaDettaglioVoceProporzionale(ListinoCommessaVoce voce) {
		ListinoCommessaVoceProporzionale proporzionale = daoProporzionale.trova(voce.getId());
		voce.setProporzionale(proporzionale);
	}

	private void trovaDettaglioVocePercentuale(ListinoCommessaVoce voce) {
		ListinoCommessaVocePercentuale percentuale = daoPercentuale.trova(voce.getId());
		voce.setPercentuale(percentuale);
	}

	private void trovaDettagliVoceFissa(ListinoCommessaVoce voce) {
		ListinoCommessaVoceFissa fissa = daoFissa.trova(voce.getId());
		voce.setFissa(fissa);
	}

	@Override
	public ListinoCommessaVoce trova(int id) {
		ListinoCommessaVoce entity = findByID(id);
		trovaDettagli(entity);
		return entity;
	}

	@Override
	public ListinoCommessaVoce inserisci(ListinoCommessaVoce voce) {
		ListinoCommessaVoce entity = insert(voce);
		inserisciDettagli(entity, voce);
		return entity;
	}

	private void inserisciDettagli(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		switch(voce.getTipoCalcolo()) {
			case FIS : inserisciDettagliVoceFissa(entity, voce); break;
			case PER : inserisciDettaglioVocePercentuale(entity, voce); break;
			case PRO : inserisciDettaglioVoceProporzionale(entity, voce); break;
			case SCR : inserisciDettaglioVoceRipetuti(entity, voce); break;
			case SCA : inserisciDettaglioScaglioni(entity, voce); break;
			default : break;
		}	
	}

	private void inserisciDettaglioScaglioni(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		List<VoceScaglioneJSON> inseriti = new LinkedList<>();
		List<VoceScaglioneJSON> jsons = voce.getScaglioni();
		for (VoceScaglioneJSON json : jsons) {
			json.setIdVoce(voce.getId());
			ListinoCommessaVoceScaglioni scaglione = deserializza(json);
			ListinoCommessaVoceScaglioni dettaglio = daoScaglioni.inserisci(scaglione);
			inseriti.add(serializza(dettaglio));
		}
		entity.setScaglioni(inseriti);
	}

	private void inserisciDettaglioVoceRipetuti(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		ListinoCommessaVoceScaglioniRipetuti ripetuti = voce.getRipetuti();
		ripetuti.setIdVoce(voce.getId());
		ListinoCommessaVoceScaglioniRipetuti dettaglio = daoRipetuti.inserisci(ripetuti);
		entity.setRipetuti(dettaglio);
	}

	private void inserisciDettaglioVoceProporzionale(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		ListinoCommessaVoceProporzionale proporzionale = voce.getProporzionale();
		proporzionale.setIdVoce(voce.getId());
		ListinoCommessaVoceProporzionale dettaglio = daoProporzionale.inserisci(proporzionale);
		entity.setProporzionale(dettaglio);
	}

	private void inserisciDettaglioVocePercentuale(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		ListinoCommessaVocePercentuale percentuale = voce.getPercentuale();
		percentuale.setIdVoce(voce.getId());
		ListinoCommessaVocePercentuale dettaglio = daoPercentuale.inserisci(percentuale);
		entity.setPercentuale(dettaglio);
	}

	private void inserisciDettagliVoceFissa(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		ListinoCommessaVoceFissa fissa = voce.getFissa();
		fissa.setIdVoce(voce.getId());
		ListinoCommessaVoceFissa dettaglio = daoFissa.inserisci(fissa);
		entity.setFissa(dettaglio);
	}

	@Override
	public ListinoCommessaVoce aggiorna(ListinoCommessaVoce voce) {
		ListinoCommessaVoce entity = update(voce, voce.getId());
		aggiornaDettagli(entity, voce);
		return entity;
	}

	private void aggiornaDettagli(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		switch(voce.getTipoCalcolo()) {
			case FIS : aggiornaDettagliVoceFissa(entity, voce); break;
			case PER : aggiornaDettagliVocePercentuale(entity, voce); break;
			case PRO : aggiornaDettaglioVoceProporzionale(entity, voce); break;
			case SCR : aggiornaDettaglioVoceRipetuti(entity, voce); break;
			case SCA : aggiornaDettaglioScaglioni(entity, voce); break;
			default : break;
		}
	}

	private void aggiornaDettaglioScaglioni(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		//Elimino i scaglioni esistenti
		List<ListinoCommessaVoceScaglioni> scaglioniDaEliminare = daoScaglioni.trovaScaglioni(entity.getId());
		for (ListinoCommessaVoceScaglioni scaglione : scaglioniDaEliminare) {
			daoScaglioni.elimina(scaglione);
		}
		//Inserisco i nuovi scaglioni
		List<VoceScaglioneJSON> aggiornati = new LinkedList<>();
		List<VoceScaglioneJSON> jsons = voce.getScaglioni();
		for (VoceScaglioneJSON json : jsons) {
			json.setIdVoce(voce.getId());
			ListinoCommessaVoceScaglioni scaglione = deserializza(json);
			ListinoCommessaVoceScaglioni dettaglio = daoScaglioni.inserisci(scaglione);
			aggiornati.add(serializza(dettaglio));
		}
		entity.setScaglioni(aggiornati);
	}

	private void aggiornaDettaglioVoceRipetuti(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		ListinoCommessaVoceScaglioniRipetuti ripetuti = voce.getRipetuti();
		ListinoCommessaVoceScaglioniRipetuti dettaglio = daoRipetuti.aggiorna(ripetuti);
		entity.setRipetuti(dettaglio);
	}

	private void aggiornaDettaglioVoceProporzionale(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		ListinoCommessaVoceProporzionale proporzionale = voce.getProporzionale();
		ListinoCommessaVoceProporzionale dettaglio = daoProporzionale.aggiorna(proporzionale);
		entity.setProporzionale(dettaglio);
	}

	private void aggiornaDettagliVocePercentuale(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		ListinoCommessaVocePercentuale percentuale = voce.getPercentuale();
		ListinoCommessaVocePercentuale dettaglio = daoPercentuale.aggiorna(percentuale);
		entity.setPercentuale(dettaglio);
	}

	private void aggiornaDettagliVoceFissa(ListinoCommessaVoce entity, ListinoCommessaVoce voce) {
		ListinoCommessaVoceFissa fissa = voce.getFissa();
		ListinoCommessaVoceFissa dettaglio = daoFissa.aggiorna(fissa);
		entity.setFissa(dettaglio);
	}

	@Override
	public ListinoCommessaVoce elimina(ListinoCommessaVoce voce) {
		ListinoCommessaVoce entity = delete(voce.getId());
		return entity;
	}

	@Override
	protected void updateValues(ListinoCommessaVoce oldEntity, ListinoCommessaVoce entity) {
		oldEntity.setDescrizione(entity.getDescrizione());
		oldEntity.setIdListino(entity.getIdListino());
		oldEntity.setIdSottoAmbito(entity.getIdSottoAmbito());
		oldEntity.setNome(entity.getNome());
		oldEntity.setStrategiaCalcolo(entity.getStrategiaCalcolo());
		oldEntity.setTipoCalcolo(entity.getTipoCalcolo());
		oldEntity.setValoreSottoAmbito(entity.getValoreSottoAmbito());
	}
	
	private VoceScaglioneJSON serializza(ListinoCommessaVoceScaglioni voce) {
		VoceScaglioneJSON json = new VoceScaglioneJSON();
		ListinoCommessaVoceScaglioniPK pk = voce.getId();
		json.setIdVoce(pk.getIdVoce());
		json.setInizio(pk.getInizio());
		json.setFine(pk.getFine());
		json.setValore(voce.getValore());
		return json;
	}
	
	private ListinoCommessaVoceScaglioni deserializza(VoceScaglioneJSON json) {
		ListinoCommessaVoceScaglioni voce = new ListinoCommessaVoceScaglioni();
		ListinoCommessaVoceScaglioniPK pk = new ListinoCommessaVoceScaglioniPK();
		pk.setIdVoce(json.getIdVoce());
		pk.setInizio(json.getInizio());
		pk.setFine(json.getFine());
		voce.setId(pk);
		voce.setValore(json.getValore());
		return voce;
	}

}
