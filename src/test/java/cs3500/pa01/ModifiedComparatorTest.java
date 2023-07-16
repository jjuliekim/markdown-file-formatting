package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.jupiter.api.Test;

/**
 * Tests for the method in ModifiedComparator class
 */
public class ModifiedComparatorTest {
  private final Path arrays = Path.of("src", "test", "resources", "PA01Examples",
      "SomeFolder", "arrays.md");
  private final Path vectors = Path.of("src", "test", "resources", "PA01Examples",
      "vectors.md");
  private final Path formattedArrays = Path.of("src", "test", "resources",
      "formattedArrays.md");

  /**
   * Tests the compare method in the ModifiedComparator class.
   */
  @Test
  public void testCompare() throws IOException {
    ModifiedComparator comparator = new ModifiedComparator();

    int expected;
    BasicFileAttributes attrs1 = Files.readAttributes(vectors, BasicFileAttributes.class);
    BasicFileAttributes attrs2 = Files.readAttributes(arrays, BasicFileAttributes.class);
    expected = attrs2.lastModifiedTime().compareTo(attrs1.lastModifiedTime());

    assertEquals(expected, comparator.compare(arrays, vectors));
    assertEquals(expected, comparator.compare(arrays, formattedArrays));
    assertEquals(expected, comparator.compare(formattedArrays, vectors));
    assertThrows(RuntimeException.class, () -> comparator.compare(Path.of(
        "src", "test", "resources", "unknown.md"), vectors));
  }
}