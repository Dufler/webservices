/**
 * Qui sono contenuti gli oggetti JSON d'esempio per la documentazione dei Web Services per gli ordini.
 */

var destinatario = {
	ragioneSociale : "pluto",
    indirizzo : "via pluto, 5",
    cap : "12345",
    localita : "pipponia",
    provincia : "PG",
    nazione : "ITA",
    telefono : "123456789",
    email : "pluto@pippo.it"	
};

var mittente = {
	ragioneSociale : "pippo",
	indirizzo : "via pippo, 1",
	cap : "12345",
	localita : "pipponia",
	provincia : "PG",
	nazione : "ITA",
	telefono : "123456789",
	email : "pippo@pippo.it"
};

var documento = {
	riferimento : "1",
	data : "2018-04-23T18:25:43.511Z",
	tipo : "ORDINE"
};

var riga1 = {
	riga: 1,
	quantitaOrdinata : 1,
	magazzino : "TEST",
	prodotto : "ABC"
};

var riga2 = {
		riga: 2,
		quantitaOrdinata : 1,
		magazzino : "TEST",
		prodotto : "DEF"
};

var ordine = {
        dataOrdine: "2018-04-23T18:25:43.511Z",
        dataConsegna: "2018-04-23T18:25:43.511Z",
        tipo: "PRODUZIONE",
        priorita: 1,
        riferimentoOrdine: "123",
        pezziOrdinati: 2,
        destinatario : destinatario,
        mittente : mittente
};

var prodottoImballato = {
		prodotto: "ABC",
        quantitaImballata: 1
};

var imballo = {
	riferimento : "1800000001",
    barcode : "COL1",
    peso : 0.66,
    volume : 0.096,
    h : 40.0,
    l : 40.0,
    z : 60.0,
    prodotti: [prodottoImballato],
    seriali : ["4B05355D0000000000018E4B"]	
};

var ordineCompleto = {
		ordine : ordine,
	    documento : documento,
	    dettagli : [riga1, riga2]
};

var ordineImballato = {
	ordine : ordine,
	imballi : [imballo]
};

var spedizione = {
	riferimenti : ["123"],
	corriere : "TNT",
    servizioCorriere : "DEF"
};

var contrassegno = {
		tipo: "SC",
        valore : 123.45
}

$(document).ready(function() {
	
	var nodoOrdineCompleto = new PrettyJSON.view.Node({ el:$('#jsonOrdineCompleto'), data: ordineCompleto }).expandAll();
	var nodoTestata = new PrettyJSON.view.Node({ el:$('#jsonTestata'), data: ordine }).expandAll();
	var nodoIndirizzo = new PrettyJSON.view.Node({ el:$('#jsonIndirizzo'), data: destinatario }).expandAll();
	var nodoProdotto = new PrettyJSON.view.Node({ el:$('#jsonProdotto'), data: riga1 }).expandAll();
	var nodoImballo = new PrettyJSON.view.Node({ el:$('#jsonImballo'), data: imballo }).expandAll();
	var nodoProdottoImballato = new PrettyJSON.view.Node({ el:$('#jsonProdottoImballato'), data: prodottoImballato }).expandAll();
	var nodoSpedizione = new PrettyJSON.view.Node({ el:$('#jsonSpedizione'), data: spedizione }).expandAll();	
	var nodoContrassegno = new PrettyJSON.view.Node({ el:$('#jsonContrassegno'), data: contrassegno	}).expandAll();
	var nodoDocumento = new PrettyJSON.view.Node({ el:$('#jsonDocumento'), data: documento }).expandAll();

});