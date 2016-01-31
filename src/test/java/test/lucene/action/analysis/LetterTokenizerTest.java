package test.lucene.action.analysis;

import java.io.StringReader;

import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class LetterTokenizerTest {

	public static void main(String[] args) throws Exception {
		
		LetterTokenizer source = new LetterTokenizer();
		source.setReader(new StringReader("The quick brown fox Jumped over the lazydog"));
		
		CharTermAttribute term = source.addAttribute(CharTermAttribute.class);
		source.reset();
		
		while(source.incrementToken()) {
			System.out.println( term.toString() );
		}
		
		source.close();
	}
	
}
