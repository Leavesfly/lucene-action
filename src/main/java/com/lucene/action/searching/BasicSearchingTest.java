package com.lucene.action.searching;

import com.lucene.action.util.TestUtil;
import junit.framework.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by enosent on 2015. 10. 4..
 */
public class BasicSearchingTest extends TestCase {

    Logger logger = LoggerFactory.getLogger(BasicSearchingTest.class);

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

        assertEquals("Ant in Action, " +
                    "JUnit in Action, Second Edition", 2, docs.totalHits);


        for(ScoreDoc doc : docs.scoreDocs) {
            logger.info("score doc => {}", doc.toString());
        }

        reader.close();
        dir.close();
    }
}
