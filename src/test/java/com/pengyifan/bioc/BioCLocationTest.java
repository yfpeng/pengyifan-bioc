package com.pengyifan.bioc;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.testing.EqualsTester;
import com.pengyifan.bioc.BioCLocation;

public class BioCLocationTest {

  private static final int LENGTH = 1;
  private static final int OFFSET = 2;

  private static final int LENGTH_2 = 2;
  private static final int OFFSET_2 = 3;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void test_allFields() {
    BioCLocation base = new BioCLocation(OFFSET, LENGTH);
    assertEquals(base.getOffset(), OFFSET);
    assertEquals(base.getLength(), LENGTH);
  }

  @Test
  public void test_equals() {
    BioCLocation base = new BioCLocation(OFFSET, LENGTH);
    BioCLocation baseCopy = new BioCLocation(base);

    BioCLocation diffOffset = new BioCLocation(OFFSET_2, LENGTH);
    BioCLocation diffOffset2 = new BioCLocation(base);
    diffOffset2.setOffset(OFFSET_2);

    BioCLocation diffLength = new BioCLocation(OFFSET, LENGTH_2);
    BioCLocation diffLength2 = new BioCLocation(base);
    diffLength2.setLength(LENGTH_2);

    new EqualsTester()
        .addEqualityGroup(base, baseCopy)
        .addEqualityGroup(diffOffset, diffOffset2)
        .addEqualityGroup(diffLength, diffLength2)
        .testEquals();
  }

  @Test
  public void test_negLength() {
    BioCLocation base = new BioCLocation(OFFSET, -1);
    thrown.expect(IllegalArgumentException.class);
    base.getLength();
  }

  @Test
  public void test_negOffset() {
    BioCLocation base = new BioCLocation(-1, LENGTH);
    thrown.expect(IllegalArgumentException.class);
    base.getOffset();
  }

}
