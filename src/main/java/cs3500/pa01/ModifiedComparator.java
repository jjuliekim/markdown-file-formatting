package cs3500.pa01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;

/**
 * Compares the last modified time of the given Paths.
 */
public class ModifiedComparator implements Comparator<Path> {

  /**
   * Compares the two Paths based on their last modified time.
   *
   * @param p1 the first object to be compared.
   * @param p2 the second object to be compared.
   * @return -1, 0, 1: depends on the two Paths' last modified time.
   */
  public int compare(Path p1, Path p2) {
    try {
      BasicFileAttributes attrs1 = Files.readAttributes(p1, BasicFileAttributes.class);
      BasicFileAttributes attrs2 = Files.readAttributes(p2, BasicFileAttributes.class);
      return attrs1.lastModifiedTime().compareTo(attrs2.lastModifiedTime());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
