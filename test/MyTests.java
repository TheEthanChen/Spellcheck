import static org.junit.Assert.*;

import org.junit.*;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.*;
import java.lang.*;





/** Put your OWN test cases in this file, for all classes in the assignment. */
public class MyTests {
    
        private Set<String> makeSet(String[] strings) {
        Set<String> mySet = new TreeSet<>();
        for (String s : strings) {
            mySet.add(s);
        }

        return mySet;
    }

  @Test
  public void testTokenScannerEmpty() throws IOException {
        Reader in = new StringReader(""); 
        TokenScanner d = new TokenScanner(in);
        try {
            assertFalse(d.hasNext());
        } finally {
            in.close();
        }
    }

  @Test
  public void testTokenScannerNull() {
    Reader in;
    try {
      in = new StringReader(null);
      TokenScanner d = new TokenScanner(in);
      in.close();
    } catch (IOException e) {
      return;
    } catch (NullPointerException e) {
        return;
    }
    fail();
  }

      @Test
      public void testTokenScannerError() throws IOException{
          try {
        Reader in = new StringReader("-1");
        TokenScanner d = new TokenScanner(in);
          } catch (IOException e) {
              return;
          }
      }

    @Test
    public void testTokenScannerSingleWordToken() throws IOException {
      Reader in = new StringReader("word");
      TokenScanner d = new TokenScanner(in);
      try {
        assertTrue("has next", d.hasNext());
        assertEquals("word", d.next());

        assertFalse("reached end of stream", d.hasNext());
      } finally {
        in.close();
      }
    }

    @Test
    public void testTokenScannerSingleNonWordToken() throws IOException {
      Reader in = new StringReader("!");
      TokenScanner d = new TokenScanner(in);
      try {
        assertTrue("has next", d.hasNext());
        assertEquals("!", d.next());

        assertFalse("reached end of stream", d.hasNext());
      } finally {
        in.close();
      }
    }

    @Test
    public void testTokenScannerEndsWithWord() throws IOException {
      Reader in = new StringReader("An Apple");
      TokenScanner d = new TokenScanner(in);
      try {
        assertTrue("has next", d.hasNext());
        assertEquals("An", d.next());

        assertTrue("has next", d.hasNext());
        assertEquals(" ", d.next());

        assertTrue("has next", d.hasNext());
        assertEquals("Apple", d.next());

        assertFalse("reached end of stream", d.hasNext());
      } finally {
        in.close();
      }
    }

    @Test
    public void testTokenScannerEndsWithNonWord() throws IOException {
        Reader in = new StringReader("An Apple!");
        TokenScanner d = new TokenScanner(in);
      try {
        assertTrue("has next", d.hasNext());
        assertEquals("An", d.next());

        assertTrue("has next", d.hasNext());
        assertEquals(" ", d.next());

        assertTrue("has next", d.hasNext());
        assertEquals("Apple", d.next());

        assertTrue("has next", d.hasNext());
        assertEquals("!", d.next());

        assertFalse("reached end of stream", d.hasNext());
      } finally {
        in.close();
      }
    }
    
        @Test
    public void testIsWordCharacter() {
        assertTrue(TokenScanner.isWordCharacter('A'));
        assertFalse(TokenScanner.isWordCharacter('1'));
    }
    
    
    
    @Test(timeout=500)
    public void testDictionaryNumberWords() throws IOException {
        Dictionary d = Dictionary.make("files/smallDictionary.txt");
        assertEquals(32, d.getNumWords());
    }
    
    @Test(timeout=500)
    public void testDictionarySpaceNotWord() throws IOException {
        Dictionary d = Dictionary.make("files/smallDictionary.txt");
        assertFalse("space is not in the file", d.isWord(""));
    }
    
    @Test(timeout=500)
    public void testDictionaryNullNotWord() throws IOException {
        Dictionary d = Dictionary.make("files/smallDictionary.txt");
        assertFalse("null is not in the file", d.isWord(null));
    }
    
    @Test(timeout=500)
    public void testDictionaryAllCaps() throws IOException {
        Dictionary d = Dictionary.make("files/testDictionary.txt");
        assertTrue("All Caps Dog in file", d.isWord("DOG"));
    }
    
    @Test(timeout=500)
    public void testDictionaryMiddleBlankLine() throws IOException {
        Dictionary d = Dictionary.make("files/testDictionary.txt");
        assertTrue("Hello in file", d.isWord("Hello"));
        assertTrue("Goodbye in file", d.isWord("Goodbye"));
        assertTrue("Dog in file", d.isWord("Dog"));
    }
    
    @Test(timeout=500)
    public void testDictionaryContainsMixedCase() throws IOException {
        Dictionary d = Dictionary.make("files/testDictionary.txt");
        assertTrue("Mixed Case in file", d.isWord("Ender"));
    }
    
    @Test(timeout=500)
    public void testDictionaryContainsWhiteSpaceWord() throws IOException {
        Dictionary d = Dictionary.make("files/testDictionary.txt");
        assertTrue("Mixed Case in file", d.isWord("Bean"));
    }
    
    @Test(timeout=500)
    public void testDictionaryContainsDuplicate() throws IOException {
        Dictionary d = Dictionary.make("files/testDictionary.txt");
        assertEquals(10, d.getNumWords());
    }
    
    @Test(timeout=500)
    public void testDictionaryGetLastWord() throws IOException {
        Dictionary d = Dictionary.make("files/testDictionary.txt");
        assertTrue("Last Word in File", d.isWord("trouble"));
    }
    
    @Test(timeout=500)
    public void testDictionaryEmptyFile() throws IOException {
        try {
        Dictionary d = Dictionary.make("files/testEmptyFile.txt");
        } catch(IOException e) {
            return;
        }
    }
    
    @Test(timeout=500)
    public void testDictionaryNonExistent() throws IOException {
        try {
        Dictionary d = Dictionary.make("files/nonexistentFile.txt");
        } catch(FileNotFoundException e) {
            return;
        }
    }
    
    @Test
    public void testFileCorrectorExtraWhiteSpace() throws IOException, FileCorrector.FormatException{
        FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
        Set<String> testCorrections = new TreeSet<>();
        testCorrections.add("fire");
        assertEquals(testCorrections, fc.getCorrections("frie"));
    }
    
    @Test
    public void testFileCorrectorCorrect() throws IOException, FileCorrector.FormatException{
        FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
        Set<String> testCorrections = new TreeSet<>();
        assertEquals(testCorrections, fc.getCorrections("fire"));
    }
    
    @Test
    public void testFileCorrectorMutipleSuggestions() throws IOException, FileCorrector.FormatException{
        FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
        Set<String> testCorrections = new TreeSet<>();
        testCorrections.add("said");
        testCorrections.add("sass");
        testCorrections.add("sad");
        assertEquals(testCorrections, fc.getCorrections("sasd"));
    }
    
    @Test
    public void testFileCorrectorNoCorrections() throws IOException, FileCorrector.FormatException {
        FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
        Set<String> testCorrections = new TreeSet<>();
        assertEquals(testCorrections, fc.getCorrections("mala"));
    }
    
    @Test
    public void testFileCorrectorMixedCase() throws IOException, FileCorrector.FormatException{
        FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
        Set<String> testCorrections1 = new TreeSet<>();
        testCorrections1.add("Balloon");
        Set<String> testCorrections2 = new TreeSet<>();
        testCorrections2.add("laity");
        Set<String> testCorrections3 = new TreeSet<>();
        testCorrections3.add("Mouse");
        assertEquals(testCorrections1, fc.getCorrections("BaLon"));
        assertEquals(testCorrections2, fc.getCorrections("lapty"));
        assertEquals(testCorrections3, fc.getCorrections("MoSuE"));
    }
    
    @Test
    public void testFileCorrectorAllUppercase() throws IOException, FileCorrector.FormatException {
        FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
        Set<String> testCorrections = new TreeSet<>();
        testCorrections.add("Tiger");
        assertEquals(testCorrections, fc.getCorrections("TGIER"));
    }
    
    @Test
    public void testFileCorrectorAllLowercase() throws IOException, FileCorrector.FormatException {
        FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
        Set<String> testCorrections = new TreeSet<>();
        testCorrections.add("tiger");
        assertEquals(testCorrections, fc.getCorrections("tgier"));
    }
    
    @Test
    public void testFileCorrectorFirstLetterCapitalized() throws IOException, FileCorrector.FormatException {
        FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
        Set<String> testCorrections = new TreeSet<>();
        testCorrections.add("Tiger");
        assertEquals(testCorrections, fc.getCorrections("Tgier"));
    }
    
    @Test
    public void testFileCorrectorFormatException() throws IOException, FileCorrector.FormatException {
        try {      
            Reader in1 = new StringReader(", correct");
            BufferedReader bf = new BufferedReader(in1);
            FileCorrector fc = new FileCorrector(bf);
        } catch (FileCorrector.FormatException e) {
            
        }
        try {
            Reader in2 = new StringReader("wrong, ");
            BufferedReader bf = new BufferedReader(in2);
            FileCorrector fc =  new FileCorrector(bf);
        } catch (FileCorrector.FormatException e) {
            
        }
        try {
            Reader in3 = new StringReader("wrong correct");
            BufferedReader bf = new BufferedReader(in3);
            FileCorrector fc = new FileCorrector(bf);
        } catch (FileCorrector.FormatException e) {
            
        }
        try {
            Reader in4 = new StringReader("wrong, correct,");
            BufferedReader bf = new BufferedReader(in4);
            FileCorrector fc = new FileCorrector(bf);
        } catch (FileCorrector.FormatException e) {
            return;
        }
    }
    
   @Test
    public void testFileCorrectorEmpty() throws IOException, FileCorrector.FormatException{
        try {
            Reader in1 = new StringReader("");
            BufferedReader bf = new BufferedReader(in1);
            FileCorrector fc = new FileCorrector(bf);
        } catch (IOException e) {
            return;
        }
    }
    
    @Test
    public void testFileCorrectorNull() throws IOException, FileCorrector.FormatException {
        try {
            FileCorrector fc = new FileCorrector(null);
        } catch (IllegalArgumentException e) {
            return;
        }
    }
    
    @Test
    public void testFileCorrectorMakeNonexistentFile() throws IOException, FileCorrector.FormatException {
        try {
        FileCorrector fc = FileCorrector.make("files/nonexistentFile.txt");
        } catch (FileNotFoundException e) {
            return;
        }
    }
    
    @Test
    public void testFileCorrectorMakeEmptyFile() throws IOException, FileCorrector.FormatException {
        try {
            FileCorrector fc = FileCorrector.make("files/testEmptyFile.txt");
        } catch (IOException e) {
            return;
        }
    }
    
    @Test
    public void testFileCorrectorMakeFormatException() throws IOException, FileCorrector.FormatException {
        try {
            FileCorrector fc = FileCorrector.make("files/FileCorrectorFormatException.txt");

        } catch (FileCorrector.FormatException e) {
            return;
        }
    }
    
    @Test
    public void testFileCorrectorGetCorrectionsNonWord() throws IOException, FileCorrector.FormatException{
        try {
          FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
          assertEquals("fail", fc.getCorrections("#$!!@"));
        } catch (IllegalArgumentException e) {
            return;
        }
    }
    
    @Test
    public void testFileCorrectorGetCorrectionsEmpty() throws IOException, FileCorrector.FormatException {
        try {
          FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
          assertEquals("fail", fc.getCorrections(""));
        } catch (IllegalArgumentException e) {
            return;
        }
    }
    
    @Test
    public void testFileCorrectorGetCorrectionsNull() throws IOException, FileCorrector.FormatException {
        try {
          FileCorrector fc = FileCorrector.make("files/testFileCorrector.txt");
          assertEquals("fail", fc.getCorrections(null));
        } catch (IllegalArgumentException e) {
            return;
        }
    }
    
    @Test
    public void testSwapCorrectionsNullDictionary() throws IOException {
        try {
            Dictionary d = new Dictionary(null);
            SwapCorrector swap = new SwapCorrector(d);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail();
    }
    
  @Test
  public void testSwapCorrectorNullReader() throws IOException, FileCorrector.FormatException {
    // Here's how to test expecting an exception...
    try {
      new SwapCorrector(null);
      fail("Expected an IllegalArgumentException - cannot create SwapCorrector with null.");
    } catch (IllegalArgumentException f) {
      // Do nothing. It's supposed to throw an exception
    }
  }
    
    
    @Test
    public void testSwapCorrectionsAllCaps() throws IOException {
        Reader reader = new FileReader("files/smallDictionary.txt");
        try {
            Dictionary d = new Dictionary(new TokenScanner(reader));
            SwapCorrector swap = new SwapCorrector(d);
            assertEquals("CYA -> {Cay}", makeSet(new String[]{"Cay"}), swap.getCorrections("CYA"));
        } finally {
            reader.close();
        }
    }
    
    @Test
    public void testSwapCorrectionsFirstLetterCapitalized() throws IOException {
        Reader reader = new FileReader("files/smallDictionary.txt");
        try {
            Dictionary d = new Dictionary(new TokenScanner(reader));
            SwapCorrector swap = new SwapCorrector(d);
            assertEquals("Cya -> {Cay}", makeSet(new String[]{"Cay"}), swap.getCorrections("Cya"));
        } finally {
            reader.close();
        }
    }
    
    @Test
    public void testSwapCorrectionsAlreadyCorrect() throws IOException {
        Reader reader = new FileReader("files/smallDictionary.txt");
        try {
            Dictionary d = new Dictionary(new TokenScanner(reader));
            SwapCorrector swap = new SwapCorrector(d);
            assertEquals("cay -> {}", makeSet(new String[]{}), swap.getCorrections("cay"));
        } finally {
            reader.close();
        }
    }
    
    @Test
    public void testSwapCorrectionsMixedCase() throws IOException {
        Reader reader = new FileReader("files/smallDictionary.txt");
        try {
            Dictionary d = new Dictionary(new TokenScanner(reader));
            SwapCorrector swap = new SwapCorrector(d);
            assertEquals("CyA -> {Cay}", makeSet(new String[]{"Cay"}), swap.getCorrections("CyA"));
        } finally {
            reader.close();
        }
    }
    
    @Test
    public void testSwapCorrectionsMultipleSuggestions() throws IOException {
        Reader reader = new FileReader("files/testSwapCorrectorDictionary.txt");
        try {
            Dictionary d = new Dictionary(new TokenScanner(reader));
            SwapCorrector swap = new SwapCorrector(d);
            assertEquals("haet -> {heat, hate, ahet}", makeSet(new String[]{"heat", "hate", "ahet"}), 
                         swap.getCorrections("haet"));
        } finally {
            reader.close();
        }
    }
    
    @Test
    public void testSwapCorrectionsGetCorrectionsNonWord() throws IOException {
        Reader reader = new FileReader("files/smallDictionary.txt");
        try {
            Dictionary d = new Dictionary(new TokenScanner(reader));
            SwapCorrector swap = new SwapCorrector(d);
            assertEquals("!@#@# not a word", makeSet(new String[]{""}), swap.getCorrections("!@#@#"));
        } catch (IllegalArgumentException e) {
            return;
        } finally {
            reader.close();
        }
    }
    
    
    
  /**
    * spellCheckFiles Runs the spell checker on some test input.  See the description of the
    * inputs below.
    *
    * @param fdict The filename of the dictionary
    * @param dictSize If the int dictSize is -1, it is ignored. Otherwise we check the size of the
    *                 dictionary after creating it, to make sure it was parsed correctly and the
    *                 tests will work.
    * @param fcorr The filename of the corrections to use, or null if the swap corrector should be
    *              used.
    * @param fdoc The filename of the document to check
    * @param fout The filename where the output should be written
    * @param finput The filename where the user input should be read from
    */
    public static void spellCheckFiles(String fdict, int dictSize, String fcorr, String fdoc,
        String fout, String finput)  throws IOException, FileCorrector.FormatException {
        
        Dictionary dict = Dictionary.make(fdict);
        Corrector corr = null;
        
        if (fcorr == null) {
            corr = new SwapCorrector(dict);
        } else {
            corr = FileCorrector.make(fcorr);
        }

        // Make sure the dictionary has expected number of entries. Otherwise, when spellchecking,
        // it might miss words and cause the simulated user input to fall out of sync.
        if (dictSize >= 0) {
            assertEquals("Dictionary size = " + dictSize, dictSize, dict.getNumWords());
        }

        FileInputStream input = new FileInputStream(finput);
        Reader in = new BufferedReader(new FileReader(fdoc));
        Writer out = new BufferedWriter(new FileWriter(fout));
        SpellChecker sc = new SpellChecker(corr,dict);

        sc.checkDocument(in, input, out);
        in.close();
        input.close();
        out.flush();
        out.close();
    }


    
    @Test(timeout=500)
    public void testCheckEmptyFile() throws IOException, FileCorrector.FormatException {
        try {
            spellCheckFiles("files/theFoxDictionary.txt", 7, "files/theFoxMisspellings.txt", 
                            "files/testEmptyFile", "foxout.txt", "files/theFox_goodinput.txt");
        compareDocs("foxout.txt", "files/theFox_expected_output.txt");
        } catch (IOException e) {
            return;
        }
    }


    /**
    * This is a helper method that compares two documents.
    */
    public static void compareDocs(String out, String expected)
        throws IOException, FileNotFoundException {

        BufferedReader f1 = new BufferedReader(new FileReader(out));
        BufferedReader f2 = new BufferedReader(new FileReader(expected));

        try {
            String line1 = f1.readLine();
            String line2 = f2.readLine();
            while (line1 != null && line2 != null) {
                assertEquals("Output file did not match expected output.", line2, line1);
                line1 = f1.readLine();
                line2 = f2.readLine();
            }

            if (line1 != null) {
                fail("Expected end of file, but found extra lines in the output.");
            } else if (line2 != null) {
                fail("Expected more lines, but found end of file in the output. ");
            }
        } finally {
            f1.close();
            f2.close();
            
        }
    }
    
}
