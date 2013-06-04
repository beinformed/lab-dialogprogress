package org.uva.predictions;

public class FormQuestion {
	private int id;
	private String answer;
	private boolean isNecessary;
}


/*

* elk formulier heeft een unieke ID
* een observatie is het invullen van 1 formulier in 1 sessie
* een vraag heeft een binnen een formulier unieke id, een antwoord 
  (leeg indien geen antwoord) en een flag die aangeeft of de vraag 
  in de toekomst nog gesteld gaat worden
* op elke timestamp (relatief tov het begin van een observation) verandert het antwoord/de flag van een of meer vragen

de data ziet er dan in csv formaat als volgt uit:

observation_id, form_id, timestamp, question_id, answer   , is_necessary
0             , 0      , 0        ,  0         , Antwoord , false         # deze vraag is beantwoord
0             , 0      , 0        ,  21        ,          , false         # op basis van vraag 0 kon deze vraag worden weggestreept
0             , 0      , 1        ,  1         , Antwoord , false         # deze vraag is beantwoord
1             , 5      , 0        ,  0         , Antwoord , false         # Nieuwe observation

Verder moet er een tabel zijn met het totaal aantal vragen per formulier:
form_id, total_questions
0      , 50
1      , 25

*/