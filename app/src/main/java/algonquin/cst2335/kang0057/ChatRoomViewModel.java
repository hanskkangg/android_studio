package algonquin.cst2335.kang0057;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

    public class ChatRoomViewModel extends ViewModel {
        public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData<>();
    }
