package com.example.phonebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private final List<Contact> allContacts;
    private final List<Contact> shownContacts;
    private String currentQuery = "";

    public ContactAdapter(List<Contact> contacts) {
        this.allContacts = new ArrayList<>(contacts);
        this.shownContacts = new ArrayList<>(contacts);
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvEmail;
        ImageView ivDelete;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = shownContacts.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhone());
        holder.tvEmail.setText(contact.getEmail());

        holder.ivDelete.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            Contact removed = shownContacts.remove(pos);
            allContacts.remove(removed);
            notifyItemRemoved(pos);
        });
    }

    @Override
    public int getItemCount() {
        return shownContacts.size();
    }

    public void addContact(Contact contact) {
        allContacts.add(contact);
        filter(currentQuery);
    }

    public void filter(String query) {
        currentQuery = query == null ? "" : query.trim();
        String q = currentQuery.toLowerCase(Locale.getDefault());

        shownContacts.clear();
        if (q.isEmpty()) {
            shownContacts.addAll(allContacts);
        } else {
            for (Contact c : allContacts) {
                if (c.getName().toLowerCase(Locale.getDefault()).contains(q)
                        || c.getPhone().toLowerCase(Locale.getDefault()).contains(q)
                        || c.getEmail().toLowerCase(Locale.getDefault()).contains(q)) {
                    shownContacts.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }
}