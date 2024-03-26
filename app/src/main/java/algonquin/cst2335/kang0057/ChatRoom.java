package algonquin.cst2335.kang0057;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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

        // Initialize the toolbar
        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_messages) {
            showDeleteConfirmationDialogForMenuItem();
            return true;
        } else if (item.getItemId() == R.id.about_app){

            Toast.makeText(this, "Version 1.0, created by Hans Kang", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDeleteConfirmationDialogForMenuItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this)
                .setTitle(getString(R.string.delete_all_messages_title))
                .setMessage(getString(R.string.delete_all_messages_confirmation))
                .setPositiveButton(getString(R.string.delete_message_positive_button), (dialog, which) -> {
                    messages.clear(); // Clear the local list
                    myAdapter.notifyDataSetChanged(); // Notify the adapter of the data change

                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> {
                        mDAO.deleteAllMessages(); // Delete all messages in the database
                    });
                })
                .setNegativeButton(getString(R.string.delete_message_negative_button), null); // No action on "No"

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteConfirmationDialog(int position) {
        ChatMessage messageToDelete = messages.get(position);
        String confirmationMessage = getString(R.string.delete_message_confirmation, messageToDelete.getMessage());

        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this)
                .setTitle(getString(R.string.delete_message_title))
                .setMessage(confirmationMessage)
                .setPositiveButton(getString(R.string.delete_message_positive_button), (dialog, which) -> {
                    ChatMessage deletedMessage = messages.remove(position);
                    myAdapter.notifyItemRemoved(position);
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> mDAO.deleteMessage(deletedMessage));

                    Snackbar snackbar = Snackbar.make(binding.getRoot(), getString(R.string.message_deleted_snackbar, position), Snackbar.LENGTH_LONG);
                    snackbar.setAction(getString(R.string.undo), v -> {
                        messages.add(position, deletedMessage);
                        myAdapter.notifyItemInserted(position);
                    });
                    snackbar.show();
                })
                .setNegativeButton(getString(R.string.delete_message_negative_button), (dialog, which) -> {
                });
        builder.show();
    }

}
