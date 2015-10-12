package com.lucene.action.analysis;

import com.lucene.action.util.AnalyzerUtils;
import org.apache.lucene.analysis.core.SimpleAnalyzer;

import java.io.IOException;

/**
 * Created by enosent on 2015. 10. 11..
 */
public class SimpleAnalyzerDemo {

    public static void main(String[] args) throws IOException {
        String text = "The quick brown fox....";

        AnalyzerUtils.displayTokensWithFullDetails(new SimpleAnalyzer(), text);
        AnalyzerUtils.displayPositionIncrements(new SimpleAnalyzer(), text);
    }
}
