package android.app.cryptogram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.util.List;

import android.app.cryptogram.R;

import android.app.cryptogram.domain.Player;

public class LoginActivity extends AppCompatActivity {

    private RadioButton admin;
    private RadioButton player;
    private EditText userID;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        admin = (RadioButton) findViewById(R.id.AdminRadioButton);
        player = (RadioButton) findViewById(R.id.playerRadioButton);
        userID = (EditText) findViewById(R.id.userId);
        loginButton = (Button) findViewById(R.id.loginButton);
    }

    public void handleClick(View view) {
        boolean validInput = true;

        if (!admin.isChecked() && !player.isChecked()) {
            player.setError("Please choose Admin or Player");
            validInput = false;
        } else {
            player.setError(null);
        }

        if(player.isChecked()) {
            if (TextUtils.isEmpty(userID.getText())) {
                userID.setError("User ID can't empty");
                validInput = false;
            } else {
                String id = userID.getText().toString();
                if (id.length() > 10) {
                    userID.setError("Number of characters must less than 10");
                    validInput = false;
                } else if (Player.find(Player.class, "username = ?", userID.getText().toString()).isEmpty()) {
                    userID.setError("User ID not recognized.");
                    validInput = false;
                }
            }
        }

        if (validInput) {
            if (admin.isChecked()) {
                goToAdminActivity(view);
            } else if (player.isChecked()) {

                List<Player> players = Player.find(Player.class, "username = ?", userID.getText().toString());
                PersistentData.currentPlayer = players.get(0);

                goToPlayerActivity(view);
            }
        }
    }

    public void goToAdminActivity(View view) {
        startActivity(view, AdminActivity.class);
    }

    public void goToPlayerActivity(View view) {
        startActivity(view, PlayerActivity.class);
    }

    private void startActivity(View view, Class clazz){
        Intent intent = new Intent(this, clazz);
        ActivityTransitionLauncher.with(this).from(view).launch(intent);
    }
}
