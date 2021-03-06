package com.pengyifan.bioc.util;

import com.pengyifan.bioc.BioCAnnotation;
import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCLocation;
import com.pengyifan.bioc.BioCNode;
import com.pengyifan.bioc.BioCPassage;
import com.pengyifan.bioc.BioCRelation;
import com.pengyifan.bioc.BioCSentence;

import java.util.Collection;
import java.util.StringJoiner;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @deprecated use {@link com.pengyifan.bioc.util.BioCValidate3} instead
 */
public class BioCValidate2 {

  /**
   * Checks annotations and ie.
   *
   * @param collection input collection
   */
  public static void check(BioCCollection collection) {
    for (BioCDocument document : collection.getDocuments()) {
      check(document);
    }
  }

  /**
   * Checks annotations and ie.
   *
   * @param document input document
   */
  public static void check(BioCDocument document) {
    String text = BioCUtils.getText(document);
    // check annotation offset and text
    checkAnnotations(document.getAnnotations(), text, 0, BioCLog.log(document));
    // check relation
    for (BioCRelation relation : document.getRelations()) {
      for (BioCNode node : relation.getNodes()) {
        checkArgument(document.getAnnotation(node.getRefid()).isPresent(),
            "Cannot find node %s in relation %s\n" +
                "Location: %s", node, relation, BioCLog.log(document));
      }
    }

    // check passage
    for (BioCPassage passage : document.getPassages()) {
      text = BioCUtils.getText(passage);
      // check annotation offset and text
      checkAnnotations(passage.getAnnotations(), text, passage.getOffset(),
          BioCLog.log(document, passage));
      // check relation
      for (BioCRelation relation : passage.getRelations()) {
        for (BioCNode node : relation.getNodes()) {
          checkArgument(passage.getAnnotation(node.getRefid()).isPresent(),
              "Cannot find node %s in relation %s\n" +
                  "Location: %s", node, relation, BioCLog.log(document, passage));
        }
      }

      // check sentence
      for (BioCSentence sentence : passage.getSentences()) {
        // check annotation offset and text
        checkAnnotations(sentence.getAnnotations(), sentence.getText().get(),
            sentence.getOffset(), BioCUtils.getXPathString(document, passage, sentence));
        // check relation
        for (BioCRelation relation : sentence.getRelations()) {
          for (BioCNode node : relation.getNodes()) {
            checkArgument(sentence.getAnnotation(node.getRefid()).isPresent(),
                "Cannot find node %s in relation %s\n" +
                    "  Location: %s", node, relation,
                BioCUtils.getXPathString(document, passage, sentence));
          }
        }
      }
    }
  }

  public static void checkAnnotations(Collection<BioCAnnotation> annotations,
      String text, int offset, String head) {
    for (BioCAnnotation annotation : annotations) {
      BioCLocation total = annotation.getTotalLocation();
      for (BioCLocation location: annotation.getLocations()) {
        String expected = text.substring(
            location.getOffset() - offset,
            location.getOffset() + location.getLength() - offset);
        String actual = annotation.getText().get().substring(
            location.getOffset() - total.getOffset(),
            location.getOffset() + location.getLength() - total.getOffset());
        checkArgument(expected.equals(actual),
            "Annotation text is incorrect.\n" +
                "  Annotation : %s\n" +
                "  Actual text: %s\n" +
                "  Location   : %s",
            annotation, actual, head);
      }
    }
  }
}
