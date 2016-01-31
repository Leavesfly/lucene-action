package com.lucene.action.tokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class SynonymAnalyzer extends Analyzer {
	
	private SynonymEngine engine;
	
	public SynonymAnalyzer() { }
	
	public SynonymAnalyzer(SynonymEngine engine) {
		this.engine = engine;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		final StandardTokenizer source = new StandardTokenizer();
		
		StandardFilter standardFilter = new StandardFilter(source);
		LowerCaseFilter lowerCaseFilter = new LowerCaseFilter(standardFilter);
		StopFilter stopFilter = new StopFilter(lowerCaseFilter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		
		SynonymFilter synonymFilter = new SynonymFilter(stopFilter, engine);
		
		return new TokenStreamComponents(source, synonymFilter);
	}

}
