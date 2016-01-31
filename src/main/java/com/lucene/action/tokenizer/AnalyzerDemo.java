package com.lucene.action.tokenizer;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import com.lucene.action.util.AnalyzerUtils;

public class AnalyzerDemo {
	
    private static final Analyzer[] analyzers = new Analyzer[] {
        new WhitespaceAnalyzer(),
        new SimpleAnalyzer(),
        new StopAnalyzer(),
        new StandardAnalyzer()
    };

	public static void main(String[] args) throws IOException {
		String text = "The quick brown fox jumped over the lazy dog";
		text = "XY&Z Corporation - xyz@example.com";
		
		for(Analyzer analyzer : analyzers) {
            String name = analyzer.getClass().getSimpleName();

            System.out.println(" " + name + ":");
            System.out.print("     ");

            AnalyzerUtils.displayTokens(analyzer, text);
            System.out.println("\n");
        }
			
	}
}
