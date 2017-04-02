package com.example.ee.androidcalculator1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

import me.grantland.widget.AutofitTextView;


public class MainActivity extends AppCompatActivity {
    //private TextView tv;
    private AutofitTextView tv;
    private String display="";
    private String currentOperator=" ";
    private String previousOperator=" ";
    private final Integer maxDisplay = 13;
    private final Double maxResult = 1e10;     // maxResult
    private final Double minResult = 1e9;      // -minResult  minResult is the absolute value
    private Boolean errFree = true;
    private Boolean numAllow = true;    // cannot be true when calculation "=" is done and only number in display
                                        // see how this value is changed in the function onClickEqual()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tv=(TextView)findViewById(R.id.resultView);
        tv=(AutofitTextView)findViewById(R.id.resultView);
        tv.setText(display);
    }

    protected void onUpdate(){
        tv.setText(display);
    }

    protected void onClickNumber(View v){
        Button btn = (Button) v;
        if (display.length()>=maxDisplay) {
            clear();
            display = "display limit";
            onUpdate();
            errFree = false;
            return;
        }
        if (!numAllow) {                // if calculation "=" is executed and then a new digit is detected, clear screen and start a new calculation
            clear();
            display += btn.getText().toString();
            onUpdate();
            return;
        }
        if (numAllow && errFree) {       // if calculation "=" is executed and only number in display, no further number is allowed
            display += btn.getText().toString();
            onUpdate();
        }
    }

    protected void onClickOperator(View v){
        if (errFree) {
            if (display.length()>=maxDisplay) {
                clear();
                display = "display limit";
                onUpdate();
                errFree = false;
                return;
            }
            Button btn = (Button) v;
            DecimalFormat df = new DecimalFormat("#.#####");
            if (display.isEmpty() && (btn.getText().toString().startsWith("-"))) {   //first number is negative
                display += "-";
                numAllow = true;
            } else if (!display.isEmpty() && !(display.length() == 1 && display.startsWith("-"))) {   //when display is not empty and not only contain negative sign "-"
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
                        if ((results - maxResult) >= 0 || results + minResult <= 0) {
                            clear();
                            display = "out of range";
                            onUpdate();
                            errFree = false;
                            return;
                        }
                        if (df.format(results).length() > maxDisplay) {     //maximum display length 12 chars
                            display = df.format(results).substring(0, maxDisplay);
                            display += currentOperator;   // new value 18+
                            numAllow = true;
                        } else {
                            display = df.format(results);
                            display += currentOperator;   // new value 18+
                            numAllow = true;
                        }
                    } else if (operation.length == 3 && operation[0].isEmpty()) {   // eg -1-2
                        operation[1] = "-".concat(operation[1]);
                        results = operate(operation[1], operation[2], previousOperator);
                        if ((results - maxResult) >= 0 || results + minResult <= 0) {
                            clear();
                            display = "out of range";
                            onUpdate();
                            errFree = false;
                            return;
                        }
                        if (df.format(results).length() > maxDisplay) {     //maximum display length 12 chars
                            display = df.format(results).substring(0, maxDisplay);
                            display += currentOperator;   // new value 18+
                            numAllow = true;
                        } else {
                            display = df.format(results);
                            display += currentOperator;   // new value 18+
                            numAllow = true;
                        }
                    } else {
                        display += currentOperator;     // 9+
                        numAllow = true;
                    }
                }
            }
            onUpdate();
        }
    }

    protected void clear(){
        display = "";
        currentOperator=" ";
        previousOperator=" ";
        numAllow = true;
        errFree = true;    //reset error
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
        DecimalFormat df = new DecimalFormat ("#.#####");
        String[] operation = display.split(Pattern.quote(currentOperator));
        if (operation.length==2 && !operation[0].isEmpty()) {   // eg 1-2
            result = operate (operation[0], operation[1], currentOperator);
            if ((result-maxResult)>=0 || result+minResult<=0) {
                clear();
                display = "out of range";
                onUpdate();
                errFree = false;
                return;
            }
            if (df.format(result).length()>maxDisplay) {     //maximum display length 12 chars
                display = df.format(result).substring(0,maxDisplay);
                onUpdate();
                numAllow = false;
            } else {
                display = df.format(result);
                onUpdate();
                numAllow = false;
            }
        } else if (operation.length == 3 && operation[0].isEmpty()) {   // eg -1-2
            operation[1] = "-".concat(operation[1]);
            result = operate(operation[1], operation[2], currentOperator);
            if ((result-maxResult)>=0 || result+minResult<=0) {
                clear();
                display = "out of range";
                onUpdate();
                errFree = false;
                return;
            }
            if (df.format(result).length()>maxDisplay) {     //maximum display length 12 chars
                display = df.format(result).substring(0,maxDisplay);
                onUpdate();
                numAllow = false;
            } else {
                display = df.format(result);
                onUpdate();
                numAllow = false;
            }
        }
    }
}
