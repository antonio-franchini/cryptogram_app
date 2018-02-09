package android.app.cryptogram;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.cryptogram.R;

import android.app.cryptogram.domain.CryptogramHistory;


public class CryptogramHistoryAdapter extends RecyclerView.Adapter<CryptogramHistoryAdapter.ViewHolder> {

    private List<CryptogramHistoryDisplay> historyDisplays = new ArrayList<>();
    private static RecyclerViewClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView statusImage;
        public TextView timeView,  statusView;
        public Drawable failed, success, pending;

        public ViewHolder(View view) {
            super(view);
            timeView = (TextView) view.findViewById(R.id.cryptogramhistory_time);
            statusView = (TextView) view.findViewById(R.id.cryptogramhistory_status_text);
            statusImage = (ImageView) view.findViewById(R.id.cryptogramhistory_status_image);


            failed = view.getContext().getResources().getDrawable(R.drawable.failed);
            success = view.getContext().getResources().getDrawable(R.drawable.success);
            pending = view.getContext().getResources().getDrawable(R.drawable.pending);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }

    public CryptogramHistoryAdapter(final RecyclerViewClickListener clickListener, final Long cryptogram_id) {
        CryptogramHistoryAdapter.clickListener = clickListener;

        this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                historyDisplays.clear();
                loadData(cryptogram_id);
            }
        });

        loadData(cryptogram_id);
    }

    private void loadData(Long cryptogram_id) {
        List<CryptogramHistory> cryptogramHistories = CryptogramHistory.find(CryptogramHistory.class, "cryptogram = ? and player = ?", cryptogram_id.toString(), String.valueOf(PersistentData.currentPlayer.getId()));
        for (CryptogramHistory cryptogramHistory : cryptogramHistories) {
            CryptogramHistoryDisplay display = new CryptogramHistoryDisplay();
            display.setStartDate(""+ DateFormat.format("dd/MM/yy", cryptogramHistory.getDate()));
            display.setStartTime("" + DateFormat.format("HH:mm:ss", cryptogramHistory.getDate()));
            display.setStatus(cryptogramHistory.getStatus());
            display.setCryptogramHistory(cryptogramHistory);

            historyDisplays.add(display);
        }

        Collections.sort(cryptogramHistories, new Comparator<CryptogramHistory>() {
            @Override
            public int compare(CryptogramHistory o1, CryptogramHistory o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cryptogramhistory_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CryptogramHistoryDisplay display = historyDisplays.get(position);

        holder.timeView.setText(display.getStartDate()+ " " + display.getStartTime());
        holder.statusView.setText(String.format("Status: %s", display.getStatus().getDesc()));

        switch (display.getStatus()){
            case FAILED:
                holder.statusImage.setImageDrawable(holder.failed);
                break;
            case SOLVED:
                holder.statusImage.setImageDrawable(holder.success);
                break;
            case IN_PROGRESS:
                holder.statusImage.setImageDrawable(holder.pending);
                break;
        }
    }

    public CryptogramHistory getItem(int pos){
        return historyDisplays.get(pos).getCryptogramHistory();
    }

    @Override
    public int getItemCount() {
        return historyDisplays.size();
    }

    class CryptogramHistoryDisplay{
        private String startDate;
        private String startTime;
        private CryptogramHistory.Status status;
        private CryptogramHistory cryptogramHistory;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public CryptogramHistory.Status getStatus() {
            return status;
        }

        public void setStatus(CryptogramHistory.Status status) {
            this.status = status;
        }

        public CryptogramHistory getCryptogramHistory() {
            return cryptogramHistory;
        }

        public void setCryptogramHistory(CryptogramHistory cryptogramHistory) {
            this.cryptogramHistory = cryptogramHistory;
        }
    }
}