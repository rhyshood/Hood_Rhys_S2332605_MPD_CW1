package org.me.gcu.hood_rhys_s2332605.viewModels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.me.gcu.hood_rhys_s2332605.R;

public class MainMenuViewModel extends AppCompatActivity implements View.OnClickListener {

    private Button threeDayBtn;

    private Button latestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_view);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        findElements();
        setListeners();

    }

    @Override
    public void onClick(View v) {
        if (v == threeDayBtn){
            Intent threeDayView = new Intent(MainMenuViewModel.this, ThreeDayViewModel.class);
            startActivity(threeDayView);
        } else if (v == latestBtn){
            Intent latestView = new Intent(MainMenuViewModel.this, LatestObservationViewModel.class);
            startActivity(latestView);
        }
    }

    private void findElements(){
        threeDayBtn = findViewById(R.id.threeDayBtn);
        latestBtn = findViewById(R.id.latestBtn);
    }

    private void setListeners(){
        threeDayBtn.setOnClickListener(this);
        latestBtn.setOnClickListener(this);
    }

}