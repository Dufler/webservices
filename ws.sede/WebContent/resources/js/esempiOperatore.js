/**
 * Qui sono contenuti gli oggetti JSON d'esempio per la documentazione dei Web Services per gli operatori.
 */

var attivita = {
	id : 1,
	utente : "paolino.paperino",
	assegnatoDa : "paperone.depaperoni",
	commessa : 1,
	priorita : "URGENTE",
	tipo : "CARICO",
	riferimento : "DDT 123",
	note : "Porta la massima cura nipote!"
};

$(document).ready(function() {
	
	var nodoAttivita = new PrettyJSON.view.Node({ el:$('#jsonAttivita'), data: attivita }).expandAll();

});