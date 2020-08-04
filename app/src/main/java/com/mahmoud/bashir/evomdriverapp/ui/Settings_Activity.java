package com.mahmoud.bashir.evomdriverapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahmoud.bashir.evomdriverapp.R;
import com.mahmoud.bashir.evomdriverapp.paperdb.helper.LocaleHelper;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class Settings_Activity extends AppCompatActivity {

    @BindView(R.id.spin_lang)
    Spinner spin_lang;
    @BindView(R.id.sl)
    TextView sl;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        // init toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // init paper first
        Paper.init(this);

        //Default language is English
        String language = Paper.book().read("language");
        if (language == null){
            Paper.book().write("language","en");}

        updateView((String) Paper.book().read("language"));


        spin_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 1){
                    Toast.makeText(Settings_Activity.this, "arabic", Toast.LENGTH_SHORT).show();
                    Paper.book().write("language","ar");

                   // setLocale("ar");

                    updateView((String) Paper.book().read("language"));
                    Intent intent = new Intent(Settings_Activity.this,Splash_Screen_Activity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }else if (i == 2 ){

                    //setLocale("en");

                    Paper.book().write("language","en");
                    updateView((String) Paper.book().read("language"));
                    Toast.makeText(Settings_Activity.this, "en", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings_Activity.this,Splash_Screen_Activity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(this,language);
        Resources resources = context.getResources();

        sl.setText(resources.getString(R.string.verify_hint_w));
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        Intent intent = new Intent(Settings_Activity.this,Splash_Screen_Activity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}