package android.app.cryptogram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;

import android.app.cryptogram.R;

import android.app.cryptogram.domain.Cryptogram;
import android.app.cryptogram.domain.Player;
import edu.gatech.seclass.utilities.ExternalWebService;

public class AdminActivity extends AppCompatActivity {

    Boolean validUsername = false;
    Boolean validFirstname = false;
    Boolean validLastname = false;

    private ExitActivityTransition transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        transition = ActivityTransition.with(getIntent()).to(findViewById(R.id.admin_activity)).start(savedInstanceState);

        /* Set listener for Add Player button */
        Button btnAddPlayer = (Button) findViewById(R.id.btnAddPlayer);
        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edtFirstName = (EditText) findViewById(R.id.edtFirstName);
                EditText edtLastName = (EditText) findViewById(R.id.edtLastName);
                EditText edtUsername = (EditText) findViewById(R.id.edtUsername);

                validUsername = false;
                validFirstname = false;
                validLastname = false;

                /* Check username validity */
                if(edtUsername.getText().toString().length() == 0){
                    edtUsername.setError("Missing Username");
                }
                else {
                    for (int i = 0; i < edtUsername.getText().toString().length(); i++) {
                        char c = edtUsername.getText().toString().charAt(i);
                        int ascii = (int) c;
                        if ((ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122)) {
                            validUsername = true;
                            edtUsername.setError(null);
                        } else {
                            validUsername = false;
                            edtUsername.setError("Invalid Username");
                            break;
                        }
                    }
                    if(!Player.find(Player.class, "username = ?", edtUsername.getText().toString()).isEmpty()){
                        validUsername = false;
                        edtUsername.setError("username already in use");
                    }
                }

                /* Check first name validity */
                if(edtFirstName.getText().toString().length() == 0){
                    edtFirstName.setError("Missing First name");
                }
                else {
                    for (int i = 0; i < edtFirstName.getText().toString().length(); i++) {
                        char c = edtFirstName.getText().toString().charAt(i);
                        int ascii = (int) c;
                        if ((ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122)) {
                            validFirstname = true;
                            edtFirstName.setError(null);
                        } else {
                            validFirstname = false;
                            edtFirstName.setError("Invalid First name");
                            break;
                        }
                    }
                }

                /* Check last name validity */
                if(edtLastName.getText().toString().length() == 0){
                    edtLastName.setError("Missing Last name");
                }
                else {
                    for (int i = 0; i < edtLastName.getText().toString().length(); i++) {
                        char c = edtLastName.getText().toString().charAt(i);
                        int ascii = (int) c;
                        if ((ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122)) {
                            validLastname = true;
                            edtLastName.setError(null);
                        } else {
                            validLastname = false;
                            edtLastName.setError("Invalid Last name");
                            break;
                        }
                    }
                }

                /* If player information is valid, add player to local database */
                if (validUsername == true && validFirstname == true && validLastname == true) {
                    Player player = new Player(edtUsername.getText().toString(),
                            edtFirstName.getText().toString(),
                            edtLastName.getText().toString());
                    player.save();
                    Toast.makeText(getApplicationContext(), "Player saved", Toast.LENGTH_LONG).show();
                }
            }
        });


        /* Set listener for Add Cryptogram button */
        Button btnAddCryptogram = (Button) findViewById(R.id.btnAddCryptogram);
        btnAddCryptogram.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText edtEncoded = (EditText) findViewById(R.id.edtEncoded);
                EditText edtSolution = (EditText) findViewById(R.id.edtSolution);

                /* Check that both cryptogram and solution were provided and that they have the same length */
                if(edtEncoded.getText().toString().length() == 0) {
                    edtEncoded.setError("Missing cryptogram");
                }
                if( edtSolution.getText().toString().length() == 0){
                    edtSolution.setError("Missing solution");
                }
                if (edtEncoded.getText().toString().length() != 0 && edtSolution.getText().toString().length() != 0){
                    Cryptogram cryptogram = new Cryptogram();
                    cryptogram.setEncrypted(edtEncoded.getText().toString());
                    cryptogram.setSolution(edtSolution.getText().toString());

                    /* Add new cryptogram to external web service */
                    try {
                        String uid = ExternalWebService.getInstance().addCryptogramService(cryptogram.getEncrypted(), cryptogram.getSolution());
                        cryptogram.setUid(uid);
                        cryptogram.save();
                        Toast.makeText(getApplicationContext(), "Success | Cryptogram UID: " + uid, Toast.LENGTH_LONG).show();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void onBackPressed() {
        transition.exit(this);
    }
}
