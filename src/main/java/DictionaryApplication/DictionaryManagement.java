package DictionaryApplication;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

import Trie.Trie;

public class DictionaryManagement {
	private Trie trie;
	private Scanner sc;
	private DictionaryCommandline dictionaryCommandline;

	public DictionaryManagement() {
		this.sc = new Scanner(System.in);
		this.trie = new Trie();
		this.dictionaryCommandline = new DictionaryCommandline();
	}

	public void insertFromCommandline(Dictionary dictionary) {
		int n = 0;

		do {
			System.out.print("Nhap vao ban phim so luong tu vung n (n>0): ");
			n = sc.nextInt();
		} while (n <= 0);

		sc.nextLine();

		System.out.println("Nhap vao du lieu tu vung: ");
		for (int i = 0; i < n; i++) {
			System.out.println("Tu so " + (i + 1) + ":");
			System.out.print("Nhap tu tieng anh : ");
			String en = sc.nextLine();
			System.out.print("Nhap giai thich bang tieng viet: ");
			String vi = sc.nextLine();
			Word word = new Word(en, vi);
			dictionary.add(word);
		}
	}

	private void removeDuplicates(Dictionary dictionary) {
        Set<String> uniqueWords = new HashSet<>();
        List<Word> nonDuplicateWords = new ArrayList<>();

        for (Word w : dictionary) {
            String wordTarget = w.getWord_target().toLowerCase();
            if (!uniqueWords.contains(wordTarget)) {
                uniqueWords.add(wordTarget);
                nonDuplicateWords.add(w);
            }
        }

        dictionary.clear();
        dictionary.addAll(nonDuplicateWords);
    }
	
	public void insertFromFile(Dictionary dictionary, String filePath) {
		File file = new File(filePath);
		try {
			List<String> wordList = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			for (String str : wordList) {
				String[] words = str.split("\t");
				String en = words[0];
				String vi = words[1];
				Word word = new Word(en, vi);
				dictionary.add(word);
			}
			removeDuplicates(dictionary);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Word searchWord(Dictionary dictionary, String keyWord) {
		try {
			dictionary.sort(new SortDictionaryByWord());
			int left = 0;
			int right = dictionary.size() - 1;
			while (left <= right) {
				int mid = left + (right - left) / 2;
				int res = dictionary.get(mid).getWord_target().compareToIgnoreCase(keyWord.trim());
				if (res == 0)
					return dictionary.get(mid);
				if (res <= 0)
					left = mid + 1;
				else
					right = mid - 1;
			}
		} catch (NullPointerException e) {
			System.out.println("Null Exception.");
		}
		return null;
	}

	public void dictionaryLookup(Dictionary dictionary) {
		System.out.print("Nhap tu can tra cuu: ");
		String lookupWord = sc.nextLine();
		if(!dictionary.contains(searchWord(dictionary, lookupWord))) {
			System.out.println("Khong co tu \"" + lookupWord + "\" trong tu dien! Vui long tra cuu dung tu!");
			return;
		}
		Word word = searchWord(dictionary, lookupWord);
		Dictionary lookupWordList= new Dictionary();
		lookupWordList.add(word);
		dictionaryCommandline.showAllWords(lookupWordList);
	}

	public void addWord(Dictionary dictionary) {
		System.out.print("Nhap tu tieng anh : ");
		String en = sc.nextLine();
		if (dictionary.contains(searchWord(dictionary, en))) {
			System.out.println("Tu nay da xuat hien trong tu dien (vui long them tu khac hoac cap nhat tu)!");
			return;
		}
		System.out.print("Nhap giai thich bang tieng viet: ");
		String vi = sc.nextLine();
		Word word = new Word(en, vi);
		dictionary.add(word);
		System.out.println("Da them \"" + en + "\" vao tu dien.");
	}

	public void updateWord(Dictionary dictionary) {
		System.out.print("Nhap tu can sua: ");
		String updateWordTaget = sc.nextLine();
		if (!dictionary.contains(searchWord(dictionary, updateWordTaget))) {
			System.out.println("Khong tim thay \"" + updateWordTaget + "\" trong tu dien, hay them vao tu dien!");
			return;
		}
		System.out.print("Nhap nghia moi cua tu: ");
		String meaning = sc.nextLine();
		Word updateWord = searchWord(dictionary, updateWordTaget.trim());
		dictionary.remove(updateWord);
		updateWord.setWord_explain(meaning);
		dictionary.add(updateWord);
		System.out.println("Da cap nhat \"" + updateWordTaget + "\" trong tu dien.");
	}

	public void deleteWord(Dictionary dictionary) {
		System.out.print("Nhap tu can xoa: ");
		String deleteWord = sc.nextLine();
		if (!dictionary.contains(searchWord(dictionary, deleteWord))) {
			System.out.println("Khong tim thay \"" + deleteWord + "\" trong tu dien, hay them vao tu dien!");
			return;
		}
		Word word = searchWord(dictionary, deleteWord.trim());
		dictionary.remove(word);
		System.out.println("Da xoa \"" + deleteWord + "\" khoi tu dien.");
	}
	
	public void dictionaryExportToFile(Dictionary dictionary, String filePath) {
		insertFromFile(dictionary, filePath);
        try {
        	BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            for (Word w : dictionary) {
                String str = (w.getWord_target() + "\t" + w.getWord_explain()).trim().toLowerCase();
                bw.write(str);
                bw.newLine();
            }
        	bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
