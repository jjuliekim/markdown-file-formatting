package cs3500.pa01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args path to files, ordering flag, and output path
   * @throws IOException throws IOException
   */
  public static void main(String[] args) throws IOException {
    System.out.println("Walking the file system.");
    Path notesRoot = Path.of(args[0]);
    Order order = Order.valueOf(args[1].toUpperCase());
    Path outputFile = Path.of(args[2]);

    // walk through files
    ArrayList<Path> list = new ArrayList<>();
    MdVisitor visitor = new MdVisitor(list);
    Files.walkFileTree(notesRoot, visitor);
    ArrayList<Path> listOfMdFiles = visitor.getList();
    orderingFiles(order, listOfMdFiles);

    // write study guide as a new .md file
    String notes = formatNotes(listOfMdFiles);
    byte[] data = notes.getBytes();
    Files.write(outputFile, data);
  }

  /**
   * Sorts the given list of .md files based on the given ordering flag.
   *
   * @param order         ordering flag
   * @param listOfMdFiles list of .md files to sort
   */
  public static void orderingFiles(Order order, ArrayList<Path> listOfMdFiles) {
    switch (order) {
      case FILENAME -> Collections.sort(listOfMdFiles);
      case CREATED -> listOfMdFiles.sort(new CreatedComparator());
      default -> listOfMdFiles.sort(new ModifiedComparator());
    }
  }

  /**
   * Formats the text in the .md files.
   * Only keep headers and separate bracketed text with bullets.
   *
   * @param listOfMdFiles list of sorted .md files to format
   * @return study guide text
   * @throws IOException if scanner can not scan file
   */
  public static String formatNotes(ArrayList<Path> listOfMdFiles) throws IOException {
    Scanner scanner;
    StringBuilder keepContents = new StringBuilder();
    for (Path file : listOfMdFiles) {
      StringBuilder contents = new StringBuilder();
      scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        contents.append(line).append('\n');
      }
      char[] text = contents.toString().trim().toCharArray();

      boolean inHeader = false;
      boolean inBrackets = false;
      for (int i = 0; i < text.length; i++) {
        char character = text[i];
        // check if there's a newline (end of the header), else continue adding to keepContents
        if (inHeader) {
          if (character == '\n') {
            inHeader = false;
          } else {
            keepContents.append(character);
          }
          continue;
        }
        // check if it's the end of bracketed text, else add to keepContents (but no newlines)
        if (inBrackets) {
          if (character == ']' && text[i + 1] == ']') {
            inBrackets = false;
            continue;
          }
          if (character != '\n') {
            keepContents.append(character);
          }
          continue;
        }
        // check if it's the start of the header, separate with newlines and indicate with '#'
        if (character == '#') {
          inHeader = true;
          if (i > 0) {
            keepContents.append("\n\n");
          }
          keepContents.append('#');
          continue;
        }
        // check if it's the start of bracketed text and indicate with a bullet point on new line
        if (character == '[' && text[i + 1] == '[') {
          inBrackets = true;
          keepContents.append("\n- ");
          i++;
        }
      }
      keepContents.append("\n\n");
    }
    return keepContents.toString().trim();
  }
}