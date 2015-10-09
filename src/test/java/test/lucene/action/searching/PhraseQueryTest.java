package test.lucene.action.searching;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by enosent on 2015. 10. 9..
 */
public class PhraseQueryTest {

    private Directory dir;
    private IndexSearcher searcher;
    private IndexReader reader;

    @Before
    public void setUp() throws Exception {
        dir = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(new WhitespaceAnalyzer());
        IndexWriter writer = new IndexWriter(dir, config);

        Document doc = new Document();
        doc.add(new TextField("field", "the quick brown fox jumped over the lazy dog", Field.Store.YES));

        writer.addDocument(doc);
        writer.close();

        reader = DirectoryReader.open(dir);
        searcher = new IndexSearcher(reader);
    }

    @After
    public void tearDown() throws IOException {
        reader.close();
        dir.close();
    }

    private boolean matched(String[] phrase, int slop) throws IOException {
        PhraseQuery.Builder query = new PhraseQuery.Builder();

        // Slop : 항상 텀을 문서와 같은 순서로 배열할 때 필요한 이동 횟수의 최댓값
        query.setSlop(slop);

        for(String word : phrase) {
            query.add(new Term("field", word));
        }

        TopDocs matches = searcher.search(query.build(), 10);

        return matches.totalHits > 0;
    }

    @Test
    public void testSlopComparison() throws Exception {
        String[] phrase = new String[] {"quick", "fox"};

        assertTrue("close enough", matched(phrase, 1));
    }

    @Test
    public void testReverse() throws Exception {
        String[] phrase = new String[] {"fox", "quick"};

        assertFalse("hop flop", matched(phrase, 2));
        assertTrue("hop hop flop", matched(phrase, 3));
    }

    @Test
    public void testMultiple() throws Exception {
        assertFalse("not close enough", matched(new String[]{"quick", "jumped", "lazy"}, 3));
        assertTrue("just enough", matched(new String[]{"quick", "jumped", "lazy"}, 4));

        assertFalse("almost but not quite", matched(new String[]{"lazy", "jumped", "quick"}, 7));
        assertTrue("bingo", matched(new String[]{"lazy", "jumped", "quick"}, 8));
    }
}