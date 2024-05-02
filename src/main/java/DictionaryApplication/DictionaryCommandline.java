package DictionaryApplication;

import java.util.*;

public class DictionaryCommandline {
	public void showAllWords(Dictionary dictionary) {
		int no = 1;
		
		Collections.sort(dictionary, new Comparator<Word>() {
			@Override
			public int compare(Word o1, Word o2) {
				if (o1.getWord_target().toLowerCase().compareTo(o2.getWord_target().toLowerCase()) < 0)
					return -1;
				else if (o1.getWord_target().toLowerCase().compareTo(o2.getWord_target().toLowerCase()) > 0)
					return 1;
				else
					return 0;
			}
		});
		
		System.out.println(String.format("%-5s|\t%-20s|\t%s", "No", "English", "Vietnamese"));
		for (Word w : dictionary) {
			System.out.println(String.format("%-5d|\t%-20s|\t%s", no, w.getWord_target(), w.getWord_explain()));
			no++;
		}
	}
}
