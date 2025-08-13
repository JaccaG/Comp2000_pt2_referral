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

import com.example.comp200pt1.api.LibraryApi;
import com.example.comp200pt1.api.dto.ApiMessage;
import com.example.comp200pt1.api.dto.IssueBookRequest;
import com.example.comp200pt1.db.DatabaseWrapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRequestAdapter extends ArrayAdapter<BookRequest> {

    // Keeps references I need to handle approve/deny and refresh the list
    private final Context context;
    private final List<BookRequest> data;
    private final DatabaseWrapper db;
    private final LibraryApi api;
    private final Runnable onChanged;

    public BookRequestAdapter(Context context, List<BookRequest> data,
                              DatabaseWrapper db, LibraryApi api, Runnable onChanged) {
        super(context, 0, data);
        this.context = context;
        this.data = data;
        this.db = db;
        this.api = api;
        this.onChanged = onChanged;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate row view to custom layout
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.request_item, parent, false);
        }

        // Grab the request being shown
        BookRequest r = data.get(position);

        // Bind inputs
        TextView title = convertView.findViewById(R.id.title);
        TextView requestedBy = convertView.findViewById(R.id.requestedBy);
        TextView info1 = convertView.findViewById(R.id.infoLine1);
        TextView info2 = convertView.findViewById(R.id.infoLine2);
        TextView statusBadge = convertView.findViewById(R.id.statusBadge);
        TextView statusLabel = convertView.findViewById(R.id.statusLabel);
        LinearLayout actions = convertView.findViewById(R.id.actions);
        Button approve = convertView.findViewById(R.id.approveBtn);
        Button deny = convertView.findViewById(R.id.denyBtn);

        // Fill in the row with request details
        title.setText(r.getTitle());
        requestedBy.setText(context.getString(R.string.requested_by_adapter, r.getRequestedBy()));
        info1.setText(context.getString(R.string.requested_date_adapter, r.getRequestedDate()));

        // For staff view, default to “pending” with actions visible
        statusBadge.setText(R.string.pending_adapter);
        statusBadge.setBackgroundResource(R.drawable.badge_pending);
        statusLabel.setText(R.string.pending_adapter);
        info2.setText(r.getEstText());
        info2.setTextColor(0xFF4B5563);
        actions.setVisibility(View.VISIBLE);

        // Button clicks for this row
        approve.setOnClickListener(v -> approveRequest(r));
        deny.setOnClickListener(v -> denyRequest(r));

        return convertView;
    }

    // When approved: builds issue + return dates, calls API to /books (issue)
    private void approveRequest(BookRequest r) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Calendar.getInstance().getTime());

        Calendar ret = Calendar.getInstance();
        ret.add(Calendar.DAY_OF_YEAR, 14);
        String returnDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(ret.getTime());

        IssueBookRequest body = new IssueBookRequest(
                r.getRequestedBy(),
                r.getTitle(),
                today,
                returnDate
        );

        // Issue callback after action to refresh list
        api.issueBook(body).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Issue failed (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If OK, update local DB (mark request approved + make book unavailable)
                db.approveRequest(r.getRequestedBy(), r.getTitle(), today, returnDate);
                db.setAvailabilityByTitle(r.getTitle(), false);

                Toast.makeText(context, "Approved & issued", Toast.LENGTH_SHORT).show();
                if (onChanged != null) onChanged.run();
            }

            @Override
            public void onFailure(Call<ApiMessage> call, Throwable t) {
                Toast.makeText(context, "Network error: " + t.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Deny just marks the request as denied in SQLite and refreshes
    private void denyRequest(BookRequest r) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Calendar.getInstance().getTime());

        db.denyRequest(r.getRequestedBy(), r.getTitle(), "Denied by staff", today);
        Toast.makeText(context, "Denied", Toast.LENGTH_SHORT).show();
        if (onChanged != null) onChanged.run();
    }
}
