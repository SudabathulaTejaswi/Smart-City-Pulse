package com.example.smart_city_pulse.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import com.example.smart_city_pulse.R;

public class DevelopersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DevelopersAdapter adapter;
    private List<Developer> developerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample developer data
        developerList = new ArrayList<>();
        developerList.add(new Developer("Tejaswini", "Developer", R.drawable.dev3));
        developerList.add(new Developer("Chaitanya", "Developer", R.drawable.dev2));
        developerList.add(new Developer("Naveen Kumar", "Developer", R.drawable.dev1));
        developerList.add(new Developer("Mallika", "Developer", R.drawable.dev4));

        adapter = new DevelopersAdapter(developerList);
        recyclerView.setAdapter(adapter);
    }

    // Developer Model Class
    class Developer {
        private String name;
        private String role;
        private int imageResId;

        public Developer(String name, String role, int imageResId) {
            this.name = name;
            this.role = role;
            this.imageResId = imageResId;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public int getImageResId() {
            return imageResId;
        }
    }

    // Adapter Class
    class DevelopersAdapter extends RecyclerView.Adapter<DevelopersAdapter.ViewHolder> {
        private List<Developer> developerList;

        public DevelopersAdapter(List<Developer> developerList) {
            this.developerList = developerList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout layout = new LinearLayout(parent.getContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setPadding(16, 16, 16, 16);

            ImageView imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            layout.addView(imageView);

            LinearLayout textContainer = new LinearLayout(parent.getContext());
            textContainer.setOrientation(LinearLayout.VERTICAL);
            textContainer.setPadding(16, 0, 0, 0);

            TextView nameView = new TextView(parent.getContext());
            nameView.setTextSize(18);
            textContainer.addView(nameView);

            TextView roleView = new TextView(parent.getContext());
            roleView.setTextSize(14);
            textContainer.addView(roleView);

            layout.addView(textContainer);

            return new ViewHolder(layout, imageView, nameView, roleView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Developer developer = developerList.get(position);
            holder.name.setText(developer.getName());
            holder.role.setText(developer.getRole());
            holder.image.setImageResource(developer.getImageResId());
        }

        @Override
        public int getItemCount() {
            return developerList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, role;
            ImageView image;

            public ViewHolder(@NonNull LinearLayout itemView, ImageView image, TextView name, TextView role) {
                super(itemView);
                this.image = image;
                this.name = name;
                this.role = role;
            }
        }
    }
}