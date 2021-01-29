#include <Arduino.h>
#include <TFLI2C.h>
#include <SoftwareSerial.h>
#include <Wire.h>

// RX/TX LED
#define TX_RX_LED_INIT      DDRD = (1<<2) | (1<<3)              // LED Initalize
#define TXLED0              PORTD |= (1<<2)                     // TXLED OFF     
#define TXLED1              PORTD &= ~(1<<2)                    // TXLED ON
#define RXLED0              PORTD |= (1<<3)                     // RXLED OFF
#define RXLED1              PORTD &= ~(1<<3)                    // RXLED ON

// 2상6선식 Stepping Motor(Unipolar)
#define in1_1Pin            8                                   // ROW PIN1
#define in1_2Pin            7                                   // ROW PIN2
#define in1_3Pin            6                                   // ROW PIN3
#define in1_4Pin            5                                   // ROW PIN4
#define in2_1Pin            17                                  // Column PIN1
#define in2_2Pin            16                                  // Column PIN2
#define in2_3Pin            15                                  // Column PIN3
#define in2_4Pin            14                                  // Column PIN4
#define btRX                10
#define btTX                9

// Average Filter Valiable
#define index               10

float steps = 0;
boolean firstRun = true;
boolean initialization = false;

// direction of servo movement 
// -1 = back, 1 = forward

TFLI2C tflI2C;                                                  // Class Valiable

SoftwareSerial BT(btTX, btRX);                                  // Bluetooth Valiable

String ReceiveBTString = "";                                    // Receive the Buletooth
String TrasmitSRString = "";                                    // Trasmit the Buletooth

// Moving Average Filter Data Array
float tfValue[index];

// TF-LUNA variables
int16_t  tfAddr = TFL_DEFAULT_ADDR;                             // default TF-LUNA I2C address
uint16_t tfFrame = 0xFA;                                        // default frame rate
uint16_t settfFrame = 0;
uint16_t gettfFrame = 0;

// device variables passed back by getData
int16_t  tfDist = 0;                                            // distance in centimeters
int16_t  tfFlux = 0;                                            // signal quality in arbitrary units
int16_t  tfTemp = 0;                                            // temperature in 0.01 degree Celsius

// other device variables
uint16_t tfTime = 0;                                            // devie clock in milliseconds
uint8_t  tfVer[3];                                              // device version number
uint8_t  tfCode[14];                                            // device serial number

// sub-loop counter for Time display
uint8_t tfCount = 0;

String tfLunaVer = "";

uint32_t ptfData = 0;

void setup()
{
    Wire.begin();                                               // Initalize Wire library
    Serial.begin(115200);                                       // Initialize Serial port
    BT.begin(115200);                                           // Initialize Buletooth port

    TX_RX_LED_INIT;                                             // LED Initialize

    // Stepping Motor PIN
    pinMode(in1_1Pin, OUTPUT);
    pinMode(in1_2Pin, OUTPUT);
    pinMode(in1_3Pin, OUTPUT);
    pinMode(in1_4Pin, OUTPUT);

    pinMode(in2_1Pin, OUTPUT);
    pinMode(in2_2Pin, OUTPUT);
    pinMode(in2_3Pin, OUTPUT);
    pinMode(in2_4Pin, OUTPUT);

    tfLunaInit(tfAddr);
}

void loop()
{
    steppingMotorController();
}

// Several sample commands
// run at setup as examples
// 1: Device Address 2: System Reset 3: Firmware Version 4: Serial Number 5: Time
// 6: Setting Frame Rate 7: Frame Rate Data
void tfLunaInit(uint8_t adr)
{
    Serial.print("Device Address: ");
    Serial.println(adr);

    Serial.print("System Reset: ");
    if (tflI2C.Soft_Reset(adr))
    {
        Serial.println("Passed");
    }
    else tflI2C.printStatus();                                  // 'printStatus()' is for troubleshooting
    delay(500);                                                 //  are is not usually necessary

    Serial.print("Get Firmware Version: ");
    if (tflI2C.Get_Firmware_Version(tfVer, adr))
    {
        Serial.print(tfVer[2]);
        Serial.print(".");
        Serial.print(tfVer[1]);
        Serial.print(".");
        Serial.println(tfVer[0]);
        uint8_t tfVerTemp = tfVer[2];
        tfVer[2] = tfVer[0];
        tfVer[0] = tfVerTemp;
    }
    else tflI2C.printStatus();
    delay(500);

    Serial.print("Get Serial Number: ");
    if (tflI2C.Get_Prod_Code(tfCode, adr))
    {
        for (uint8_t i = 0; i < 14; ++i)
        {
            Serial.print(char(tfCode[i]));
        }
        Serial.println();
    }
    else tflI2C.printStatus();
    delay(500);

    Serial.print("Get Time: ");
    if (tflI2C.Get_Time(tfTime, adr))
    {
        Serial.println(tfTime);
    }
    else tflI2C.printStatus();
    delay(500);

    Serial.print("Set Frame Rate to: ");
    if (tflI2C.Set_Frame_Rate(tfFrame, adr))
    {
        Serial.println(tfFrame);
        settfFrame = tfFrame;
    }
    else tflI2C.printStatus();
    delay(500);

    Serial.print("Get Frame Rate: ");
    if (tflI2C.Get_Frame_Rate(tfFrame, adr))
    {
        Serial.println(tfFrame);                                    //  Read frame rate from device
        gettfFrame = tfFrame;
    }
    else tflI2C.printStatus();
    delay(500);
}

void tfGetData()
{
    // If data is read without error
    if (tflI2C.getData(tfDist, tfFlux, tfTemp, tfAddr))
    {
        tfTemp = int16_t(tfTemp / 100);

        if (tfCount < 10) ++tfCount;

        else
        {
            tflI2C.Get_Time(tfTime, tfAddr);
            tfCount = 0;
        }
    }
    delay(1);
}

// Average Filter
void dataAverageFilter()
{
    float tfData = 0;
    for (uint16_t i = 0; i < index; i++)
    {
        tfGetData();
        tfData += tfValue[i];
    }
    tfData /= index;
    ptfData = uint32_t(tfData + 0.5);
}

void serialPrint(uint32_t steps)
{
    Serial.print(steps);
    Serial.print(",");
    Serial.print(ptfData);
    Serial.print(",");
    Serial.print(tfTemp);
    Serial.print(",");
    Serial.print(tfFlux);
    Serial.print(",");
    
    for(uint32_t i = 0; i <= 2; i++)
    {
        if (i == 2)
        {
            Serial.print(tfVer[i]);
        }
        else
        {
          Serial.print(tfVer[i]);
          Serial.print(".");
        }
    }
    
    Serial.print(",");
    
    for (uint8_t i = 0; i < 14; ++i)
    {
        Serial.print(char(tfCode[i]));
    }
    
    Serial.print(",");
    Serial.print(tfTime);
    Serial.print(",");
    Serial.print(settfFrame);
    Serial.print(",");
    Serial.println(gettfFrame);
}

void btPrint()
{
    BT.print(steps);
    BT.print(",");
    BT.print(ptfData);
    BT.print(",");
    BT.print(tfTemp);
    BT.print(",");
    BT.print(tfFlux);
    BT.print(",");
    
    for(uint32_t i = 0; i <= 2; i++)
    {
        if (i == 2)
        {
            BT.print(tfVer[i]);
        }
        else
        {
          BT.print(tfVer[i]);
          BT.print(".");
        }
    }
    
    BT.print(",");
    for (uint8_t i = 0; i < 14; ++i)
    {
        BT.print(char(tfCode[i]));
    }
    
    BT.print(",");
    BT.print(tfTime);
    BT.print(",");
    BT.print(settfFrame);
    BT.print(",");
    BT.println(gettfFrame);
}

void ledTxControl(uint32_t steps)
{
    serialPrint(steps);
    TXLED1;
    btPrint();
    delay(10);
    RXLED0;
    TXLED0;
}


// Debugging Test(Serial, Bluetooth)
void testPrint()
{
    while (BT.available())
    {
        ReceiveBTString = BT.readStringUntil('\n');
        RXLED1;
        delay(5);
        Serial.print(ReceiveBTString);
    }
    
    while (Serial.available()) {
        TrasmitSRString = Serial.readStringUntil('\n');
        TXLED1;
        delay(5);
        BT.print(TrasmitSRString);
    }
}

void steppingMotorController()
{
    for(uint32_t i = 0; i <= 24; i++)
    {
        digitalWrite(8, 1); digitalWrite(6, 0); digitalWrite(7, 0); digitalWrite(5, 0); ledTxControl(steps); steps++;
        digitalWrite(8, 1); digitalWrite(6, 1); digitalWrite(7, 0); digitalWrite(5, 0); ledTxControl(steps); steps++;
        digitalWrite(8, 0); digitalWrite(6, 1); digitalWrite(7, 0); digitalWrite(5, 0); ledTxControl(steps); steps++;
        digitalWrite(8, 0); digitalWrite(6, 1); digitalWrite(7, 1); digitalWrite(5, 0); ledTxControl(steps); steps++;
        digitalWrite(8, 0); digitalWrite(6, 0); digitalWrite(7, 1); digitalWrite(5, 0); ledTxControl(steps); steps++;
        digitalWrite(8, 0); digitalWrite(6, 0); digitalWrite(7, 1); digitalWrite(5, 1); ledTxControl(steps); steps++;
        digitalWrite(8, 0); digitalWrite(6, 0); digitalWrite(7, 0); digitalWrite(5, 1); ledTxControl(steps); steps++;
        digitalWrite(8, 1); digitalWrite(6, 0); digitalWrite(7, 0); digitalWrite(5, 1); ledTxControl(steps); steps++;
    }
    
    for(uint32_t i = 0; i <= 24; i++)
    {
        digitalWrite(8, 1); digitalWrite(6, 0); digitalWrite(7, 0); digitalWrite(5, 1); ledTxControl(steps); steps--;
        digitalWrite(8, 0); digitalWrite(6, 0); digitalWrite(7, 0); digitalWrite(5, 1); ledTxControl(steps); steps--;
        digitalWrite(8, 0); digitalWrite(6, 0); digitalWrite(7, 1); digitalWrite(5, 1); ledTxControl(steps); steps--;
        digitalWrite(8, 0); digitalWrite(6, 0); digitalWrite(7, 1); digitalWrite(5, 0); ledTxControl(steps); steps--;
        digitalWrite(8, 0); digitalWrite(6, 1); digitalWrite(7, 1); digitalWrite(5, 0); ledTxControl(steps); steps--;
        digitalWrite(8, 0); digitalWrite(6, 1); digitalWrite(7, 0); digitalWrite(5, 0); ledTxControl(steps); steps--;
        digitalWrite(8, 1); digitalWrite(6, 1); digitalWrite(7, 0); digitalWrite(5, 0); ledTxControl(steps); steps--;
        digitalWrite(8, 1); digitalWrite(6, 0); digitalWrite(7, 0); digitalWrite(5, 0); ledTxControl(steps); steps--;
    }
}
