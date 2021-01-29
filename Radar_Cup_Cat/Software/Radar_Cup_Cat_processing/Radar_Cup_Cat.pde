import processing.serial.*;     // import serial library
Serial myPort;                  // declare a serial port

float x, y;                     // variable to store x and y co-ordinates for vertices   
int radius = 350;               // set the radius of objects
int w = 300;                    // set an arbitary width value
int degree = 0;                 // servo position in degrees
int value = 0;                  // value from sensor(150 or less)
int valueOver = 0;              // value print in text(over 150)
int temp = 0;                   // TF-LUNA temp
int flux = 0;                   // TF-LUNA flux(signal strength, quality or confidence)
String fluxString = "";         // TF-LUNA flux in text
int std = 0;                    // TF-LUNA flux standard deviation
String tfLunaVer = "";          // TF-LUNA version in text
String serialNumber = "";       // TF-LUNA serial number in text
float tfTime = 0;               // TF-LUNA Time(sec) in text
String settfFrame = "";         // TF-LUNA set Frame in text
String gettfFrame = "";         // TF-LUNA get Frame in text
int motion = 0;                 // value to store which way the servo is panning
int[] newValue = new int[181];  // create an array to store each new sensor value for each servo position
int[] oldValue = new int[181];  // create an array to store the previous values.
PFont myFont;                   // setup fonts in Processing
int radarDist = 0;              // set value to configure Radar distance labels
int firstRun = 0;               // value to ignore triggering motion on the first 2 servo sweeps
 
/* create background and serial buffer */
void setup(){
  // setup the background size, colour and font.
  size(750, 450);
  background (0); // 0 = black
  myFont = createFont("consolas", 13);
  textFont(myFont);
  // setup the serial port and buffer
  myPort = new Serial(this, Serial.list()[1], 115200);
  myPort.bufferUntil('\n');
}
 
/* draw the screen */
void draw(){
  fill(0);                              // set the following shapes to be black
  noStroke();                           // set the following shapes to have no outline
  ellipse(radius, radius, 750, 750);    // draw a circle with a width/height=750 with its center position (x and y) set by the radius
  rectMode(CENTER);                     // set the following rectangle to be drawn around its center
  rect(350,402,800,100);                // draw rectangle (x, y, width, height)
  if (degree >= 179) {                  // if at the far right then set motion = 1/ true we're about to go right to left
    motion = 1;                         // this changes the animation to run right to left
  }
  if (degree <= 1) {                    // if servo at 0 degrees then we're about to go left to right
    motion = 0;                         // this sets the animation to run left to right
  }
  /* setup the radar sweep */
  strokeWeight(7);                      // set the thickness of the lines
  if (motion == 0) {                    // if going left to right
    for (int i = 0; i <= 20; i++) {     // draw 20 lines with fading colour each 1 degree further round than the last
      stroke(0, (10*i), 0);             // set the stroke colour (Red, Green, Blue) base it on the the value of i
      line(radius, radius, radius + cos(radians(degree+(180+i)))*w, radius + sin(radians(degree+(180+i)))*w);
// line(start x, start y, end x, end y)
    }
  } else {                              // if going right to left
    for (int i = 20; i >= 0; i--) {     // draw 20 lines with fading colour
      stroke(0,200-(10*i), 0);          // using standard RGB values, each between 0 and 255
      line(radius, radius, radius + cos(radians(degree+(180+i)))*w, radius + sin(radians(degree+(180+i)))*w);
    }
  }
  /* Setup the shapes made from the sensor values */
  noStroke();                           // no outline
  /* first sweep */
  fill(0,50,0);                         // set the fill colour of the shape (Red, Green, Blue)
  beginShape();                         // start drawing shape
    for (int i = 0; i < 180; i++) {     // for each degree in the array
      x = radius + cos(radians((180+i)))*((oldValue[i]*2)); // create x coordinate
      y = radius + sin(radians((180+i)))*((oldValue[i]*2)); // create y coordinate
      vertex(x, y);                     // plot vertices
    }
  endShape();                           // end shape
  /* second sweep */
  fill(0,110,0);
  beginShape();
    for (int i = 0; i < 180; i++) {
      x = radius + cos(radians((180+i)))*(newValue[i]*2);
      y = radius + sin(radians((180+i)))*(newValue[i]*2);
      vertex(x, y);
    }
  endShape();
  /* average */
  fill(0,170,0);
  beginShape();
    for (int i = 0; i < 180; i++) {
      x = radius + cos(radians((180+i)))*(((newValue[i]+oldValue[i])/2)*2); // create average
      y = radius + sin(radians((180+i)))*(((newValue[i]+oldValue[i])/2)*2);
      vertex(x, y);
    }
  endShape();
  /* if after first 2 sweeps, highlight motion with red circle*/
  if (firstRun >= 360) {
    stroke(150,0,0);
    strokeWeight(1);
    noFill();
      for (int i = 0; i < 180; i++) {
        if (oldValue[i] - newValue[i] > 35 || newValue[i] - oldValue[i] > 35) {
          x = radius + cos(radians((180+i)))*(newValue[i]*2);
          y = radius + sin(radians((180+i)))*(newValue[i]*2);
          ellipse(x, y, 10, 10); 
        }
      }
  }
  /* set the radar distance rings and out put their values, 50, 100, 150 etc.. */
  for (int i = 0; i <=6; i++){
    noFill();
    strokeWeight(1);
    stroke(0, 255-(30*i), 0);
    ellipse(radius, radius, (100*i), (100*i)); 
    //fill(0, 100, 0);
    fill(231, 70, 146);
    noStroke();
    text(Integer.toString(radarDist+25)+"cm", 380, (305-(radarDist*2)), 50, 50); // change this to measure up to 150cm
    radarDist+=25;
  }
  radarDist = 0;
  /* draw the grid lines on the radar every 30 degrees and write their values 180, 210, 240 etc.. */
  for (int i = 0; i <= 6; i++) {
    strokeWeight(1);
    stroke(0, 55, 0);
    line(radius, radius, radius + cos(radians(180+(30*i)))*w, radius + sin(radians(180+(30*i)))*w);
    fill(231, 70, 146);
    noStroke();
    if (180+(30*i) >= 300) {
      text(Integer.toString(30*i)+"°",
         (radius+10) + cos(radians(180+(30*i)))*(w+10), (radius+10) + sin(radians(180+(30*i)))*(w+10), 30, 50);
    } else {
      text(Integer.toString(30*i)+"°", radius + cos(radians(180+(30*i)))*w, radius + sin(radians(180+(30*i)))*w, 60,40);
    }
  }
  /* Write information text and values. */
  noStroke();
  fill(0);
  rect(400,402,800,100);
  fill(0, 100, 0);
  text("Signal: "+fluxString, 460, 380, 200, 50);
  text("Temp: "+Integer.toString(temp)+"°C", 410, 400, 100, 50);
  text("Degrees: "+Integer.toString(degree)+"˚", 420, 420, 120, 50);
  // use Integet.toString to convert numeric to string as text() only outputs strings
  
  if (valueOver >= 150) {
    text("Distance: "+Integer.toString(valueOver)+"(over 150)", 460, 440, 200, 50);
  }
  
  else {
    text("Distance: "+Integer.toString(valueOver), 410, 440, 100, 50);
  }
  
  text("TF-LUNA Version: " + tfLunaVer+"("+serialNumber+")", 200, 380, 300, 50);
  text("Sensor Time: " + int(tfTime) + "sec", 125, 400, 150, 50);
  text("Set Frame Rate: " + settfFrame, 150, 420, 200, 50);
  text("Get Frame Rate: " + gettfFrame, 150, 440, 200, 50);
  fill(0);
  rect(70,60,150,100);
  fill(0, 100, 0); 
  text("Screen Key:", 100, 50, 150, 50);
  fill(0,50,0);
  rect(30,53,10,10);
  text("First sweep", 115, 70, 150, 50);
  fill(0,110,0);
  rect(30,73,10,10);
  text("Second sweep", 115, 90, 150, 50);
  fill(0,170,0);
  rect(30,93,10,10);
  text("Average", 115, 110, 150, 50);
  noFill();
  stroke(150,0,0);
  strokeWeight(1);
  ellipse(29, 113, 10, 10); 
  fill(150,0,0);
  text("Motion", 115, 130, 150, 50);
}
 
/* get values from serial port */
void serialEvent (Serial myPort) {
  String xString = myPort.readStringUntil('\n');            // read the serial port until a new line
    if (xString != null) {                                  // if theres data in between the new lines
      xString = trim(xString);                              // get rid of any whitespace just in case
      String[] getStringArr = split(xString, ',');          // get the values to Array
      degree = Integer.parseInt(getStringArr[0]);           // set the values to degree variables
      value = Integer.parseInt(getStringArr[1]);            // set the values to value variables
      temp = Integer.parseInt(getStringArr[2]);             // set the values to temp variables
      flux = Integer.parseInt(getStringArr[3]);             // set the values to flux variables
      tfLunaVer = getStringArr[4];
      serialNumber = getStringArr[5];
      tfTime = Float.parseFloat(getStringArr[6]);
      settfFrame = getStringArr[7];
      gettfFrame = getStringArr[8];
      
      degree = int(float(degree)*0.9);
      value = int((round(value * 10))/10);
      tfTime = (int(round(tfTime/100.0)))/10;
      
      if (flux <= 100) {
        temp /= 100;
        fluxString = "Weak";
      }
      
      else {
        fluxString = "Strong";
      }
      
      valueOver = value;
      /*
      If our values are outside either end of the sensors range then convert them to the max/min for a better display without the spikes
      */
      if (value > 150) {
        value = 150; 
      }
      
      if (value < 20) {
        value = 20;
      }
      
      oldValue[degree] = newValue[degree]; // store the values in the arrays.

      if (flux > 100) {
        newValue[degree] = value;
      }
      
      else {
        newValue[degree] = oldValue[degree];
      }
      /* sets a counter to allow for the first 2 sweeps of the servo */
      firstRun++;
      
      if (firstRun > 360) {
        firstRun = 360; // keep the value at 360 
      }
  }
}
