package android.app.cryptogram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.kogitune.activity_transition.ExitActivityTransition;

import java.util.List;

import android.app.cryptogram.R;

import android.app.cryptogram.domain.Cryptogram;
import edu.gatech.seclass.utilities.ExternalWebService;


public class ChooseCryptogramActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private static final String TAG = "ChooseCryptogramAct";

    private RecyclerView recyclerView;
    private CryptogramAdapter adapter;
    private ExitActivityTransition transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cryptogram);
        transition = ActivityTransition.with(getIntent()).to(findViewById(R.id.choosecryptogram_layout)).start(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.cryptogram_recycler_view);
        retrieveData();
        adapter = new CryptogramAdapter(this);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    private void retrieveData() {
        List<String[]> strings = ExternalWebService.getInstance().syncCryptogramService();

        if(strings != null){
            for (String[] string : strings) {
                String uid = string[0];
                String encrypted = string[1];
                String solution = string[2];

                List<Cryptogram> cryptograms = Cryptogram.find(Cryptogram.class, "uid = ?", uid);
                if(cryptograms.isEmpty()){
                    Cryptogram cryptogram = new Cryptogram(encrypted, solution, uid);
                    cryptogram.save();
                    Log.i(TAG,"new cryptogram_id=" +cryptogram.getId());
                }
            }
        }

    }

    @Override
    public void recyclerViewListClicked(View view, int position) {
        Log.i(TAG,"position="+position);

        Intent intent = new Intent(this, ViewCryptogramActivity.class);
        intent.putExtra("cryptogram_id", adapter.getItem(position).getId());
        ActivityTransitionLauncher.with(this).from(view).launch(intent);
    }

    public void onBackPressed() {
        transition.exit(ChooseCryptogramActivity.this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();;
    }
}
