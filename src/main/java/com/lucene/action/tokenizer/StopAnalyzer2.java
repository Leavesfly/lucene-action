package com.lucene.action.tokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.util.CharArraySet;

public class StopAnalyzer2 extends Analyzer {

	private CharArraySet stopWords;

	public StopAnalyzer2() {
		stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	}


	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		final Tokenizer source = new LetterTokenizer();
		
		// 필터 적용 순서 중요
		LowerCaseFilter lowerCaseFilter = new LowerCaseFilter(source);
		StopFilter stopFilter = new StopFilter(lowerCaseFilter, stopWords);
		return new TokenStreamComponents(source, stopFilter);
		
//		StopFilter stopFilter = new StopFilter(source, stopWords);
//		LowerCaseFilter lowerCaseFilter = new LowerCaseFilter(stopFilter);
//		return new TokenStreamComponents(source, lowerCaseFilter);
//		Exception - org.junit.ComparisonFailure: expected:<[quick]> but was:<[the]>
		
	}


}
