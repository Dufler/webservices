package it.ltc.services.clienti.data.ordine;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.jboss.logging.Logger;

import it.ltc.database.model.legacy.ColliImballo;
import it.ltc.database.model.legacy.ColliPreleva;
import it.ltc.database.model.legacy.Destinatari;
import it.ltc.database.model.legacy.MittentiOrdine;
import it.ltc.database.model.legacy.RigaCorr;
import it.ltc.database.model.legacy.TestataOrdini;
import it.ltc.database.model.legacy.documento.TestaCorrConDocumento;
import it.ltc.model.shared.json.cliente.ContrassegnoJSON;
import it.ltc.model.shared.json.cliente.DocumentoJSON;
import it.ltc.model.shared.json.cliente.SpedizioneJSON;
import it.ltc.services.custom.exception.CustomException;

public class OrdineColtortiDAOImpl extends OrdineLegacyDAOImpl {
	
	private static final Logger logger = Logger.getLogger("OrdineColtortiDAOImpl");

	public OrdineColtortiDAOImpl(String persistenceUnit) {
		super(persistenceUnit);
	}
	
	protected boolean aggiornaInfoSpedizione(List<TestataOrdini> ordiniDaSpedire, SpedizioneJSON infoSpedizione) {
		boolean aggiornamento;
		// Utility
		SimpleDateFormat meseGiorno = new SimpleDateFormat("MMdd");
		// Recupero la spedizione dal primo ordine, ho già controllato che siano tutti uguali
		TestataOrdini primoOrdine = ordiniDaSpedire.get(0);
		EntityManager em = getManager();
		TestaCorrConDocumento spedizione = em.find(TestaCorrConDocumento.class, primoOrdine.getIdTestaCorr());
		// Aggiorno i campi necessari
		spedizione.setCorriere(infoSpedizione.getCorriere());
		int dataConsegna = infoSpedizione.getDataConsegna() != null ? Integer.parseInt(meseGiorno.format(infoSpedizione.getDataConsegna())) : 0;
		spedizione.setDataConsegna(dataConsegna);
		String note = infoSpedizione.getNote() != null ? infoSpedizione.getNote() : "";
		String note1 = note.length() > 35 ? note.substring(0, 35) : note;
		String note2 = note.length() > 35 ? note.substring(35, note.length() > 70 ? 70 : note.length()) : "";
		spedizione.setNote1(note1);
		spedizione.setNote2(note2);
		spedizione.setServizio(infoSpedizione.getServizioCorriere());
		spedizione.setValoreMerce(infoSpedizione.getValoreDoganale());
		
		//Documento
		DocumentoJSON documento = infoSpedizione.getDocumentoFiscale();
		if (documento == null || documento.getDocumentoBase64() == null || documento.getDocumentoBase64().length == 0) {
			throw new CustomException("E' necessario allegare un documento valido per la spedizione.");
		} else {
			spedizione.setDocumentoBase64(documento.getDocumentoBase64());
			spedizione.setDocumentoData(new Timestamp(documento.getData().getTime()));
			spedizione.setDocumentoRiferimento(documento.getRiferimento());
			spedizione.setDocumentoTipo(documento.getTipo());
		}

		// Contrassegno
		ContrassegnoJSON infoContrassegno = infoSpedizione.getContrassegno();
		if (infoContrassegno != null) {
			spedizione.setCodBolla("4 ");
			spedizione.setContrassegno(infoContrassegno.getValore());
			spedizione.setTipoIncasso(infoContrassegno.getTipo());
			spedizione.setValutaIncasso(infoContrassegno.getValuta());
		} else {
			spedizione.setCodBolla("1 ");
			spedizione.setContrassegno(0.0);
			spedizione.setTipoIncasso("  ");
			spedizione.setValutaIncasso("EUR");
		}

		// Vado in scrittura in maniera transazionale.
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(spedizione);
			transaction.commit();
			aggiornamento = true;
		} catch (Exception e) {
			logger.error(e);
			transaction.rollback();
			aggiornamento = false;
		} finally {
			em.close();
		}
		return aggiornamento;
	}

	protected boolean inserisciInfoSpedizione(List<TestataOrdini> ordiniDaSpedire, SpedizioneJSON infoSpedizione) {
		boolean inserimento;
		// Preparo le variabili che mi serviranno per andare in insert.
		TestaCorrConDocumento spedizione = new TestaCorrConDocumento();
		List<ColliPreleva> colliDaPrelevare = new LinkedList<>();
		List<RigaCorr> righeSpedizione = new LinkedList<>();
		int pezzi = 0;
		int colli = 0;
		double peso = 0.0;
		double volume = 0.0;
		// Recupero il mittente e il destinatario dal primo ordine, ho già
		// controllato che siano tutti uguali
		TestataOrdini primoOrdine = ordiniDaSpedire.get(0);
		Destinatari destinatario = daoIndirizzi.trovaDestinatario(primoOrdine.getIdDestina());
		MittentiOrdine mittente = daoIndirizzi.trovaMittente(primoOrdine.getIdMittente());
		// Imposto alcune info di testacorr
		spedizione.setCorriere(infoSpedizione.getCorriere());
		spedizione.setCodMittente(infoSpedizione.getCodiceCorriere());
		int dataConsegna = infoSpedizione.getDataConsegna() != null ? Integer.parseInt(meseGiorno.format(infoSpedizione.getDataConsegna())) : 0;
		spedizione.setDataConsegna(dataConsegna);
		spedizione.setMittenteAlfa(primoOrdine.getNrOrdine());
		int progressivoSpedizione = daoTestaCorr.getProgressivoSpedizioneTestaCorr();
		spedizione.setMittenteNum(progressivoSpedizione);
		String note = infoSpedizione.getNote() != null ? infoSpedizione.getNote() : ""; //Ho già verificato che sia <= 70 caratteri.
		String note1 = note.length() > 35 ? note.substring(0, 35) : note;
		String note2 = note.length() > 35 ? note.substring(35, note.length()) : "";
		spedizione.setNote1(note1);
		spedizione.setNote2(note2);
		spedizione.setNrSpedi(progressivoSpedizione);
		spedizione.setServizio(infoSpedizione.getServizioCorriere());
		spedizione.setValoreMerce(infoSpedizione.getValoreDoganale());
		
		DocumentoJSON documento = infoSpedizione.getDocumentoFiscale();
		if (documento == null || documento.getDocumentoBase64() == null || documento.getDocumentoBase64().length == 0) {
			throw new CustomException("E' necessario allegare un documento valido per la spedizione.");
		} else {
			spedizione.setDocumentoBase64(documento.getDocumentoBase64());
			spedizione.setDocumentoData(new Timestamp(documento.getData().getTime()));
			spedizione.setDocumentoRiferimento(documento.getRiferimento());
			spedizione.setDocumentoTipo(documento.getTipo());
		}

		// Contrassegno
		ContrassegnoJSON infoContrassegno = infoSpedizione.getContrassegno();
		if (infoContrassegno != null) {
			spedizione.setCodBolla("4 ");
			spedizione.setContrassegno(infoContrassegno.getValore());
			spedizione.setTipoIncasso(infoContrassegno.getTipo());
			spedizione.setValutaIncasso(infoContrassegno.getValuta());
		} else {
			spedizione.setCodBolla("1 ");
			spedizione.setContrassegno(0.0);
			spedizione.setTipoIncasso("  ");
			spedizione.setValutaIncasso("EUR");
		}

		spedizione.setCap(destinatario.getCap());
		spedizione.setIndirizzo(destinatario.getIndirizzo());
		spedizione.setLocalita(destinatario.getLocalita());
		spedizione.setNazione(destinatario.getCodIso());
		spedizione.setProvincia(destinatario.getProvincia());
		spedizione.setRagSocDest(destinatario.getRagSoc1());
		spedizione.setRagSocEst(destinatario.getRagSoc2());
		spedizione.setTelefono(destinatario.getTel());

		spedizione.setCapMitt(mittente.getCap());
		spedizione.setNazioneMitt(mittente.getNazione());
		spedizione.setRagSocMitt(mittente.getRagioneSociale());

		// Recupero le info necessarie da ogni ordine e aggiorno quelle che dovranno essere salvate
		for (TestataOrdini ordine : ordiniDaSpedire) {
			//Fix, secondo Antonio e Andrea il campo nr lista di testaCorr andrebbe riempito con uno qualsiasi dei nr lista delle testate.
			spedizione.setNrLista(ordine.getNrLista());
			//Aggiorno le info dell'ordine necessarie
			ordine.setStato("INSP");
			ordine.setCodCorriere(infoSpedizione.getCorriere());
			ordine.setCodiceClienteCorriere(infoSpedizione.getCodiceCorriere());
			if (infoContrassegno != null) {
				ordine.setTipoIncasso(infoContrassegno.getTipo());
				ordine.setValContrassegno(infoContrassegno.getValore());
			}
			// Recupero i ColliImballo corrispondenti
			List<ColliImballo> colliImballati = recuperaImballiDaOrdine(ordine);
			for (ColliImballo colloImballato : colliImballati) {
				// Creo il ColliPreleva corrispondente
				ColliPreleva colloDaPrelevare = creaColloDaPrelevare(ordine, colloImballato, infoSpedizione);
				colliDaPrelevare.add(colloDaPrelevare);
				// Creo la RigaCorr corrispondente
				RigaCorr rigaSpedizione = creaRigaSpedizione(ordine, colloImballato, infoSpedizione, progressivoSpedizione);
				righeSpedizione.add(rigaSpedizione);
				// Aggiungo per avere i totali di pezzi, peso e volume
				colli += 1;
				pezzi += colloImballato.getPezziCollo();
				peso += colloImballato.getPesoKg();
				volume += rigaSpedizione.getVolume();
			}
		}
		// Aggiorno le info su testacorr
		spedizione.setPeso(peso);
		spedizione.setVolume(volume);
		spedizione.setNrColli(colli);
		spedizione.setPezzi(pezzi);
		// Vado in scrittura in maniera transazionale.
		EntityManager em = getManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			// Inserisco TestaCorr, RigaCorr e ColliPreleva
			em.persist(spedizione);
			for (RigaCorr riga : righeSpedizione) {
				em.persist(riga);
			}
			for (ColliPreleva collo : colliDaPrelevare) {
				em.persist(collo);
			}
			// Aggiorno le TestataOrdini con il testacorr creato e le altre info.
			for (TestataOrdini ordine : ordiniDaSpedire) {
				ordine.setIdTestaCorr(spedizione.getIdTestaCor());
				em.merge(ordine);
			}
			transaction.commit();
			inserimento = true;
		} catch (Exception e) {
			logger.error(e);
			if (transaction != null && transaction.isActive())
				transaction.rollback();
			inserimento = false;
		} finally {
			em.close();
		}
		return inserimento;
	}
	
	@Override
	public SpedizioneJSON getDocumentoDiTrasporto(int idSpedizione) {
		EntityManager em = getManager();
		TestaCorrConDocumento spedizione = em.find(TestaCorrConDocumento.class, idSpedizione);
		em.close();
		SpedizioneJSON json;
		if (spedizione != null) {
			json = new SpedizioneJSON();
			DocumentoJSON documento = new DocumentoJSON();
			documento.setData(new Date());
			documento.setRiferimento(spedizione.getMittenteAlfa());
			documento.setTipo("ORDINE");
			documento.setDocumentoBase64(spedizione.getDocumentoBase64());
			json.setDocumentoFiscale(documento);
			json.setCorriere(spedizione.getCorriere());
		} else {
			json = null;
		}
		return json;
	}

}
