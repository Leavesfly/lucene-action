package test.lucene.action.searching;

import static org.junit.Assert.*;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.junit.Test;

import com.lucene.action.util.TestUtil;

public class NumericRangeQueryTest {

	@Test
	public void testInclusive() throws Exception {
		Directory directory = TestUtil.getBookIndexDirectory();
		IndexReader reader = DirectoryReader.open(directory);

		IndexSearcher searcher = new IndexSearcher(reader);

		NumericRangeQuery<Integer> query = NumericRangeQuery.newIntRange("pubmonth", 200605, 200609, true, true);

		TopDocs matches = searcher.search(query, 10);

		for(int i=0;i<matches.totalHits;i++) {
			System.out.println("match " + i + ": " + searcher.doc(matches.scoreDocs[i].doc).get("author"));
		}

		assertEquals(1, matches.totalHits);

		reader.close();
		directory.close();
	}

	@Test
	public void testExclusive() throws Exception {
		Directory directory = TestUtil.getBookIndexDirectory();
		IndexReader reader = DirectoryReader.open(directory);

		IndexSearcher searcher = new IndexSearcher(reader);

		NumericRangeQuery<Integer> query = NumericRangeQuery.newIntRange("pubmonth", 200605, 200609, false, false);

		TopDocs matches = searcher.search(query, 10);

		assertEquals(0, matches.totalHits);

		reader.close();
		directory.close();
	}

}