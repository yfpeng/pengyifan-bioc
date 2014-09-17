package org.biocreative.bioc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;

/**
 * Stand off annotation. The connection to the original text can be made
 * through the {@code location} and the {@code text} fields.
 */
public class BioCAnnotation {

  private String id;
  private ImmutableMap<String, String> infons;
  private ImmutableList<BioCLocation> locations;
  private String text;

  private BioCAnnotation() {
  }

  /**
   * Returns the id used to identify this annotation in a {@link BioCRelation}.
   */
  public String getID() {
    return id;
  }

  /**
   * Returns the information in this annotation.
   */
  public ImmutableMap<String, String> getInfons() {
    return infons;
  }

  /**
   * Returns the value to which the specified key is mapped, or null if this
   * {@code infons} contains no mapping for the key.
   */
  public Optional<String> getInfon(String key) {
    return Optional.fromNullable(infons.get(key));
  }

  /**
   * Returns locations of the annotated text. Multiple locations indicate a
   * multispan annotation.
   */
  public ImmutableList<BioCLocation> getLocations() {
    return locations;
  }

  /**
   * Returns the location at the specified position in this annotation.
   */
  public BioCLocation getLocation(int i) {
    return locations.get(i);
  }

  /**
   * Returns the number of locations in this annotation.
   */
  public int getLocationCount() {
    return locations.size();
  }

  /**
   * Returns a unmodifiable iterator over the locations in this annotation in
   * proper sequence.
   */
  public UnmodifiableIterator<BioCLocation> locationIterator() {
    return locations.iterator();
  }

  /**
   * The original text of the annotation
   */
  public Optional<String> getText() {
    return Optional.fromNullable(text);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(id)
        .append(text)
        .append(infons)
        .append(locations)
        .toHashCode();
  }

  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    BioCAnnotation rhs = (BioCAnnotation) obj;
    return new EqualsBuilder()
        .append(id, rhs.id)
        .append(text, rhs.text)
        .append(infons, rhs.infons)
        .append(locations, rhs.locations)
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", id)
        .append("text", text)
        .append("infons", infons)
        .append("locations", locations)
        .toString();
  }

  /**
   * Constructs a builder initialized with the current annotation. Use this to
   * derive a new annotation from the current one.
   */
  public Builder toBuilder() {
    return newBuilder()
        .setID(id)
        .setText(text)
        .setInfons(infons)
        .setLocations(locations);
  }

  /**
   * Constructs a new builder. Use this to derive a new annotation.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {

    private String id;
    private String text;
    private Map<String, String> infons;
    private List<BioCLocation> locations;

    private Builder() {
      infons = new Hashtable<String, String>();
      locations = new ArrayList<BioCLocation>();
    }

    public Builder setID(String id) {
      Validate.notNull(id, "id cannot be null");
      this.id = id;
      return this;
    }

    public Builder setInfons(Map<String, String> infons) {
      this.infons = new Hashtable<String, String>(infons);
      return this;
    }

    public Builder clearInfons() {
      infons.clear();
      return this;
    }

    public Builder clearLocations() {
      locations.clear();
      return this;
    }

    public Builder putInfon(String key, String value) {
      infons.put(key, value);
      return this;
    }

    public Builder removeInfon(String key) {
      infons.remove(key);
      return this;
    }

    public Builder addLocation(BioCLocation location) {
      Validate.notNull(location, "location cannot be null");
      locations.add(location);
      return this;
    }

    public Builder setLocations(List<BioCLocation> locations) {
      this.locations = new ArrayList<BioCLocation>(locations);
      return this;
    }

    public Builder addLocation(int offset, int length) {
      return addLocation(BioCLocation.newBuilder()
          .setOffset(offset)
          .setLength(length)
          .build());
    }

    public Builder setText(String text) {
      Validate.notNull(text, "text cannot be null");
      this.text = text;
      return this;
    }

    public Builder clearText() {
      text = null;
      return this;
    }

    public Builder clearID() {
      id = null;
      return this;
    }

    public BioCAnnotation build() {
      checkArguments();

      BioCAnnotation result = new BioCAnnotation();
      result.id = id;
      result.text = text;
      result.infons = ImmutableMap.copyOf(infons);
      result.locations = ImmutableList.copyOf(locations);
      return result;
    }

    private void checkArguments() {
      Validate.isTrue(id != null, "id has to be set");
      Validate.isTrue(
          !locations.isEmpty(),
          "there must be at least one location");
    }
  }
}
