package org.uva.predictions;

import java.util.Map;
import java.util.HashMap;

public class NgramCount {

	Map<String, HashMap<String, Integer>> counts;
	public NGramCount( Iterable<Observation> data, int n, ValueType type ){
		
	}

	public HashMap<String,Integer> getNgramCounts( ValueType type){
		
	}

	private void parseData( Iterable<Observation> data, ValueType type ){
		
		for ( Observation obs : data ){
			String form = obs.getForm();
			Iterable<Question> questions = obs.getQuestions();
			
			setNGrams( form, questions, n );
		}

	private void setNGrams(String form, Iterable<Question> questions, int n){
			ArrayList<String> questArray = new ArrayList<String>();
			for (Question q : Questions ){
				switch (type) {
					case ValueType.Question : questArray.add( q.getId() );
					case ValueType.Answer : questArray.add(q.getAnswer() );
				}
			
			for( int i = 0 ; i < questAray.size(); i++ ){
				String nGram = questArray.()
				if( !counts.containsKey( form ))
					counts.put(form, questAray)
				
				
	}
}











/**
/* class NGramModelNew
 * NB: code bevat redundantie: deze class maakt modellen voor alle 1 <= N <= maxN.
 * maxN heet in de code numberOfWords en wordt ge•nitialiseerd in de constructor.
 * Voor trigrammen maakt dit niet uit omdat sowieso tabellen voor bigrammen en unigrammen
 * moeten worden opgesteld. Als je hexagrammen wilt berekenen kost dit wat wel meer tijd.
 * Dit is handig als er backoff moet worden ge•mplementeerd.
 *
public class NGramCount {
	
	private enum TypeOfProbability {
		Unsmoothed, KatzSmoothed, GoodTuringSmoothed
	}
	
	// de main methode voert de vragen uit de opracht uit
	public static void main(String[]args) {
		// test file en train file: dit zijn standaardwaarden die worden gebruikt als args[0] == __standards
		String test = "/Users/Mart/Desktop/MART/school/uva/semester VI/nti/workspace/NGramSmoothed/src/testset.txt";
		String train = "/Users/Mart/Desktop/MART/school/uva/semester VI/nti/workspace/NGramSmoothed/src/trainset.txt";
		int gram = 2;
		
		if ( args.length < 1 ) {
			System.out.println("Gebruik: java NGramModel trainfile testfile NGram");
			return;
		} else if ( args.length < 3 && !args[0].equals("__standards") ) {
			System.out.println("Gebruik: java NGramModel trainfile testfile NGram");
			return;
		} else if ( !args[0].equals("__standards") ) {
			train = args[0];
			test = args[1];
			gram = Integer.parseInt(args[2]);
		}		 
		
		// maak een NGramModel
		NGramModelNew ngt = new NGramModelNew( gram, train, 5 );
		BufferedReader fr = null;
		
		// lees alle regels in het testbestand en bepaal per regel de waarschijnlijkheid,
		// gebaseerd op het ngrammodel. 
		try {
			String line;
			fr = new BufferedReader( new FileReader(test) );
			
			System.out.println("De gesmoothe waarschijnlijkheden van de zinnen in de testset:");
		
			int counter = 0, counter0 = 0, cutoff = 5;

			// print de ongesmoothe waarschijnlijkheden van de zinnen die een
			// log(P) van -oneindig hebben (dus P = 0)
			while ( ( line = fr.readLine()) != null )
			{
				counter++;
				double unsmoothedProb = ngt.probabilityOfSentence(line, gram, TypeOfProbability.Unsmoothed);
				if ( unsmoothedProb <= Double.NEGATIVE_INFINITY && (++counter0 <= cutoff | cutoff == 0) )
					System.out.println( line + "; P = " + unsmoothedProb );
			}
			
			// percentage van 0 kansen
			System.out.println("Percentage Log(P(S)) = -Infinity: " + (double)counter0/(double)counter + "; " + counter0 + " van de " + counter );
			
			fr.close();
			
			fr = new BufferedReader( new FileReader(test) );
			
			System.out.println("");
			System.out.println("De GT-gesmoothe waarschijnlijkheden van de zinnen in de testset:");
			
			// print de gesmoothe waarschijnlijkheden van de zinnen
			
			counter = counter0 = 0;
			
			while ( ( line = fr.readLine()) != null )
			{
				counter++;
				double smoothedProb = ngt.probabilityOfSentence(line, gram, TypeOfProbability.GoodTuringSmoothed);
				if ( smoothedProb <= Double.NEGATIVE_INFINITY && (++counter0 <= cutoff | cutoff ==0) )
					System.out.println( line + "; P = " + smoothedProb );
			}
			System.out.println("Percentage Log(P(S)) = -Infinity: " + (double)counter0/(double)counter + "; " + counter0 + " van de " + counter);
			
			fr.close();
			
			fr = new BufferedReader( new FileReader(test) );
			
			System.out.println("");
			System.out.println("De Katz-gesmoothe waarschijnlijkheden van de zinnen in de testset:");
			
			// print de gesmoothe waarschijnlijkheden van de zinnen
			
			counter = counter0 = 0;
			
			while ( ( line = fr.readLine()) != null )
			{
				counter++;
				double smoothedProb = ngt.probabilityOfSentence(line, gram, TypeOfProbability.KatzSmoothed);
				if ( smoothedProb <= -20 && (++counter0 <= cutoff | cutoff == 0) )
					System.out.println( line + "; P = " + smoothedProb );
			}
			System.out.println("Percentage Log(P(S)) = < -20: " + (double)counter0/(double)counter + "; " + counter0 + " van de " + counter);
			
		} // try
		catch ( FileNotFoundException e )
		{
			System.out.println("Bestand \"" + test + "\" bestaat niet.");
		}
		catch ( IOException e )
		{
			System.out.println("lezen van bestand gaat mis.");
		}
		finally
		{
			try {
				if ( fr != null )
					fr.close();
			} catch (Exception e){}
			finally {}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// Hier begint de werkelijke class declaratie
	
	private int numberOfWords;
	private String trainFile;
	private int K;
	
	private HashMap<String, Integer> nGrams[];
	// voor de onderstaande data worden arrays gebruikt ipv maps of hashmaps.
	// dit levert een veel betere prestatie op dan het gebruik van hashmaps,
	// maar kost wel aanzienlijk meer ruimte als het een groot corpus betreft.
	// gelukkig is het OVIS corpus niet zo groot
	// otherCounts[n][0] is het aantal mogelijke (n+1)grammen
	// otherCounts[n][1] is het totaal aantal geobserveerde (n+1)grammen
	// otherCounts[n][2] is de maximale count van alle (n+1)grammen
	// --- algemene data
	private double[][] otherCounts;
	private int[][] countsOfCounts;
	// linRegConstants[n][0] = alpha voor (n+1)gram
	// linRegConstants[n][1] = beta voor (n+1)gram
	// private double[][] linRegConstants; 
	
	// --- gesmoothe data
	// voor een N-gram wordt in uniqueNGramsCount bijgehouden wat
	// sum( count*(w_(i-N+1),...,w_(i-1), w) ) voor alle w in V + </s> is.
	// Dit is sneller dan elke keer opnieuw die som te berekenen. 
	private HashMap<String, Double[]> uniqueNGramsCount[]; // de Count*()s
	// de Double[] bevat de volgende waarden:
	// [0] = katzgesmoothe
	// [1] = gtgesmoothe
	private double[][] katzSmoothedCounts;
	private double[][] gtSmoothedCounts;
	
	//////////////////////////////////////////////////////////////////////////////
	// convenience methods voor String[] <--> String
	
	// voegt array met woorden samen tot een string, woorden gescheiden door spaties
	public static String StringFromArray( String[] in) {
		String out = "";
		for ( int i = 0; i < in.length-1; i++ )
			out += in[i] + " ";
		out += in[in.length-1];
		return out;		
	} // einde StringFromArray()
	
	// maakt een array van de string, verwijdert alle dubbele spaties en spaties aan begin/eind ve woord
	// split string dan bij elke enkele spatie. returnt array met woorden
	public static String[] StringToArray( String in ) {
		return in.replaceAll("  *", " ").replaceAll("^ +", "").replaceAll(" +$", "").split(" ");
	} // einde StringToArray()
	
	//////////////////////////////////////////////////////////////////////////////
	// methodes voor het parsen van een file
	
	// voeg een NGram toe aan de nGrams hashtable
	private void addItem( String[] nGram, int order ) {
		String key = StringFromArray(nGram);
		Integer value;
		otherCounts[order][1]++; // een nieuwe nGram voor order
		if ( (value = nGrams[order].get(key)) == null )
			nGrams[order].put( key, 1 );
		else {
			nGrams[order].put( key, value + 1 );
			// ervan uitgaande dat er counts > 1 zijn
			if ( value + 1 > otherCounts[order][2] )
				otherCounts[order][2] = value + 1;
		}
	} // einde addItem
	
	private void parseFile() {
		BufferedReader fr = null;
		
		// begin met het leegmaken van alle ngram hashmaps
		for ( int i = 0; i < numberOfWords; i++ )
			nGrams[i].clear();
			
		try {
			// parse alle zinnen in de train file voor alle ngram groottes tot
			// de maximale ngram grootte opgegeven in de constructor 
			for ( int i = 0; i < numberOfWords; i++ ) {
				otherCounts[i][1] = 0; // totalCount -> totaal aantal geziene tokens
				otherCounts[i][2] = 0; // maxCounts
				fr = new BufferedReader( new FileReader(trainFile) );
				String line, prefix = "", postfix = "";
				
				// voeg start-of-sentence en end-of-sentence symbolen toe
				if ( i > 0 )
					postfix = " </s>";
				for ( int j = 0; j <= i; j++ )
					prefix += "<s> ";
				
				// de String array waarin de ngram steeds wordt gekopi‘erd
				String[] tmp_ngram = new String[i];
				
				// lees elke regel in het bestand, voeg de start en stopsymbolen toe,
				// maak de bigrammen en unigrammen
				while ( (line = fr.readLine()) != null ) {					
					// array met elk woord in de inputline als een item
					String words[] = StringToArray(prefix + line + postfix);
					
					// words[words.length-1] = "</s>"
					for ( int j = 0; j < words.length; j++ ) {
						
						// als er nog ruimte is om een bigram te kopi‘ren, doe dit dan
						if ( j <= words.length - i - 1) {
							tmp_ngram = Arrays.copyOfRange( words, j, j + i + 1 );
							// als tmp_ngram een nieuwe is, is newlyAdded waar, anders niet
							addItem( tmp_ngram, i );
						} // i <= words.length etc
						
					} // einde for-loop -> regel
				} // einde tekstbestand
				fr.close();
				
			} // einde for loop
			
		} catch( IOException e ) {
			System.out.println("ERRRORRRR: " + e);
		} // einde catch
		finally { try { if ( fr != null ) fr.close(); } catch( IOException e ){}
		} // einde finally
	} // einde parseFile
	
	//////////////////////////////////////////////////////////////////////////////
	// methods voor het tellen van frequentie van frequenties en smoothen
	
	// telt voor elke count hoe vaak deze voorkomt en slaat dit op in een array
	// keuze voor array is in dit geval: relatief klein aantal counts, weinig
	// ruimte en veel grotere snelheid bij opzoeken
	private void getCounts() {
		for ( int i = 0; i < numberOfWords; i ++ ) {
			// een extra voor 0, een voor maxCount + 1 (voor Nc+1)
			countsOfCounts[i] = new int[ (int)otherCounts[i][2] + 2 ];
			gtSmoothedCounts[i] = new double[ (int)otherCounts[i][2] + 2 ];
			Iterator<String> it = nGrams[i].keySet().iterator();
			// loop door de hashTable en verhoog de cOC voor elke count
			while ( it.hasNext() ) {
				Integer count = nGrams[i].get( it.next() );
				countsOfCounts[i][ count ]++;
			} // einde while loop
		} // einde for loop
	} // einde getCounts() 
	
	// berekent het aantal mogelijke nGrammen voor een orde n, gebaseerd
	// op de aanname van een gesloten vocabulair.
	// |V_n| = (V_1)^n + 2*(V_1)^(n-1) + 2*(V_1)^(n-2) + ... + 2 * (V_1)
	private void createVocabs() {
		otherCounts[0][0] = nGrams[0].size();
		for ( int i = 1; i < numberOfWords; i++ ) {
			otherCounts[i][0] = Math.pow(otherCounts[0][0], i + 1 );
			for ( int j = 0; j < i; j++ )
				otherCounts[i][0] += 2 * Math.pow(otherCounts[0][0], j + 1);
		} // einde for
	} // einde createVocabs
	
	private void smootheCounts() {
		// vocabulaire is gesloten, dus unigrammen hoeven niet
		// gesmoothe te worden
		for ( int i = 1; i < numberOfWords; i++ ) {
			double unseen = otherCounts[i][0] - nGrams[i].size(), // aantal ongeziene
					// NGrammen is gelijk aan alle mogelijke NGrammen - het aantal gezien
					// ngrammen
					N1 = countsOfCounts[i][1],
					NkPlus1 = countsOfCounts[i][K + 1],
					k_ = (double)K;// N_0 = unseen
			
			// katz smoothing
			for ( int c = 0; c <= K; c++ ) {
				double Nc = countsOfCounts[i][c],
						NcPlus1 = countsOfCounts[i][c + 1],
						c_ = (double)c, cStar;
				if ( c > 0 ) {
					double cStarPart1 = (c_ + 1.)*(NcPlus1/Nc),
					cStarPart2 = c_*( ((k_+1.) * NkPlus1 ) / N1 ),
					cStarPart3 = 1 - ( ((k_+1.) * NkPlus1) / N1 );
					cStar = (cStarPart1 - cStarPart2)/cStarPart3;
				} else
					cStar = N1 / unseen;
				katzSmoothedCounts[i][c] = cStar;				
			} // einde for loop - katz smoothing
			
			gtSmoothedCounts[i][0] = N1 / unseen;
			
			// good-turing smoothing
			for ( int c = 1; c <= otherCounts[i][2]; c++ ) {
				double Nc = countsOfCounts[i][c],
						NcPlus1 = countsOfCounts[i][c+1];
				gtSmoothedCounts[i][c] = NcPlus1 == 0. || Nc == 0. ? 0. : (c + 1.) * (NcPlus1/Nc);
			} // einde for
		} // einde for
	} // einde smootheCounts()
	
	// hier worden uniqueNGramsCount en uniqueNGramsCountGT ingevuld. Dit gebeurt als volgt:
	// voor elke N <= numberOfWords:
	//    doorloop de keyset van de Ngram hashmap
	//    voor elke NGram maak een (N-1)gram van de N-1 eerste woorden
	//    hoog de uniqueNGramsCount hashmaps op met de count van de N-1 gram
	//    houdt voor elke N-1gram bij hoe vaak deze voor komt in de keyset: dit bepaalt
	//      het aantal geziene N-1grams, en is nodig om te bepalen hoeveel ongezien N-1grams
	//      er zijn.
	//    op basis van het aantal ongeziene N-1grams: bereken de count* van de unieke N-1grams 
	private void getUniqueCounts() {
		for ( int i = 1; i < numberOfWords; i++ ) {
			HashMap<String, Integer> rawCounts = new HashMap<String, Integer>();
			Iterator<String> it = nGrams[i].keySet().iterator();
			while ( it.hasNext() ) {
				// in deze loop: 
				// - count verwijst naar hogere NGrams
				// - value verwijst naar lagere NGrams
				String nGram = it.next();
				String[] nGramArr = StringToArray(nGram);
				Integer count = nGrams[i].get(nGram);
				String key = StringFromArray( Arrays.copyOfRange(nGramArr, 0, i) );
				double smoothedCount = count > K ? count.doubleValue() : 
					katzSmoothedCounts[i][count];
				double smoothedCountGT = gtSmoothedCounts[i][count];
				// check of de kleine NGram al eens is geteld
				Double[] value = uniqueNGramsCount[i-1].get(key);
				
				// zo niet, voeg dan de huidige count to 
				if ( value == null ) {
					rawCounts.put( key, 1 );
					Double[] tmpc = {smoothedCount, smoothedCountGT};
					uniqueNGramsCount[i-1].put( key, tmpc );
				} else {
					rawCounts.put( key, rawCounts.get(key) + 1);
					value[0] += smoothedCount;
					value[1] += smoothedCountGT;
					uniqueNGramsCount[i-1].put( key, value );
				}
			} // einde while
			
			it = uniqueNGramsCount[i-1].keySet().iterator();
			while ( it.hasNext() ) {
				String key = it.next();
				double unseen = otherCounts[0][0] - rawCounts.get(key);
				Double seen[] = uniqueNGramsCount[i-1].get(key);
				seen[0] += unseen * katzSmoothedCounts[i][0];
				seen[1] += unseen * gtSmoothedCounts[i][0];
				uniqueNGramsCount[i-1].put( key, seen );
			} // einde while
			
		} // einde for
	} // einde getUniqueCounts
	
	//////////////////////////////////////////////////////////////////////////////
	// Constructor, vanaf hier is alles public
	
	// constructor: numberOfWords moet groter dan 1 zijn
	@SuppressWarnings("unchecked")
	public NGramModelNew( int nOW, String tf, int k )
	{
		numberOfWords = nOW;
		K = k;
		trainFile = tf;
		
		if ( numberOfWords <= 1 ) {
			System.out.println("numberOfWords moet groter dan 1 zijn! geen initalisatie");
			return;
		} // numberOfWord <= 1
		
		// initialisatie van de hashmaps en de array
		
		katzSmoothedCounts = new double[numberOfWords][];
		gtSmoothedCounts = new double[numberOfWords][];
		nGrams = new HashMap[numberOfWords];
		countsOfCounts = new int[numberOfWords][];
		uniqueNGramsCount = new HashMap[numberOfWords - 1];
		otherCounts = new double[numberOfWords][];
		
		for ( int i = 0; i < numberOfWords; i++ ) {
			nGrams[i] = new HashMap<String, Integer>();
			katzSmoothedCounts[i] = new double[K + 1];
			otherCounts[i] = new double[3];
			if ( i < numberOfWords - 1 ) {
				uniqueNGramsCount[i] = new HashMap<String, Double[]>();
			}
		}
		
		parseFile(); // extraheer de NGrammen uit het trainingsbestand
		createVocabs(); // bepaal het aantal mogelijke NGrammen gebaseerd op
						// de grootte van V
		getCounts();	// bepaal de frequenties van de frequenties
		smootheCounts(); // smoothe de frequenties, volgens GT en Katz
		getUniqueCounts(); // bereken hoeveel unieke NGrammen er zijn voor 
						   // elke (n-1)gram, n > 1
		
	} // einde constructor
	
	public NGramModelNew( String tf ) {
		this( 2, tf, 5 );
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// Hier worden de NGram probabilities berekend	
	
	public double probabilityOfNGram( String[] ngram, TypeOfProbability t ) {
		int n = ngram.length - 1;
		// check of de NGram een acceptabele lengte heeft
		if ( n < 0 || n >= numberOfWords )
			return 0;
		
		Integer count = nGrams[n].get(StringFromArray(ngram));
		double smoothedCount;
		if ( count == null ) { // NGram is nog niet gezien
			if ( t == TypeOfProbability.Unsmoothed )
				return 0; // niet gezien + ongesmoothe betekent P = 0
			else // 0* is hetzelfde voor katz en gt
				smoothedCount = katzSmoothedCounts[n][0];
		}
		else if ( t == TypeOfProbability.KatzSmoothed && count <= K  ) {
			smoothedCount = katzSmoothedCounts[n][count];
		}  else if ( t == TypeOfProbability.GoodTuringSmoothed )
			smoothedCount = gtSmoothedCounts[n][count];
		else
			smoothedCount = count;
		
		// n = 0, V is gesloten dus we hoeven niet verder te zoeken
		if ( n == 0 )
			return smoothedCount / otherCounts[0][1];
		
		// haal hier de al dan niet gesmoothe counts op van de (n-1)gram
		String shorterNGram = StringFromArray(Arrays.copyOfRange( ngram, 0, n ));
		Double[] allCounts;
		Double denominator;
		Integer allCountsI;
		
		if ( t == TypeOfProbability.Unsmoothed ) {
			allCountsI = nGrams[n-1].get(shorterNGram);
			if ( allCountsI == null )
				denominator = 0.;
			else
				denominator = allCountsI.doubleValue();
		} else {
			allCounts = uniqueNGramsCount[n-1].get(shorterNGram);
			if ( allCounts == null )
				denominator = 0.;
			else if ( t == TypeOfProbability.GoodTuringSmoothed )
				denominator = allCounts[1];
			else
				denominator = allCounts[0];
		}
		
		// komt niet voor als t = TypeOfProbability.Unsmoothed
		if ( denominator == 0 ) 
			// de n-1 gram komt niet voor, dus de denominator is dan
			// het aantal mogelijke ngrammen beginnend met de n-1gram
			// maal de gesmoothe 0 count
			denominator = (otherCounts[n][0] + 1) * gtSmoothedCounts[n][0];
		
		if ( denominator == 0. ) // kan voorkomen in uniqueNGramsCountGT
			return 0.;
		else
			return smoothedCount/denominator;		
	} // einde probabilityOfNGram

	public double probabilityOfNGram( String[] ngram  ) {
		return probabilityOfNGram( ngram, TypeOfProbability.KatzSmoothed );
	}
	
	public double probabilityOfNGram( String ngram  ) {
		return probabilityOfNGram( ngram, TypeOfProbability.KatzSmoothed );
	}
	
	public double probabilityOfNGram( String ngram, TypeOfProbability t ) {
		return probabilityOfNGram( StringToArray(ngram), t );	
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// Hier worden de sentence probabilities uit de NGram probabilities berekend
	
	// berekent de log P van een zin gebaseerd op de NGrammen in die zin
	public double probabilityOfSentence( String[] sentence, int n, TypeOfProbability t ) {
		double resultProb = 0;
		
		if ( n > numberOfWords || n <= 0 || n > sentence.length )
			return Double.NaN;
		
		if ( n > 1 && !sentence[0].equals("<s>") ) {
			String[] sentence_tmp = new String[sentence.length + n];
			for ( int i = 0; i < n - 1; i++ )
				sentence_tmp[i] = "<s>";
			for ( int i = 0; i < sentence.length; i++ )
				sentence_tmp[i + n - 1] = sentence[i];
			sentence_tmp[sentence.length + n - 1] = "</s>";
			sentence = sentence_tmp;
		} // einde if
		
		double tmp_prob;
		for ( int i = 0; i < sentence.length - n; i++ ) {
			String[] tmp = Arrays.copyOfRange(sentence, i, i + n);
			tmp_prob = probabilityOfNGram(tmp, t);
			
			if ( tmp_prob == 0 )
				return Double.NEGATIVE_INFINITY;
			else
				resultProb += Math.log(tmp_prob);
		} // einde for-loop
		
		return resultProb;
	} // ProbabilityOfSentence
		
	public double probabilityOfSentence( String sentence ) {
		return probabilityOfSentence( sentence, numberOfWords, TypeOfProbability.KatzSmoothed );
	}
	
	public double probabilityOfSentence( String sentence, int n ) {
		return probabilityOfSentence( sentence, n, TypeOfProbability.KatzSmoothed );
	}
	
	public double probabilityOfSentence( String sentence, TypeOfProbability t ) {
		return probabilityOfSentence( sentence, numberOfWords, t );
	}
	
	// neemt een string als zin en voegt de begin en eindsymbolen toe
	public double probabilityOfSentence( String sentence, int n, TypeOfProbability t ) {
		if ( n <= 0 || n > numberOfWords )
			return Double.NaN;
		
		for ( int i = 0; i < numberOfWords - 1; i++ )
			sentence = "<s> " + sentence;
		if ( numberOfWords > 1 )
			sentence += " </s>";
		return probabilityOfSentence( StringToArray(sentence), n, t );
	} // probabilityOfSentence
	
	public double probabilityOfSentence( String[] sentence ) {
		return probabilityOfSentence( sentence, numberOfWords, TypeOfProbability.KatzSmoothed );
	}
	
	public double probabilityOfSentence( String[] sentence, int n ) {
		return probabilityOfSentence( sentence, n, TypeOfProbability.KatzSmoothed );
	}
	
	public double probabilityOfSentence( String[] sentence, TypeOfProbability t ) {
		return probabilityOfSentence( sentence, numberOfWords, t );
	}
	
} // einde NGramModel class
**/
