package com.example.rsa_calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText editTextP,editTextQ;
    private Button buttonPQ;
    private int p=0;
    private int q=0;

    private TextView txtN,txtPhi,txtD,txtE;
    private int n=0;
    private int phi=0;
    private int d=0;
    private int e=0;

    private EditText encEditText,decEditText;
    private Button buttonEnc,buttonDec;
    private TextView txtEnc,txtDec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        editTextP = findViewById(R.id.editTxtP);
        editTextQ = findViewById(R.id.editTxtQ);

        buttonPQ = findViewById(R.id.btnPQ);

        buttonPQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p=Integer.parseInt(editTextP.getText().toString());
                q=Integer.parseInt(editTextQ.getText().toString());
                if(!isPrime(p)){
                    Toast.makeText(MainActivity.this,p+" not prime",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!isPrime(q)){
                    Toast.makeText(MainActivity.this,q+" not prime",Toast.LENGTH_LONG).show();
                    return;
                }
                calculateKeys();
            }
        });

        txtN = findViewById(R.id.txtN);
        txtPhi = findViewById(R.id.txtPhi);
        txtD = findViewById(R.id.txtD);
        txtE = findViewById(R.id.txtE);

        encEditText=findViewById(R.id.editTxtMsgEnc);
        decEditText=findViewById(R.id.editTxtMsgDec);

        buttonEnc = findViewById(R.id.btnEncrypt);

        buttonEnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enc=enc();
                txtEnc.setText(enc);
                decEditText.setText(enc);
            }
        });

        buttonDec = findViewById(R.id.btnDecrypt);

        buttonDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDec.setText(dec());
            }
        });

        txtEnc = findViewById(R.id.txtEncrypted);
        txtDec = findViewById(R.id.txtDecrypted);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    String dec(){
        String decIn=decEditText.getText().toString();
        String res="";
        for(int i=0;i<decIn.length();i++){
            Log.d("mmmm-d",decIn.charAt(i)+" "+(int)decIn.charAt(i));
            int x=(BigInteger.valueOf((int)decIn.charAt(i)-(int)'a').pow(d)).intValue()%n;
            Log.d("mmmm-dd",x+" "+(char)x);
            res+=(char)x;
        }
        return res;
    }

    String enc(){
        String encIn=encEditText.getText().toString();
        String res="";
        encIn=encIn.toLowerCase();
        for(int i=0;i<encIn.length();i++){
            Log.d("mmmm-e",encIn.charAt(i)+" "+(int)encIn.charAt(i));
            int x=(BigInteger.valueOf(((int)encIn.charAt(i))-(int)'a').pow(e)).intValue()%n;
            Log.d("mmmm-ee",x+" "+(char)x);
            res+=(char)x;
        }
        return res;
    }

    void calculateKeys(){
        n=p*q;
        phi=(p-1)*(q-1);
        txtN.setText(Integer.valueOf(n).toString());
        txtPhi.setText(Integer.valueOf(phi).toString());
        e=getE(phi);
        txtE.setText(Integer.valueOf(e).toString());
        d=getD();
        txtD.setText(Integer.valueOf(d).toString());
    }

    private int findGCD(int number1, int number2) {
        if(number2 == 0){
            return number1;
        }
        return findGCD(number2, number1%number2);
    }

    int getD(){
        for(int d=1;d<=phi;d++){
            if((d*e)%phi==1)
                return d;
        }
        return -1;
    }

    boolean isCoPrime(int a,int b){
        return findGCD(a,b)==1;
    }

    int getE(int phi){
        for(int i=2;i<phi;i++){
            if(isCoPrime(i,phi)){
                return i;
            }
        }
        return -1;
    }

    Boolean isPrime(int n){
        int i,m;
        m=n/2;
        if(n==0||n==1){
            return false;
        }else{
            for(i=2;i<=m;i++){
                if(n%i==0){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}