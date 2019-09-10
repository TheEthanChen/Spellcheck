import java.io.*;
import java.util.*;

/**
 * A Corrector whose spelling suggestions are given in a text file.
 *
 * <p>One way to get corrections for a misspelled word is to consult an external resource. This kind
 * of Corrector uses a file that contains pairs of words on each line (a misspelled word and a
 * correction for that misspelling) to generate corrections.
 */
public class FileCorrector extends Corrector {
  private Map<String, Set<String>> mapToCorrect;

  /**
   * A special purpose exception class to indicate errors when reading the input for the
   * FileCorrector.
   */
  public static class FormatException extends Exception {
    public FormatException(String msg) {
      super(msg);
    }
  }

  /**
   * Constructs a FileCorrector from the argued Reader.
   *
   * <p>Instead of using the TokenScanner to parse this input, you should read the input line by
   * line using a BufferedReader. This way you will practice with an alternative approach to working
   * with text. For methods useful in parsing the lines of the file, see the String class javadocs
   * in java.lang.String
   *
   * <p>Each line in the input should have a single comma that separates two parts in the form:
   * misspelled_word,corrected_version
   *
   * <p>For example:<br>
   *
   * <pre>
   * aligatur,alligator<br>
   * baloon,balloon<br>
   * inspite,in spite<br>
   * who'ev,who've<br>
   * ther,their<br>
   * ther,there<br>
   * </pre>
   *
   * <p>The lines are not case-sensitive, so all of the following lines should function
   * equivalently: <br>
   *
   * <pre>
   * baloon,balloon<br>
   * Baloon,balloon<br>
   * Baloon,Balloon<br>
   * BALOON,balloon<br>
   * bAlOon,BALLOON<br>
   * </pre>
   *
   * <p>You should ignore any leading or trailing whitespace around the misspelled and corrected
   * parts of each line. Thus, the following lines should all be equivalent:<br>
   *
   * <pre>
   * inspite,in spite<br>
   *    inspite,in spite<br>
   * inspite   ,in spite<br>
   *  inspite ,   in spite  <br>
   * </pre>
   *
   * Note that spaces are allowed inside the corrected word. (In general, the FileCorrector is
   * allowed to <em>suggest</em> Strings that are not words according to TokenScanner.)
   *
   * <p>You should throw a <code>FileCorrector.FormatException</code> if you encounter input that is
   * invalid. For example, the FileCorrector constructor should throw an exception if any of these
   * inputs are encountered:<br>
   *
   * <pre>
   * ,correct<br>
   * wrong,<br>
   * wrong correct<br>
   * wrong,correct,<br>
   * </pre>
   *
   * <p>
   *
   * @param r The sequence of characters to parse
   * @throws IOException If error while reading
   * @throws FileCorrector.FormatException If an invalid line is encountered
   * @throws IllegalArgumentException If the provided reader is null
   */
  public FileCorrector(Reader r) throws IOException, FormatException {
    if (r == null) {
      throw new IllegalArgumentException();
    }
    mapToCorrect = new TreeMap<>();
    BufferedReader bf = new BufferedReader(r);
    String readLine = bf.readLine();

    try {
      while (readLine != null) {

        readLine = readLine.trim();
          
          if(readLine.length() != 0) {

        if (!readLine.matches("(.*),(.*)")) {
          throw new FileCorrector.FormatException("no commas");
        }

        int firstCommaIndex = readLine.indexOf(",");
        String firstSection = (readLine.substring(0, firstCommaIndex)).trim();
        String secondSection = (readLine.substring(firstCommaIndex + 1, readLine.length())).trim();

        if (secondSection.contains(",")) {
          throw new FileCorrector.FormatException("Too Many commas");
        }

        if (TokenScanner.isWord(firstSection) && TokenScanner.isWord(secondSection)) {

          if (!mapToCorrect.containsKey(firstSection.toLowerCase())) {
            Set<String> corrections = new TreeSet<>();
            corrections.add(secondSection.toLowerCase());
            mapToCorrect.put(firstSection.toLowerCase(), corrections);
          } else {
            mapToCorrect.get(firstSection.toLowerCase()).add(secondSection.toLowerCase());
          }
        } else {
          throw new FileCorrector.FormatException("Line contains nonwords");
        }
          }
        readLine = bf.readLine();
      }
    } catch (IOException e) {
      throw new IOException();
    }
  }

  /**
   * Constructs a FileCorrecotr from a file.
   *
   * @param filename Location of file from which to read
   * @return A FileCorrector with corrections from the argued file
   * @throws FileNotFoundException If the file does not exist
   * @throws IOException If error while reading
   * @throws FileCorrector.FormatException If an invalid line is encountered
   */
  public static FileCorrector make(String filename) throws IOException, FormatException {
    Reader r = new FileReader(filename);
    FileCorrector fc;

    try {
      fc = new FileCorrector(r);
    } finally {
      if (r != null) {
        r.close();
      }
    }

    return fc;
  }

  /**
   * Returns a set of proposed corrections for an incorrectly spelled word. The corrections should
   * match the case of the input; the matchCase method is helpful here.
   *
   * <p>For any input that is *not* a valid word, throw an IllegalArgumentException. A valid word is
   * any sequence of letters (as determined by Character.isLetter) or apostrophes characters.
   *
   * @param wrong The misspelled word
   * @return A (potentially empty) set of proposed corrections
   * @throws IllegalArgumentException If the input is not a valid word (i.e. not composed of only
   *     letters and/or apostrophes)
   */
  public Set<String> getCorrections(String wrong) {
    Set<String> corrections = new TreeSet<String>();
          if (wrong == null || !TokenScanner.isWord(wrong)) {
      throw new IllegalArgumentException();
    }
      String searchableWrong = wrong.toLowerCase();
    if (mapToCorrect.containsKey(searchableWrong)) {
      corrections = matchCase(wrong, mapToCorrect.get(searchableWrong));
    }
    return corrections;
  }
}
