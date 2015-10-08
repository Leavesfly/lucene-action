package test.lucene.action.searching;

import static org.junit.Assert.*;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lucene.action.util.TestUtil;

public class BooleanQueryTest {

	private static final Logger logger = LoggerFactory.getLogger(BooleanQueryTest.class);

	@Test
	public void testAnd() throws Exception {
		Directory directory = TestUtil.getBookIndexDirectory();
		IndexReader reader = DirectoryReader.open(directory);

		IndexSearcher searcher = new IndexSearcher(reader);

		TermQuery searchingBooks = new TermQuery(new Term("subject", "search"));
		Query books2010 = NumericRangeQuery.newIntRange("pubmonth", 201001, 201012, true, true);

		BooleanQuery.Builder searchingBooks2010 = new BooleanQuery.Builder();

		searchingBooks2010.add(searchingBooks, Occur.MUST); 
		searchingBooks2010.add(books2010, Occur.MUST);

		TopDocs matches = searcher.search(searchingBooks2010.build(), 10);

		assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "Lucene in Action, Second Edition"));

		reader.close();
		directory.close();
	}

	@Test
	public void testOr() throws Exception {
		Directory directory = TestUtil.getBookIndexDirectory();
		IndexReader reader = DirectoryReader.open(directory);

		IndexSearcher searcher = new IndexSearcher(reader);

		TermQuery methodologyBooks = new TermQuery(new Term("category", "/technology/computers/programming/methodology"));

		TermQuery easternPhilosophyBooks = new TermQuery(new Term("category", "/philosophy/eastern"));                                  

		BooleanQuery.Builder enlightenmentBooks = new BooleanQuery.Builder();

		enlightenmentBooks.add(methodologyBooks, Occur.SHOULD);
		enlightenmentBooks.add(easternPhilosophyBooks, Occur.SHOULD);

		TopDocs matches = searcher.search(enlightenmentBooks.build(), 10);
		logger.info("or => {}", enlightenmentBooks);

		assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "Extreme Programming Explained"));
		assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "Tao Te Ching \u9053\u5FB7\u7D93"));

		reader.close();
		directory.close();
	}
}