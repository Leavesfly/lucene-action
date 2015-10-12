package com.lucene.action.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by enosent on 2015. 10. 10..
 */
public class AnalyzerUtils {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzerUtils.class);

    public static void displayPositionIncrements(Analyzer analyzer, String text) throws IOException {
        TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
        PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);

        stream.reset();
        while(stream.incrementToken()) {
            System.out.println("posIncr => " + posIncr.getPositionIncrement());
        }
    }


    public static void displayTokens(Analyzer analyzer, String text) throws IOException {
        displayTokens(analyzer.tokenStream("contents", new StringReader(text)));
    }

    public static void displayTokens(TokenStream stream) throws IOException {
        CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);

        stream.reset();
        while(stream.incrementToken()) {
            //logger.info("[ {} ]", term.toString());
            System.out.print("[" + term.toString() +"] ");
        }
    }

    public static void displayTokensWithFullDetails(Analyzer analyzer, String text) throws IOException {
        TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));

        CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
        PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
        OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
        TypeAttribute type = stream.addAttribute(TypeAttribute.class);

        int position = 0;

        stream.reset();
        while(stream.incrementToken()) {
            int increment = posIncr.getPositionIncrement();

            if(increment > 0) {
                position = position + increment;
                System.out.println();

                System.out.print(position + ": ");

                System.out.print("[" + term.toString() + ":" + offset.startOffset() + "->" + offset.endOffset() + ":" + type.type() + "] ");
            }

            System.out.println();
        }
    }
}
