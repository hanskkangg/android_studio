package algonquin.cst2335.kang0057.data;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.kang0057.ChatMessage;
import algonquin.cst2335.kang0057.data.ChatMessageDAO;

@Database(entities = {ChatMessage.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {
    public abstract ChatMessageDAO cmDAO();
}
