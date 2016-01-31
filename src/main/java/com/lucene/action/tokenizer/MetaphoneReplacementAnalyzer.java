package com.lucene.action.tokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;

public class MetaphoneReplacementAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		final Tokenizer source = new LetterTokenizer();
		MetaphoneReplacementFilter metaphoneReplacementFilter = new MetaphoneReplacementFilter(source);

		return new TokenStreamComponents(source, metaphoneReplacementFilter);
	}

}
