/**
 * Qui sono contenuti gli oggetti JSON d'esempio per la documentazione dei Web Services per i fornitori.
 */

var utenza = {
	username : "test",
	nome : "Testing",
	cognome : "O'Tester",
	email : "support@ltc-logistics.it",
	sedi : [1],
	commesse : [30, 32, 40, 41],
	permessi : [1],
	features : []
};

$(document).ready(function() {
	
	var nodoUtenza = new PrettyJSON.view.Node({ el:$('#jsonUtenza'), data: utenza }).expandAll();

});