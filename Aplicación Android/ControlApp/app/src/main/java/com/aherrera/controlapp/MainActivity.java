package com.aherrera.controlapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    final DatabaseReference ledstatus = myRef.child("Datos").child("led");

    final DatabaseReference servostatus = myRef.child("Datos").child("servo");

    final DatabaseReference humedadstatus = myRef.child("Datos").child("humidity");

    final DatabaseReference temperaturastatus = myRef.child("Datos").child("temperature");

    Button btnELed;
    Button btnALed;
    Button btnEServ;
    Button btnAServ;
    TextView txtLedE;
    TextView txtServE;
    TextView txtHumE;
    TextView txtTempE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnELed = (Button)findViewById(R.id.btnEncenderLed);
        btnALed = (Button)findViewById(R.id.btnApagarLed);

        btnEServ = (Button)findViewById(R.id.btnEncenderServo);
        btnAServ = (Button)findViewById(R.id.btnApagarServo);

        txtLedE = (TextView)findViewById(R.id.txtLedEstado);
        txtServE = (TextView)findViewById(R.id.txtServoEstado);

        txtHumE = (TextView)findViewById(R.id.txtHumedadEstado);
        txtTempE = (TextView)findViewById(R.id.txtTemperaturaEstado);
        //updateText(ledstatus1,textView1);

        ledstatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("file", "Value is: " + value);
                txtLedE.setText(value);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("file", "Failed to read value.", error.toException());
            }
        });

        servostatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d("file", "Value is: " + value);
                txtServE.setText(value);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("file", "Failed to read value.", error.toException());
            }
        });

        humedadstatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Float value = dataSnapshot.getValue(Float.class);
                String valueText = String.valueOf(value);
                Log.d("file", "Value is: " + value);
                txtHumE.setText(valueText);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("file", "Failed to read value.", error.toException());
            }
        });

        temperaturastatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Float value = dataSnapshot.getValue(Float.class);
                String valueText = String.valueOf(value);
                Log.d("file", "Value is: " + value);
                txtTempE.setText(valueText);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("file", "Failed to read value.", error.toException());
            }
        });

        btnELed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ledstatus.setValue("ON");
            }
        });

        btnEServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servostatus.setValue("ON");
            }
        });

        btnALed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ledstatus.setValue("OFF");
            }
        });

        btnAServ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servostatus.setValue("OFF");
            }
        });

    }
}
