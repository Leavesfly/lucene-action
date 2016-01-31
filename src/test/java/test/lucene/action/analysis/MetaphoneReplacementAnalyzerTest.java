package test.lucene.action.analysis;

import static org.junit.Assert.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

import com.lucene.action.tokenizer.MetaphoneReplacementAnalyzer;

public class MetaphoneReplacementAnalyzerTest {

	public static void main(String[] args) throws Exception {
		RAMDirectory directory = new RAMDirectory();
		Analyzer analyzer = new MetaphoneReplacementAnalyzer();
		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setInfoStream(System.out);
		
		IndexWriter writer = new IndexWriter(directory, config);
		
		Document doc = new Document();
		doc.add(new TextField("contents", "cool cat", Field.Store.YES));
		
		writer.addDocument(doc);
		writer.close();
		
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);

		QueryParser parser = new QueryParser("contents", analyzer);
		Query query = parser.parse("kool kat");
		
		TopDocs hits = searcher.search(query, 1);
		
		assertEquals(1, hits.totalHits);
		
		int docID = hits.scoreDocs[0].doc;
		doc = searcher.doc(docID);
		
		assertEquals("cool cat", doc.get("contents"));
		
		reader.close();
	}
}
