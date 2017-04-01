package com.example.ee.androidcalculator1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private String display="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView)findViewById(R.id.resultView);
        tv.setText(display);
    }

    protected void onUpdate(){
        tv.setText(display);
    }

    protected void onClickNumber(View v){
        Button btn = (Button)v;
        display = btn.getText().toString();
        onUpdate();
    }

    protected void onClickOperator(View v){
        Button btn = (Button)v;

    }

}
