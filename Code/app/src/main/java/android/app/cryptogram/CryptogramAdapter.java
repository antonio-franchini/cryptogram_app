package android.app.cryptogram;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.cryptogram.R;

import android.app.cryptogram.domain.Cryptogram;
import android.app.cryptogram.domain.CryptogramHistory;


public class CryptogramAdapter extends RecyclerView.Adapter<CryptogramAdapter.ViewHolder> {

    private List<CryptogramDisplay> cryptogramDisplays;
    private static RecyclerViewClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView idView,  descView;

        public ViewHolder(View view) {
            super(view);
            idView = (TextView) view.findViewById(R.id.cryptogram_id);
            descView = (TextView) view.findViewById(R.id.cryptogram_desc);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }


    public CryptogramAdapter(RecyclerViewClickListener clickListener) {
        CryptogramAdapter.clickListener = clickListener;
        this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                cryptogramDisplays.clear();
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        Map<Long,CryptogramDisplay> map = new HashMap<>();
        List<CryptogramHistory> cryptogramHistories = CryptogramHistory.findWithQuery(CryptogramHistory.class, "SELECT * FROM CRYPTOGRAM_HISTORY WHERE player = ?", String.valueOf(PersistentData.currentPlayer.getId()));

        for (CryptogramHistory cryptogramHistory : cryptogramHistories) {

            if(cryptogramHistory.getCryptogram() != null) {
                CryptogramDisplay display;
                if (!map.containsKey(cryptogramHistory.getCryptogram().getId())) {
                    display = new CryptogramDisplay();
                    display.setCryptogram(cryptogramHistory.getCryptogram());
                } else {
                    display = map.get(cryptogramHistory.getCryptogram().getId());
                }
                display.setSolved(cryptogramHistory.getStatus().equals(CryptogramHistory.Status.SOLVED) || display.isSolved());
                display.setInvalidSubmissions(display.getInvalidSubmissions() + (cryptogramHistory.getStatus().equals(CryptogramHistory.Status.FAILED) ? 1 : 0));

                map.put(cryptogramHistory.getCryptogram().getId(), display);
            }
        }

        cryptogramDisplays = new ArrayList<>(map.values());
        //now add in the cryptograms that have not been played yet.
        List<Cryptogram> cryptograms = Cryptogram.findWithQuery(Cryptogram.class,
                "SELECT CRYPTOGRAM.id, CRYPTOGRAM.encrypted,CRYPTOGRAM.solution,CRYPTOGRAM.uid \n" +
                        "FROM CRYPTOGRAM\n" +
                        "LEFT JOIN CRYPTOGRAM_HISTORY ON CRYPTOGRAM_HISTORY.cryptogram = CRYPTOGRAM.id\n" +
                        "WHERE CRYPTOGRAM_HISTORY.id IS NULL OR CRYPTOGRAM_HISTORY.player <> ?\n" +
                        "GROUP BY CRYPTOGRAM.id, CRYPTOGRAM.encrypted,CRYPTOGRAM.solution,CRYPTOGRAM.uid",String.valueOf(PersistentData.currentPlayer.getId()));


        for (Cryptogram cryptogram: cryptograms) {
            CryptogramDisplay display = new CryptogramDisplay();
            display.setCryptogram(cryptogram);

            boolean exists = false;
            for (CryptogramDisplay cryptogramDisplay : cryptogramDisplays) {
                if(cryptogram.getId() == cryptogramDisplay.getCryptogram().getId()){
                    exists = true;
                }
            }
            if(!exists){
                cryptogramDisplays.add(display);
            }

        }
        Collections.sort(cryptogramDisplays, new Comparator<CryptogramDisplay>() {
            @Override
            public int compare(CryptogramDisplay o1, CryptogramDisplay o2) {
                return o1.getCryptogram().getUid().compareTo(o2.getCryptogram().getUid());
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cryptogram_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CryptogramDisplay display = cryptogramDisplays.get(position);
        holder.idView.setText(String.format("ID: %s",display.getCryptogram().getUid()));

        //holder.descView.setText(Html.fromHtml(String.format("Solved: %s | <b>Incorrect</b> Submissions: %s", crypt.getSolved(), crypt.getInvalidSubmissions())));
        holder.descView.setText(String.format("Solved: %s | Incorrect Submissions: %s", display.isSolved()? "YES" : "NO", display.getInvalidSubmissions()));
    }

    @Override
    public int getItemCount() {
        return cryptogramDisplays.size();
    }

    public Cryptogram getItem(int pos){
        return cryptogramDisplays.get(pos).getCryptogram();
    }

    class CryptogramDisplay {
        private Cryptogram cryptogram;
        private boolean solved;
        private int invalidSubmissions;


        public Cryptogram getCryptogram() {
            return cryptogram;
        }

        public void setCryptogram(Cryptogram cryptogram) {
            this.cryptogram = cryptogram;
        }

        public boolean isSolved() {
            return solved;
        }

        public void setSolved(boolean solved) {
            this.solved = solved;
        }

        public int getInvalidSubmissions() {
            return invalidSubmissions;
        }

        public void setInvalidSubmissions(int invalidSubmissions) {
            this.invalidSubmissions = invalidSubmissions;
        }
    }
}