package com.pengyifan.bioc;

import java.util.Map;
import java.util.Optional;

public interface HasInfons {
  /**
   * Returns the information.
   *
   * @return the information.
   */
  Map<String, String> getInfons();

  /**
   * Clears all information.
   */
  default void clearInfons() {
    getInfons().clear();
  }

  /**
   * Associates the specified value with the specified key.
   *
   * @param key   key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   */
  default void putInfon(String key, String value) {
    getInfons().put(key, value);
  }

  /**
   * Copies all of the mappings from the specified infons.
   *
   * @param infons the information
   */
  default void putAllInfons(Map<String, String> infons) {
    getInfons().putAll(infons);
  }

  /**
   * Removes the value for a key if it is present
   * (optional operation).
   *
   * @param key key with which the specified value is to be associated
   */
  default void removeInfon(String key) {
    getInfons().remove(key);
  }

  /**
   * Sets the information.
   *
   * @param infons the information
   */
  default void setInfons(Map<String, String> infons) {
    clearInfons();
    getInfons().putAll(infons);
  }

  /**
   * Returns the value to which the specified key is mapped, or null if this
   * {@code infons} contains no mapping for the key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped
   */
  default Optional<String> getInfon(String key) {
    return Optional.ofNullable(getInfons().get(key));
  }
}
