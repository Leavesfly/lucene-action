package test.lucene.action.searching;

import com.lucene.action.util.TestUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by enosent on 2015. 10. 9..
 */
public class QueryParserTest {

    private static final Logger logger = LoggerFactory.getLogger(QueryParserTest.class);

    private Analyzer analyzer;
    private Directory dir;

    private IndexReader reader;
    private IndexSearcher searcher;

    @Before
    public void setUp() throws Exception {
        analyzer = new WhitespaceAnalyzer();
        dir = TestUtil.getBookIndexDirectory();

        reader = DirectoryReader.open(dir);
        searcher = new IndexSearcher(reader);
    }

    @After
    public void tearDown() throws IOException {
        reader.close();
        dir.close();
    }
    @Test
    public void testToString() throws Exception {
        BooleanQuery.Builder query = new BooleanQuery.Builder();

        query.add(new FuzzyQuery(new Term("field", "kountry")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("title", "western")), BooleanClause.Occur.SHOULD);

        logger.info("query => {}", query.build().toString("field"));

        assertEquals("both kinds", "+kountry~2 title:western", query.build().toString("field"));
    }

    @Test
    public void testTermQuery() throws Exception {
        QueryParser parser = new QueryParser("subject", new WhitespaceAnalyzer());
        Query query = parser.parse("computers");

        logger.info("term: {}", query);
    }

    @Test
    public void testTermRangeQuery() throws Exception {
        Query query = new QueryParser("subject", analyzer)
                            .parse("title2:[Q TO V]");

        assertTrue(query instanceof  TermRangeQuery);

        TopDocs matches = searcher.search(query, 10);

        assertTrue(TestUtil.hitsIncludeTitle(searcher, matches, "Tapestry in Action"));

        query = new QueryParser("subject", analyzer)
                        .parse("title2:{Q TO \"Tapestry in Action\"}");

        matches = searcher.search(query, 10);

        assertFalse(TestUtil.hitsIncludeTitle(searcher, matches, "Tapestry in Action"));
    }
}