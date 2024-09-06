# COSE DA FARE
-[ ] implementare ricerca e filtro in proposte tecnico
-[ ] storico tecnico order by id (non mi fa impazzire che ci sia la data)

## Giulia:
- [x] Togliere INVIATA in StatoProposta
- [x] Far matchare gli enum col db
- [x] Cercare di togliere la sottolineatura dalle scritte delle card
- [x] Inserire una **richiesta** d'acquisto come "ordinata"
- [x] Controllare lo spazio tra le card nella home tecnico
- [x] Modificare il resize delle righe
- [x] Sistemare la dimensione della card-richieste-storico
- [x] Aggiungere data alla proposta acquisto
- [x] Finire visualizzazione proposte tecnico (mostrare l'username dell'utente)
- [x] Implementare la ricerca in proposte, richieste e storico
- [x] Implementare il filtro in proposte, richieste e storico
- [x] Rivedere PropostaAcquisto_DAO, mancano data (si può risolvere con la data di default) ma sopratuttto codiceProdotto
- [x] Risolvere l'errore del trim
- [x] Togliere la data della proposta dello storico
- [x] Risolvere il problema del codice richiesta nelle email
- [x] Sort delle richieste, proposte e ordini ordinante
- [x] Impostazioni tecnico
- [x] Fixare i colori nello storico e mettere hover
- [ ] Aggiustare button nuove richieste e pagina notifiche_tecnico
- [x] Sistemare (soprattutto colore stato) detailstorico_tecnico

## Gea:
- [ ] rivedi css gestionecatgorie
- [x] email
- [x] login (hash password etc)
- [x] header + footer
- [x] aggiusta modifica categoria

## Samanta:
-[ ]: ALERT per "andata a buon fine!"
-[ ]: css vari

PROBLEMI CONSOLE SUL BROWSER:
Compatibilità per chrome android 
-[ ]: -ms-text-size-adjust' dentro normalize.css --> text-size-adjust
-[ ]: -webkit-text-size-adjust: 100%; --> text-size-adjust
ProposteOrdinante:
-[ ]: Select element must have an accessible name: Element has no title attribute
     <select name="status" id="status" class="filter-select">
-[ ]: A 'set-cookie' header doesn't have the 'secure' directive.
    Set-Cookie: JSESSIONID=A8AE906B4801FBB31A970EE29DD5FF4D; Path=/WebMarket; HttpOnly



WARNING:
-[ ]: homepageordinante: 'charset' meta element was not specified.
-[ ]: Resource should use cache busting but URL does not match configured patterns.

# ALTRO
- [Figma dell'admin](https://www.figma.com/file/c6hYZIz2AJhbynd5b2UdG1?node-id=0-1&t=uapcU6HHGd49wdL5-0&type=whiteboard)
- [Figma del tecnico](https://www.figma.com/board/0ZPJHIJs8QkLfjyrD49TUD/Prototipo-tecnico?node-id=0-1&t=G3JxyWIsZNVApCBr-1)
- [Figma dell'ordinante](https://www.figma.com/board/Hdn2GgIKPyrXPcuaPm8Rmh/Bozza-Ordinante?node-id=0-1&t=Zk8EPDTFiqD4A2yj-0)

# COSE A CUI PENSARE
-[ ] inviare la mail nella creazione utente richiede molto tempo