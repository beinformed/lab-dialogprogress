package org.uva.predictions;


public class NGram{
	String[] ngram;

	public NGram( Iterable<String> g, int n, ValueType type  ){
		ngram = new String[n];
		int i = 0;
		for ( String gram : g)
			ngram[i++] = gram;
	}
	
	public NGram( String[] g){
		ngram = g;
	}	
	public String[] getGramArray(){
		return ngram;
	}
	
	@Override
	public boolean equals( Object  o ){
		NGram n = (NGram) o;
//		System.out.printf(" --- using equals on \n%s\n  %s\n\n", n.getGramArray(), getGramArray() );
		boolean ret = false;
		if ( this == n || n.toString().equals( toString())){
			ret = true;
//			System.out.printf( "TRUE \n" );
		}
		return ret;
	}

	@Override
	public int hashCode(){
		int hash = toString().hashCode();	
//		System.out.printf("---- using hashcode: %d \n", hash );
		return hash;
	}
	
	@Override		
	public String toString(){
		String ret = "";
		for ( String s : ngram )
			ret += s;
		return ret;
	}
}
