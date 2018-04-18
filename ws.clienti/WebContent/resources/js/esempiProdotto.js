/**
 * Qui sono contenuti gli oggetti JSON d'esempio per la documentazione dei Web Services per i prodotti.
 */

var prodotto = {
	    "cassa": "NO",
	    "chiaveCliente": "ABC-XL",
	    "codiceModello": "ABC",
	    "barcode": "ABCXL",
	    "taglia": "XL",
	    "colore": "Verde",
	    "descrizione": "Descrizione del prodotto.",
	    "composizione": "LANA",
	    "brand": "Armani",
	    "categoria": "APPESO",
	    "madeIn": "ITA",
	    "sottoCategoria": "Maglione",
	    "stagione": "AI17",
	    "valore": 100,
	    "h": 400,
	    "l": 400,
	    "z": 100,
	    "peso": 650,
	    "skuFornitore": "123XL",
	    "barcodeFornitore": "123XL",
	    "note": "Le note!"
};

$(document).ready(function() {
	
	var nodoProdotto = new PrettyJSON.view.Node({ el:$('#jsonProdotto'), data: prodotto }).expandAll();

});