package Controllers;

import DictionaryApplication.Dictionary;
import DictionaryApplication.DictionaryManagemantApp;
import DictionaryApplication.Word;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Alerts.Alerts;

public class AdditionController implements Initializable {
	private Dictionary dictionary = new Dictionary();
	private DictionaryManagemantApp dictionaryManagement = new DictionaryManagemantApp();
	private final String path = "D:\\Documents\\Course3\\oop\\DictionaryApp1\\src\\main\\resources\\Utils\\dictionaries.txt";
	private Alerts alerts = new Alerts();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		dictionaryManagement.insertFromFile(dictionary, path);
		if (explanationInput.getText().isEmpty() || wordTargetInput.getText().isEmpty()) {
			addBtn.setDisable(true);
		}

		wordTargetInput.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (explanationInput.getText().trim().equals("") || wordTargetInput.getText().trim().equals("")) {
					addBtn.setDisable(true);
				} else {
					addBtn.setDisable(false);
				}
			}
		});
		

		explanationInput.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (explanationInput.getText().trim().equals("") || wordTargetInput.getText().trim().equals(""))
					addBtn.setDisable(true);
				else
					addBtn.setDisable(false);
			}
		});

		successAlert.setVisible(false);
	}

	@FXML
	private void handleClickAddBtn() {
		Alert alertConfirmation = alerts.alertConfirmation("Add word", "Bạn chắc chắn muốn thêm từ này?");
		Optional<ButtonType> option = alertConfirmation.showAndWait();
		String englishWord = wordTargetInput.getText().trim();
		String meaning = explanationInput.getText().trim();

		if (option.get() == ButtonType.OK) {
			Word word = new Word(englishWord, meaning);
			if (dictionary.contains(word)) {
				int indexOfWord = dictionaryManagement.searchWord(dictionary, englishWord);
				Alert selectionAlert = alerts.alertConfirmation("This word already exists",
						"Từ này đã tồn tại.\nThay thế hoặc bổ sung nghĩa vừa nhập cho nghĩa cũ.");
				selectionAlert.getButtonTypes().clear();
				ButtonType replaceBtn = new ButtonType("Thay thế");
				ButtonType insertBtn = new ButtonType("Bổ sung");
				selectionAlert.getButtonTypes().addAll(replaceBtn, insertBtn, ButtonType.CANCEL);
				Optional<ButtonType> selection = selectionAlert.showAndWait();

				if (selection.get() == replaceBtn) {
					dictionary.get(indexOfWord).setWord_explain(meaning);
					dictionaryManagement.dictionaryExportToFile(dictionary, meaning);
					showSuccessAlert();
				}
				if (selection.get() == insertBtn) {
					String oldMeaning = dictionary.get(indexOfWord).getWord_explain();
					dictionary.get(indexOfWord).setWord_explain(oldMeaning + "\n= " + meaning);;
					dictionaryManagement.dictionaryExportToFile(dictionary, meaning);
					showSuccessAlert();
				}
				if (selection.get() == ButtonType.CANCEL)
					alerts.showAlertInfo("Information", "Thay đổi không được công nhận.");
			} else {
				dictionary.add(word);
				dictionaryManagement.addWord(word, path);
				showSuccessAlert();
			}
			addBtn.setDisable(true);
			resetInput();
		} else if (option.get() == ButtonType.CANCEL)
			alerts.showAlertInfo("Information", "Thay đổi không được công nhận.");
	}

	private void resetInput() {
		wordTargetInput.setText("");
		explanationInput.setText("");
	}

	private void showSuccessAlert() {
		successAlert.setVisible(true);
		dictionaryManagement.setTimeout(() -> successAlert.setVisible(false), 1500);
	}

	@FXML
	private Button addBtn;

	@FXML
	private TextField wordTargetInput;

	@FXML
	private TextArea explanationInput;

	@FXML
	private Label successAlert;
}
