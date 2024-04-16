package org.me.gcu.hood_rhys_s2332605.viewModels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.widget.Toolbar;

import org.me.gcu.hood_rhys_s2332605.R;
import org.me.gcu.hood_rhys_s2332605.models.ThreeDayWeather;
import org.me.gcu.hood_rhys_s2332605.models.Weather;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

public class DetailedViewModel extends AppCompatActivity implements OnClickListener {
    private TextView maxTempTxt;
    private Button returnBtn;
    private Button locationRightButton;
    private String[] locationNames = {"Glasgow", "London", "New York", "Oman", "Mauritius", "Bangladesh"};
    private String[] locationIDs = {"2648579", "2643743", "5128581", "287286", "934154", "1185241"};
    private int selectedIndex = 0;
    private ThreeDayWeather threeDayWeather;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            selectedIndex = 0;
        }else {
            selectedIndex = bundle.getInt("selectedIndex");
            threeDayWeather = (ThreeDayWeather) getIntent().getExtras().getParcelable("threeDayWeather");
            threeDayWeather.setFirstDay((Weather) getIntent().getExtras().getParcelable("dayOne"));
            threeDayWeather.setSecondDay((Weather) getIntent().getExtras().getParcelable("dayTwo"));
            threeDayWeather.setThirdDay((Weather) getIntent().getExtras().getParcelable("dayThree"));
        }
        TextView locationName = (TextView)findViewById(R.id.locationNameTxt);
        locationName.setText(locationNames[selectedIndex]);
        maxTempTxt = (TextView)findViewById(R.id.maxTempTxt);
        maxTempTxt.setText(threeDayWeather.getFirstDay().getMaxTemp());
        returnBtn = (Button)findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(this);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void onClick(View v)
    {
        if (v == returnBtn) {
            Bundle bundle = new Bundle();
            bundle.putInt("selectedIndex",selectedIndex);
            Intent startThreeDayView = new Intent(DetailedViewModel.this, ThreeDayViewModel.class);
            startThreeDayView.putExtras(bundle);
            startActivity(startThreeDayView);
        }
    }


}