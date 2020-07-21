package example.android.roomtalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText userName, roomName;
    CardView enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        roomName = findViewById(R.id.roomName);
        enterButton = findViewById(R.id.enterButton);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RoomChatActivity.class);
                intent.putExtra("userName", userName.getText().toString());
                intent.putExtra("roomName", roomName.getText().toString());
                startActivity(intent);
            }
        });

    }
}
