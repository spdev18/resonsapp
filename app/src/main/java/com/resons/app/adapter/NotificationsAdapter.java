package com.resons.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.resons.app.R;
import com.resons.app.model.request.response.NotificationsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<NotificationsModel> notificationsModels;

    private itemClick onclick;

    public NotificationsAdapter(Context context, ArrayList<NotificationsModel> notificationsModel,
                                itemClick onclick) {

        this.context = context;
        this.notificationsModels = notificationsModel;
        this.onclick=onclick;
    }

    @NonNull
    @Override
    public NotificationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adp_notifications, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.MyViewHolder holder, int position) {
        NotificationsModel notificationsModel = notificationsModels.get(position);
        holder.tvName.setText(notificationsModel.getMessage());
        holder.tvDate.setText(convertAndDisplayDate(notificationsModel.getTimestamp()));

        if (notificationsModel.isIs_read()){
            holder.ivCircle.setImageResource(R.drawable.circle_grey);
            holder.tvRead.setVisibility(View.VISIBLE);
            holder.tvRead.setText("Seen");
            holder.tvRead.setTextColor(ContextCompat.getColor(context,R.color.green));
        }else {
            holder.ivCircle.setImageResource(R.drawable.circle_blue);
            holder.tvRead.setVisibility(View.GONE);
            holder.tvName.setTypeface(holder.tvName.getTypeface(), Typeface.BOLD);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onClick(position,notificationsModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName,tvDate,tvRead;
        private ImageView ivCircle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvRead=itemView.findViewById(R.id.tvRead);
            ivCircle=itemView.findViewById(R.id.ivCircle);
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

    public interface itemClick{
        void onClick(int pos,NotificationsModel notificationsModel);
    }
}
