function Campo(nome, richiesto, tipo, descrizione) {
	this.nome = nome;
	this.richiesto = richiesto;
	this.tipo = tipo;
	this.descrizione = descrizione;
}

function Risposta(dato, codiceHttp, tipo, descrizione) {
	this.dato = dato;
	this.codiceHttp = codiceHttp;
	this.tipo = tipo;
	this.descrizione = descrizione;
}

// Creazione del modulo Angular, app principale.
var ltcApp = angular.module('ltcApp', ['ngSanitize']);

ltcApp.run(function($rootScope) {});

//Controller principale e inject Angular's $scope
ltcApp.controller('mainController', function($scope) {

	// Variabili condivise in tutta la app
	

});

ltcApp.controller('tipiCaricoController', function($scope) {
	
	$scope.risposte = [new Risposta('<a href="#tipocarico">tipi di carico</a>', 200, 'Array di JSON', 'Un array di oggetti JSON che rappresentano i tipi di carico.')];
	
});
	
ltcApp.controller('tipoCaricoController', function($scope) {
	
	$scope.campi = [new Campo('codice', 'N/A', 'testo', 'Il codice che identifica la tipologia di carico. Questo valore appare nel campo <strong>tipo</strong> del <a href="#carico">carico</a>.'), new Campo('descrizione', 'N/A', 'testo', 'Descrizione testuale della tipologia di carico.')];

});

ltcApp.controller('cercaCarichiController', function($scope) {
	
	$scope.risposte = [new Risposta('<a href="#ingresso">Ingressi</a>', 200, 'Array di JSON', "Un array di oggetti JSON che contiene gli ingressi cercati."), new Risposta("Insieme vuoto", 204, "Array vuoto", "Un array vuoto nel caso in cui non ci siano carichi con le caratteristiche desiderate."), new Risposta("Errore", 400, 'Oggetto JSON', "L'oggetto JSON che contiene le informazioni relative agli errori riscontrati durante l'elaborazione della richiesta.")];
	$scope.campi = [new Campo('<a href="#ingresso">Ingresso</a>', 'facoltativo', 'Oggetto JSON', "L'oggetto JSON che contiene alcuni dei campi su cui filtrare.<br>&Egrave; possibile filtrare per tipo, stato, riferimento e fornitore.")];

});

ltcApp.controller('cercaCaricoController', function($scope) {
	
	$scope.risposte = [new Risposta('<a href="#carico">Carico</a>', 200, 'Oggetto JSON', "L'oggetto JSON che contiene tutte le informazioni sul carico."), new Risposta("Errore", 400, 'Oggetto JSON', "Nel caso in cui non esiste un carico con l'ID specificato.")];
	$scope.campi = [new Campo('ID', 'necessario', 'numerico intero', "L'ID del carico desiderato, va passato nella URL.")];

});

ltcApp.controller('nuovoColloController', function($scope) {
	
	$scope.risposte = [new Risposta('<a href="#collo">Collo</a>', 201, 'Oggetto JSON', "L'oggetto JSON che rappresenta il collo appena inserito"), new Risposta("Errore", 400, 'Oggetto JSON', "L'oggetto JSON che contiene le informazioni relative agli errori riscontrati durante l'elaborazione della richiesta.")];
	$scope.campi = [new Campo('<a href="#collo">Collo</a>', 'necessario', 'Oggetto JSON', "L'oggetto JSON che rappresenta il collo e il contenuto che sta per essere inserito. Il campo ID verr&agrave; valorizzato automaticamente dal sistema e si trover&agrave; nella risposta.")];

});

ltcApp.controller('aggiornaColloController', function($scope) {
	
	$scope.risposte = [new Risposta('<a href="#collo">Collo</a>', 200, 'Oggetto JSON', "L'oggetto JSON che rappresenta il collo da modificare.<br>Il campo id deve essere valorizzato."), new Risposta("Errore", 400, 'Oggetto JSON', "L'oggetto JSON che contiene le informazioni relative agli errori riscontrati durante l'elaborazione della richiesta.")];
	$scope.campi = [new Campo('<a href="#collo">Collo</a>', 'necessario', 'Oggetto JSON', "L'oggetto JSON che rappresenta il collo e il contenuto che &egrave; stato aggiornato.")];

});

ltcApp.controller('eliminaColloController', function($scope) {
	
	$scope.risposte = [new Risposta('<a href="#collo">Collo</a>', 200, 'Oggetto JSON', "L'oggetto JSON che rappresenta il collo da eliminare.<br>basta valorizzare i campi id e carico."), new Risposta("Errore", 400, 'Oggetto JSON', "L'oggetto JSON che contiene le informazioni relative agli errori riscontrati durante l'elaborazione della richiesta.")];
	$scope.campi = [new Campo('<a href="#collo">Collo</a>', 'necessario', 'Oggetto JSON', "L'oggetto JSON che rappresenta il collo e il contenuto che &egrave; stato eliminato.")];

});

ltcApp.controller('colloController', function($scope) {
	
	$scope.campi = [
		new Campo('id', 'N/A', 'numerico intero', "L'ID del collo, viene valorizzato automaticamente dal sistema durante l'inserimento ed &egrave; necessario indicarlo in fase di aggiornamento o eliminazione."),
		new Campo('carico', 'necessario', 'numerico intero', "L'ID del carico a cui appartiene la merce."),
		new Campo('magazzino', 'necessario', 'testo', "La codifica del magazzino su cui caricare la merce. Questo valore pu&ograve; essere modificato successivamente."),
		new Campo('barcodeCliente', 'facoltativo', 'testo', "Il barcode che identifica il collo nei sistemi del cliente. Potrebbe essere fisicamente stampato sul collo."),
		new Campo('etichetta', 'N/A', "La stringa ZPL che contiene i comandi per stampare l'etichetta.")
		];

});

ltcApp.controller('prodottoController', function($scope) {
	
	$scope.campi = [
		new Campo('id', 'necessario', 'numerico intero', "L'ID del riscontro, viene valorizzato automaticamente dal sistema in inserimento e viene usato per parametrizzare le chiamate di aggiornamento ed eliminazione."),
		new Campo('carico', 'necessario', 'numerico intero', "L'ID del carico a cui appartiene la merce."),
		new Campo('riga', 'necessario', 'numerico intero', "L'ID della riga di carico a cui appartiene la merce."),
		new Campo('collo', 'necessario', 'numerico intero', "L'ID del collo creato precedentemente."),
		new Campo('prodotto', 'necessario', 'numerico intero', "L'ID del prodotto."),
		new Campo('quantita', 'necessario', 'numerico intero', "La quantit&agrave; di prodotto presente."),
		new Campo('seriali', 'facoltativo', 'array di stringhe', "Un array di stringhe che rappresentano i seriali del prodotto."),
		new Campo('forzaEccedenza', 'necessario', 'booleano', "Indica se va forzata l'eccedenza della quantit&agrave; per la riga specificata."),
		];

});

ltcApp.controller('caricoController', function($scope) {
	
	$scope.campi = [
		new Campo('<a href="#ingresso">ingresso</a>', 'necessario', 'Oggetto JSON', "L'oggetto JSON che rappresenta le informazioni sul carico, vedi sotto."),
		new Campo('<a href="#ingressodettaglio">dettagli</a>', 'necessario', 'Array di JSON', "Un array di oggetti JSON che rappresenta i dettagli dei prodotti presenti nel carico, vedi sotto."),
		new Campo('<a href="#documento">documento</a>', 'necessario', 'Oggetto JSON', "L'oggetto JSON che rappresenta le informazioni relative al documento di ordine.<br>Rappresenta il documento accompagnatorio della merce in arrivo presso LTC.")
		];

});

ltcApp.controller('ingressoController', function($scope) {
	
	$scope.campi = [
		new Campo('id', 'N/A', 'numerico intero', "L'ID dell'ingresso dentro i nostri sistemi. Tale valore viene attribuito in automatico al momento dell'inserimento e viene usato successivamente per parametrizzare la <a href=\"#dettaglicarico\">chiamata di dettaglio (ID)</a>."),
		new Campo('dataArrivo', 'facoltativo', 'data', "La data di arrivo prevista."),
		new Campo('fornitore', 'necessario', 'testo', "Il riferimento del fornitore della merce cos&igrave; come &egrave; stato indicato nell'inserimento sul <a href=\"http://test.services.ltc-logistics.it/clienti/fornitori.html\">web service per i fornitori</a>."),
		new Campo('pezziLetti', 'N/A', 'numerico intero', "La quantit&agrave; totale dei pezzi letti nel riscontro del carico. Questo campo viene valorizzato dai nostri sistemi. Qualsiasi valore in ingresso viene ignorato."),
		new Campo('pezziStimati', 'necessario', 'numerico intero', "La quantit&agrave; totale dei pezzi stimati nel carico in arrivo."),
		new Campo('riferimentoCliente', 'necessario', 'testo', "Chiave di comunicazione fra i sistemi per far riferimento al carico. E' consigliabile inserire qui il valore identificativo del documento di trasporto della merce.<br>Tale valore verr&agrave; usato per parametrizzare le chiamate.<br>Il numero massimo di caratteri consentiti &egrave; 45."),
		new Campo('tipo', 'necessario', 'testo', "Indica il tipo di carico.<br>I possibili valori sono: <strong>CARICO</strong>, <strong>RESO</strong>, <strong>CAMPIONARIO</strong>.<br>E' possibile concordare altri possibili valori se necessario."),
		new Campo('note', 'facoltativo', 'testo', "Note di testo facoltative per questo carico."),
		new Campo('stato', 'N/A', 'testo', "Indica lo stato d'avanzamento del carico.<br>I possibili valori sono: <strong>INSERITO</strong>, <strong>ARRIVATO</strong>, <strong>IN_LAVORAZIONE</strong>, <strong>LAVORATO</strong>, <strong>CHIUSO</strong>.<br>Non &egrave; possibile valorizzare questo campo.")
		];

});

ltcApp.controller('dettaglioController', function($scope) {
	
	$scope.campi = [
		new Campo('riferimento', 'N/A', 'testo', "L'identificativo del carico come passato dal cliente."),
		new Campo('riga', 'N/A', 'numerico intero', "L'ID della riga. Viene usato per parametrizzare le chiamate."),
		new Campo('collo', 'facoltativo', 'testo', "L'ID del collo dentro i vostri sistemi. (es. barcode) Se non presente tale valore viene attribuito in automatico al momento dell'inserimento usando un progressivo."),
		new Campo('prodotto', 'necessario', 'testo', "Il codice dell'articolo come definito dal cliente."),
		new Campo('idProdotto', 'necessario', 'N/A', "L'ID del prodotto che &egrave; stato attribuito automaticamente dai nostri sistemi.<br>Le chiamate vengono parametrizzate cone questo valore."),
		new Campo('magazzino', 'necessario', 'testo', "Il codice del magazzino LTC."),
		new Campo('quantitaPrevista', 'necessario', 'numerico intero', "La quantit&agrave; di prodotto prevista per la riga."),
		new Campo('quantitaVerificata', 'necessario', 'numerico intero', "La quantit&agrave; di prodotto effettivamente verificata per la riga."),
		new Campo('note', 'facoltativo', 'testo', "Campo di testo libero usato dal cliente."),
		new Campo('madeIn', 'facoltativo', 'testo', "Campo facoltativo per indicare il made in del prodotto nella riga.<br>Viene indicato usando la codifica ISO-3 per le nazioni."),
		new Campo('seriali', 'N/A', 'array di stringhe', "La lista dei seriali riscontrati per questa riga di documento.")
		];

});

ltcApp.controller('documentoController', function($scope) {
	
	$scope.campi = [
		new Campo('tipo', 'necessario', 'testo', "Il tipo di documento. I possibili valori sono: <strong>CARICO</strong>, <strong>RESO</strong>, <strong>ORDINE</strong>, <strong>CAMPIONARIO</strong>, <strong>LAVORAZIONE</strong>, <strong>ALTRO</strong>."),
		new Campo('riferimento', 'necessario', 'testo', "Il riferimento univoco del documento"),
		new Campo('data', 'necessario', 'data', "La data di emissione del documento.")
		];

});

