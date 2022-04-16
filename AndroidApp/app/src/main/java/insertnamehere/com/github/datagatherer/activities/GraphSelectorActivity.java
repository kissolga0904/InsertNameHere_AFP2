package insertnamehere.com.github.datagatherer.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import insertnamehere.com.github.datagatherer.R;
import insertnamehere.com.github.datagatherer.util.data.Data;

public class GraphSelectorActivity extends AppCompatActivity {

    List<Data> data = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = findViewById(R.id.linearLayout);
        // TODO add buttons programatically with LinearLayout#addView

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Button button = new Button(this);
        button.setLayoutParams(layoutParams);
        button.setText("valami");
        button.setOnClickListener(view -> {

        });
    }
}
