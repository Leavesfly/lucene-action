package test.lucene.action.analysis;

import com.lucene.action.tokenizer.MetaphoneReplacementAnalyzer;
import com.lucene.action.util.AnalyzerUtils;

public class MetaphoneReplacementTest {

	public static void main(String[] args) throws Exception {
		AnalyzerUtils.displayTokens(new MetaphoneReplacementAnalyzer(), "The quick brown fox jumped over the lazy dog");
		System.out.println();
		AnalyzerUtils.displayTokens(new MetaphoneReplacementAnalyzer(), "The quik brown phox jumpd ovvar the lazi dag");
	}
}
