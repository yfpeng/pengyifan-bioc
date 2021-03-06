package com.pengyifan.bioc;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Relationship between multiple {@link BioCAnnotation}s and possibly other {@code BioCRelation}s.
 *
 * @author Yifan Peng
 * @since 1.0.0
 */
public class BioCRelation implements HasInfons, HasID, BioCObject {

  private String id;
  private Map<String, String> infons;
  private Set<BioCNode> nodes;

  /**
   * Constructs an empty relation.
   */
  public BioCRelation() {
    infons = Maps.newHashMap();
    nodes = Sets.newHashSet();
  }

  /**
   * Constructs an empty relation with id.
   *
   * @param id the id used to identify relation
   */
  public BioCRelation(String id) {
    this.id = id;
    infons = Maps.newHashMap();
    nodes = Sets.newHashSet();
  }

  /**
   * Constructs a relation containing the information of the specified relation.
   *
   * @param relation the relation whose information is to be placed into this relation
   */
  public BioCRelation(BioCRelation relation) {
    this();
    setID(relation.id);
    setInfons(relation.infons);
    setNodes(relation.nodes);
  }

  /**
   * Add the node to this relation
   *
   * @param node node to be added to this relation
   */
  public void addNode(BioCNode node) {
    checkNotNull(node, "node cannot be null");
    nodes.add(node);
  }

  /**
   * Clears all nodes in this relation.
   */
  public void clearNodes() {
    nodes.clear();
  }

  /**
   * Returns true if this relation contains the specified node.
   *
   * @param node node whose presence in this relation is to be tested
   * @return if this relation contains the specified node
   */
  public boolean containsNode(BioCNode node) {
    return nodes.contains(node);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BioCRelation)) {
      return false;
    }
    BioCRelation rhs = (BioCRelation) obj;
    return Objects.equals(id, rhs.id)
        && Objects.equals(infons, rhs.infons)
        && Objects.equals(nodes, rhs.nodes);
  }

  @Override
  public String getID() {
    checkNotNull(id, "id cannot be null");
    return id;
  }

  @Override
  public Map<String, String> getInfons() {
    return infons;
  }

  /**
   * Gets the first node based on the role.
   *
   * @param role the role of the node
   * @return node that has the same role
   */
  public Optional<BioCNode> getNode(String role) {
    return getNodes().stream().filter(n -> n.getRole().equals(role)).findFirst();
  }

  /**
   * Returns the number of nodes in this relation.
   *
   * @return the number of nodes in this relation
   */
  public int getNodeCount() {
    return nodes.size();
  }

  /**
   * Returns nodes that describe how the referenced annotated object or other relation participates
   * in the current relationship.
   *
   * @return nodes of the relation
   */
  public Set<BioCNode> getNodes() {
    return nodes;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, infons, nodes);
  }

  /**
   * Returns a unmodifiable iterator over the nodes in this relation in proper sequence.
   *
   * @return an iterator over the nodes in this relation in proper sequence
   */
  public Iterator<BioCNode> nodeIterator() {
    return nodes.iterator();
  }

  @Override
  public void setID(String id) {
    this.id = id;
  }

  /**
   * Sets the nodes in this relation.
   *
   * @param nodes the nodes in this relation.
   */
  public void setNodes(Set<BioCNode> nodes) {
    clearNodes();
    this.nodes.addAll(nodes);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", id)
        .append("infons", infons)
        .append("nodes", nodes)
        .toString();
  }
}
