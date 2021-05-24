package com.example.dataset_txt;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
//import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
    TextView texto2,texto3;
    Writer output;

    double ax, ay, az, mx, my, mz, gx, gy, gz;
    double Latitud, Longitud, xh, yh;
    String timeStamp;
    /*String[] time= new String[10000];
    String[] textoASalvar = new String[10000];
    double[] ax= new double[10000];
    double[] ay= new double[10000];
    double[] az= new double[10000];
    double[] gx= new double[10000];
    double[] gy= new double[10000];
    double[] gz= new double[10000];
    double[] Lat= new double[10000];
    double[] Long= new double[10000];*/

    int fila = 0;
    double Boton1, Boton2;
    //double roll,pitch,yaw;
    int CD;    //Con CD vamos a indicar que la toma de datos sera cada cuatro muestras ya que es como se repiten estas mismas
    int i=1;    //Con i va a ser para etiquetar textoAsalvar
    int N_dataset=0;    // Sera para que cada mil datos imprimamos textoAsalvar en el TXT


    String textoASalvar,Vaciarcadena;
    String NombreAchivo;
    LocationManager locationManager;
    LocationListener locationListener;
    Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGuardarExcel = findViewById(R.id.btnGuardarExcel);
        texto2 = (TextView) findViewById(R.id.texto2);
        texto3 = (TextView) findViewById(R.id.texto3);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        btnGuardarExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });
        // Permisos GPS
        /*if (ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }*/


    }

    public void Tomardatos(View view) throws IOException {
        Boton1 = 1;
        Boton2 = 0;
        String NombreAchivo = new String("Datos_"+ i+".txt");
        File file =  new File(getExternalFilesDir(null), NombreAchivo);
        FileOutputStream outputStream = null;
        textoASalvar= new String("Timestamp, ax, ay, az, gx, gy, gz, Latitud, Longitud  \n");
        outputStream = new FileOutputStream(file);
        outputStream.write(textoASalvar .getBytes());
        outputStream.close();

        texto2.setText("");
        texto2.append("Se inicio la aplicación en el tiempo:" +"\n"+ new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss:ms").format(new Date()));


        /*FileInputStream inputStream = null;
        inputStream = new FileInputStream(file);
        inputStream.read(textoASalvar.getBytes());

        textoASalvar= textoASalvar +"\n"+ text;*/

        /*BufferedReader aux= new BufferedReader(new FileReader(file));
        String text= aux.readLine();
        textoASalvar= textoASalvar + text;

        outputStream = new FileOutputStream(file);
        outputStream.write(textoASalvar.getBytes());
        outputStream.close();*/


        Toast.makeText(getApplicationContext(),"INICIO DE TOMA DE DATOS",Toast.LENGTH_LONG).show();

    }
    public void guardar() {
        Boton1 = 0;
        Boton2 = 1;
        /*

        // Lo primero que va a hacer es generar el String;
        Toast.makeText(getApplicationContext(),"PROCESANDO LOS DATOS",Toast.LENGTH_LONG).show();

        String NombreAchivo = new String("Datos_"+ i+".txt");
        File file =  new File(getExternalFilesDir(null), NombreAchivo);
        //Creo un flujo de salida para poder escribir datos en el file:
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(textoASalvar.getBytes());
            outputStream.close();
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

         */
        texto3.setText("");
        texto3.append("Se finalizo la toma de datos" + "\n"+ new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss:ms").format(new Date())+"\n"+"La cantidad de datos son:"+N_dataset );
        Toast.makeText(getApplicationContext(),"FIN DE TOMA DE DATOS",Toast.LENGTH_LONG).show();





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
                    SensorManager.SENSOR_DELAY_NORMAL);
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Del GPS
        /*LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

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
        }*/
        //
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(sensorEvent.values, 0, accelerometerReading,
                    0, accelerometerReading.length);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(sensorEvent.values, 0, magnetometerReading,
                    0, magnetometerReading.length);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            System.arraycopy(sensorEvent.values, 0, gyroscopeReading,
                    0, gyroscopeReading.length);
        }

        //Etiqueta de tiempo para los sensores;



        //CD == 3

        if (Boton1 == 1 && Boton2 == 0 ) {
            timeStamp = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss:ms").format(new Date());
            ax = accelerometerReading[0];
            ay= accelerometerReading[1];
            az = accelerometerReading[2];
            mx = magnetometerReading[0];
            my = magnetometerReading[1];
            mz = magnetometerReading[2];
            gx = gyroscopeReading[0];
            gy = gyroscopeReading[1];
            gz = gyroscopeReading[2];

            textoASalvar = timeStamp + ", " + ax + ", " +  ay + ", " + az + ", " + gx + ", " + gy + ", " + gz + ", "
                    /*+ Latitud + ", " + Longitud + ", "*/ + "\n";
            String NombreAchivo = new String("Datos_"+ i+".txt");
            File file =  new File(getExternalFilesDir(null), NombreAchivo);
            //Creo un flujo de salida para poder escribir datos en el file:
            FileOutputStream outputStream = null;
            try {
                //BufferedReader aux= new BufferedReader(new FileReader(file));
                //String text= aux.readLine();
                //textoASalvar= text+"\n"+textoASalvar ;
                output = new BufferedWriter(new FileWriter(file, true));
                output.append(textoASalvar);
                output.close();
                N_dataset=N_dataset+1;

                /*outputStream = new FileOutputStream(file);
                outputStream.write(textoASalvar.getBytes());
                outputStream.close();

                 */
            }catch (java.io.IOException e) {
                e.printStackTrace();
                Boton1=0;
                Boton2=1;
                texto3.setText("");
                texto3.append("Se produjeron errores en el tiempo:" + "\n"+ new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss:ms").format(new Date())+"\n"+"La cantidad de datos son:"+N_dataset );
                Toast.makeText(getApplicationContext(),"FIN DE TOMA DE DATOS",Toast.LENGTH_LONG).show();
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (N_dataset == 10000) {
                Boton1 = 0;
                Boton2 = 1;
            }
            /*
                String NombreAchivo = new String("Datos_"+ i+".txt");
                File file =  new File(getExternalFilesDir(null), NombreAchivo);
                //Creo un flujo de salida para poder escribir datos en el file:
                FileOutputStream outputStream = null;

                try {
                    outputStream = new FileOutputStream(file);
                    outputStream.write(textoASalvar.getBytes());
                    outputStream.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(), "NO OK", Toast.LENGTH_LONG).show();
                    try {
                        outputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                i=i+1;
                N_dataset=0;
                Vaciarcadena= "";
                textoASalvar= Vaciarcadena;

            }*/



        }

    }

}
