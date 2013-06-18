package org.uva.predictions;


public class NGram{
	String ngram;
	
	public NGram( String g, int n, ValueType type  ){
		ngram = g;
	}
	

	public NGram( Iterable<String> g, int n, ValueType type  ){
		ngram = "";
		int i = 0;
		for ( String gram : g){
			ngram += gram;
			if( ++i < n )
				ngram += ",";
		}
	}
	
	public NGram( String[] g  ){
		ngram = "";
		int i = 0;
		for ( String gram : g){
			ngram += gram;
			if( ++i < g.length )
				ngram += ",";
		}
	}
	public NGram( String g){
		ngram = g;
	}	
	public String[] getGramArray(){
		return ngram.split(",");
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
		return ngram;
	}
}
