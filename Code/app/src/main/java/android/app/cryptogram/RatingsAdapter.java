package android.app.cryptogram;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import android.app.cryptogram.R;

public class RatingsAdapter extends ArrayAdapter<SimulatedRating> {

    private Context mContext;
    int mResource;

    public RatingsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<SimulatedRating> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //String username = getItem(position).getUsername();
        String firstname = getItem(position).getFistname();
        String lastname = getItem(position).getLastname();
        String solved = ""+getItem(position).getSolved();
        String started = ""+getItem(position).getStarted();
        String failed = ""+getItem(position).getFailed();

        SimulatedRating r1 = new SimulatedRating(firstname,lastname,Integer.parseInt(solved),Integer.parseInt(started),Integer.parseInt(failed));

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.textView_name);
        //TextView tvFirstname = (TextView) convertView.findViewById(R.id.textView_firstname);
        //TextView tvLastname = (TextView) convertView.findViewById(R.id.textView_lastname);
        TextView tvStats = (TextView) convertView.findViewById(R.id.textView_stats);
        //TextView tvSubmitted = (TextView) convertView.findViewById(R.id.textView_submitted);
        //TextView tvFailed = (TextView) convertView.findViewById(R.id.textView_failed);

        tvName.setText("Name: " + firstname + " " + lastname);
        //tvFirstname.setText(firstname);
        //tvLastname.setText(lastname);
        tvStats.setText("# solved: " + solved + " | # started: " + started + " | # wrong: " + failed);
        //tvSubmitted.setText(submitted);
        //tvFailed.setText(failed);

        return convertView;


    }
}
