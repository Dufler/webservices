/**
 * Qui sono contenuti gli oggetti JSON d'esempio per la documentazione dei Web
 * Services per i carichi.
 */

var tipoCarico = {
	codice : "RESO",
	descrizione : "Reso di merce dal cliente finale."
};

var prodotto = {
	id : 1,
	quantita : 2,
	seriali : ["10000123", "10000124"]
};

var collo = {
	id : 1,
	carico : 1,
	magazzino : "PG1",
	barcodeCliente : "XY123-456",
	prodotti : [prodotto]
};

var documento = {
	riferimento : "1",
	data : "2018-04-23T18:25:43.511Z",
	tipo : "INGRESSO"
};

var ingresso = {
	dataArrivo : "2018-04-23T18:25:43.511Z",
	fornitore : "FORNITORE1",
	pezziStimati : 40,
	riferimentoCliente : "123",
	tipo : "PRODUZIONE"
};

var dettaglio1 = {
	riga : 1,
	magazzino : "TEST",
	collo : "A",
	prodotto : "ABC",
	quantitaPrevista : 20
};

var dettaglio2 = {
	riga : 2,
	magazzino : "TEST",
	collo : "A",
	prodotto : "DEF",
	quantitaPrevista : 20
};

var dettaglioSingolo = {
	riga : 2,
	magazzino : "TEST",
	collo : "A",
	prodotto : "DEF",
	quantitaPrevista : 15,
	riferimento : "123"
};

var carico = {
	ingresso : ingresso,
	documento : documento,
	dettagli : [ dettaglio1, dettaglio2 ]
};

$(document).ready(function() {

	var nodoTipoCarico = new PrettyJSON.view.Node({
		el : $('#jsonTipoCarico'),
		data : tipoCarico
	}).expandAll();
	var nodoCarico = new PrettyJSON.view.Node({
		el : $('#jsonCarico'),
		data : carico
	}).expandAll();
	var nodoIngresso = new PrettyJSON.view.Node({
		el : $('#jsonIngresso'),
		data : ingresso
	}).expandAll();
	var nodoDettaglio = new PrettyJSON.view.Node({
		el : $('#jsonDettaglio'),
		data : dettaglioSingolo
	}).expandAll();
	var nodoDocumento = new PrettyJSON.view.Node({
		el : $('#jsonDocumento'),
		data : documento
	}).expandAll();
	var nodoCollo = new PrettyJSON.view.Node({
		el : $('#jsonCollo'),
		data : collo
	}).expandAll();
	var nodoProdotto = new PrettyJSON.view.Node({
		el : $('#jsonProdotto'),
		data : prodotto
	}).expandAll();

});