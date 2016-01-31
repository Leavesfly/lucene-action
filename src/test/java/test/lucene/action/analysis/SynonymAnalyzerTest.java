package test.lucene.action.analysis;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import com.lucene.action.tokenizer.SynonymAnalyzer;
import com.lucene.action.tokenizer.TestSynonymEngine;

public class SynonymAnalyzerTest {

	public static void main(String[] args) throws Exception {
		SynonymAnalyzer analyzer = new SynonymAnalyzer(new TestSynonymEngine());
		
		TokenStream stream = analyzer.tokenStream("cotents", new StringReader("jumps"));
		
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
		
		int i = 0;
		String[] expected = new String[] { "jumps", "hops", "leaps" };
		
		stream.reset();
		while(stream.incrementToken()) {
			assertEquals(expected[i], term.toString());
			
			int expectedPos;
			
			if(i == 0) {
				expectedPos = 1;
			} else {
				expectedPos = 0;
			}
			
			assertEquals(expectedPos, posIncr.getPositionIncrement());
			
			i++;
		}
		
		assertEquals(3, i);
		analyzer.close();
	}
}
