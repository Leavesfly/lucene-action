package com.lucene.action.meetlucene;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lucene.action.util.Const;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Created by enosent on 2015. 9. 30..
 */
public class Indexer {

	public static void main(String[] args) throws Exception {

		String indexDir = Const.OUTPUT_PATH;
		String dataDir = Const.INPUT_PATH;

		long start = System.currentTimeMillis();
		Indexer indexer = new Indexer(indexDir);
		int numIndexed = 0;

		try {
			numIndexed = indexer.index(dataDir, new TextFilesFilter());
		} catch (Exception e) {
			System.err.println( e.getMessage() );
		} finally {
			indexer.close();
		}

		long end = System.currentTimeMillis();

		System.out.println("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");
	}

	private IndexWriter writer;

	public Indexer(String indexDir) throws IOException {
		Path path = Paths.get(indexDir);
		Directory dir = FSDirectory.open(path);

		StandardAnalyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		writer = new IndexWriter(dir, config);
	}

	public void close() throws IOException {
		writer.close();
	}

	public int index(String dataDir, FileFilter filter) throws Exception {
		File[] files = new File(dataDir).listFiles();
		for (File f: files) {
			if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead() && (filter == null || filter.accept(f))) {
				System.out.println( f.getName());
				indexFile(f);
			}
		}

		return writer.numDocs();
	}

	private static class TextFilesFilter implements FileFilter {
		public boolean accept(File path) {
			return path.getName().toLowerCase().endsWith((".txt"));
		}
	}

	protected Document getDocument(File file) throws Exception {
		Document doc = new Document();
		doc.add(new TextField("contents", new FileReader(file)));
		doc.add(new StringField("filename", file.getName(), Field.Store.YES));
		doc.add(new StringField("fullpath", file.getCanonicalPath(), Field.Store.YES));

		return doc;
	}

	private void indexFile(File file) throws Exception {
		Document doc = getDocument(file);
		writer.addDocument(doc);
	}

}
