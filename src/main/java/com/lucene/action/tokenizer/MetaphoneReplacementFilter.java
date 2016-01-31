package com.lucene.action.tokenizer;

import java.io.IOException;

import org.apache.commons.codec.language.Metaphone;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaphoneReplacementFilter extends TokenFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(MetaphoneReplacementFilter.class);
	
	public static final String METAPHONE = "metaphone";
	
	private Metaphone metaphone = new Metaphone();
	
	private final CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);
	private final TypeAttribute typeAttr = addAttribute(TypeAttribute.class);

	protected MetaphoneReplacementFilter(TokenStream input) {
		super(input);
	}

	@Override
	public boolean incrementToken() throws IOException {
		
		if(!input.incrementToken())
			return false;
		
		String encoded;
		encoded = metaphone.encode(termAttr.toString());
		
		if(logger.isDebugEnabled()) {
			logger.debug("# term [ {} ] - encoded [ {} ]", termAttr.toString(), encoded );
		}
		
		termAttr.setEmpty().append(encoded); // 기존 term을 지우고 변환된 정보로 재설정
		
		typeAttr.setType(METAPHONE);
		return true;
	}

}
