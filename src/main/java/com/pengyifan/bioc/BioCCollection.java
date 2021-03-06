package com.pengyifan.bioc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Collection of documents.
 * <p>
 * Collection of documents for a project. They may be an entire corpus or some portion of a corpus.
 * Fields are provided to describe the collection.
 * <p>
 * Documents may appear empty if doing document at a time IO.
 *
 * @author Yifan Peng
 * @since 1.0.0
 */
public class BioCCollection implements HasInfons, BioCObject {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  // XML information
  private String encoding;
  private String version;
  private boolean standalone;

  private String source;
  private String date;
  private String key;
  private Map<String, String> infons;
  private List<BioCDocument> documents;

  /**
   * Constructs an empty collection.
   * <ul>
   * <li>encoding: UTF-8</li>
   * <li>date: today</li>
   * <li>version: 1.0</li>
   * <li>standalone: true</li>
   * <li>source: empty</li>
   * <li>key: empty</li>
   * </ul>
   */
  public BioCCollection() {
    infons = Maps.newHashMap();
    documents = Lists.newArrayList();
    setEncoding("UTF-8");
    setDate(formatter.format(LocalDateTime.now()));
    setVersion("1.0");
    setStandalone(true);
    setSource("");
    setKey("");
  }

  /**
   * Constructs an empty collection.
   * <p>
   * <ul>
   * <li>encoding: UTF-8</li>
   * <li>date: today</li>
   * <li>version: 1.0</li>
   * <li>standalone: true</li>
   * </ul>
   *
   * @param source source
   * @param key    key
   */
  public BioCCollection(String source, String key) {
    this();
    setSource(source);
    setKey(key);
  }

  /**
   * Constructs an empty collection.
   *
   * @param encoding     encoding
   * @param version      version
   * @param date         date
   * @param isStandalone standalone
   * @param source       source
   * @param key          key
   */
  public BioCCollection(String encoding, String version, String date,
      boolean isStandalone, String source, String key) {
    this();
    setEncoding(encoding);
    setDate(date);
    setVersion(version);
    setStandalone(isStandalone);
    setSource(source);
    setKey(key);
  }

  /**
   * Constructs a collection containing the information of the specified collection.
   *
   * @param collection the collection whose information is to be placed into this collection
   */
  public BioCCollection(BioCCollection collection) {
    this();
    setSource(collection.source);
    setDate(collection.date);
    setInfons(collection.infons);
    setKey(collection.key);
    setVersion(collection.version);
    setEncoding(collection.encoding);
    setStandalone(collection.standalone);
    setDocuments(collection.documents);
  }

  /**
   * Adds document in this collection.
   *
   * @param document document
   */
  public void addDocument(BioCDocument document) {
    checkNotNull(document, "document cannot be null");
    this.documents.add(document);
  }

  /**
   * Clears all documents.
   */
  public void clearDocuments() {
    documents.clear();
  }

  /**
   * Returns an iterator over the document in this collection in proper sequence.
   *
   * @return an iterator over the document in this collection in proper sequence.
   */
  public Iterator<BioCDocument> documentIterator() {
    return documents.iterator();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BioCCollection)) {
      return false;
    }
    BioCCollection rhs = (BioCCollection) obj;
    return Objects.equals(source, rhs.source)
        && Objects.equals(infons, rhs.infons)
        && Objects.equals(key, rhs.key)
        && Objects.equals(date, rhs.date)
        && Objects.equals(version, rhs.version)
        && Objects.equals(encoding, rhs.encoding)
        && Objects.equals(standalone, rhs.standalone)
        && Objects.equals(documents, rhs.documents);
  }

  /**
   * Returns date the documents obtained from the source.
   *
   * @return date the documents obtained from the source
   */
  public String getDate() {
    checkNotNull(date, "date cannot be null");
    return date;
  }

  /**
   * Returns the number of documents in this collection.
   *
   * @return the number of documents in this collection
   */
  public int getDocmentCount() {
    return documents.size();
  }

  /**
   * Returns the document at the specified position in this collection.
   *
   * @param index document position in this collection
   * @return the document at the specified position in this collection
   */
  public BioCDocument getDocument(int index) {
    return documents.get(index);
  }

  /**
   * Returns all the documents in the collection. This will be empty if document at a time IO is
   * used to read the XML file. Any contents will be ignored if written with document at a time IO.
   *
   * @return all the documents in the collection
   */
  public List<BioCDocument> getDocuments() {
    return documents;
  }

  /**
   * Returns the charset encoding of the BioC file.
   *
   * @return the charset encoding of the BioC file
   */
  public String getEncoding() {
    checkNotNull(encoding, "haven't set encoding yet");
    return encoding;
  }

  public Map<String, String> getInfons() {
    return infons;
  }

  /**
   * Returns name of a file describing the contents and conventions used in this XML file.
   *
   * @return name of a file describing the contents and conventions used in this XML file
   */
  public String getKey() {
    checkNotNull(key, "key cannot be null");
    return key;
  }

  /**
   * Returns the original source of the documents.
   *
   * @return the original source of the documents
   */
  public String getSource() {
    checkNotNull(source, "source cannot be null");
    return source;
  }

  /**
   * Gets the XML version declared on the XML declaration. Returns null if none was declared.
   *
   * @return the XML version declared on the XML declaration
   */
  public String getVersion() {
    checkNotNull(version, "haven't set version yet");
    return version;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        source,
        date,
        key,
        infons,
        version,
        encoding,
        standalone,
        documents);
  }

  /**
   * Gets the standalone declaration from the XML declaration
   *
   * @return true if the DTD is ignored by the parser
   */
  public boolean isStandalone() {
    return standalone;
  }

  /**
   * Sets the date the documents obtained from the source.
   *
   * @param date the date the documents obtained from the source
   */
  public void setDate(String date) {
    this.date = date;
  }

  /**
   * Sets the documents in this collection.
   *
   * @param documents the passage in this collection
   */
  public void setDocuments(List<BioCDocument> documents) {
    clearDocuments();
    this.documents.addAll(documents);
  }

  /**
   * Sets the charset encoding of the BioC file.
   *
   * @param encoding the charset encoding of the BioC file
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * Sets the name of a file describing the contents and conventions used in this XML file.
   *
   * @param key the name of a file describing the contents and conventions used in this XML file
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * Sets the original source of the documents.
   *
   * @param source the original source of the documents
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Sets the standalone declaration to the XML declaration.
   *
   * @param standalone true if the parser can ignore the DTD
   */
  public void setStandalone(boolean standalone) {
    this.standalone = standalone;
  }

  /**
   * Sets the XML version declared on the XML declaration.
   *
   * @param version the XML version declared on the XML declaration
   */
  public void setVersion(String version) {
    Validate.notNull(version);
    this.version = version;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("source", source)
        .append("date", date)
        .append("key", key)
        .append("infons", infons)
        .append("documents", documents)
        .toString();
  }

}
