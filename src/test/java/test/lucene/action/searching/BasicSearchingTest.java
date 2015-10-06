package test.lucene.action.searching;

import com.lucene.action.util.TestUtil;

import static org.junit.Assert.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by enosent on 2015. 10. 4..
 */
public class BasicSearchingTest {

	private static final Logger logger = LoggerFactory.getLogger(BasicSearchingTest.class);

	@Test
	public void testTerm() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexReader reader = DirectoryReader.open(dir);

		IndexSearcher searcher = new IndexSearcher(reader);

		Term t = new Term("subject", "ant");
		Query query = new TermQuery(t);

		TopDocs docs = searcher.search(query, 10);

		assertEquals("Ant in Action", 1, docs.totalHits);

		t = new Term("subject", "junit");
		docs = searcher.search(new TermQuery(t), 10);

		assertEquals("Ant in Action, JUnit in Action, Second Edition", 2, docs.totalHits);


		for(ScoreDoc doc : docs.scoreDocs) {
			Document d = searcher.doc(doc.doc);
			logger.info("score doc => title: {}, subject: {}", d.get("title"), d.get("subject"));
		}

		reader.close();
		dir.close();
	}

	@Test
	public void testKeyword() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexReader reader = DirectoryReader.open(dir);

		IndexSearcher searcher = new IndexSearcher(reader);

		Term t = new Term("isbn", "9781935182023");
		Query query = new TermQuery(t);
		TopDocs docs = searcher.search(query, 10);
		assertEquals("JUnit in Action, Second Edition", 1, docs.totalHits);

		reader.close();
		dir.close();
	}

	@Test
	public void testQueryParser() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexReader reader = DirectoryReader.open(dir);

		IndexSearcher searcher = new IndexSearcher(reader);

		QueryParser parser = new QueryParser("contents", new StandardAnalyzer());;

		Query query = parser.parse("+JUNIT +ANT -MOCK");
		TopDocs docs = searcher.search(query, 10);

		assertEquals(1, docs.totalHits);

		Document d = searcher.doc(docs.scoreDocs[0].doc);

		assertEquals("Ant in Action", d.get("title"));

		query = parser.parse("mock OR junit");
		docs = searcher.search(query, 10);

		assertEquals("Ant in Action, JUnit in Action, Second Edition", 2, docs.totalHits);

		reader.close();
		dir.close();
	}

}