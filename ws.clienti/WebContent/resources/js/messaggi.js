var messaggiIta = {
		index : { 
			titolo = 'Logica - Web Services',
			sottotitolo = "Qui sono ospitati i web services utilizzati da Logica.<br>Di seguito viene riportato l'indice della documentazione.",
			lista = {
					autenticazione = "Autenticazione ai servizi",
					prodotti = "Prodotti",
					carichi = "Carichi",
					fornitori = "Fornitori",
					ordini = "Ordini"
			},
			info = 'Per maggiori info contattare <a href="mailto:support@ltc-logistics.it">support@ltc-logistics.it'
		},
		autenticazione : {
			titolo = "Autenticazione ai web services",
			indietro = "torna all'indice",
			testo = "I web services necessitano l'autenticazione dell'utente per poter essere usati.<br>Ogni risorsa rest può essere acceduta sfruttando il metodo <a href=\"https://it.wikipedia.org/wiki/Basic_access_authentication\">basic authentication</a>.<br>In pratica basta aggiungere alla chiamata HTTP l'header <b>Authorization</b> valorizzandolo con la stringa <i>Basic username:password</i> codificata in base64.<br><br>Esempio: se il client utilizza 'Aladdin' come username e 'open sesame' come password, l'header è formato nel seguente modo: \"Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==\"",
			testo2 = "Attualmente le risorse vengono accedute tramite protocollo HTTP ma ci stiamo strutturando per poter sfruttare il protocollo HTTPS aumentando così il livello di sicurezza.<br>Quando questa transizione sarà pronta sarà necessario cambiare solo il tipo di protocollo per le chiamate, il metodo di autenticazione rimarrà il medesimo.",
			testo3 = "Per maggiori informazioni o supporto per l'autenticazione potete mandare una mail a <a href=\"mailto:support@ltc-logistics.it\">support@ltc-logistics.it</a>."
		},
		ordine : {
			
		}	
};
var messaggiEng = {};
