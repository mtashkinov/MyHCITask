package com.spbu.localization.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextWatcher
{
    EditText income;
    NumberFormat currency;
    private String current = "000";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        income = (EditText) findViewById(R.id.currency);
        income.addTextChangedListener(this);
        currency = NumberFormat.getCurrencyInstance();
        currency.setParseIntegerOnly(true);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.change_lang:
                String lang = getString(R.string.lang);
                Locale locale;
                if (lang.equals("Рус"))
                {
                    locale = Locale.US;
                    item.setTitle("En");
                } else
                {
                    locale = new Locale("ru","RU");
                    item.setTitle("Рус");
                }
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }

    @Override
    public void afterTextChanged(Editable editable)
    {
        String s = editable.toString();
        if(!s.equals(current))
        {
            income.removeTextChangedListener(this);

            String replaceable = String.format("[%s,.\\s]", currency.getCurrency().getSymbol());
            String cleanString = s.replaceAll(replaceable, "");

            if (cleanString.length() != 2)
            {
                if (cleanString.length() > 2)
                {
                    cleanString = cleanString.substring(0, cleanString.length() - 2);
                }

                double parsed = Double.parseDouble(cleanString);
                String formatted = currency.format((parsed));


                current = formatted;
                income.setText(formatted);
                income.setSelection(formatted.lastIndexOf("00") - 1);
            } else
            {
                income.setText("");
            }

            income.addTextChangedListener(this);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {
    }
}
