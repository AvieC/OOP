package DictionaryApplication;

import java.util.Comparator;

public class SortDictionaryByWord implements Comparator<Word> {
    @Override
    public int compare(Word word1, Word word2) {
        return word1.getWord_target().compareTo(word2.getWord_target());
    }
}
