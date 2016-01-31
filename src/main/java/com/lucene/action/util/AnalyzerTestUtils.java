package com.lucene.action.util;

import java.io.StringReader;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Assert;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

public class AnalyzerTestUtils {

	public static void assertAnalyzesTo(Analyzer analyzer, String input, String[] output) throws Exception {
		TokenStream stream = analyzer.tokenStream("field", new StringReader(input));
		
		CharTermAttribute termAttr = stream.addAttribute(CharTermAttribute.class);
		
		stream.reset();
		for(String expected : output) {
			Assert.assertTrue(stream.incrementToken());
			Assert.assertEquals(expected, termAttr.toString());
		}
		
		Assert.assertFalse(stream.incrementToken());
		stream.close();
	}
}
