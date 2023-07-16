package cs3500.pa01;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * Visits each file.
 */
public class MdVisitor implements FileVisitor<Path> {

  private final ArrayList<Path> listOfMdFiles;

  /**
   * Constructor for MdVisitor
   *
   * @param arrayList arrayList of the Paths (.md files)
   */
  public MdVisitor(ArrayList<Path> arrayList) {
    listOfMdFiles = arrayList;
  }

  /**
   * Print indication of the start of processing a directory
   *
   * @param dir
   *          a reference to the directory
   * @param attrs
   *          the directory's basic attributes
   *
   * @return continue
   */
  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
    System.out.format("Visiting: %s%n", dir);
    return FileVisitResult.CONTINUE;
  }

  /**
   * Everytime the file system walker encounters a .md file,
   * add it to the list storing the markdown files.
   *
   * @param file
   *          a reference to the file
   * @param attrs
   *          the file's basic attributes
   *
   * @return continue
   */
  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
    if (file.toString().endsWith(".md")) {
      listOfMdFiles.add(file);
    }
    return FileVisitResult.CONTINUE;
  }

  /**
   * Throws exception because the file cannot be visited
   *
   * @param file
   *          a reference to the file
   * @param exc
   *          the I/O exception that prevented the file from being visited
   *
   * @throws IOException throws IOException
   */

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    throw exc;
  }

  /**
   * Print indication of the end of processing a directory
   *
   * @param dir
   *          a reference to the directory
   * @param exc
   *          {@code null} if the iteration of the directory completes without
   *          an error; otherwise the I/O exception that caused the iteration
   *          of the directory to complete prematurely
   *
   * @return continue
   */
  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
    System.out.format("Visited: %s%n", dir);
    return FileVisitResult.CONTINUE;
  }

  /**
   * Returns the list
   *
   * @return the list storing the .md files
   */
  public ArrayList<Path> getList() {
    return listOfMdFiles;
  }

}
