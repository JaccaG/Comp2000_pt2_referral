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

import com.example.comp200pt1.api.ApiClient;
import com.example.comp200pt1.api.LibraryApi;
import com.example.comp200pt1.api.dto.ApiMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberAdapter extends ArrayAdapter<Member> {

    // Kept both the display list (members) and a parallel list of usernames (usernames) - saved me changing my row model
    private final Context context;
    private final List<Member> members;
    private final List<String> usernames;

    public MemberAdapter(Context context, List<Member> members, List<String> usernames) {
        super(context, 0, members);
        this.context = context;
        this.members = members;
        this.usernames = usernames;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Member member = members.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.member_item, parent, false);
        }

        // Connect input to rows
        TextView name = convertView.findViewById(R.id.memberName);
        TextView email = convertView.findViewById(R.id.memberEmail);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

        name.setText(member.getFullName());
        email.setText(member.getEmail());

        // Edit flow: pass username + split the full name into first/last to prefill the edit screen
        ImageButton editBtn = convertView.findViewById(R.id.editButton);
        editBtn.setOnClickListener(v -> {
            String username = "";
            if (usernames != null && position >= 0 && position < usernames.size()) {
                username = usernames.get(position);
            }

            String first = "";
            String last = "";
            String fullName = member.getFullName() == null ? "" : member.getFullName().trim();
            if (!fullName.isEmpty()) {
                String[] parts = fullName.split("\\s+", 2);
                first = parts.length > 0 ? parts[0] : "";
                last  = parts.length > 1 ? parts[1] : "";
            }

            Intent i = new Intent(context, EditMemberActivity.class);
            i.putExtra("username", username);
            i.putExtra("firstname", first);
            i.putExtra("lastname", last);
            i.putExtra("email", member.getEmail());
            i.putExtra("contact", member.getContact());
            i.putExtra("membershipEndDate", member.getMemberSince());
            context.startActivity(i);
        });

        // Delete: look up the matching username for this row (same index) and put both into the dialog.
        final String usernameForRow =
                (usernames != null && position >= 0 && position < usernames.size())
                        ? usernames.get(position) : "";
        deleteButton.setOnClickListener(v -> showDeleteDialog(member, usernameForRow, position));

        return convertView;
    }

    // Custom confirm dialog for delete so I can show a message, saves deleting by accident
    private void showDeleteDialog(Member member, String username, int position) {
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

        confirm.setOnClickListener(v -> {
            // If don’t have a username (edge case), just removes locally
            if (username == null || username.isEmpty()) {
                members.remove(member);
                notifyDataSetChanged();
                Toast.makeText(context, context.getString(R.string.deleted_member_toast, member.getFullName()), Toast.LENGTH_SHORT).show();
                dlg.dismiss();
                return;
            }

            // Disable buttons while the network call is processing
            confirm.setEnabled(false);
            cancel.setEnabled(false);

            LibraryApi api = ApiClient.get();
            api.deleteMember(username).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ApiMessage> call, @NonNull Response<ApiMessage> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Delete failed (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                        confirm.setEnabled(true);
                        cancel.setEnabled(true);
                        return;
                    }

                    // Success: remove from both lists to keep indexes aligned, then refresh the adapter
                    members.remove(member);
                    if (usernames != null && position >= 0 && position < usernames.size()) {
                        usernames.remove(position);
                    }
                    notifyDataSetChanged();
                    Toast.makeText(context, context.getString(R.string.deleted_member_toast, member.getFullName()), Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<ApiMessage> call, @NonNull Throwable t) {
                    // Allows for retry in case error was one-time case
                    Toast.makeText(context, "Network error: " + t.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                    confirm.setEnabled(true);
                    cancel.setEnabled(true);
                }
            });
        });

        cancel.setOnClickListener(v -> dlg.dismiss());

        // Gets rid of sharp white corners on dialog box (ugly)
        dlg.show();
        if (dlg.getWindow() != null) {
            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
