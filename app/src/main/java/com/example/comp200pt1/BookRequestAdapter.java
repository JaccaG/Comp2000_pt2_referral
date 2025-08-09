package com.example.comp200pt1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

// Adapter for the staff "Book Requests" list
public class BookRequestAdapter extends ArrayAdapter<BookRequest> {

    private final Context context;
    private final List<BookRequest> data;

    public BookRequestAdapter(Context context, List<BookRequest> data) {
        super(context, 0, data);
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) { // Inflate the row into item layout I made
            convertView = LayoutInflater.from(context).inflate(R.layout.request_item, parent, false);
        }

        BookRequest r = data.get(position);

        // Bind views
        TextView title = convertView.findViewById(R.id.title);
        TextView requestedBy = convertView.findViewById(R.id.requestedBy);
        TextView info1 = convertView.findViewById(R.id.infoLine1);
        TextView info2 = convertView.findViewById(R.id.infoLine2);
        TextView statusBadge = convertView.findViewById(R.id.statusBadge);
        TextView statusLabel = convertView.findViewById(R.id.statusLabel);
        LinearLayout actions = convertView.findViewById(R.id.actions);
        Button approve = convertView.findViewById(R.id.approveBtn);
        Button deny = convertView.findViewById(R.id.denyBtn);

        // Populate fields
        title.setText(r.getTitle());
        requestedBy.setText(context.getString(R.string.requested_by_adapter, r.getRequestedBy()));
        info1.setText(context.getString(R.string.requested_date_adapter, r.getRequestedDate()));

        // Update status UI - badge colour etc
        if (r.isApproved()) {
            statusBadge.setText(R.string.approved_adapter);
            statusBadge.setBackgroundResource(R.drawable.badge_approved);
            statusLabel.setText(R.string.approved_adapter);
            info2.setText(r.getPickupText());
            info2.setTextColor(0xFF16A34A);
            actions.setVisibility(View.GONE);
        } else {
            statusBadge.setText(R.string.pending_adapter);
            statusBadge.setBackgroundResource(R.drawable.badge_pending);
            statusLabel.setText(R.string.pending_adapter);
            info2.setText(r.getEstText());
            info2.setTextColor(0xFF4B5563);
            actions.setVisibility(View.VISIBLE);
        }

        // on approve - set pickup date and refresh
        approve.setOnClickListener(v -> {
            String pickupBy = "Pickup by: " + calcPickupDate();
            r.approve(pickupBy);
            notifyDataSetChanged();
            Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show();
        });

        // on deny - remove from list
        deny.setOnClickListener(v -> {
            data.remove(r);
            notifyDataSetChanged();
            Toast.makeText(context, "Denied", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    // temporarily fixed date 3 days from given time for pickup TODO adapt for API later
    private String calcPickupDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 3);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
    }
}
