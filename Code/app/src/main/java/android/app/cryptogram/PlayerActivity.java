package android.app.cryptogram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.kogitune.activity_transition.ExitActivityTransition;

import android.app.cryptogram.R;

public class PlayerActivity extends AppCompatActivity {
    private Button cryptogram;
    private Button ranking;
    private ExitActivityTransition transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        transition = ActivityTransition.with(getIntent()).to(findViewById(R.id.player_activity)).start(savedInstanceState);

        cryptogram = (Button) findViewById(R.id.chooseCryptogramButton);
        ranking = (Button) findViewById(R.id.viewRankingButton);
    }

    public void goToCryptogramActivity(View view) {
        startActivity(view, ChooseCryptogramActivity.class);
    }

    public void goToRankingActivity(View view) {
        startActivity(view, ViewRatingsActivity.class);
    }

    private void startActivity(View view, Class clazz){
        Intent intent = new Intent(this, clazz);
        ActivityTransitionLauncher.with(this).from(view).launch(intent);
    }

    public void onBackPressed() {
        transition.exit(this);
    }
}
