package android.app.cryptogram;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.util.Date;

import android.app.cryptogram.R;

import android.app.cryptogram.domain.Cryptogram;
import android.app.cryptogram.domain.CryptogramHistory;


public class ViewCryptogramActivity extends AppCompatActivity  implements RecyclerViewClickListener {

    private static final String TAG = "ViewCryptogramActivity";

    private EmptyRecyclerView recyclerView;
    private CryptogramHistoryAdapter adapter;
    private FloatingActionButton fabViewCryptogram;
    private ExitActivityTransition transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cryptogram);
        transition = ActivityTransition.with(getIntent()).to(findViewById(R.id.viewcryptogram_layout)).start(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final long cryptogram_id = intent.getLongExtra("cryptogram_id",-1);
        Log.i(TAG, "cryptogram id="+ cryptogram_id);

        recyclerView = (EmptyRecyclerView) findViewById(R.id.cryptogram_history_recycler_view);
        recyclerView.setEmptyView(findViewById(R.id.cryptogramhistory_empty_view));
        adapter = new CryptogramHistoryAdapter(this, cryptogram_id);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        fabViewCryptogram = (FloatingActionButton) findViewById(R.id.fabViewCryptogram);
        fabViewCryptogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cryptogram cryptogram = Cryptogram.findById(Cryptogram.class, cryptogram_id);

                CryptogramHistory history = new CryptogramHistory();
                history.setStatus(CryptogramHistory.Status.IN_PROGRESS);
                history.setDate(new Date());
                history.setAttempt(cryptogram.getEncrypted());
                history.setCryptogram(cryptogram);
                history.setPlayer(PersistentData.currentPlayer);
                history.save();

                startViewCryptogramActivity(v, history.getId());
            }
        });

    }

    @Override
    public void recyclerViewListClicked(View view, int position) {
        CryptogramHistory cryptogramHistory = adapter.getItem(position);
        Log.i(TAG, cryptogramHistory.toString());

        startViewCryptogramActivity(view, cryptogramHistory.getId());
    }

    private void startViewCryptogramActivity(View view, Long id) {
        Intent intent = new Intent(ViewCryptogramActivity.this, CryptogramActivity.class);
        intent.putExtra("cryptogramHistory_id", id);
        Bundle transitionBundle = ActivityTransitionLauncher.with(this).from(view).createBundle();
        intent.putExtras(transitionBundle);
        startActivityForResult(intent, 1);
// you should prevent default activity transition animation
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.notifyDataSetChanged();
    }

    public void onBackPressed() {
        transition.exit(this);
    }

}
