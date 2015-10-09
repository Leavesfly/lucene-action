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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TermRangeQueryTest {

	private static final Logger logger = LoggerFactory.getLogger(TermRangeQueryTest.class);

	@Test
	public void testTermRangeQuery() throws Exception {
		Directory dir = TestUtil.getBookIndexDirectory();
		IndexReader reader = DirectoryReader.open(dir);

		IndexSearcher searcher = new IndexSearcher(reader);

		TermRangeQuery query = TermRangeQuery.newStringRange("title2", "d", "j", true, true);
		// ( d <= keyword <= j ) => title2:[d TO j] 인데 .. 왜 검색 결과에는 junit in action, second edition 이 안나오는 걸까 ..
		// ( a <  keyword <  j ) => title2:{a TO j} 인데 .. 왜 검색 결과에 a 로 시작하는 정보가 나오는 걸까 ..

		logger.info("query => {}", query.toString());

		TopDocs matches = searcher.search(query, 100);

		for(int i=0;i<matches.totalHits;i++) {
			System.out.println("match " + i + ": " + searcher.doc(matches.scoreDocs[i].doc).get("title2"));
		}

		assertEquals(3, matches.totalHits);

		reader.close();
		dir.close();
	}

}