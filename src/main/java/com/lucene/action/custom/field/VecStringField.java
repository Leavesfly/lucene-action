package com.lucene.action.custom.field;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;

public class VecStringField extends Field {

	/* Indexed, tokenized, not stored. */
	public static final FieldType TYPE_NOT_STORED = new FieldType();

	/* Indexed, tokenized, stored. */
	public static final FieldType TYPE_STORED = new FieldType();

	static {
		TYPE_NOT_STORED.setOmitNorms(true);
		TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS);
		TYPE_NOT_STORED.setTokenized(false);
		TYPE_NOT_STORED.setStoreTermVectors(true);
		TYPE_NOT_STORED.setStoreTermVectorPositions(true);
		TYPE_NOT_STORED.setStoreTermVectorOffsets(true);
		TYPE_NOT_STORED.freeze();

		TYPE_STORED.setOmitNorms(true);
		TYPE_STORED.setIndexOptions(IndexOptions.DOCS);
		TYPE_STORED.setStored(true);
		TYPE_STORED.setTokenized(false);
		TYPE_STORED.setStoreTermVectors(true);
		TYPE_STORED.setStoreTermVectorPositions(true);
		TYPE_STORED.setStoreTermVectorOffsets(true);
		TYPE_STORED.freeze();
	}

	// TODO: add sugar for term vectors...?

	/** Creates a new TextField with Reader value. */
	public VecStringField(String name, Reader reader, Store store) {
		super(name, reader, store == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
	}

	/** Creates a new TextField with String value. */
	public VecStringField(String name, String value, Store store) {
		super(name, value, store == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
	}

	/** Creates a new un-stored TextField with TokenStream value. */
	public VecStringField(String name, TokenStream stream) {
		super(name, stream, TYPE_NOT_STORED);
	}
}