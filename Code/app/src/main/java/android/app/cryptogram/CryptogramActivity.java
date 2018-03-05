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


public class CryptogramActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cryptogram);
        transition = ActivityTransition.with(getIntent()).to(findViewById(R.id.activity_cryptogram)).start(savedInstanceState);

        /* Link Spinner objects to spinner widgets */
        fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        toSpinner = (Spinner) findViewById(R.id.toSpinner);

        /* Populate spinners with alphabet array (that is defined in res/values/strings.xml) */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alpha_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        /* Get CryptogramHistory_id from intent (from calling page, ChooseCryptogramActivity)*/
        /* Use CryptogramHistory_id to get the cryptogram history from the local database    */
        Intent intent = getIntent();
        final long cryptogramHistory_id = intent.getLongExtra("cryptogramHistory_id",-1);
        cryptogramHistory = CryptogramHistory.findById(CryptogramHistory.class, cryptogramHistory_id);

        /* Link objects to widgets */
        submitButton = (Button)findViewById(R.id.submitButton);
        changeButton = (Button)findViewById(R.id.changeButton);
        cryptogramID = (TextView)findViewById(R.id.cryptogramID);
        timeField    = (TextView)findViewById(R.id.timeField);
        dateField    = (TextView)findViewById(R.id.dateField);
        statusField  = (TextView)findViewById(R.id.statusField);
        attempt      = (TextView)findViewById(R.id.attempt);
        cryptogram   = (TextView)findViewById(R.id.cryptogram);

        /* Use the current cryptogram history to populate the various fileds in this page. */
        /* These include cryptogram ID, status, attempt (as it was left off), etc.         */
        cryptogramID.setText("Cryptogram ID: " + cryptogramHistory.getCryptogram().getUid());
        timeField.setText(DateFormat.format("dd/MM/yy", cryptogramHistory.getDate()));
        dateField.setText(DateFormat.format("HH:mm:ss", cryptogramHistory.getDate()));
        statusField.setText("Status: " + cryptogramHistory.getStatus().getDesc());
        AttemptTestString = cryptogramHistory.getAttempt();
        CryptoTestString =  cryptogramHistory.getCryptogram().getEncrypted();

        attempt.setText(AttemptTestString);
        cryptogram.setText(CryptoTestString);

        /* Apply status specific settings.  In particular, disable the spinners and the Submit button if this cryptogram was already solved or failed */
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

    /* Handle buttons */
    public void checkClick(View view1){
        switch (view1.getId()){

            /* Handle Submit button */
            case R.id.submitButton:

                /* Check if solution was right or wrong */
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

                /* Save current attempt */
                cryptogramHistory.setAttempt(attempt.getText().toString());
                cryptogramHistory.save();
                break;

            /* Handle Go button, which will trigger the letter change as indicated by the From and To spinners */
            case R.id.changeButton:

                /* Scan cryptogram-string for upper case matches */
                String fromChar = fromSpinner.getSelectedItem().toString().toUpperCase();
                String toChar = toSpinner.getSelectedItem().toString().toUpperCase();
                StringBuilder updatedStr = new StringBuilder(attempt.getText().toString());

                Vector<Integer> indexes = new Vector<>();
                indexes = findIndexes(fromChar);

                /* Use the found indexes to replace occurrences of that letter in the attempt-string (keeping them uppercase) */
                for(int i = 0; i < indexes.size(); i++) {
                    updatedStr.setCharAt(indexes.get(i), toChar.charAt(0));
                }
                attempt.setText(updatedStr);

                /* Scan cryptogram-string for lower case matches */
                fromChar = fromSpinner.getSelectedItem().toString().toLowerCase();
                toChar = toSpinner.getSelectedItem().toString().toLowerCase();
                Vector<Integer> indexes2 = new Vector<>();
                indexes2 = findIndexes(fromChar);

                /* Use the found indexes to replace occurrences of that letter in the attempt-string (keeping them lowercase) */
                for(int i = 0; i < indexes2.size(); i++) {
                    updatedStr.setCharAt(indexes2.get(i), toChar.charAt(0));
                }
                attempt.setText(updatedStr);

                /* Display feedback message that indicates whether any occurence was found */
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

    /* Handle back button */
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewCryptogramActivity.class);
        intent.putExtra("cryptogram_id", cryptogramHistory.getCryptogram().getId());
        cryptogramHistory.setAttempt(attempt.getText().toString());
        cryptogramHistory.save();

        setResult(RESULT_OK, intent);
        transition.exit(this);
    }

    /* Finds indexes of given character in cryptogram string */
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
