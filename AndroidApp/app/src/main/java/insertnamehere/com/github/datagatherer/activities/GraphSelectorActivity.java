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
import insertnamehere.com.github.datagatherer.util.DataMath;
import insertnamehere.com.github.datagatherer.util.data.Data;

public class GraphSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data[] data = DataMath.getData();

        LinearLayout layout = findViewById(R.id.linearLayout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        for(Data d : data){
            Button button = new Button(this);
            button.setLayoutParams(layoutParams);
            button.setText(d.getName());
            button.setOnClickListener(view -> {
            });
            layout.addView(button);
        }
    }
}
