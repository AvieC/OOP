package DictionaryApplication;

import java.io.*;
import java.util.*;

public class EnglishQuizGame {
    private List<Question> questions;
    private Scanner scanner;

    public EnglishQuizGame(String filePath) throws FileNotFoundException {
        questions = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadQuestions(filePath);
    }
    
	public List<Question> getQuestions() {
		return questions;
	}

	private void loadQuestions(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner fileScanner = new Scanner(file);

        while (fileScanner.hasNextLine()) {
            String type = fileScanner.nextLine();
            String questionText = fileScanner.nextLine();
            if (type.equals("MC")) { // Multiple Choice
                String[] options = new String[4];
                for (int i = 0; i < 4; i++) {
                    options[i] = fileScanner.nextLine();
                }
                String correctAnswer = fileScanner.nextLine();
                questions.add(new Question(type, questionText, options, correctAnswer));
            } else if (type.equals("FIB") || type.equals("SA")) { // Fill In the Blank or Short Answer
                String correctAnswer = fileScanner.nextLine();
                questions.add(new Question(type, questionText, null, correctAnswer));
            }
        }

        fileScanner.close();
    }

    public void startGame() {
        Collections.shuffle(questions);

        for (Question question : questions) {
            System.out.println(question.getQuestionText());
            if (question.getType().equals("MC")) {
                String[] options = question.getOptions();
                for (int i = 0; i < options.length; i++) {
                    System.out.println(options[i]);
                }
            } else if (question.getType().equals("SA")) {
                System.out.println("Type your answer (or choose from the list if provided):");
            }
            System.out.print("Your answer: ");
            String userAnswer = scanner.nextLine();

            if (userAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
                System.out.println("Correct!\n");
            } else {
                System.out.println("Wrong! The correct answer was '" + question.getCorrectAnswer() + "'.");
                break; // End the game if the answer is wrong
            }
        }

        System.out.println("Game over!");
    }
}