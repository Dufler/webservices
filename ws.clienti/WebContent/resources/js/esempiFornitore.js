/**
 * Qui sono contenuti gli oggetti JSON d'esempio per la documentazione dei Web Services per i fornitori.
 */

var indirizzo = {
	ragioneSociale : "Best Dealer spa",
    indirizzo : "via Appia, 1",
    cap : "00127",
    localita : "Roma",
    provincia : "RM",
    nazione : "ITA",
    telefono : "06-123456",
    email : "info@bestdealer.it"	
};

var fornitore = {
	nome : "Best Dealer",
	riferimentoCliente : "F23",
	note : "Il miglior fornitore",
	indirizzo : indirizzo
};

$(document).ready(function() {
	
	var nodoFornitore = new PrettyJSON.view.Node({ el:$('#jsonFornitore'), data: fornitore }).expandAll();
	var nodoIndirizzo = new PrettyJSON.view.Node({ el:$('#jsonIndirizzo'), data: indirizzo }).expandAll();

});