#include <ESP32Servo.h>
#include "DHTesp.h"
#include <WiFi.h>
#include "FirebaseESP32.h"


#define FIREBASE_HOST "control-home-b42d9.firebaseio.com"
#define FIREBASE_AUTH "jfsN7DrgvPIXGTyzlNUPn3uUNrJclx5JcI3hYrs6"
#define WIFI_SSID "d82bc4"
#define WIFI_PASSWORD "A28M01S24M29"

#define DHTpin 15     //D15 en ESP32 DevKit
#define LED 4         //D4 en ESP32 DevKit
#define SERVO 13       //D16 en ESP32 DevKit

FirebaseData firebaseData;
FirebaseData firebaseDataLed;
FirebaseData firebaseDataServo;
String ruta = "/Datos";
String led = "";
String servo = "";

Servo servoMotor;
DHTesp dht;

void setup() 
{
  Serial.begin(115200);

  dht.setup(DHTpin, DHTesp::DHT11); //Declaramos el pin y que sensor se usa en este caso DHT11
  pinMode(LED,OUTPUT);
  servoMotor.attach(SERVO);
  
  Serial.println();
  Serial.println();

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Conectado al Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Conectado con IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

  //------------------Streaming-------------------
  Serial.println("Inicio de streaming");
  Serial.println("RUTA: "+ruta + "/led");
  Firebase.beginStream(firebaseDataLed, ruta + "/led");
  Firebase.beginStream(firebaseDataServo, ruta + "/servo");
}

void loop() 
{
  Firebase.readStream(firebaseDataLed);
  Firebase.readStream(firebaseDataServo);
  float humidity = dht.getHumidity();
  float temperature = dht.getTemperature();
     
  if (firebaseDataLed.streamAvailable()) 
    {  
        //Serial.println(firebaseData.stringData()); // El tipo de dato debe coincidir con el dato cargado en la nube
        led = firebaseDataLed.stringData();
        Serial.println("Estado led: " + led );
    }

   if (firebaseDataServo.streamAvailable()) 
    {  
        //Serial.println(firebaseData.stringData()); // El tipo de dato debe coincidir con el dato cargado en la nube
        servo = firebaseDataServo.stringData();
        Serial.println("Estado servo: " + servo );
    }

   if(led == "ON"){
    digitalWrite(LED,HIGH);
   }else if(led == "OFF"){
    digitalWrite(LED,LOW);
   }

   if(servo == "ON"){
    servoMotor.write(90);
   }else if(servo == "OFF"){
    servoMotor.write(0);
   }

   //push humidity y temperature
   Firebase.setFloat(firebaseData, ruta + "/humidity", humidity);
   Firebase.setFloat(firebaseData, ruta + "/temperature", temperature);
}
