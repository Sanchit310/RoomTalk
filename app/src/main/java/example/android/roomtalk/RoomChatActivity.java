package example.android.roomtalk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import example.android.roomtalk.model.InitialData;
import example.android.roomtalk.model.Message;
import example.android.roomtalk.model.SendMessage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoomChatActivity extends AppCompatActivity {
    public static final int CHAT_MINE = 0;
    public static final int  CHAT_PARTNER = 1;
    public static final int  USER_JOIN = 2;
    public static final int  USER_LEAVE = 3;
    public static final int  IMAGE_SENT = 4;
    public static final int  IMAGE_RECEIVED = 5;

    public static final int IMAGE_REQUEST = 101;

    Socket socket;
    String userName, roomName;
    Gson gson = new Gson();
    RecyclerView recyclerView;
    List<Message> messageList = new ArrayList<>();
    ChatAdapter chatAdapter;
    EditText contentText;
    TextView sendButton;
    ImageButton sendImageBtn;
    OutputStream outputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        contentText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);
        sendButton = findViewById(R.id.send);
        sendImageBtn = findViewById(R.id.imageButton);



        sendImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messageList, this);
        recyclerView.setAdapter(chatAdapter);
        chatAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onImageClicked(int position) {
                if (ActivityCompat.checkSelfPermission(RoomChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    }

                }else {
                    Message message = messageList.get(position);
                    String image =  message.getMsgContent();
                    Bitmap bitmap = chatAdapter.getBitmapFromString(image);

                    File filePath = Environment.getExternalStorageDirectory();
                    File dir =  new File(filePath.getAbsolutePath() + "/RoomTalk");
                    dir.mkdir();
                    File file =  new File(dir, System.currentTimeMillis() + ".jpg");
                    try {
                        outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(file));
                        sendBroadcast(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("___", "onImageClicked: ", e.getCause());
                    }

                    Toast.makeText(RoomChatActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();
                }

            }
        });


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(inputStream);
                sendImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private void sendImage(Bitmap image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, os);

        String base64String = Base64.encodeToString(os.toByteArray(), Base64.DEFAULT);
        SendMessage sendMessage = new SendMessage(userName, roomName, true, base64String);
        String jsonString = gson.toJson(sendMessage);
        Log.d("___", "sendImage: " + jsonString);
        socket.emit("newMessage",jsonString);

        Message chat = new Message(userName, roomName, base64String, true, IMAGE_SENT);
        addToRecyclerView(chat);

    }

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String leftUserName =  args[0].toString();
            Message chat =  new Message(leftUserName + " left", "", "",null, USER_LEAVE);
            addToRecyclerView(chat);
        }
    };

    private Emitter.Listener onUpdateChat = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("___", "call: args" + args[0].toString());

            Message chat = gson.fromJson(args[0].toString(), Message.class);
            if (!chat.isImage()){
                chat.setViewType(CHAT_PARTNER);
                addToRecyclerView(chat);
            }else {
                chat.setViewType(IMAGE_RECEIVED);
                addToRecyclerView(chat);
            }

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
            Message chat = new Message(name + " joined", roomName, "", null, USER_JOIN);
            addToRecyclerView(chat);
        }
    };

    public void sendMessage(){
        String content = contentText.getText().toString();
        SendMessage sendMessage = new SendMessage(userName, roomName, false, content);
        String jsonData = gson.toJson(sendMessage);
        socket.emit("newMessage", jsonData);
        Log.d("___", "sendMessage: ." + jsonData);
        Message message = new Message(userName, roomName, content,false, CHAT_MINE);
        addToRecyclerView(message);
        contentText.setText("");

    }

    private void addToRecyclerView(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(message);
                chatAdapter.notifyItemInserted(messageList.size());

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
