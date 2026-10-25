package com.example.phonebook;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ContactAdapter adapter;
    private RecyclerView recyclerView;

    private final ActivityResultLauncher<Intent> addLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    Contact c = new Contact(
                            data.getStringExtra("name"),
                            data.getStringExtra("phone"),
                            data.getStringExtra("email"));
                    adapter.addContact(c);
                    if (adapter.getItemCount() > 0) {
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        EditText etSearch = findViewById(R.id.etSearch);
        Button btnAdd = findViewById(R.id.btnAdd);

        adapter = new ContactAdapter(createSampleData());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnAdd.setOnClickListener(v ->
                addLauncher.launch(new Intent(MainActivity.this, AddContactActivity.class)));
    }

    private List<Contact> createSampleData() {
        List<Contact> list = new ArrayList<>();
        list.add(new Contact("Johnny", "0988312345", "johnnyhong@gmail.com"));
        list.add(new Contact("Jessica", "0981911934", "jessicahuang@gmail.com"));
        for (int i = 1; i <= 30; i++) {
            list.add(new Contact(
                    "聯絡人" + i,
                    String.format("09%08d", 12345600 + i),
                    "user" + i + "@example.com"));
        }
        return list;
    }
}