package test.lucene.action.indexing;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lucene.action.util.TestUtil;

public class IndexingTest {

	private static final Logger logger = LoggerFactory.getLogger(IndexingTest.class);

	protected String[] ids = {"1", "2"};
	protected String[] unindexed = {"Netherlands", "Italy"};
	protected String[] unstored = {"Amsterdam has lots of bridges", "Venice has lots of canals"};
	protected String[] text = {"Amsterdam", "Venice"};

	private Directory directory;

	@Before
	public void setUp() throws Exception {
		directory = new RAMDirectory();

		IndexWriter writer = getWriter();

		for(int i = 0; i < ids.length; i++) {
			Document doc = new Document();

			//before: new Field("id", idx[i], Fiedl.Store.YES, Field.Index.NOT_ANALYZED);
			doc.add(new StringField("id", ids[i], Field.Store.YES));

			//before: new Field("country", unindexed[i], Fiedl.Store.YES, Field.Index.NO);
			doc.add(new StoredField("country", unindexed[i]));

			//before: new Field("contents", unstored[i], Fiedl.Store.NO, Field.Index.ANALYZED);
			doc.add(new TextField("contents", unstored[i], Field.Store.NO));

			//before: new Field("city", text[i], Fiedl.Store.YES, Field.Index.ANALYZED);
			doc.add(new StringField("city", text[i], Field.Store.YES));

			writer.addDocument(doc);
			logger.info("doc => {}",doc);
		}

		writer.close();
	}

	private IndexWriter getWriter() throws IOException {
		WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setInfoStream(System.out);
		//logger.info("index writer config => {}", config.toString());

		return new IndexWriter(directory, config);
	}

	private int getHitCount(String fieldName, String searchString) throws IOException {
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);

		Term t = new Term(fieldName, searchString);
		Query query = new TermQuery(t);

		int hitCount = TestUtil.hitCount(searcher, query);

		reader.close();

		return hitCount;
	}

	@Test
	public void testIndexWriter() throws IOException {
		IndexWriter writer = getWriter();

		logger.info("numDocs => {}", writer.numDocs());

		assertEquals(ids.length, writer.numDocs());
		writer.close();
	}

	@Test
	public void testIndexReader() throws IOException {
		IndexReader reader = DirectoryReader.open(directory);

		logger.info("max doc => {}, numDocs => {}", reader.maxDoc(), reader.numDocs());

		//logger.info("doc => {}", reader.document(0));

		assertEquals(ids.length, reader.maxDoc());
		assertEquals(ids.length, reader.numDocs());

		reader.close();
	}

	@Test
	public void testDeleteBeforeOptimize() throws IOException {
		IndexWriter writer = getWriter();
		assertEquals(2, writer.numDocs());

		writer.deleteDocuments(new Term("id", "1"));
		writer.commit();

		assertTrue(writer.hasDeletions());
		assertEquals(2, writer.maxDoc());
		assertEquals(1, writer.numDocs());

		writer.close();
	}

	@Test
	public void testUpdate() throws IOException {
		assertEquals(1, getHitCount("city", "Amsterdam"));

		IndexWriter writer = getWriter();

		Document doc = new Document();
		doc.add(new StringField("id", "1", Field.Store.YES));
		doc.add(new StoredField("country", "Netherlands"));
		doc.add(new TextField("contents", "Den Haag has a lot of museums", Field.Store.NO));
		doc.add(new StringField("city", "Den Haag", Field.Store.YES));

		writer.updateDocument(new Term("id","1"), doc);

		writer.close();

		assertEquals(0, getHitCount("city", "Amsterdam"));
		assertEquals(1, getHitCount("city", "Den Haag"));
	}

}