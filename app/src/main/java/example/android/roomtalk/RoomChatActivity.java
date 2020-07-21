package example.android.roomtalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import example.android.roomtalk.model.InitialData;
import example.android.roomtalk.model.Message;
import example.android.roomtalk.model.SendMessage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RoomChatActivity extends AppCompatActivity {
    public static final int CHAT_MINE = 0;
    public static final int  CHAT_PARTNER = 1;
    public static final int  USER_JOIN = 2;
    public static final int  USER_LEAVE = 3;

    Socket socket;
    String userName, roomName;
    Gson gson = new Gson();
    RecyclerView recyclerView;
    List<Message> messageList = new ArrayList<>();
    ChatAdapter chatAdapter;
    EditText contentText;
    TextView sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        contentText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);
        sendButton = findViewById(R.id.send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messageList, this);
        recyclerView.setAdapter(chatAdapter);


        userName = getIntent().getStringExtra("userName");
        roomName = getIntent().getStringExtra("roomName");
        Log.d("___", "onCreate: " + userName + " " + roomName);

        try {
            socket = IO.socket("https://sanchitroomchat.azurewebsites.net/");
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }

        socket.connect();
        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on("newUserToChatRoom", onNewUser);
        socket.on("updateChat", onUpdateChat);
        socket.on("userLeftChatRoom", onUserLeft);

    }

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String leftUserName =  args[0].toString();
            Message chat =  new Message(leftUserName + " left", "", "", USER_LEAVE);
            addToRecyclerView(chat);
        }
    };

    private Emitter.Listener onUpdateChat = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("___", "call: args" + args[0].toString());

            Message chat = gson.fromJson(args[0].toString(), Message.class);
            chat.setViewType(CHAT_PARTNER);
            addToRecyclerView(chat);
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            InitialData data = new InitialData(userName, roomName);
            String jsonData = gson.toJson(data);
            Log.d("___", "call: 20" + jsonData);
            socket.emit("subscribe", jsonData);
        }
    };

    private Emitter.Listener onNewUser =  new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String name = args[0].toString();
            Log.d("___", "call: name" + name);
            Message chat = new Message(name + " joined", roomName, "", USER_JOIN);
            addToRecyclerView(chat);
        }
    };

    public void sendMessage(){
        String content = contentText.getText().toString();
        SendMessage sendMessage = new SendMessage(userName, roomName, content);
        String jsonData = gson.toJson(sendMessage);
        socket.emit("newMessage", jsonData);

        Message message = new Message(userName, roomName, content, CHAT_MINE);
        addToRecyclerView(message);

    }

    private void addToRecyclerView(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(message);
                chatAdapter.notifyItemInserted(messageList.size());
                contentText.setText("");
                recyclerView.scrollToPosition(messageList.size() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InitialData data = new InitialData(userName, roomName);
        String jsonData = gson.toJson(data);
        socket.emit("unsubscribe", jsonData);
        socket.disconnect();
    }

    //    public List<Msg> pop(){
//
//        messageList.add(new Msg(null, "Sanchit", "hello",0));
//        messageList.add(new Msg(null, "Sanchit", "hello",1));
//        messageList.add(new Msg(null, "Sanchit", "hello",2));
//        return messageList;
//    }


}
