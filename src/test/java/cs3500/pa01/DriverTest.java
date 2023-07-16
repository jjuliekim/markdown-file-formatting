package cs3500.pa01;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the methods in the Driver class.
 */
public class DriverTest {

  private Path project;
  private Path root;
  private Path output;
  private Path arrays;
  private Path vectors;
  private MdVisitor visitor;
  private ArrayList<String> expected;
  private ArrayList<String> actual;
  private ArrayList<Path> expectedPath;
  private ArrayList<Path> actualPath;

  /**
   * Initialize data and set up before each test
   *
   * @throws IOException throws IOException
   */
  @BeforeEach
  void setUpTest() throws IOException {
    project = Path.of("src/test/resources");
    root = Path.of(project + "/PA01Examples");
    output = Path.of(project + "/output.md");
    expected = new ArrayList<>();
    actual = new ArrayList<>();
    Files.deleteIfExists(output);

    arrays = Path.of(root + "/SomeFolder/arrays.md");
    vectors = Path.of(root + "/vectors.md");
    visitor = new MdVisitor(new ArrayList<>());
    expectedPath = new ArrayList<>();
    actualPath = new ArrayList<>();
  }

  /**
   * Tests the main method in the Driver class.
   * command line argument 2 = "filename"
   *
   * @throws IOException if it can not visit all the files.
   */
  @Test
  void testMainFileName() throws IOException {
    String[] args = new String[] {root.toString(), "filename", output.toString()};
    Driver.main(args);

    expected = new ArrayList<>(Files.readAllLines(Path.of(project + "/outputFilename.md")));
    actual = new ArrayList<>(Files.readAllLines(Path.of(args[2])));
    assertEquals(expected, actual);
  }

  /**
   * Tests the main method in the Driver class.
   * command line argument 2 = "created"
   *
   * @throws IOException if it can not visit all the files.
   */
  @Test
  void testMainCreated() throws IOException {
    String[] args = new String[] {root.toString(), "created", output.toString()};
    Driver.main(args);

    expected = new ArrayList<>(Files.readAllLines(Path.of(project + "/outputCreated.md")));
    actual = new ArrayList<>(Files.readAllLines(Path.of(args[2])));
    assertEquals(expected, actual);
  }

  /**
   * Tests the main method in the Driver class.
   * command line argument 2 = "modified"
   *
   * @throws IOException if it can not visit all the files.
   */
  @Test
  void testMainModified() throws IOException {
    String[] args = new String[] {root.toString(), "modified", output.toString()};
    Driver.main(args);

    expected = new ArrayList<>(Files.readAllLines(Path.of(project + "/outputModified.md")));
    actual = new ArrayList<>(Files.readAllLines(Path.of(args[2])));
    assertEquals(expected, actual);
  }

  /**
   * Tests the orderingFiles method in the Driver class.
   * Sorting by filename
   *
   * @throws IOException if it can not visit all the files
   */
  @Test
  void testOrderingFilesName() throws IOException {
    Files.walkFileTree(root, visitor);
    actualPath = visitor.getList();
    Driver.orderingFiles(Order.FILENAME, actualPath);
    expectedPath = new ArrayList<>();
    expectedPath.add(arrays);
    expectedPath.add(vectors);
    assertEquals(expectedPath, actualPath);
  }

  /**
   * Tests the orderingFiles method in the Driver class.
   * Sorting by created time
   *
   * @throws IOException if it can not visit all the files
   */
  @Test
  void testOrderingFilesCreated() throws IOException {
    Files.walkFileTree(root, visitor);
    actualPath = visitor.getList();
    Driver.orderingFiles(Order.CREATED, actualPath);
    expectedPath.add(vectors);
    expectedPath.add(arrays);
    assertEquals(expectedPath, actualPath);
  }

  /**
   * Tests the orderingFiles method in the Driver class.
   * Sorting by last modified time
   *
   * @throws IOException if it can not visit all the files
   */
  @Test
  void testOrderingFilesModified() throws IOException {
    Files.walkFileTree(root, visitor);
    actualPath = visitor.getList();
    Driver.orderingFiles(Order.MODIFIED, actualPath);
    expectedPath.add(vectors);
    expectedPath.add(arrays);
    assertEquals(expectedPath, actualPath);
  }

  /**
   * Tests the formatNotes method in the Driver class.
   *
   * @throws IOException if it is unable to scan the contents of each file
   */
  @Test
  void testFormatNotes() throws IOException {
    actualPath.add(Path.of(root + "/SomeFolder/arrays.md"));

    expected = new ArrayList<>(Files.readAllLines(
        Path.of(project + "/formattedArrays.md")));
    actual = new ArrayList<>(Files.readAllLines(
        Files.write(output, Driver.formatNotes(actualPath).getBytes())));
    assertEquals(expected, actual);
  }
}