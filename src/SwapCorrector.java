
import java.util.*;
/**
 * A Corrector whose spelling suggestions come from "swapped letter" typos.
 * <p>
 * A common misspelling is accidentally swapping two adjacent letters, e.g. "with" -> "wiht". This
 * Corrector is given a Dictionary of valid words and proposes corrections that are precisely one
 * "swap" away from the incorrect word.
 * <p>
 * For example, if the incorrect word is "haet", then this Corrector might suggest "heat" and
 * "hate", provided that both of these words occur in the dictionary.
 * <p>
 * <strong>Only swaps between adjacent letters are considered by this corrector.</strong>
 */
public class SwapCorrector extends Corrector {
    private Dictionary dict;

    /**
     * Constructs a SwapCorrector using the argued Dictionary.
     *
     * @param dict The reference dictionary to use to look for corrections arising from letter swaps
     * @throws IllegalArgumentException If the argued Dictionary is null
     */
    public SwapCorrector(Dictionary dict) {
         if (dict == null) {
             throw new IllegalArgumentException();
         }
        this.dict = dict;
    }

    /**
     * Suggests as corrections any words in the Dictionary (provided when this object is constructed)
     * that are one swap away from the input word. A swap is defined as two adjacent letters
     * exchanging positions.
     * <p>
     * For example, if the Dictionary contains the words "heat" and "hate", then both should be
     * returned if the input wrong word is "haet".
     * <p>
     * If the provided word is already spelled correctly, then the empty set should be returned. 
     *
     * The corrections should match the case of the input; the matchCase method is helpful here.
     *
     * For any input that is *not* a valid word, throw an IllegalArgumentException. A valid word is
     * any sequence of letters (as determined by Character.isLetter) or apostrophes characters.
     *
     * @param wrong The misspelled word
     * @return A (potentially empty) set of proposed corrections
     * @throws IllegalArgumentException If the input is not a valid word (i.e. not composed of only
     *                                  letters and/or apostrophes) 
     */
    public Set<String> getCorrections(String wrong) {
        Set<String> corrections = new TreeSet<>();
        if (!TokenScanner.isWord(wrong) || wrong == null) {
            throw new IllegalArgumentException();
        }
        
        String searchableWord = wrong.toLowerCase();
        
        for (int i = 1; i < wrong.length(); i++) {
            String compareWord = searchableWord;
            char firstSwap = searchableWord.charAt(i - 1);
            char secondSwap = searchableWord.charAt(i);
            compareWord = searchableWord.substring(0, i - 1) + secondSwap + 
                firstSwap + searchableWord.substring(i + 1, wrong.length());
            if (dict.isWord(compareWord)) {
                corrections.add(compareWord);
            }
        }
        
        return matchCase(wrong, corrections);
    }
}
