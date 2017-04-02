package com.example.ee.androidcalculator1;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private String display="";
    private String currentOperator=" ";
    private String previousOperator=" ";

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
        display += btn.getText().toString();
        onUpdate();
    }

    protected void onClickOperator(View v){
        if (display!="") {
            Button btn = (Button) v;
            previousOperator = currentOperator;  // eg. + in 9+9
            currentOperator = btn.getText().toString();  // read new operator   9+9+9 = 18+9
            if (display.endsWith("*") || display.endsWith("/") || display.endsWith("+") || display.endsWith("-")) {
                String op = display.substring(0, display.length() - 1);
                display = op.concat(currentOperator);
            } else {
                Double results = 0.0;
                String[] operation = display.split(Pattern.quote(previousOperator));
                if (operation.length == 2 && !operation[0].isEmpty() && !operation[1].isEmpty()) {
                    // for + * /
                    Log.d("2", "hi2");
                    results = operate(operation[0], operation[1], previousOperator);
                    display = String.valueOf(results);
                    display += currentOperator;   // new value 18+
                } else if (operation.length == 3 && operation[0].isEmpty()){   // eg -1-2
                    Log.d("2", operation[1]);
                    Log.d("l", String.valueOf(operation.length));
                    operation[1] = "-".concat(operation[1]);
                    results = operate(operation[1], operation[2], previousOperator);
                    display = String.valueOf(results);
                    display += currentOperator;
                } else {
                    display += currentOperator;     // 9+
                }
            }
            onUpdate();
        }
    }

    protected void clear(){
        display = "";
        currentOperator="";
    }

    protected void onClickClear(View v){
        clear();
        onUpdate();
    }

    protected double operate (String a, String b, String op){
        switch(op){
            case "+": return Double.valueOf(a)+Double.valueOf(b);
            case "-": return Double.valueOf(a)-Double.valueOf(b);
            case "*": return Double.valueOf(a)*Double.valueOf(b);
            case "/": return Double.valueOf(a)/Double.valueOf(b);
            default: return -9999;
        }
    }

    protected void onClickEqual(View v){
        String[] operation = display.split(Pattern.quote(currentOperator));
        if (operation.length<2) return;
        Double result = operate (operation[0], operation[1], currentOperator);
        display = String.valueOf(result);
        onUpdate();
    }
}
