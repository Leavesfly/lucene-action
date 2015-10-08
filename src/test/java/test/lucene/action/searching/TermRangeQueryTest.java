package test.lucene.action.searching;

import static org.junit.Assert.*;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.junit.Test;

import com.lucene.action.util.TestUtil;

public class TermRangeQueryTest {

	@Test
	public void testTermRangeQuery() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexReader reader = DirectoryReader.open(dir);

		IndexSearcher searcher = new IndexSearcher(reader);

		TermRangeQuery query = TermRangeQuery.newStringRange("title2", "d", "j", true, true);

		TopDocs matches = searcher.search(query, 100);

		for(int i=0;i<matches.totalHits;i++) {
			System.out.println("match " + i + ": " + searcher.doc(matches.scoreDocs[i].doc).get("title2"));
		}

		assertEquals(3, matches.totalHits);

		reader.close();
		dir.close();
	}

}