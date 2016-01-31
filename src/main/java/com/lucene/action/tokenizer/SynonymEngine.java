package com.lucene.action.tokenizer;

import java.io.IOException;

public interface SynonymEngine {

	String[] getSynonyms(String s) throws IOException;
	
}