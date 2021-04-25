package com.example.dataset_txt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
//import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Button btnGuardarExcel;

    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];
    private final float[] gyroscopeReading = new float[3];


    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    TextView texto;

    double ax, ay, az, mx, my, mz, gx, gy, gz, Latitud, Longitud, xh, yh;
    int fila = 0;
    double Boton1, Boton2;
    //double roll,pitch,yaw;
    long time;
    int CD=0;


    String textoASalvar;

    LocationManager locationManager;
    LocationListener locationListener;
    Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGuardarExcel = findViewById(R.id.btnGuardarExcel);
        texto = (TextView) findViewById(R.id.texto);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        btnGuardarExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });


    }

    public void Tomardatos(View view) throws IOException {
        Boton1 = 1;
        Boton2 = 0;

        File file = new File(getExternalFilesDir(null), "Datos.txt");
        //Creo un flujo de salida para poder escribir datos en el file:
        FileOutputStream outputStream = null;

        textoASalvar = new String("Timestamp, ax, ay, az, gx, gy, gz, Latitud, Longitud  \n");
        outputStream = new FileOutputStream(file);
        outputStream.write(textoASalvar.getBytes());
        Toast.makeText(getApplicationContext(),"INICIO DE TOMA DE DATOS",Toast.LENGTH_LONG).show();

    }
    public void guardar() {
        Boton1 = 0;
        Boton2 = 1;

        File file = new File(getExternalFilesDir(null), "Datos.txt");
        //Creo un flujo de salida para poder escribir datos en el file:
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(textoASalvar.getBytes());
            Toast.makeText(getApplicationContext(),"FIN DE TOMA DE DATOS",Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(),"NO OK",Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_NORMAL);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_NORMAL);
        }
        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }

    // Get readings from accelerometer and magnetometer. To simplify calculations,
    // consider storing these readings as unit vectors.


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Del GPS
        LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocation(Location location) {

            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                Latitud = location.getLatitude();
                Longitud = location.getLongitude();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnable(String provider) {
            }

            public void onProviderDisable(String provider) {
            }
        };
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        //
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(sensorEvent.values, 0, accelerometerReading,
                    0, accelerometerReading.length);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(sensorEvent.values, 0, magnetometerReading,
                    0, magnetometerReading.length);
        }
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            System.arraycopy(sensorEvent.values, 0, gyroscopeReading,
                    0, gyroscopeReading.length);
        }

        //Etiqueta de tiempo para los sensores;
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss:ms").format(new Date());
        //time = new Date().getTime();

        ax = accelerometerReading[0];
        ay = accelerometerReading[1];
        az = accelerometerReading[2];
        mx = magnetometerReading[0];
        my = magnetometerReading[1];
        mz = magnetometerReading[2];
        gx= gyroscopeReading[0];
        gy= gyroscopeReading[1];
        gz= gyroscopeReading[2];
        CD=CD+1;


        // Ya teniendo los valores podemos calcular los angulos
        /*roll = Math.toDegrees(Math.atan(ax/az));
        pitch = Math.toDegrees(Math.atan(ay/az));
        //Estimación de yaw con magnetometro
        xh=mx * Math.toDegrees(Math.cos(roll))-my * Math.toDegrees(Math.sin(roll)) * Math.toDegrees(Math.sin(pitch))-
                mz * Math.toDegrees(Math.sin(roll)) * Math.toDegrees(Math.cos(pitch));
        yh=my * Math.toDegrees(Math.cos(pitch))-mz * Math.toDegrees(Math.sin(pitch));
        yaw= Math.toDegrees(Math.atan2(xh,yh));*/

        //TOMO LOS VALORES DE LOS ACELEROMETROS SOLAMENTE, PODRIAMOS TOMAR LOS VALORES DE ROLL, PITCH
        // Y YAW O HACER EL ANALISIS DESPUÉS
        //CON RESPECTO AL TIMESTAMP POR AHI HABRIA QUE VERLO DADO CAMBIA CADA CUATRO VALORES
        // OTRA QUE SE OCURRE ES TOMAR LAS FOTOS CADA 1 SEGUNDO, NO YA QUE NO SE SI ES NECESARIO TAN
        //SENSIBLE





        //texto.setText("");
        //texto.append("\n" + timeStamp + "\n" + "Roll:" + roll + "\n" + "Pitch:" + pitch + "\n" + "Yaw:" +yaw);
        //+"\n"+ "Campo MX:" + mx + "\n" + "Campo MY:" + my + "\n" + "Campo MZ:" + mz +
        //  "\n" + "GiroX:" + gx + "\n" + "Giro Y:" + gy + "\n" + "Giro Z:" + gz);

        if (Boton1==1 && Boton2==0 && CD==3) {
            CD=0;
            textoASalvar= textoASalvar+timeStamp+", "+ax+", " +", "+ay+", " +", "+az+", " +", " +gx+", " +", "+gy+", " +", "+gz+", " +", "
                    +Latitud+", " +", "+Longitud+", " +"\n";


        }

    }

}
