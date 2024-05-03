package Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class TranslationController implements Initializable {
    private String sourceLanguage = "en";
    private String toLanguage = "vi";
    private boolean isToVietnameseLang = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        translateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    handleOnClickTranslateBtn();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        sourceLangField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (sourceLangField.getText().trim().isEmpty()) 
                	translateBtn.setDisable(true);
                else 
                	translateBtn.setDisable(false);
            }
        });

        translateBtn.setDisable(true);
        toLangField.setEditable(false);
    }
    
    public static String translate(String langFrom, String langTo, String text) throws IOException {
        String APIKEY = "AKfycbzxtNpZD2Ogs4oeUnj8nTaCmPlKwgwsLWPasyIsLQPB_WXvKdKU";
        URL url = new URL("https://script.google.com/macros/s/" + APIKEY + "/exec?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) + "&target=" + langTo + "&source=" + langFrom);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = inputStream.readLine()) != null) 
        	response.append(inputLine);
        inputStream.close();
//        return StringEscapeUtils.unescapeHtml4(response.toString());
        return response.toString();
    }

    @FXML
    private void handleOnClickTranslateBtn() throws IOException {
    	String srcText = sourceLangField.getText();
    	toLangField.setText(translate(sourceLanguage, toLanguage, srcText));
    }

    @FXML
    private void handleOnClickSwitchToggle() {
        sourceLangField.clear();
        toLangField.clear();
        if (isToVietnameseLang) {
            englishLabel.setLayoutX(426);
            vietnameseLabel.setLayoutX(104);
            sourceLanguage = "vi";
            toLanguage = "en";
        } else {
            englishLabel.setLayoutX(100);
            vietnameseLabel.setLayoutX(426);
            sourceLanguage = "en";
            toLanguage = "vi";
        }
        isToVietnameseLang = !isToVietnameseLang;
    }

    @FXML
    private TextArea sourceLangField, toLangField;

    @FXML
    private Button translateBtn;

    @FXML
    private Label englishLabel , vietnameseLabel;
}
