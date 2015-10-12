package com.lucene.action.analysis;

import com.lucene.action.util.AnalyzerUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;

/**
 * Created by enosent on 2015. 10. 11..
 */
public class StandardAnalyzerDemo {

    public static void main(String[] args) throws IOException {
        AnalyzerUtils.displayTokensWithFullDetails(new StandardAnalyzer(), "\"I'll email you at xyz@example.com\"");
    }
}
