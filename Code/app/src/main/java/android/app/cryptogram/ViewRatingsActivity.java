package android.app.cryptogram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.cryptogram.R;

import android.app.cryptogram.domain.CryptogramHistory;
import android.app.cryptogram.domain.Player;
import edu.gatech.seclass.utilities.ExternalWebService;



public class ViewRatingsActivity extends AppCompatActivity {

    private ExitActivityTransition transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ratings);
        transition = ActivityTransition.with(getIntent()).to(findViewById(R.id.ratings_layout)).start(savedInstanceState);

        ListView ratingsView = (ListView) findViewById(R.id.ratings_list);
        Iterator<Player> onePlayer = Player.findAll(Player.class);
        ArrayList<SimulatedRating> ratingsList = new ArrayList<>();
        //ArrayList<SimulatedRating> ratingsListSorted = new ArrayList<>();

        while(onePlayer.hasNext()) {
            Player player = onePlayer.next();
            Iterator<CryptogramHistory> itr = CryptogramHistory.findAll(CryptogramHistory.class);
            int numSolved = 0;
            int numStarted = 0;
            int numFailed = 0;

            while(itr.hasNext()) {
                CryptogramHistory cryptogramHistory = itr.next();
                if ((cryptogramHistory.getStatus() == CryptogramHistory.Status.IN_PROGRESS) && (cryptogramHistory.getPlayer().getUsername().equals(player.getUsername()))) {
                    numStarted++;
                } else if((cryptogramHistory.getStatus() == CryptogramHistory.Status.SOLVED) && (cryptogramHistory.getPlayer().getUsername().equals(player.getUsername()))) {
                    numSolved++;
                } else if((cryptogramHistory.getStatus() == CryptogramHistory.Status.FAILED) && (cryptogramHistory.getPlayer().getUsername().equals(player.getUsername()))) {
                    numFailed++;
                }
            }

            ExternalWebService.getInstance().updateRatingService(player.getUsername(), player.getFirstname(), player.getLastname(), numSolved, numStarted, numFailed);

        }


        List<ExternalWebService.PlayerRating> playerRatings = ExternalWebService.getInstance().syncRatingService();

        for (ExternalWebService.PlayerRating playerRating : playerRatings) {
            SimulatedRating rating = new SimulatedRating(playerRating.getFirstname(),playerRating.getLastname(),playerRating.getSolved(),playerRating.getStarted(),playerRating.getIncorrect());
            ratingsList.add(rating);
        }

        for (int a = 0; a < ratingsList.size(); a ++) {
            for (int b = a ; b < ratingsList.size(); b ++) {
                if ((ratingsList.get(a).getSolved()) < (ratingsList.get(b).getSolved())) {

                    SimulatedRating temp = ratingsList.get(a);
                    ratingsList.set(a, ratingsList.get(b));
                    ratingsList.set(b,temp);

                }

            }

        }

        RatingsAdapter adapter = new RatingsAdapter(this, R.layout.ratingslayout, ratingsList);
        ratingsView.setAdapter(adapter);

    }


    public void onBackPressed() {
        transition.exit(this);
    }

}


