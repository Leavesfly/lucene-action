package test.lucene.action.analysis;

import java.io.IOException;

import com.lucene.action.tokenizer.SynonymAnalyzer;
import com.lucene.action.tokenizer.SynonymEngine;
import com.lucene.action.tokenizer.TestSynonymEngine;
import com.lucene.action.util.AnalyzerUtils;

public class SynonymAnalyzerViewer {

	public static void main(String[] args) throws IOException {
		SynonymEngine engine = new TestSynonymEngine();
		
		AnalyzerUtils.displayTokensWithPositions(new SynonymAnalyzer(engine), "The quick brown fox jumps over the lazy dog");
	}
}
