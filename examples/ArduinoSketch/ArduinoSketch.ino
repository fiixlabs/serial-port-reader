int reading;
int inputPin = A0;

void setup()
{
  // initialize serial communication with computer:
  Serial.begin(9600);
}

void loop() {
  // read from the sensor:  
  reading = analogRead(inputPin);
  // send it to the computer as ASCII digits
  Serial.println(reading);
  delay(1000);  // delay in between reads for stability            
}
