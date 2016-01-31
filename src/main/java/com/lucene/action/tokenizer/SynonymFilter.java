package com.lucene.action.tokenizer;

import java.io.IOException;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

public class SynonymFilter extends TokenFilter {
	
	public static final String TOKEN_TYPE_SYNONYM = "SYNONIM";
	
	private Stack<String> synonymStack;
	private SynonymEngine engine;
	private AttributeSource.State current;
	
	private final CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);

	public SynonymFilter(TokenStream in, SynonymEngine engine) {
		super(in);
		synonymStack =new Stack<String>(); // 유사어를 담아 둘 버퍼
		this.engine = engine;
	}

	@Override
	public boolean incrementToken() throws IOException {
		
		if(synonymStack.size() > 0) {
			String syn = synonymStack.pop(); // 버퍼에 담겨있는 유사어를 뽑아낸다.
			restoreState(current);
			termAttr.setEmpty().append(syn);
			posIncrAtt.setPositionIncrement(0); // 위치 증가 값을 0으로 지정
			return true;
		}
		
		if(!input.incrementToken()) // 다음 토큰을 읽는다. 
			return false;
		
		if(addAliasesToStack()) { // 유사어를 찾아내 버퍼에 담는다 
			current = captureState(); // 현재 토큰 보관
		}
		
		return true; //현재 토큰 리턴
	}

	private boolean addAliasesToStack() throws IOException {
		System.out.println( termAttr.toString() );
		
		String[] synonyms = engine.getSynonyms(termAttr.toString());
		
		if(synonyms == null) {
			return false;
		}
		
		for(String synonym : synonyms) {
			synonymStack.push(synonym); // 유사어를 버퍼에 보관
		}
		
		return true;
	}
}