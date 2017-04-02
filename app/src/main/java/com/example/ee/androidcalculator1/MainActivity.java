package com.example.ee.androidcalculator1;

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
    private Boolean numAllow = true;    // cannot be true when calculation "=" is done and only number in display
                                        // see how this value is changed in the function onClickEqual()

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
        if (numAllow) {       // if calculation "=" is executed and only number in display, no further number is allowed
            Button btn = (Button)v;
            display += btn.getText().toString();
            onUpdate();
        }
    }

    protected void onClickOperator(View v){
        Button btn = (Button) v;
        if (display.isEmpty() && (btn.getText().toString().startsWith("-"))) {   //first number is negative
            display += "-";
            numAllow = true;
        } else if (display!="" && display!="-") {
            previousOperator = currentOperator;  // eg. + in 9+9
            currentOperator = btn.getText().toString();  // read new operator   9+9+9 = 18+9
            if (display.endsWith("*") || display.endsWith("/") || display.endsWith("+") || display.endsWith("-")) {
                String op = display.substring(0, display.length() - 1);
                display = op.concat(currentOperator);
                numAllow = true;
            } else {
                Double results = 0.0;
                String[] operation = display.split(Pattern.quote(previousOperator));
                if (operation.length == 2 && !operation[0].isEmpty() && !operation[1].isEmpty()) {
                    // for + * /
                    results = operate(operation[0], operation[1], previousOperator);
                    display = String.valueOf(results);
                    display += currentOperator;   // new value 18+
                    numAllow = true;
                } else if (operation.length == 3 && operation[0].isEmpty()){   // eg -1-2
                    operation[1] = "-".concat(operation[1]);
                    results = operate(operation[1], operation[2], previousOperator);
                    display = String.valueOf(results);
                    display += currentOperator;
                    numAllow = true;
                } else {
                    display += currentOperator;     // 9+
                    numAllow = true;
                }
            }
        }
        onUpdate();
    }

    protected void clear(){
        display = "";
        currentOperator=" ";
        previousOperator=" ";
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
        Double result;
        String[] operation = display.split(Pattern.quote(currentOperator));
        if (operation.length==2 && !operation[0].isEmpty()) {   // eg 1-2
            result = operate (operation[0], operation[1], currentOperator);
            display = String.valueOf(result);
            onUpdate();
            numAllow = false;
        } else if (operation.length == 3 && operation[0].isEmpty()) {   // eg -1-2
            operation[1] = "-".concat(operation[1]);
            result = operate(operation[1], operation[2], currentOperator);
            display = String.valueOf(result);
            onUpdate();
            numAllow = false;
        }
    }
}
