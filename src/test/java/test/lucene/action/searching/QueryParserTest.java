package test.lucene.action.searching;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.TermQuery;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * Created by enosent on 2015. 10. 9..
 */
public class QueryParserTest {

    private static final Logger logger = LoggerFactory.getLogger(QueryParserTest.class);

    @Test
    public void testToString() throws Exception {
        BooleanQuery.Builder query = new BooleanQuery.Builder();

        query.add(new FuzzyQuery(new Term("field", "kountry")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("title", "western")), BooleanClause.Occur.SHOULD);

        logger.info("query => {}", query.build().toString("field"));

        assertEquals("both kinds", "+kountry~2 title:western", query.build().toString("field"));
    }

}
