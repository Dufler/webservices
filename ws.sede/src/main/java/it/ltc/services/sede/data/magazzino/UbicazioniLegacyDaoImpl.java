package it.ltc.services.sede.data.magazzino;

import it.ltc.database.dao.legacy.ColliCaricoDao;
import it.ltc.database.dao.legacy.UbicaColliLogDao;
import it.ltc.database.dao.legacy.UbicazioniDao;
import it.ltc.database.model.legacy.ColliCarico;
import it.ltc.database.model.legacy.UbicaColliLog;
import it.ltc.database.model.legacy.Ubicazioni;
import it.ltc.services.custom.exception.CustomException;
import it.ltc.services.sede.model.carico.ColloCaricoJSON;
import it.ltc.services.sede.model.magazzino.UbicazioneJSON;
import it.ltc.services.sede.model.magazzino.UbicazioneJSON.TipoUbicazione;

public class UbicazioniLegacyDaoImpl implements GestoreUbicazioniDao {
	
	protected final ColliCaricoDao daoColli;
	protected final UbicazioniDao daoUbicazioni;
	protected final UbicaColliLogDao daoLogUbicazioni;
	
	protected String utente;
	
	public UbicazioniLegacyDaoImpl(String persistenceUnit) {
		daoColli = new ColliCaricoDao(persistenceUnit);
		daoUbicazioni = new UbicazioniDao(persistenceUnit);
		daoLogUbicazioni = new UbicaColliLogDao(persistenceUnit);
	}
	
	public void setUtente(String utente) {
		this.utente = utente;
	}

	@Override
	public ColloCaricoJSON ubicaCollo(ColloCaricoJSON collo) {
		checkUbicazione(collo);
		ColliCarico c = checkCollo(collo);
		c.setKeyUbicaCar(collo.getUbicazione());
		c = daoColli.aggiorna(c);
		UbicaColliLog log = getLogPerCollo(collo);
		if (c == null || log == null)
			collo = null;
		return collo;
	}
	
	protected UbicaColliLog getLogPerCollo(ColloCaricoJSON collo) {
		UbicaColliLog log = new UbicaColliLog();
		log.setKeyCollo(collo.getCollo());
		log.setKeyUbbica(collo.getUbicazione());
		log.setOpeUbica(utente);
		log.setTipoCollo("COLLICARICO"); //Esiste solo questo valore nei DB.
		log = daoLogUbicazioni.inserisci(log);
		return log;
	}
	
	protected Ubicazioni checkUbicazione(ColloCaricoJSON collo) {
		String codiceUbicazione = collo.getUbicazione();
		if (codiceUbicazione == null || codiceUbicazione.isEmpty())
			throw new CustomException("Bisogna indicare un'ubicazione");
		Ubicazioni ubicazione = daoUbicazioni.trovaDaCodice(codiceUbicazione);
		if (ubicazione == null)
			throw new CustomException("L'ubicazione indicata per il collo non esiste. (" + codiceUbicazione + ")");
		return ubicazione;
	}
	
	protected ColliCarico checkCollo(ColloCaricoJSON collo) {
		ColliCarico entity = daoColli.trovaDaCodice(collo.getCollo());
		if (entity == null)
			throw new CustomException("Nessun collo trovato con n°: '" + collo.getCollo() + "'");
		return entity;
	}

	@Override
	public UbicazioneJSON inserisci(UbicazioneJSON ubicazione) {
		
		return null;
	}

	@Override
	public UbicazioneJSON aggiona(UbicazioneJSON ubicazione) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UbicazioneJSON elimina(UbicazioneJSON ubicazione) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected Ubicazioni deserializza(UbicazioneJSON json) {
		Ubicazioni ubicazione = new Ubicazioni();
		ubicazione.setKeyUbica(json.getUbicazione());
		//FIXME : Trovare i valori area, etc a partire dalla stringa dell'ubicazione.
		ubicazione.setTipoUbica(json.getTipo() != null ? json.getTipo().getValoreLegacy() : "SC");
		return ubicazione;
	}
	
	protected UbicazioneJSON serializza(Ubicazioni ubicazione) {
		UbicazioneJSON json = new UbicazioneJSON();
		json.setTipo(TipoUbicazione.trovaDaValoreLegacy(ubicazione.getTipoUbica()));
		json.setUbicazione(ubicazione.getKeyUbica());
		return json;
	}

}
