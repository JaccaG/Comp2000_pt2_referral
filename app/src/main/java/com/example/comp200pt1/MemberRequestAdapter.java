package com.example.comp200pt1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

// Adapter for members "My Requests" list
public class MemberRequestAdapter extends ArrayAdapter<MemberRequest> {

    private final Context context;
    private final List<MemberRequest> data;

    public MemberRequestAdapter(Context context, List<MemberRequest> data) {
        super(context, 0, data);
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    // Inflate row to custom layout for list
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.request_item_member, parent, false);
        }

        MemberRequest r = data.get(position);

        // Bind views
        TextView title = convertView.findViewById(R.id.title);
        TextView badge = convertView.findViewById(R.id.statusBadge);
        TextView lineRequested = convertView.findViewById(R.id.lineRequested);
        TextView lineA = convertView.findViewById(R.id.lineA);
        TextView lineB = convertView.findViewById(R.id.lineB);

        title.setText(r.getTitle());
        lineRequested.setText(context.getString(R.string.requested_date_adapter, r.getRequestedDate()));

        // Show specific info any styling based on status e.g. denied/approved
        String s = r.getStatus();
        if ("approved".equalsIgnoreCase(s)) {
            badge.setText(R.string.approved_adapter);
            badge.setBackgroundResource(R.drawable.badge_approved);
            lineA.setText(context.getString(R.string.approved_date_adapter, r.getApprovedDate()));
            lineA.setTextColor(0xFF16A34A);
            lineB.setText(context.getString(R.string.pickup_by_adapter, r.getPickupBy()));
            lineB.setTextColor(0xFF16A34A);
            lineA.setVisibility(View.VISIBLE);
            lineB.setVisibility(View.VISIBLE);
        } else if ("denied".equalsIgnoreCase(s)) {
            badge.setText(R.string.denied_adapter);
            badge.setBackgroundResource(R.drawable.badge_denied);
            lineA.setText(context.getString(R.string.denied_date_adapter, r.getDeniedDate()));
            lineA.setTextColor(0xFFDC2626);
            lineB.setText(r.getDenyReason());
            lineB.setTextColor(0xFFDC2626);
            lineA.setVisibility(View.VISIBLE);
            lineB.setVisibility(View.VISIBLE);
        } else {
            badge.setText(R.string.pending_adapter);
            badge.setBackgroundResource(R.drawable.badge_pending); // if typo, change to your file name
            lineA.setText(r.getEstText());
            lineA.setTextColor(0xFF2563EB);
            lineB.setText(r.getPickupHint());
            lineB.setTextColor(0xFF2563EB);
            lineA.setVisibility(View.VISIBLE);
            lineB.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
