package petarkitanovic.androidkurs.k7tech.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import petarkitanovic.androidkurs.k7tech.R;
import petarkitanovic.androidkurs.k7tech.models.Hourly;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private Context context;
    private List<Hourly> hourlyWeather;


    public WeatherAdapter(Context context, List<Hourly> hourlyWeather) {
        this.context = context;
        this.hourlyWeather = hourlyWeather;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_view, parent, false);

        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Log.d("Lista", "onBindViewHolder: " + hourlyWeather.size());

        long unix_seconds = hourlyWeather.get(position).getDt();
        Date date = new Date(unix_seconds * 1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("HH:mm");
        jdf.setTimeZone(TimeZone.getDefault());
        String java_date = jdf.format(date);

        holder.timeTV.setText(java_date);
        holder.temperaturaTV.setText(Integer.valueOf(hourlyWeather.get(position).getTemp().intValue()) + "\u00B0");
        holder.vlaznostTV.setText(hourlyWeather.get(position).getHumidity() + "%");
        holder.vetarTV.setText(hourlyWeather.get(position).getWindSpeed() + "m/s");


    }

    @Override
    public int getItemCount() {

        return 24;

    }


    static class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView timeTV, temperaturaTV, vlaznostTV, vetarTV;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTV = itemView.findViewById(R.id.timeTextView);
            temperaturaTV = itemView.findViewById(R.id.temperaturaTextView);
            vlaznostTV = itemView.findViewById(R.id.vlaznostTextView);
            vetarTV = itemView.findViewById(R.id.vetarTextView);

        }

    }
}
