package org.me.gcu.hood_rhys_s2332605.viewModels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.me.gcu.hood_rhys_s2332605.R;

public class SettingsViewModel extends AppCompatActivity implements View.OnClickListener {

    private Button returnBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);
        findElements();
        assignListeners();
    }

    @Override
    public void onClick(View v) {
        if (v == returnBtn){
            Intent mainMenu = new Intent(SettingsViewModel.this, MainMenuViewModel.class);
            startActivity(mainMenu);
        }
    }

    private void findElements(){
        returnBtn = findViewById(R.id.returnBtn);
    }

    private void assignListeners(){
        returnBtn.setOnClickListener(this);
    }
}