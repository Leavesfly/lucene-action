package com.lucene.action.meetlucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

		String indexDir = Const.OUTPUT_PATH + "/MeetLucene";
		String dataDir = Const.INPUT_PATH + "/meetlucene/data";

		long start = System.currentTimeMillis();
		Indexer indexer = new Indexer(indexDir);
		int numIndexed;

		try {
			numIndexed = indexer.index(dataDir, new TextFilesFilter());
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
		Path path = Paths.get(dataDir);

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
			for (Path p : directoryStream) {
				File f = p.toFile();

				if(!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead() && (filter == null || filter.accept(f))) {
					indexFile( p );
				}
			}
		} catch (IOException ex) {}

		return writer.numDocs();
	}

	private static class TextFilesFilter implements FileFilter {
		public boolean accept(File path) {
			return path.getName().toLowerCase().endsWith((".txt"));
		}
	}

	protected Document getDocument(Path path) throws Exception {
		Document doc = new Document();

		InputStream stream = Files.newInputStream(path);
		doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
		doc.add(new StringField("filename", path.toFile().getName(), Field.Store.YES));
		doc.add(new StringField("fullpath", path.toFile().getCanonicalPath(), Field.Store.YES));

		return doc;
	}

	private void indexFile(Path path) throws Exception {
		Document doc = getDocument(path);
		writer.addDocument(doc);
	}

}
