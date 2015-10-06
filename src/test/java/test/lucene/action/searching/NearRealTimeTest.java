package test.lucene.action.searching;

import static org.junit.Assert.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class NearRealTimeTest {

	@Test
	public void testNearRealTime() throws Exception {
		Directory dir = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		config.setInfoStream(System.out);
		
		IndexWriter writer = new IndexWriter(dir, config);

		for(int i = 0; i < 10; i++) {
			Document doc = new Document();
			doc.add(new StringField("id", ""+ i, Field.Store.NO));
			doc.add(new TextField("text", "aaa", Field.Store.NO));

			writer.addDocument(doc);
		}

		DirectoryReader reader = DirectoryReader.open(writer, true);
		IndexSearcher searcher = new IndexSearcher(reader);

		Query query = new TermQuery(new Term("text", "aaa"));
		TopDocs docs = searcher.search(query, 1);

		assertEquals(10, docs.totalHits);

		writer.deleteDocuments(new Term("id", "7"));

		Document doc = new Document();
		doc.add(new StringField("id", "11", Field.Store.NO));
		doc.add(new TextField("text", "bbb", Field.Store.NO));

		writer.addDocument(doc);

		DirectoryReader newReader = DirectoryReader.openIfChanged(reader, writer, true); // reader.reopen(); - Deprecate..
		assertFalse(reader == newReader);      
		reader.close();     
		
		searcher = new IndexSearcher(newReader);
		
		TopDocs hits = searcher.search(query, 10);
		
		assertEquals(9, hits.totalHits); 
		// assertEquals(9, hits.totalHits); - Failure  java.lang.AssertionError: expected:<9> but was:<10> StoreField 로 지정한 대상을 StringField 변경하니 오류 없음 ..

	    query = new TermQuery(new Term("text", "bbb")); 
	    hits = searcher.search(query, 1);    
	    assertEquals(1, hits.totalHits); 
	    
		newReader.close();
		writer.close();
	}

}