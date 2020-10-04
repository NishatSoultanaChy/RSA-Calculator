package com.example.rsa_calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText InputP, InputQ, InputMsg;

    private Button btnSavePQ, btnSaveE, btnEncryptDecrypt;

    private TextView txtN, txtPhi_N, txt_D, txt_pub_key, txt_privat_key, txt_Encrypted, txt_Decrypted;

    private Spinner sp_E;

    public int N=0;

    public double msg = 0, e =0, d= 0;
    private String strArray[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        InputP =(EditText) findViewById(R.id.editTxtP);
        InputQ =(EditText) findViewById(R.id.editTxtQ);
        InputMsg = (EditText) findViewById(R.id.editTxtMsg);

        btnSavePQ =(Button) findViewById(R.id.btnPQ);
        btnSaveE =(Button) findViewById(R.id.btnSaveE);
        btnEncryptDecrypt = (Button)findViewById(R.id.btnEncrypt_decrypt);

        sp_E= (Spinner) findViewById(R.id.sp_selectE);


        txtN =(TextView) findViewById(R.id.txtN);
        txtPhi_N =(TextView) findViewById(R.id.txtPhi);
        txt_D =(TextView) findViewById(R.id.txtD);
        txt_pub_key =(TextView) findViewById(R.id.txtPublicKey);
        txt_privat_key =(TextView) findViewById(R.id.txtprivateKey);
        txt_Encrypted = (TextView) findViewById(R.id.txtEncrypted);
        txt_Decrypted = (TextView) findViewById(R.id.txtDecrypted);


        //save the value of P and Q(prime numbers
        btnSavePQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePQ();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                InputP.setText("");
                InputQ.setText("");
                txtN.setText("");
                txtPhi_N.setText("");
                sp_E.setAdapter(null);

                txt_D.setText("");
                txt_pub_key.setText("");
                txt_privat_key.setText("");
                InputMsg.setText("");
                txt_Encrypted.setText("");
                txt_Decrypted.setText("");

                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //function for saving p, q
    private void savePQ(){


        String p = InputP.getText().toString();
        String q = InputQ.getText().toString();

        if(TextUtils.isEmpty(p))
        {
            Toast.makeText(this, "Please enter a value for P...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(q))
        {
            Toast.makeText(this, "Please enter a value for Q...", Toast.LENGTH_SHORT).show();
        }

        else{
            double valueP = 0;
            if (!"".equals(p)){
                valueP = Double.parseDouble(p);
            }

            double valueQ = 0;
            if (!"".equals(q)){
                valueQ = Double.parseDouble(q);
            }


            N = (int)(valueP * valueQ);

            if(N<127)
            {
                Toast.makeText(this, "P*Q is less than 127. Please enter bigger prime numbers...", Toast.LENGTH_SHORT).show();
            }
            else
            {
                final int phi_N =  (int)((valueP-1)*(valueQ-1));

                //show the value of N and phi(n) in textbox
                txtN.setText(""+N);
                txtPhi_N.setText(""+phi_N);

                int array_E[]= new int[100];

                int e = 2;
                int i=0;
                while (e < phi_N)
                {
                    // e must be co-prime to phi and
                    // smaller than phi.
                    if (gcd(e, phi_N)==1) {
                        array_E[i] = e;
                        i++;
                        e++;
                    }
                    else
                        e++;
                }

                int l= i;

                //converting int array to string array
                String strArray[] = new String[l];
                for (int s = 0; s < l; s++)
                    strArray[s] = String.valueOf(array_E[s]);


                // Application of the Array to the Spinner
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, strArray);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                sp_E.setAdapter(spinnerArrayAdapter);



                btnSaveE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveE(phi_N);
                    }
                });
            }
        }
    }

    private void saveE(int phi_N){


        int phi_NN = phi_N;

        //getting selected value from spinner
        String ee = sp_E.getSelectedItem().toString();
        e  = Integer.parseInt(ee);

        //d = (k*Φ(n) + 1) / e
        /*for(double j=0; j<=9; j++)
        {
            double x=1+(j*phi_N);
            if(x%e==0)
            {
                d=x/e;
                break;
            }
        }*/
        for(d=1;d<=phi_N;d++){
            if((d*e)%phi_N==1)
                break;
        }

        //show the value of d in textbox
        txt_D.setText(""+d);

        //show the public and private key in the textbox
        txt_pub_key.setText(ee+","+N);
        txt_privat_key.setText((int)d+","+N);

        btnEncryptDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encryptDecrypt();
            }
        });


    }

    private void encryptDecrypt(){

        String mm = InputMsg.getText().toString();

        if (!"".equals(mm)){
            msg = Double.parseDouble(mm);
        }

       /* double c=(Math.pow(msg,e))%N;
        txt_Encrypted.setText(""+(int)c);

        double m=(Math.pow(c,d))%N;
        txt_Decrypted.setText(""+(int)m);*/

        int len = mm.length();
        String array_msg_encrypted[]= new String[mm.length()];
        String array_msg_decrypted[]= new String[mm.length()];


        for(int j=0; j<=mm.length(); j++)
        {
            double ascii = mm.charAt(j)-'a';
            double c=(Math.pow(ascii,e))%N;
            array_msg_encrypted[j] = String.valueOf(c);
            double m=(Math.pow(c,d))%N;
            array_msg_decrypted[j] = String.valueOf(m);
        }


        //double c=(Math.pow(msg,e))%N;
        txt_Encrypted.setText(""+array_msg_encrypted);

        //double m=(Math.pow(c,d))%N;
        txt_Decrypted.setText(""+array_msg_decrypted);



    }

    static int gcd(int e, int z)
    {
        if(e==0)
            return z;
        else
            return gcd(z%e,e);
    }
}