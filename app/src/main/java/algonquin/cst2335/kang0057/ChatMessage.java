package algonquin.cst2335.kang0057;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
public class ChatMessage {
    private String message;
    private String timeSent;
    private boolean isSentButton;

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }

    public ChatMessage(String m, String t, boolean sent) {
        message = m;
        timeSent = t;
        isSentButton = sent;
    }
}
