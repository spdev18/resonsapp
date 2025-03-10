package com.resons.app.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.resons.app.R;
import com.resons.app.model.request.response.HistoryResponse;
import com.resons.app.model.request.response.NotificationsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<HistoryResponse> historyResponses;

    public HistoryAdapter(Context context, ArrayList<HistoryResponse> historyResponses) {

        this.context = context;
        this.historyResponses = historyResponses;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adp_casehistory, null);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        HistoryResponse historyResponse = historyResponses.get(position);
        holder.tvCaseId.setText("Case ID: " + historyResponse.getId());
        holder.tvMessage.setText(historyResponse.getText());
        holder.tvDate.setText("Filed: " + convertAndDisplayDate(historyResponse.getTimestamp()));


    }

    @Override
    public int getItemCount() {
        return historyResponses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCaseId, tvMessage, tvDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCaseId = itemView.findViewById(R.id.tvCaseId);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    private String convertAndDisplayDate(String isoDate) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = isoFormat.parse(isoDate);  // Parse the ISO 8601 string to Date object

            // Format the date to "ddMMM yyyy 'at' h:mm a"
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy 'at' h:mm a", Locale.getDefault());
            String formattedDate = displayFormat.format(date);
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

