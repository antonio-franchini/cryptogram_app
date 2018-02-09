package android.app.cryptogram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;

import java.util.Vector;

import android.app.cryptogram.R;

import android.app.cryptogram.domain.CryptogramHistory;

/******************************************************
 This is the cryptogram activity class
 ******************************************************/

public class CryptogramActivity extends AppCompatActivity {
    /******************************************************
     Local variables
     ******************************************************/
    Button submitButton;
    Button changeButton;
    CryptogramHistory cryptogramHistory;
    String AttemptTestString;
    String CryptoTestString;
    Spinner toSpinner;
    Spinner fromSpinner;
    TextView attempt;
    TextView cryptogram;
    TextView cryptogramID;
    TextView timeField;
    TextView dateField;
    TextView statusField;
    private ExitActivityTransition transition;

    /******************************************************
     Entry function
     ******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptogram);
        transition = ActivityTransition.with(getIntent()).to(findViewById(R.id.activity_cryptogram)).start(savedInstanceState);

        /******************************************************
         Initialize spinners
         ******************************************************/
        fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        toSpinner = (Spinner) findViewById(R.id.toSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alpha_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        /******************************************************
         Get CryptogramHistory_id from intent
         ******************************************************/
        Intent intent = getIntent();
        final long cryptogramHistory_id = intent.getLongExtra("cryptogramHistory_id",-1);

        cryptogramHistory = CryptogramHistory.findById(CryptogramHistory.class, cryptogramHistory_id);


        /******************************************************
         Link objects to fields
        ******************************************************/
        submitButton = (Button)findViewById(R.id.submitButton);
        changeButton = (Button)findViewById(R.id.changeButton);
        cryptogramID = (TextView)findViewById(R.id.cryptogramID);
        timeField    = (TextView)findViewById(R.id.timeField);
        dateField    = (TextView)findViewById(R.id.dateField);
        statusField  = (TextView)findViewById(R.id.statusField);
        attempt      = (TextView)findViewById(R.id.attempt);
        cryptogram   = (TextView)findViewById(R.id.cryptogram);

        /******************************************************
         Initialize fields
         ******************************************************/
        cryptogramID.setText("Cryptogram ID: " + cryptogramHistory.getCryptogram().getUid());
        timeField.setText(DateFormat.format("dd/MM/yy", cryptogramHistory.getDate()));
        dateField.setText(DateFormat.format("HH:mm:ss", cryptogramHistory.getDate()));
        statusField.setText("Status: " + cryptogramHistory.getStatus().getDesc());
        AttemptTestString = cryptogramHistory.getAttempt();
        CryptoTestString =  cryptogramHistory.getCryptogram().getEncrypted();

        attempt.setText(AttemptTestString);
        cryptogram.setText(CryptoTestString);

        /******************************************************
         Apply status specific settings
         ******************************************************/
        if(cryptogramHistory.getStatus() == CryptogramHistory.Status.SOLVED || cryptogramHistory.getStatus() == CryptogramHistory.Status.FAILED){
            submitButton.setEnabled(false);
            changeButton.setEnabled(false);
            toSpinner.setEnabled(false);
            fromSpinner.setEnabled(false);
        }
        else{
            submitButton.setEnabled(true);
            changeButton.setEnabled(true);
            toSpinner.setEnabled(true);
            fromSpinner.setEnabled(true);
        }
    }

    /******************************************************
     Respond to submit button
     ******************************************************/
    public void checkClick(View view1){
        switch (view1.getId()){
            case R.id.submitButton:
                /***********************************************************
                 Check is solution was right or wrong
                 ***********************************************************/
                if(attempt.getText().toString().equals(cryptogramHistory.getCryptogram().getSolution())) {
                    cryptogramHistory.setStatus(CryptogramHistory.Status.SOLVED);
                    submitButton.setEnabled(false);
                    changeButton.setEnabled(false);
                    toSpinner.setEnabled(false);
                    fromSpinner.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Good job!", Toast.LENGTH_SHORT).show();
                }
                else {
                    cryptogramHistory.setStatus(CryptogramHistory.Status.FAILED);
                    submitButton.setEnabled(false);
                    changeButton.setEnabled(false);
                    toSpinner.setEnabled(false);
                    fromSpinner.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Wrong solution!", Toast.LENGTH_SHORT).show();
                }
                statusField.setText("Status: " + cryptogramHistory.getStatus().getDesc());
                /***********************************************************
                 Check is solution was right or wrong
                 ***********************************************************/
                cryptogramHistory.setAttempt(attempt.getText().toString());
                cryptogramHistory.save();
                break;

            case R.id.changeButton:
                /******************************************************
                 Scan for upper case matches
                 ******************************************************/
                String fromChar = fromSpinner.getSelectedItem().toString().toUpperCase();
                String toChar = toSpinner.getSelectedItem().toString().toUpperCase();
                StringBuilder updatedStr = new StringBuilder(attempt.getText().toString());

                Vector<Integer> indexes = new Vector<>();
                indexes = findIndexes(fromChar);
                for(int i = 0; i < indexes.size(); i++) {
                    updatedStr.setCharAt(indexes.get(i), toChar.charAt(0));
                }
                attempt.setText(updatedStr);

                /******************************************************
                 Scan for lower case matches
                 ******************************************************/
                fromChar = fromSpinner.getSelectedItem().toString().toLowerCase();
                toChar = toSpinner.getSelectedItem().toString().toLowerCase();
                Vector<Integer> indexes2 = new Vector<>();
                indexes2 = findIndexes(fromChar);
                for(int i = 0; i < indexes2.size(); i++)
                {
                    updatedStr.setCharAt(indexes2.get(i), toChar.charAt(0));
                }
                attempt.setText(updatedStr);

                if(indexes.size() == 0 && indexes2.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No match found", Toast.LENGTH_SHORT).show();
                }
                else {
                        Toast.makeText(getApplicationContext(), "Changed " + fromSpinner.getSelectedItem().toString() + " to " + toSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    /******************************************************
     Respond to back button
     ******************************************************/
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewCryptogramActivity.class);
        intent.putExtra("cryptogram_id", cryptogramHistory.getCryptogram().getId());
        cryptogramHistory.setAttempt(attempt.getText().toString());
        cryptogramHistory.save();

        setResult(RESULT_OK, intent);
        transition.exit(this);

    }

    /******************************************************
     Find indexes of character occurences
     ******************************************************/
    public Vector<Integer> findIndexes(String fromChar) {
        Vector<Integer> indexes = new Vector<>();
        String temp = cryptogram.getText().toString();

        int i = temp.indexOf(fromChar);
        while(i >= 0) {
            indexes.add(i);
            i = temp.indexOf(fromChar, i+1);
        }
        return indexes;
    }

}
