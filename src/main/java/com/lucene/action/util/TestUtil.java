package com.lucene.action.util;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by enosent on 2015. 10. 3..
 */
public class TestUtil {

	private static final Logger logger = LoggerFactory.getLogger(TestUtil.class);

	public static boolean hitsIncludeTitle(IndexSearcher searcher, TopDocs hits, String title) throws IOException {
		for (ScoreDoc match : hits.scoreDocs) {
			Document doc = searcher.doc(match.doc);

			logger.info("title => {}", doc.get("title"));
			if (title.equals(doc.get("title"))) {
				return true;
			}
		}

		logger.info("title '{}' not found", title);
		return false;
	}

	public static int hitCount(IndexSearcher searcher, Query query) throws IOException {
		int hitCount = searcher.search(query, 1).totalHits;
		logger.info("hit count => {}", hitCount);

		return hitCount;
	}

	public static Directory getBookIndexDirectory() throws IOException {
		Path path = Paths.get(Const.OUTPUT_PATH);
		Directory dir = FSDirectory.open(path);

		logger.info("dir => {}", dir);

		return dir;
	}
}