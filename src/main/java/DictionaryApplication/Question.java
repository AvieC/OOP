package DictionaryApplication;

public class Question {
	private String type;
    private String questionText;
    private String[] options;
    private String correctAnswer;

    public Question(String type, String questionText, String[] options, String correctAnswer) {
        this.type = type;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
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
}
