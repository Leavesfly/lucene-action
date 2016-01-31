package test.lucene.action.analysis;

import static org.junit.Assert.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
import org.apache.lucene.store.RAMDirectory;

import com.lucene.action.tokenizer.SynonymAnalyzer;
import com.lucene.action.tokenizer.TestSynonymEngine;
import com.lucene.action.util.TestUtil;

public class SynonymMain {

	public static void main(String[] args) throws Exception {
		RAMDirectory directory = new RAMDirectory();
		Analyzer analyzer = new SynonymAnalyzer(new TestSynonymEngine());
		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setInfoStream(System.out);
		
		IndexWriter writer = new IndexWriter(directory, config);
		
		Document doc = new Document();
		doc.add(new TextField("contents", "The quick brown fox jumps over the lazy dog", Field.Store.YES));
		
		writer.addDocument(doc);
		writer.close();
		
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		Query query = new QueryParser("contents", new SynonymAnalyzer(new TestSynonymEngine())).parse("\"fox jumps\"");
		
		assertEquals(1, TestUtil.hitCount(searcher, query));
		
		System.out.println("With SynonymAnalyzer, \"fox jumps\" parses to " + query.toString("contents"));
		
		query = new QueryParser("contents", new StandardAnalyzer()).parse("\"fox jumps\"");
		
		assertEquals(1, TestUtil.hitCount(searcher, query));
		
		System.out.println("With StandardAnalyzer, \"fox jumps\" parses to " + query.toString("contents"));
	}
	
}