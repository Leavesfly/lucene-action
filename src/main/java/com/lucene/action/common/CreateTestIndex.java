package com.lucene.action.common;

import com.lucene.action.custom.field.VecStringField;
import com.lucene.action.custom.field.VecTextField;
import com.lucene.action.util.Const;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by enosent on 2015. 10. 4..
 *
 * Lucene IN ACTION - Chap.3 검색 테스트를 위한 Indexing ( 색인 ) 데이터 제공
 */
public class CreateTestIndex {

    private static final Logger logger = LoggerFactory.getLogger(CreateTestIndex.class);

    private static final String DATA_DIR = Const.PROJECT_PATH + "/data";

    public static void main(String[] args) throws IOException {
        List<File> results = new ArrayList<File>();
        findFiles(results, new File(DATA_DIR));

        logger.info("{}  books to index", results.size());

        IndexWriterConfig config = new IndexWriterConfig(new MyStandardAnalyzer());
        config.setOpenMode(OpenMode.CREATE);

        Path path = Paths.get(Const.OUTPUT_PATH);
        Directory dir = FSDirectory.open(path);

        IndexWriter writer = new IndexWriter(dir, config);


        for(File file : results) {
            Document doc = getDocument(file);
            writer.addDocument(doc);
        }

        writer.close();
        dir.close();
    }

    private static void findFiles(List<File> results, File dir) {

        for(File file : dir.listFiles()) {
            if (file.getName().endsWith(".properties")) {
                results.add(file);
            } else if (file.isDirectory()) {
                findFiles(results, file);
            }
        }
    }

    private static class MyStandardAnalyzer extends Analyzer {

        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            return new TokenStreamComponents(new StandardTokenizer());
        }

        @Override
        public int getPositionIncrementGap(String field) {
            if ("contents".equals(field)) {
                return 100;
            } else {
                return 0;
            }
        }
    }

    public static Document getDocument(File file) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(file));

        Document doc = new Document();

        // category comes from relative path below the base directory
        String category = file.getParent().substring(DATA_DIR.length());
        category = category.replace(File.separatorChar, '/');

        String isbn = props.getProperty("isbn");
        String title = props.getProperty("title");
        String author = props.getProperty("author");
        String url = props.getProperty("url");
        String subject = props.getProperty("subject");

        String pubmonth = props.getProperty("pubmonth");

        logger.info("book info => {} {} {} {} {}", title, author, subject, pubmonth, category);

        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        doc.add(new StringField("category", category, Field.Store.YES));
        doc.add(new VecTextField("title", title, Field.Store.YES));  // Field.TermVector.WITH_POSITIONS_OFFSETS
        doc.add(new StoredField("title2", title.toLowerCase())); // Field.Index.NOT_ANALYZED_NO_NORMS, Field.TermVector.WITH_POSITIONS_OFFSETS

        // split multiple authors into unique field instances
        String[] authors = author.split(",");
        for (String auth : authors) {
            doc.add(new VecStringField("author", auth, Field.Store.YES));  // Field.TermVector.WITH_POSITIONS_OFFSETS
        }

        doc.add(new StoredField("url", url)); // Field.Index.NOT_ANALYZED_NO_NORMS
        doc.add(new VecTextField("subject", subject, Field.Store.YES)); // Field.TermVector.WITH_POSITIONS_OFFSETS

        doc.add(new IntField("pubmonth", Integer.parseInt(pubmonth), Field.Store.YES));
        // doc.add(new NumericField("pubmonth", Field.Store.YES, true).setIntValue(Integer.parseInt(pubmonth)));

        Date date;
        try {
            date = DateTools.stringToDate(pubmonth); // 3
        } catch (ParseException pe) { // 3
            throw new RuntimeException(pe); // 3
        }

        doc.add(new IntField("pubmonthAsDay", (int) (date.getTime()/(1000*3600*24)), Field.Store.NO));
        // doc.add(new NumericField("pubmonthAsDay").setIntValue((int) (d.getTime()/(1000*3600*24))));

        for(String text : new String[] {title, subject, author, category}) {
            doc.add(new VecTextField("contents", text, Field.Store.NO)); // Field.TermVector.WITH_POSITIONS_OFFSETS
        }

        return doc;
    }
}