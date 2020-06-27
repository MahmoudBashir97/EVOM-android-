package com.mahmoud.bashir.evomdriverapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.mahmoud.bashir.evomdriverapp.paperdb.helper.LocaleHelper;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // init paper first
        Paper.init(this);

        //Default language is English
        String language = Paper.book().read("language");
        if (language == null)
            Paper.book().write("language","en");

        updateView((String) Paper.book().read("language"));
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this,language);
        Resources resources = context.getResources();

        //txtview.setText(resources.getString(R.string.name));
    }
}