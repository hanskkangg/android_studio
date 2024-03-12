package algonquin.cst2335.kang0057;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.os.Bundle;

import algonquin.cst2335.kang0057.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.kang0057.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if (messages == null) {
            chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());
        }

        binding.sendButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_time_format_pattern));
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage chatMessage = new ChatMessage(binding.textInput.getText().toString(), currentDateAndTime, true);

            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.recycleView.scrollToPosition(binding.recycleView.getAdapter().getItemCount() - 1);
            // clear the previous text:
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {
            SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_time_format_pattern));
            String currentDateAndTime = sdf.format(new Date());

            ChatMessage chatMessage = new ChatMessage(binding.textInput.getText().toString(), currentDateAndTime, false);

            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.recycleView.scrollToPosition(binding.recycleView.getAdapter().getItemCount() - 1);
            // clear the previous text:
            binding.textInput.setText("");
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                } else {
                    @SuppressLint("InflateParams") View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_message, null, false);
                    RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    rootView.setLayoutParams(lp);
                    return new MyRowHolder(rootView);
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.messageText.setText("");
                holder.timeText.setText("");
                String message = messages.get(position).getMessage();
                holder.messageText.setText(message);
                String timeSent = messages.get(position).getTimeSent();
                holder.timeText.setText(timeSent);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                return messages.get(position).isSentButton() ? 0 : 1;
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        binding.recycleView.setLayoutManager(llm);
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}