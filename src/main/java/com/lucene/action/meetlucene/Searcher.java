package com.lucene.action.meetlucene;

import java.io.IOException;
import java.nio.file.Paths;

import com.lucene.action.util.Const;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 * Created by enosent on 2015. 9. 30..
 */
public class Searcher {

	public static void main(String[] args) throws Exception {

		String indexDir = Const.OUTPUT_PATH + "/MeetLucene";
		String q = "LICENSE";

		search(indexDir, q);
	}

	public static void search(String indexDir, String q) throws IOException, ParseException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
		IndexSearcher searcher = new IndexSearcher(reader);

		QueryParser parser = new QueryParser("contents", new StandardAnalyzer());
		Query query = parser.parse(q);

		long start = System.currentTimeMillis();
		TopDocs hits = searcher.search(query, 10);
		long end = System.currentTimeMillis();

		System.err.println("Found " + hits.totalHits + " document(s) (in " + (end - start) + " milliseconds) that matched query '" + q + "':");

		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);

			System.out.println(doc.get("fullpath"));
		}

		reader.close();
	}
}
