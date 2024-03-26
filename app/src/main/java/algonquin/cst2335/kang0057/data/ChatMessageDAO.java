package algonquin.cst2335.kang0057.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import algonquin.cst2335.kang0057.ChatMessage;

@Dao
public interface ChatMessageDAO {

    @Insert
    long insertMessage(ChatMessage message);

    @Query("SELECT * FROM chat_messages")
    List<ChatMessage> getAllMessages();

    @Delete
    void deleteMessage(ChatMessage message);

    @Query("DELETE FROM chat_messages")
    void deleteAllMessages();
}
