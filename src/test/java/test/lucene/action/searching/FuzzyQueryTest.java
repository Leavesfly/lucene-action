package test.lucene.action.searching;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by enosent on 2015. 10. 9..
 */
public class FuzzyQueryTest {

    private static final Logger logger = LoggerFactory.getLogger(FuzzyQueryTest.class);

    private Directory dir;

    @Before
    public void setUp() {
        dir = new RAMDirectory();
    }

    private void indexSingleFieldDocs(Field[] fields) throws Exception {
        IndexWriterConfig config = new IndexWriterConfig(new WhitespaceAnalyzer());

        IndexWriter writer = new IndexWriter(dir, config);

        for(Field f : fields) {
            Document doc = new Document();
            doc.add(f);

            writer.addDocument(doc
            );
        }

        //writer.optimize()
        // Deprecate
        /*
        This method has been deprecated, as it is horribly inefficient and very rarely justified.
        Lucene's multi-segment search performance has improved over time, and the default TieredMergePolicy now targets segments with deletions.
         */

        writer.close();
    }

    @Test
    public void testFuzzy() throws Exception {
        indexSingleFieldDocs(new Field[] {
                new TextField("contents", "fuzzy", Field.Store.YES),
                new TextField("contents", "wuzzy", Field.Store.YES)
        });

        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        Query query = new FuzzyQuery(new Term("contents", "wuzza"));

        TopDocs matches = searcher.search(query, 10);

        assertEquals("both close enough", 2, matches.totalHits);

        float score1 = matches.scoreDocs[0].score;
        float score2 = matches.scoreDocs[1].score;

        logger.info("score1 => {}, score2 => {}", score1, score2);

        // 편집 거리가 더 짧은 문서가 더 높은 점수를 받음
        assertTrue("wuzzy closer than fuzzy", score1 != score2);

        Document doc = searcher.doc(matches.scoreDocs[0].doc);

        assertEquals("wuzza bear", "wuzzy", doc.get("contents"));

        reader.close();
    }
}
