package example.android.roomtalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 )
        {
            if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
