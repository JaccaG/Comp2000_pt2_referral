package com.example.comp200pt1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

// Adapter for the members list rows
public class MemberAdapter extends ArrayAdapter<Member> {

    private final Context context;
    private final List<Member> members;

    public MemberAdapter(Context context, List<Member> members) {
        super(context, 0, members);
        this.context = context;
        this.members = members;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Member member = members.get(position);

        // Inflate custom row layout for list items
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.member_item, parent, false);
        }

        // Bind views
        TextView name = convertView.findViewById(R.id.memberName);
        TextView email = convertView.findViewById(R.id.memberEmail);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

        name.setText(member.getFullName());
        email.setText(member.getEmail());

        // Edit button to open edit screen for member
        ImageButton editBtn = convertView.findViewById(R.id.editButton);
        editBtn.setOnClickListener(v -> {
            Intent i = new Intent(context, EditMemberActivity.class);
            i.putExtra("member", member);
            context.startActivity(i);
        });

        // Delete button - open delete confirmation dialog
        deleteButton.setOnClickListener(v -> showDeleteDialog(member));

        return convertView;
    }

    // Build and show custom delete confirmation dialog
    private void showDeleteDialog(Member member) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_member, null, false);

        TextView title = dialogView.findViewById(R.id.deleteTitle);
        TextView msg = dialogView.findViewById(R.id.deleteMessage);
        Button confirm = dialogView.findViewById(R.id.confirmDeleteBtn);
        Button cancel = dialogView.findViewById(R.id.cancelDeleteBtn);

        title.setText(context.getString(R.string.delete_member_dialog_title));
        msg.setText(context.getString(R.string.delete_member_message, member.getFullName()));
        confirm.setText(context.getString(R.string.delete_member_button_confirm));
        cancel.setText(context.getString(R.string.cancel_delete_member_button));

        AlertDialog dlg = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        // Confirm button - remove member and refresh list
        confirm.setOnClickListener(v -> {
            members.remove(member);
            notifyDataSetChanged();
            Toast.makeText(context, context.getString(R.string.deleted_member_toast, member.getFullName()), Toast.LENGTH_SHORT).show();
            dlg.dismiss();
        });

        // Cancel button - close dialog
        cancel.setOnClickListener(v -> dlg.dismiss());

        // rounded dialog box - gets rid of white corners
        dlg.show();
        if (dlg.getWindow() != null) {
            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
