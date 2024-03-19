package algonquin.cst2335.kang0057;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "chat_messages")
public class ChatMessage {

    private boolean isSentButton;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "TimeSent")
    protected String timeSent;

    @ColumnInfo(name = "message")
    protected String message;

    @ColumnInfo(name = "SendorReceive")
    protected int sendorReceive;

    public ChatMessage() {
        // Default constructor required by Room
    }

    public ChatMessage(String timeSent, String message, int sendorReceive) {
        this.timeSent = timeSent;
        this.message = message;
        this.sendorReceive = sendorReceive;
    }

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }

    public void setSentButton(boolean sentButton) {
        isSentButton = sentButton;
    }

    public int getSendorReceive() {
        return sendorReceive;
    }

    public void setSendorReceive(int sendorReceive) {
        this.sendorReceive = sendorReceive;
    }

    public String getCurrentTime() {
        // Implement this method to get the current time
        // For example:
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
