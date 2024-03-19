package algonquin.cst2335.kang0057;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.kang0057.data.ChatMessageDAO;
import algonquin.cst2335.kang0057.data.ChatRoomViewModel;
import algonquin.cst2335.kang0057.data.MessageDatabase;
import algonquin.cst2335.kang0057.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.kang0057.databinding.ReceiveMessageBinding;
import algonquin.cst2335.kang0057.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    private ActivityChatRoomBinding binding;
    private ArrayList<ChatMessage> messages;
    private ChatRoomViewModel chatModel;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ChatRoomViewModel
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        // Retrieve the ArrayList stored in ChatRoomViewModel
        messages = chatModel.messages.getValue();

        // Verify if messages is null and initialize it if necessary
        if (messages == null) {
            chatModel.messages.setValue(messages = new ArrayList<>());
        }

        // Open database and initialize DAO
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        // Retrieve messages from the database and populate the ArrayList
        if (messages.isEmpty()) {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                messages.addAll(mDAO.getAllMessages());
                runOnUiThread(() -> {
                    myAdapter.notifyDataSetChanged(); // Notify adapter of data change
                    binding.recycleView.setAdapter(myAdapter); // Set adapter for RecyclerView
                });
            });
        }

        binding.send.setOnClickListener(click -> {
            addMessage(true);
        });

        binding.receive.setOnClickListener(click -> {
            addMessage(false);
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding sentMessageBinding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(sentMessageBinding.getRoot(), viewType);
                } else {
                    ReceiveMessageBinding receivedMessageBinding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(receivedMessageBinding.getRoot(), viewType);
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
                holder.timeText.setText(chatMessage.getTimeSent());
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
    }

    private void addMessage(boolean isSent) {
        String messageContent = binding.TextInput.getText().toString();
        if (!messageContent.isEmpty()) {
            String currentTime = getCurrentTime();

            ChatMessage newMessage = new ChatMessage(messageContent, currentTime, isSent);

            messages.add(newMessage);

            myAdapter.notifyItemInserted(messages.size() - 1);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                mDAO.insertMessage(newMessage);
            });

            binding.TextInput.setText("");
        }
    }


    private String getCurrentTime() {
        // Get current time
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        return sdf.format(new Date());
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == 0) {
                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time);
            } else {
                messageText = itemView.findViewById(R.id.receive_message);
                timeText = itemView.findViewById(R.id.receive_time);
            }

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                showDeleteConfirmationDialog(position);
            });
        }

        private void showDeleteConfirmationDialog(int position) {
            ChatMessage messageToDelete = messages.get(position);
            String confirmationMessage = "Are you sure you want to delete this message?\n\n" + messageToDelete.getMessage();

            AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this)
                    .setTitle("Delete Message")
                    .setMessage(confirmationMessage)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        ChatMessage deletedMessage = messages.remove(position);
                        myAdapter.notifyItemRemoved(position);
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> mDAO.deleteMessage(deletedMessage));

                        Snackbar snackbar = Snackbar.make(binding.getRoot(), "You deleted message #" + position, Snackbar.LENGTH_LONG);
                        snackbar.setAction("Undo", v -> {
                            messages.add(position, deletedMessage);
                            myAdapter.notifyItemInserted(position);
                        });
                        snackbar.show();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                    });
            builder.show();
        }


    }}

