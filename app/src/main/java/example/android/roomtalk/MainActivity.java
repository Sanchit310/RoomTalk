package example.android.roomtalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    EditText userName, roomName;
    CardView enterButton;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.loginConstraintLayout);
        userName = findViewById(R.id.userName);
        roomName = findViewById(R.id.roomName);
        enterButton = findViewById(R.id.enterButton);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().trim().isEmpty()  && roomName.getText().toString().trim().isEmpty()){

                    Snackbar snackbar = Snackbar.make(constraintLayout, "Please enter fields", Snackbar.LENGTH_SHORT);
                    snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                    snackbar.show();
                }else if(roomName.getText().toString().trim().isEmpty()){

                    Snackbar snackbar = Snackbar.make(constraintLayout, "Room name is empty", Snackbar.LENGTH_SHORT);
                    snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                    snackbar.show();

                }else if(userName.getText().toString().trim().isEmpty()){

                    Snackbar snackbar = Snackbar.make(constraintLayout, "Username is empty", Snackbar.LENGTH_SHORT);
                    snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
                    snackbar.show();

                } else {

                    Intent intent = new Intent(getApplicationContext(), RoomChatActivity.class);
                    intent.putExtra("userName", userName.getText().toString());
                    intent.putExtra("roomName", roomName.getText().toString());
                    startActivity(intent);
                }

            }
        });


    }
}
