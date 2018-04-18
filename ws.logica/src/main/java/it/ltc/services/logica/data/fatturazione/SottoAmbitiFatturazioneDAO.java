package it.ltc.services.logica.data.fatturazione;

import java.util.List;

import it.ltc.database.model.centrale.FatturaSottoAmbito;

public interface SottoAmbitiFatturazioneDAO {
	
	public List<FatturaSottoAmbito> trovaTutti();
	
	public FatturaSottoAmbito trova(int id);
	
	public FatturaSottoAmbito inserisci(FatturaSottoAmbito ambito);
	
	public FatturaSottoAmbito aggiorna(FatturaSottoAmbito ambito);
	
	public FatturaSottoAmbito elimina(FatturaSottoAmbito ambito);

}
