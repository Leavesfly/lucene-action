package test.lucene.action.analysis;

import org.junit.Test;

import com.lucene.action.tokenizer.StopAnalyzer2;
import com.lucene.action.util.AnalyzerTestUtils;

public class CustomStopAnalyzerTest {

	@Test
	public void testStopAnalyzer2() throws Exception {
		AnalyzerTestUtils.assertAnalyzesTo(new StopAnalyzer2(), "The quick brown...", new String[]{"quick", "brown"});
	}
	
}
