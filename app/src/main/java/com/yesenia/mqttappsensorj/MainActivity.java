package com.yesenia.mqttappsensorj;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



//Añadimos nuevas librerias


import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;



public class MainActivity extends AppCompatActivity {

    private static String mqttHost = "tcp://mqttservidoriot3.icloud.shiftr.io";

    private static String IdUsuario = "mqttservidoriot3";
    private static String Topico = "sensor";
    private static String User = "mqttservidoriot3";
    private static String Pass = "11111111";

    private TextView textView;
    private MqttClient mqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlace de la variable del tipo de vista en el activity main donde imprimiremos los datos
        textView = findViewById(R.id.textView);

        try {
            // Creación de un cliente MQTT
            mqttClient = new MqttClient(mqttHost, IdUsuario, null);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(User);
            options.setPassword(Pass.toCharArray());

            // Conexión al servidor MQTT
            mqttClient.connect(options);

            // Si se conecta imprimirá un mensaje de MQTT
            Toast.makeText(this, "Aplicación conectada al Servidor MQTT", Toast.LENGTH_SHORT).show();

            // Manejo de entrega de datos y pérdida de conexión
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d("MQTT", "Conexión perdida");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String payload = new String(message.getPayload());
                    runOnUiThread(() -> textView.setText(payload));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d("MQTT", "Entrega completa");
                }
            });

            // El cliente se suscribe al tópico
            mqttClient.subscribe(Topico);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Se desconecta el cliente al cerrar la aplicación
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}

