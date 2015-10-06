package com.lucene.action.searching;

import com.lucene.action.util.TestUtil;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

/**
 * Created by enosent on 2015. 10. 6..
 */
public class Explainer {

    public static void main(String[] args) throws Exception {
        Directory dir = TestUtil.getBookIndexDirectory();
        QueryParser parser = new QueryParser("contents", new SimpleAnalyzer());

        Query query = parser.parse("junit");

        System.out.println("Query: " + query);

        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        TopDocs topDocs = searcher.search(query, 10);

        for(ScoreDoc match : topDocs.scoreDocs) {
            Explanation explanation = searcher.explain(query, match.doc);

            System.out.println("----------");

            Document doc = searcher.doc(match.doc);
            System.out.println(doc.get("title"));
            System.out.println(explanation.toString());
        }

        reader.close();
        dir.close();
    }

}