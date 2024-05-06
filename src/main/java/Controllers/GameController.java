package Controllers;

import javafx.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import DictionaryApplication.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class GameController implements Initializable {
	private static final String MULTIPLE_CHOICE = "MC";
	private static final String FILL_IN_THE_BLANK = "FIB";
	private static final String SHORT_ANSWER = "SA";
	private int currentQuestionIndex = 0;
	private int score = 0;
	private int maxQuestions = 5; // Số câu hỏi tối đa
	private int answerTime = 30; // Thời gian trả lời mỗi câu hỏi là 30 giây
	private boolean hasAnswered = false;
	private Timer timer;
	private List<Question> questions;
	private List<answeredQuestion> answerQuestions;
	private EnglishQuizGame Egame;
	private String gameFilePath = "D:\\Documents\\Course3\\oop\\DictionaryApp\\src"
			+ "\\main\\resources\\Utils\\questions.txt";

	public GameController() throws FileNotFoundException {
		Egame = new EnglishQuizGame(gameFilePath);
		questions = Egame.getQuestions();
		Collections.shuffle(questions);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Question question = questions.get(currentQuestionIndex);
		questionLabel.setText("Question " + (currentQuestionIndex + 1) + ": " + question.getQuestionText());
		if (question.getType().equals(MULTIPLE_CHOICE)) {
			answerField.setVisible(false);
			answerTitle.setVisible(false);
			submitButton.setVisible(false);

			optionA.setVisible(true);
			optionB.setVisible(true);
			optionC.setVisible(true);
			optionD.setVisible(true);

			optionA.setText(question.getOptions()[0]);
			optionB.setText(question.getOptions()[1]);
			optionC.setText(question.getOptions()[2]);
			optionD.setText(question.getOptions()[3]);
		} else if (question.getType().equals(FILL_IN_THE_BLANK) || question.getType().equals(SHORT_ANSWER)) {
			optionA.setVisible(false);
			optionB.setVisible(false);
			optionC.setVisible(false);
			optionD.setVisible(false);

			answerField.setVisible(true);
			answerTitle.setVisible(true);
			submitButton.setVisible(true);

			answerField.setOnKeyTyped(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent keyEvent) {
					submitButton.setDisable(true);
					if (answerField.getText().trim().equals(""))
						submitButton.setDisable(true);
					else
						submitButton.setDisable(false);
				}
			});
		}

		questionLabel.setMinHeight(72.0);

		// Thêm một ChangeListener để theo dõi khi chiều cao của label thay đổi
		questionLabel.heightProperty().addListener((obs, oldHeight, newHeight) -> {
			if (newHeight.doubleValue() > questionLabel.getMinHeight()) {
				questionLabel.setMinHeight(newHeight.doubleValue());
			}
		});

		scoreLabel.setText("Score: " + score);
		correctLabel.setVisible(false);
		submitButton.setDisable(true);
		incorrectLabel.setVisible(false);
		correctAnswerTitle.setVisible(false);
		correctAnswerContent.setVisible(false);
		answerField.clear();
		startTimer();
	}

	private void startTimer() {
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		answerTime = 5; // Thiết lập thời gian đếm ngược bắt đầu từ 5 giây
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					if (answerTime >= 0) {
						timerLabel.setText(answerTime + "");
						answerTime--;
					} else {
						timer.cancel();
						timer = null;
						checkAnswer(null);
					}
				});
			}
		}, 0, 1000);
	}

	@FXML
	private void handleOptionAction(ActionEvent event) {
		if (timer != null) {
			timer.cancel();
		}
		if (!hasAnswered) {
			Button clickedButton = (Button) event.getSource();
			checkAnswer(clickedButton);
		}
	}

	private void checkAnswer(Button selectedButton) {
		if (hasAnswered)
			return;
		hasAnswered = true;

		if (timer != null) {
			timer.cancel(); // Hủy timer hiện tại khi câu trả lời đã được gửi
		}
		Question currentQuestion = questions.get(currentQuestionIndex);

		if (currentQuestion.getType().equals(MULTIPLE_CHOICE)) {
			if (selectedButton != null) {
				String selectedAnswer = selectedButton.getText().substring(0, 1);
				Button correctButton = null;

				// Xác định nút nào chứa đáp án đúng
				if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionA.getText().substring(0, 1)))
					correctButton = optionA;
				else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionB.getText().substring(0, 1)))
					correctButton = optionB;
				else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionC.getText().substring(0, 1)))
					correctButton = optionC;
				else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionD.getText().substring(0, 1)))
					correctButton = optionD;
				if (selectedAnswer.equalsIgnoreCase(currentQuestion.getCorrectAnswer())) {
					score++;
					scoreLabel.setText("Score: " + score);
					correctButton.setStyle("-fx-background-color: #21e833;"); // Đáp án đúng
				} else {
					correctButton.setStyle("-fx-background-color: #21e833;"); // Hiển thị đáp án đúng
					selectedButton.setStyle("-fx-background-color: #ff4747 ;"); // Hiển thị đáp án sai
					new Thread(() -> {
						try {
							Thread.sleep(2000); // Đợi 2 giây
							Platform.runLater(this::endGame);
						} catch (InterruptedException ex) {
							Thread.currentThread().interrupt();
						}
					}).start();
					return;
				}
			} else {
				Button correctButton = null;

				// Xác định nút nào chứa đáp án đúng
				if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionA.getText().substring(0, 1)))
					correctButton = optionA;
				else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionB.getText().substring(0, 1)))
					correctButton = optionB;
				else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionC.getText().substring(0, 1)))
					correctButton = optionC;
				else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionD.getText().substring(0, 1)))
					correctButton = optionD;

				correctButton.setStyle("-fx-background-color: #21e833;"); // Hiển thị đáp án đúng

			}

//			answeredQuestion userAnswer = new answeredQuestion(MULTIPLE_CHOICE, currentQuestion.getQuestionText(),
//					currentQuestion.getOptions(), currentQuestion.getCorrectAnswer(),
//					selectedButton.getText().substring(0, 1));
//			answerQuestions.add(userAnswer);

			prepareNextQuestion();
		} else if (currentQuestion.getType().equals(FILL_IN_THE_BLANK)
				|| currentQuestion.getType().equals(SHORT_ANSWER)) {
			String fillAnswer = answerField.getText();
			if (fillAnswer.equals("")) {
				if (timer == null) {
					submitButton.setVisible(false);
					incorrectLabel.setVisible(false);
					correctAnswerTitle.setVisible(true);
					correctAnswerContent.setText(currentQuestion.getCorrectAnswer());
					correctAnswerContent.setVisible(true);
					answerField.setVisible(false);
					answerTitle.setVisible(false);
				}
			} else {
				if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(fillAnswer)) {
					correctLabel.setVisible(true);
					score++;
					scoreLabel.setText("Score: " + score);
				} else {
					incorrectLabel.setVisible(true);
					correctAnswerTitle.setVisible(true);
					correctAnswerContent.setText(currentQuestion.getCorrectAnswer());
					correctAnswerContent.setVisible(true);
					answerField.setVisible(false);
					answerTitle.setVisible(false);
					new Thread(() -> {
						try {
							Thread.sleep(3000); // Đợi 2 giây
							Platform.runLater(this::endGame);
						} catch (InterruptedException ex) {
							Thread.currentThread().interrupt();
						}
					}).start();
					return;
				}
			}
			prepareNextQuestion();

//			answeredQuestion userAnwer;
//			if (currentQuestion.getType().equals(FILL_IN_THE_BLANK)) {
//				userAnwer = new answeredQuestion(FILL_IN_THE_BLANK, currentQuestion.getQuestionText(),
//						currentQuestion.getOptions(), currentQuestion.getCorrectAnswer(), fillAnswer);
//				answerQuestions.add(userAnwer);
//			} else if (currentQuestion.getType().equals(SHORT_ANSWER)) {
//				userAnwer = new answeredQuestion(SHORT_ANSWER, currentQuestion.getQuestionText(),
//						currentQuestion.getOptions(), currentQuestion.getCorrectAnswer(), fillAnswer);
//				answerQuestions.add(userAnwer);
//			}
		}

	}

//	@FXML
//	private void handleReviewAction(ActionEvent event) {
//		Button clickedButton = (Button) event.getSource();
//		showAnsweredQuestion(clickedButton);
//	}

//	private void showAnsweredQuestion(Button nextButton) {
//		submitButton.setVisible(false);
//		scoreLabel.setVisible(false);
//		timerLabel.setVisible(false);
//
//		int currentIndex = 0;
//		answeredQuestion userAnswer = answerQuestions.get(currentIndex);
//		if (userAnswer.getType().equals(MULTIPLE_CHOICE)) {
//			Button correctButton = null;
//			Button inCorrectButton = null;
//
//			answerField.setVisible(false);
//			answerTitle.setVisible(false);
//			submitButton.setVisible(false);
//			correctAnswerContent.setVisible(false);
//			correctAnswerTitle.setVisible(false);
//			correctLabel.setVisible(false);
//			incorrectLabel.setVisible(false);
//
//			questionLabel.setVisible(true);
//			optionA.setVisible(true);
//			optionB.setVisible(true);
//			optionC.setVisible(true);
//			optionD.setVisible(true);
//
//			questionLabel.setText(userAnswer.getQuestionText());
//			optionA.setText(userAnswer.getOptions()[0]);
//			optionB.setText(userAnswer.getOptions()[1]);
//			optionC.setText(userAnswer.getOptions()[2]);
//			optionD.setText(userAnswer.getOptions()[3]);
//
//			// Xác định nút nào chứa đáp án đúng
//			if (userAnswer.getCorrectAnswer().equalsIgnoreCase(optionA.getText().substring(0, 1)))
//				correctButton = optionA;
//			else if (userAnswer.getCorrectAnswer().equalsIgnoreCase(optionB.getText().substring(0, 1)))
//				correctButton = optionB;
//			else if (userAnswer.getCorrectAnswer().equalsIgnoreCase(optionC.getText().substring(0, 1)))
//				correctButton = optionC;
//			else if (userAnswer.getCorrectAnswer().equalsIgnoreCase(optionD.getText().substring(0, 1)))
//				correctButton = optionD;
//
//			// Xác định nút nào chứa đáp án sai
//			if (userAnswer.getSelectedAnswer().equalsIgnoreCase(optionA.getText().substring(0, 1)))
//				inCorrectButton = optionA;
//			else if (userAnswer.getSelectedAnswer().equalsIgnoreCase(optionB.getText().substring(0, 1)))
//				inCorrectButton = optionB;
//			else if (userAnswer.getSelectedAnswer().equalsIgnoreCase(optionC.getText().substring(0, 1)))
//				inCorrectButton = optionC;
//			else if (userAnswer.getSelectedAnswer().equalsIgnoreCase(optionD.getText().substring(0, 1)))
//				inCorrectButton = optionD;
//
//			if (userAnswer.getCorrectAnswer().equalsIgnoreCase(userAnswer.getSelectedAnswer())
//					|| userAnswer.getSelectedAnswer() == null) {
//				correctButton.setStyle("-fx-background-color: #21e833;");
//			} else {
//				correctButton.setStyle("-fx-background-color: #21e833;"); // Hiển thị đáp án đúng
//				inCorrectButton.setStyle("-fx-background-color: #ff4747 ;"); // Hiển thị đáp án sai
//			}
//		} else if (userAnswer.getType().equals(FILL_IN_THE_BLANK) || userAnswer.getType().equals(SHORT_ANSWER)) {
//			optionA.setVisible(false);
//			optionB.setVisible(false);
//			optionC.setVisible(false);
//			optionD.setVisible(false);
//
//			questionLabel.setVisible(true);
//
//			if (userAnswer.getCorrectAnswer().equalsIgnoreCase(userAnswer.getSelectedAnswer())) {
//				answerTitle.setVisible(true);
//				correctAnswerContent.setVisible(true);
//				correctAnswerContent.setText(userAnswer.getCorrectAnswer());
//				correctLabel.setVisible(true);
//			} else {
//				incorrectLabel.setVisible(true);
//				correctAnswerTitle.setVisible(true);
//				correctAnswerContent.setVisible(true);
//				correctAnswerContent.setText(userAnswer.getCorrectAnswer());
//			}
//		}
//	}

	private void prepareNextQuestion() {
		// Chờ vài giây để người chơi xem kết quả, sau đó hiển thị câu hỏi tiếp theo
		new Thread(() -> {
			try {
				Thread.sleep(2000); // Đợi 2 giây
				Platform.runLater(this::displayNextQuestion);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}

	private void resetButtonColors() {
		optionA.setStyle("-fx-background-color: #1dfee8;");
		optionB.setStyle("-fx-background-color: #1dfee8;");
		optionC.setStyle("-fx-background-color: #1dfee8;");
		optionD.setStyle("-fx-background-color: #1dfee8;");

		correctLabel.setVisible(false);
		answerField.clear();
		correctAnswerContent.setVisible(false);
		correctAnswerTitle.setVisible(false);
	}

	private void displayNextQuestion() {
		currentQuestionIndex++;
		startTimer();
		if (currentQuestionIndex < maxQuestions) {
			hasAnswered = false;
			resetButtonColors();
			Question question = questions.get(currentQuestionIndex);
			questionLabel.setText("Question " + (currentQuestionIndex + 1) + ": " + question.getQuestionText());
			// Cập nhật giao diện dựa trên loại câu hỏi
			if (question.getType().equals(MULTIPLE_CHOICE)) {
				// Tương tự như đã thiết lập trong `initialize()`
				answerField.setVisible(false);
				answerTitle.setVisible(false);
				submitButton.setVisible(false);

				optionA.setVisible(true);
				optionB.setVisible(true);
				optionC.setVisible(true);
				optionD.setVisible(true);

				optionA.setText(question.getOptions()[0]);
				optionB.setText(question.getOptions()[1]);
				optionC.setText(question.getOptions()[2]);
				optionD.setText(question.getOptions()[3]);
			} else if (question.getType().equals(FILL_IN_THE_BLANK) || question.getType().equals(SHORT_ANSWER)) {
				// Tương tự như đã thiết lập cho FIB và SA
				optionA.setVisible(false);
				optionB.setVisible(false);
				optionC.setVisible(false);
				optionD.setVisible(false);

				answerField.setVisible(true);
				answerTitle.setVisible(true);
				submitButton.setVisible(true);

//				if (answerField.getText().trim().equals(""))
//					submitButton.setDisable(true);
//				else
//					submitButton.setDisable(false);
				answerField.setOnKeyTyped(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent keyEvent) {
						submitButton.setDisable(true);
						if (answerField.getText().trim().equals(""))
							submitButton.setDisable(true);
						else
							submitButton.setDisable(false);
					}
				});
				submitButton.setDisable(true);
			}
		} else {
			endGame();
		}
	}

	private void endGame() {
		if (timer != null) {
			timer.cancel();
		}
		questionLabel.setText("Trò chơi kết thúc! Điểm của bạn là: " + score + " / " + maxQuestions);
		optionA.setVisible(false);
		optionB.setVisible(false);
		optionC.setVisible(false);
		optionD.setVisible(false);
		answerField.setVisible(false);
		submitButton.setVisible(false);
		correctLabel.setVisible(false);
		incorrectLabel.setVisible(false);
		correctAnswerTitle.setVisible(false);
		correctAnswerContent.setVisible(false);
		timerLabel.setVisible(false);
		correctAnswerTitle.setVisible(false);
		answerTitle.setVisible(false);
	}

	@FXML
	private TextField answerField;
	@FXML
	private Label questionLabel, correctLabel, incorrectLabel, scoreLabel, answerTitle, correctAnswerTitle,
			correctAnswerContent, timerLabel;
	@FXML
	private Button optionA, optionB, optionC, optionD, submitButton, reviewButton;
}

class answeredQuestion {
	private String type;
	private String questionText;
	private String[] options;
	private String correctAnswer;
	private String selectedAnswer;

	public answeredQuestion(String type, String questionText, String[] options, String correctAnswer,
			String selectedAnswer) {
		this.type = type;
		this.questionText = questionText;
		this.options = options;
		this.correctAnswer = correctAnswer;
		this.selectedAnswer = selectedAnswer;
	}

	public String getType() {
		return type;
	}

	public String getQuestionText() {
		return questionText;
	}

	public String[] getOptions() {
		return options;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public String getSelectedAnswer() {
		return selectedAnswer;
	}
}
