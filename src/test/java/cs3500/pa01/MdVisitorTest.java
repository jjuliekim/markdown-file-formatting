package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the methods in MdVisitor class
 */
public class MdVisitorTest {

  private Path notesRoot;
  private MdVisitor visitor;
  ArrayList<Path> expected;

  /**
   * Initialize data and set up before each test
   */
  @BeforeEach
  void setUpTest() {
    notesRoot = Path.of("src", "test", "resources", "PA01Examples");
    visitor = new MdVisitor(new ArrayList<>());
    expected = new ArrayList<>();
  }

  /**
   * Tests the preVisitDirectory method in the MdVisitor class.
   *
   * @throws IOException throws IOException
   */
  @Test
  void testPreVisitDirectory() throws IOException {
    BasicFileAttributes attrs = Files.readAttributes(notesRoot, BasicFileAttributes.class);
    assertEquals(FileVisitResult.CONTINUE, visitor.preVisitDirectory(notesRoot, attrs));
  }

  /**
   * Tests the visitFile method in the MdVisitor class
   *
   * @throws IOException throws IOException
   */
  @Test
  void testVisitFile() throws IOException {
    Files.walkFileTree(notesRoot, visitor);
    ArrayList<Path> actualMdFiles = visitor.getList();
    Path arrays = Path.of("src", "test", "resources", "PA01Examples",
        "SomeFolder", "arrays.md");
    Path vectors = Path.of("src", "test", "resources", "PA01Examples", "vectors.md");
    assertTrue(actualMdFiles.contains(arrays));
    assertTrue(actualMdFiles.contains(vectors));
  }

  /**
   * Tests the visitFileFailed method in the MdVisitor class.
   */
  @Test()
  void testVisitFileFailed() {
    assertThrows(IOException.class,
        () -> visitor.visitFileFailed(Path.of("src", "test", "resources",
                "PA01Examples", "unknown.md"),
            new IOException()));
  }

  /**
   * Tests the postVisitDirectory method in the MdVisitor class.
   */
  @Test
  void testPostVisitDirectory() {
    assertEquals(FileVisitResult.CONTINUE,
        visitor.postVisitDirectory(notesRoot, new IOException()));

  }

  /**
   * Tests the getList method in the MdVisitor class.
   *
   * @throws IOException throws IOException
   */
  @Test
  void testGetList() throws IOException {
    ArrayList<Path> files = visitor.getList();
    assertEquals(expected, files);

    Path vectors = Path.of("src", "test", "resources", "PA01Examples", "vectors.md");
    expected.add(vectors);
    Files.walkFileTree(vectors, visitor);
    assertEquals(expected, files);
  }
}