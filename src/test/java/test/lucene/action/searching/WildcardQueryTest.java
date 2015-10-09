package test.lucene.action.searching;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by enosent on 2015. 10. 9..
 */
public class WildcardQueryTest {

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
    public void testWildcard() throws Exception {
        indexSingleFieldDocs(new Field[]{
                new TextField("contents", "wild", Field.Store.YES),
                new TextField("contents", "child", Field.Store.YES),
                new TextField("contents", "mild", Field.Store.YES),
                new TextField("contents", "mildew", Field.Store.YES)
        });

        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        Query query = new WildcardQuery(new Term("contents", "?ild*"));
        // * - 글자가 없거나 하나 이상의 여러 글자에 대응
        // ? - 글자가 없거나 아니면 글자 하나에 해당

        TopDocs matches = searcher.search(query, 10);

        assertEquals("child no match", 3, matches.totalHits);

        assertEquals("score the same", matches.scoreDocs[0]. score,matches.scoreDocs[1].score, 0.0);
        assertEquals("score the same", matches.scoreDocs[1]. score,matches.scoreDocs[2].score, 0.0);

    }
}
