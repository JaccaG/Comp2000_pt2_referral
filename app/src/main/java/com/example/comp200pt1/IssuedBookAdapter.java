package com.example.comp200pt1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comp200pt1.api.dto.IssuedBookDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Binds each row to custom item layout
public class IssuedBookAdapter extends ArrayAdapter<IssuedBookDto> {

    // Custom layout view inflater
    private final LayoutInflater inflater;

    public IssuedBookAdapter(@NonNull Context ctx, @NonNull List<IssuedBookDto> data) {
        super(ctx, 0, data);
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.issued_book_item, parent, false);
        }

        //Get current item
        IssuedBookDto d = getItem(position);
        if (d == null) return convertView;

        // Connect row views
        TextView title = convertView.findViewById(R.id.bookTitle);
        TextView issue = convertView.findViewById(R.id.issueDate);
        TextView ret   = convertView.findViewById(R.id.returnDate);

        title.setText(d.bookTitle == null ? "" : d.bookTitle);

        // Date formatter for yyyy-mm-dd whenever possible
        String issueText = "Issued: " + prettyDate(d.issueDate);
        String returnText = "Return: " + prettyDate(d.returnDate);

        issue.setText(issueText);
        ret.setText(returnText);

        return convertView;
    }

    // Try to turn API date into yyyy-mm-dd. If it fails, show "-"
    private String prettyDate(String raw) {
        if (raw == null || raw.trim().isEmpty()) return "-";

        String[] patterns = new String[]{
                "yyyy-MM-dd",
                "EEE, dd MMM yyyy HH:mm:ss zzz"
        };
        for (String p : patterns) {
            try {
                Date dt = new SimpleDateFormat(p, Locale.UK).parse(raw);
                if (dt != null) {
                    return new SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(dt);
                }
            } catch (ParseException ignore) {}
        }
        return raw;
    }
}
