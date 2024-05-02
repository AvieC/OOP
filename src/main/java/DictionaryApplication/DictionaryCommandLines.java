package DictionaryApplication;

import java.io.FileNotFoundException;
import java.util.*;

public class DictionaryCommandLines {
	private Dictionary dictionary;
	private DictionaryManagement dictionaryManagement;
	private DictionaryCommandline dictionaryCommandline;

	public DictionaryCommandLines() {
		this.dictionary = new Dictionary();
		this.dictionaryManagement = new DictionaryManagement();
		this.dictionaryCommandline = new DictionaryCommandline();
	}

	private void dictionaryBasic() {
		dictionaryManagement.insertFromCommandline(dictionary);
		dictionaryCommandline.showAllWords(dictionary);
	}

	private void dictionarySearcher(Dictionary dictionary) {
		List<Word> wordList = new ArrayList<Word>();
		Scanner sc = new Scanner(System.in);
		int no = 1;
		String word = "";

		System.out.print("\nNhap tu can tra: ");
		word = sc.nextLine();
		word = word.trim().toLowerCase();

		for (Word w : dictionary) {
			if (w.getWord_target().toLowerCase().indexOf(word) == 0) {
				wordList.add(w);
			}
		}

		if (wordList.isEmpty()) {
			System.out.println("Khong co tu nao!");
		} else {
			System.out.println("Danh sach tu can tra: ");
			String.format("%-5s|\t%-20s|\t%s", "No", "English", "Vietnamese");
			for (Word w : wordList) {
				System.out.println(String.format("%-5d|\t%-20s|\t%s", no, w.getWord_target(), w.getWord_explain()));
				no++;
			}
		}

	}

	private void dictionaryAdvanced() {
		Scanner sc = new Scanner(System.in);
		int choice = 0;
		String filePath = "D:\\Documents\\Course3\\oop\\DictionaryApp1\\"
				+ "src\\main\\resources\\Utils\\dictionaries1.txt";

		do {
			System.out.println("\nWelcome to My Application!");
			System.out.println("[0] Exit");
			System.out.println("[1] Add");
			System.out.println("[2] Remove");
			System.out.println("[3] Update");
			System.out.println("[4] Display");
			System.out.println("[5] Lookup");
			System.out.println("[6] Search");
			System.out.println("[7] Game");
			System.out.println("[8] Import from file");
			System.out.println("[9] Export to file");
			System.out.print("Your action: ");

			try {
				choice = sc.nextInt();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Action not supported!");
				System.exit(0);
			}

			if (choice < 0 || choice > 9) {
				System.out.println("Action not supported!");
				System.exit(0);
			}

			sc.nextLine();

			switch (choice) {
			case 0:
				System.out.println("Ban da thoat khoi chuong trinh!");
				break;
			case 1:
				dictionaryManagement.addWord(dictionary);
				break;
			case 2:
				dictionaryManagement.deleteWord(dictionary);
				break;
			case 3:
				dictionaryManagement.updateWord(dictionary);
				break;
			case 4:
				dictionaryCommandline.showAllWords(dictionary);
				break;
			case 5:
				dictionaryManagement.dictionaryLookup(dictionary);
				break;
			case 6:
				this.dictionarySearcher(dictionary);
				break;
			case 7:
				System.out.println("\n----------Start Game----------\n");
				try {
					String gameFilePath = "D:\\Documents\\Course3\\oop\\DictionaryApp1\\src"
							+ "\\main\\resources\\Utils\\questions.txt";
					EnglishQuizGame game = new EnglishQuizGame(gameFilePath);
					game.startGame();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case 8:
				dictionaryManagement.insertFromFile(dictionary, filePath);;
				break;
			case 9:
				dictionaryManagement.dictionaryExportToFile(dictionary, filePath);;
				break;

			default:
				System.out.println("Your action: ");
				break;
			}

		} while (choice != 0);
	}

	public static void main(String[] args) {
		DictionaryCommandLines d = new DictionaryCommandLines();
		
		d.dictionaryAdvanced();
	}
}
