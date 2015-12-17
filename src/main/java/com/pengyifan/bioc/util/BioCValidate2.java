package com.pengyifan.bioc.util;

import com.pengyifan.bioc.BioCAnnotation;
import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCNode;
import com.pengyifan.bioc.BioCPassage;
import com.pengyifan.bioc.BioCRelation;
import com.pengyifan.bioc.BioCSentence;

import java.util.Collection;
import java.util.StringJoiner;

import static com.google.common.base.Preconditions.checkArgument;

public class BioCValidate2 {

  /**
   * Checks annotations and relations.
   *
   * @param collection input collection
   */
  public static void check(BioCCollection collection) {
    for (BioCDocument document : collection.getDocuments()) {
      check(document);
    }
  }

  /**
   * Checks annotations and relations.
   *
   * @param document input document
   */
  public static void check(BioCDocument document) {
    String text = getText(document);
    // check annotation offset and text
    checkAnnotations(document.getAnnotations(), text, 0, BioCUtils.getXPathString(document));
    // check relation
    for (BioCRelation relation : document.getRelations()) {
      for (BioCNode node : relation.getNodes()) {
        checkArgument(document.getAnnotation(node.getRefid()).isPresent(),
            "Cannot find node %s in relation %s\n" +
                "Location: %s", node, relation, BioCUtils.getXPathString(document));
      }
    }

    // check passage
    for (BioCPassage passage : document.getPassages()) {
      text = getText(passage);
      // check annotation offset and text
      checkAnnotations(passage.getAnnotations(), text, passage.getOffset(),
          BioCUtils.getXPathString(document, passage));
      // check relation
      for (BioCRelation relation : passage.getRelations()) {
        for (BioCNode node : relation.getNodes()) {
          checkArgument(passage.getAnnotation(node.getRefid()).isPresent(),
              "Cannot find node %s in relation %s\n" +
                  "Location: %s", node, relation, BioCUtils.getXPathString(document, passage));
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
      StringJoiner sj = new StringJoiner(" ");
      annotation.getLocations().stream()
          .sorted((l1, l2) -> Integer.compare(l1.getOffset(), l2.getOffset()))
          .forEach(
              l -> {
                String substring = text.substring(
                    l.getOffset() - offset,
                    l.getOffset() + l.getLength() - offset
                );
                sj.add(substring);
              }
          );
      checkArgument(sj.toString().equals(annotation.getText().get()),
          "Annotation text is incorrect.\n" +
              "  Annotation : %s\n" +
              "  Actual text: %s\n" +
              "  Location   : %s",
          annotation, sj.toString(), head);
    }
  }

  private static StringBuilder fillText(StringBuilder sb, int offset) {
    while (sb.length() < offset) {
      sb.append('\n');
    }
    return sb;
  }

  public static String getText(BioCDocument document) {
    StringBuilder sb = new StringBuilder();
    for (BioCPassage passage : document.getPassages()) {
      fillText(sb, passage.getOffset());
      sb.append(getText(passage));
    }
    return sb.toString();
  }

  public static String getText(BioCPassage passage) {
    StringBuilder sb = new StringBuilder();
    if (passage.getText().isPresent() && !passage.getText().get().isEmpty()) {
      sb.append(passage.getText().get());
    } else {
      for (BioCSentence sentence : passage.getSentences()) {
        fillText(sb, sentence.getOffset() - passage.getOffset());
        checkArgument(sentence.getText().isPresent(), "BioC sentence has no text");
        sb.append(sentence.getText().get());
      }
    }
    return sb.toString();
  }
}