package insertnamehere.com.github.datagatherer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import insertnamehere.com.github.datagatherer.R;
import insertnamehere.com.github.datagatherer.util.DataMath;
import insertnamehere.com.github.datagatherer.util.Data;

public class GraphSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_selector);

        Map<String, Data> data = DataMath.getData();

        LinearLayout layout = findViewById(R.id.linearLayout);

        if (layout != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            for (Map.Entry<String, Data> entry : data.entrySet()) {
                Button button = new Button(this);
                button.setLayoutParams(layoutParams);
                button.setText(entry.getKey());
                button.setOnClickListener(view -> {
                    this.onDataButtonClicked(entry.getValue());
                });
                layout.addView(button);
            }
        }
    }

    private void onDataButtonClicked(Data data) {
        GraphActivity.setSelectedData(data);
        Intent switchToGraphActivity = new Intent(this, GraphActivity.class);
        startActivity(switchToGraphActivity);
    }
}
