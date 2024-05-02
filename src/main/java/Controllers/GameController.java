package Controllers;

import javafx.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
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
	private List<Question> questions;
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
		questionLabel.setText("Câu " + (currentQuestionIndex + 1) + ": " + question.getQuestionText());
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
        
        setupQuestionButtons();
        
//        optionA.minHeightProperty().bind(optionA.heightProperty());
//        optionB.minHeightProperty().bind(optionB.heightProperty());
//        optionC.minHeightProperty().bind(optionC.heightProperty());
//        optionD.minHeightProperty().bind(optionD.heightProperty());
        
        
		scoreLabel.setText("Score: " + score);
		submitButton.setDisable(true);
		correctLabel.setVisible(false);
		incorrectLabel.setVisible(false);
		correctAnswerTitle.setVisible(false);
		correctAnswerContent.setVisible(false);
		answerField.clear();
	}
	
	private void setupQuestionButtons() {
	    // Set wrapText and bind minHeight to heightProperty for automatic height adjustment
	    Button[] buttons = {optionA, optionB, optionC, optionD};
	    for (Button button : buttons) {
	        button.setWrapText(true);
	        button.setMaxWidth(200); // Giới hạn chiều rộng tối đa cho phép
	        button.minHeightProperty().bind(button.heightProperty());
	    }
	}
	
	@FXML
	private void handleOptionAction(ActionEvent event) {
	    Button clickedButton = (Button) event.getSource();
	    checkAnswer(clickedButton);
	}
	
	private void checkAnswer(Button selectedButton) {
	    Question currentQuestion = questions.get(currentQuestionIndex);
	    
	    if (currentQuestion.getType().equals(MULTIPLE_CHOICE)) {
	    	String selectedAnswer = selectedButton.getText().substring(0,1);
		    Button correctButton = null;

		    // Xác định nút nào chứa đáp án đúng
		    if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionA.getText().substring(0,1))) 
		    	correctButton = optionA;
		    else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionB.getText().substring(0,1))) 
		    	correctButton = optionB;
		    else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionC.getText().substring(0,1)))
		    	correctButton = optionC;
		    else if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(optionD.getText().substring(0,1))) 
		    	correctButton = optionD;
		    if (selectedAnswer.equalsIgnoreCase(currentQuestion.getCorrectAnswer())) {
		    	currentQuestionIndex ++;
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
	    } else if (currentQuestion.getType().equals(FILL_IN_THE_BLANK) 
	    		|| currentQuestion.getType().equals(SHORT_ANSWER)) {
	    	String fillAnswer = answerField.getText();
	    	if (fillAnswer.equals("")) {
	    		submitButton.setVisible(false);
	    	}
	    	if (currentQuestion.getCorrectAnswer().equalsIgnoreCase(fillAnswer)) {
	    		correctLabel.setVisible(true);
	    		currentQuestionIndex ++;
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
	}

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
	}
	
	private void displayNextQuestion() {
	    if (currentQuestionIndex < questions.size()) {
	    	resetButtonColors();
	        Question question = questions.get(currentQuestionIndex);
	        questionLabel.setText("Câu " + (currentQuestionIndex + 1) + ": " + question.getQuestionText());
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
	        } else if (question.getType().equals(FILL_IN_THE_BLANK) || question.getType().equals(SHORT_ANSWER)){
	            // Tương tự như đã thiết lập cho FIB và SA
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
						if (answerField.getText().trim().equals(""))
							submitButton.setDisable(true);
						else
							submitButton.setDisable(false);
					}
				});
	        }
	    } else {
	        endGame();
	    }
	}

	private void endGame() {
	    questionLabel.setText("Trò chơi kết thúc! Điểm của bạn là: " + score);
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
	}

	@FXML
	private TextField answerField;
	@FXML
	private Label questionLabel, correctLabel, incorrectLabel, scoreLabel, 
	answerTitle, correctAnswerTitle, correctAnswerContent;
	@FXML
	private Button optionA, optionB, optionC, optionD, submitButton;
}
