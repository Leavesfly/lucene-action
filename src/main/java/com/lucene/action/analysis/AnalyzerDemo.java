package com.lucene.action.analysis;

import com.lucene.action.util.AnalyzerUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * Created by enosent on 2015. 10. 10..
 */
public class AnalyzerDemo {

    private static final String[] examples =  {
        "The quick brown fox jumped over the lazy dog",
        "XY&Z Corporation - xyz@example.com"
    };

    private static final Analyzer[] analyzers = new Analyzer[] {
        new WhitespaceAnalyzer(),
        new SimpleAnalyzer(),
        new StopAnalyzer(),
        new StandardAnalyzer()
    };

    public static void main(String[] args) throws Exception {

        String text = "No Fluff, Just Stuff";
        analyze(text);
    }

    private static void analyze(String text) throws Exception {
        System.out.println("Analyzing \"" + text + "\"");

        for(Analyzer analyzer : analyzers) {
            String name = analyzer.getClass().getSimpleName();

            System.out.println(" " + name + ":");
            System.out.print("     ");

            AnalyzerUtils.displayTokens(analyzer, text);
            System.out.println("\n");
        }
    }

}
