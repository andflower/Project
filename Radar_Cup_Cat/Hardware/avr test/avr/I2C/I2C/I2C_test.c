#define F_CPU 16000000UL

#include <avr/io.h>
#include <util/delay.h>
#include <stdbool.h>
#include "UART.h"
#include "I2C.h"
#include "TFLI2C.h"

// program variables
int16_t  tfAddr = TFL_DEFAULT_ADDR;    // default I2C address
uint16_t tfFrame = TFL_DEFAULT_FPS;   // default frame rate
// device variables passed back by getData
int16_t  tfDist = 0 ;   // distance in centimeters
int16_t  tfFlux = 0 ;   // signal quality in arbitrary units
int16_t  tfTemp = 0 ;   // temperature in 0.01 degree Celsius
// other device variables
uint16_t tfTime = 0;    // devie clock in milliseconds
uint8_t  tfVer[3];      // device version number
uint8_t  tfCode[14];    // device serial number
// sub-loop counter for Time display
uint8_t tfCount = 0;

uint8_t bcd_to_decimal(uint8_t bcd)
{
	return (bcd >> 4) * 10 + (bcd & 0x0F);
}

uint8_t decimal_to_bcd(uint8_t decimal)
{
	return ( ((decimal / 10) << 4) | (decimal % 10) );
}

void write_message(uint8_t no, char *message)
{
	UART_print8bitNumber(no);
}

void sampleCommands( uint8_t adr)
{
	UART_printString( "Device Address: ");
	UART_print8bitNumber( adr);

	UART_printString("System Reset: ");
	if( Soft_Reset( adr))
	{
		UART_printString( "Passed");
		UART_printString("\n");
	}
	else printStatus();  // 'printStatus()' is for troubleshooting
	_delay_ms(500);                 //  are is not usually necessary

	UART_printString( "Get Firmware Version: ");
	if( Get_Firmware_Version( tfVer, adr))
	{
		UART_print8bitNumber( tfVer[2]);
		UART_printString( ".");
		UART_print8bitNumber( tfVer[1]);
		UART_printString( ".");
		UART_print8bitNumber( tfVer[0]);
	}
	else printStatus();
	_delay_ms(500);

	UART_printString( "Get Serial Number: ");
	if( Get_Prod_Code( tfCode, adr))
	{
		for( uint8_t i = 0; i < 14; ++i)
		{
			UART_print8bitNumber( tfCode[i]);
		}
		UART_printString("\n");
	}
	else printStatus();
	_delay_ms(500);

	UART_printString( "Get Time: ");
	if( Get_Time( tfTime, adr))
	{
		UART_print16bitNumber(  tfTime);
		UART_printString("\n");
	}
	else printStatus();
	_delay_ms(500);

	UART_printString( "Set Frame Rate to: ");
	if( Set_Frame_Rate( tfFrame, adr))
	{
		UART_print16bitNumber(  tfFrame);
		UART_printString("\n");
	}
	else printStatus();
	_delay_ms(500);
	
	UART_printString( "Get Frame Rate: ");
	if( Get_Frame_Rate( tfFrame, adr))
	{
		UART_print16bitNumber(  tfFrame);
		UART_printString("\n");  //  Read frame rate from device
	}
	else printStatus();
	_delay_ms(500);

}

void setup()
{
	I2C_INIT();
	UART_INIT();
	sampleCommands( tfAddr);
}

void loop()
{
	// If data is read without error
	if( getData( tfDist, tfFlux, tfTemp, tfAddr))
	{
		UART_printString("Dist: ");     // ...print distance.
		UART_print16bitNumber(tfDist);
		UART_printString(" | Flux: ");     // ...print quality
		UART_print16bitNumber(tfFlux);

		// Convert temperature from hundreths
		// of a degree to a whole number
		tfTemp = tfTemp / 100;

		UART_printString(" | Temp: ");     // ...print temperature
		UART_print16bitNumber( tfTemp);
		UART_printString("\n");
	}
	else printStatus();        // else, print error status

	// Every ten loops this prints device time
	// in milliseconds and resets the counter.
	if( tfCount < 10) ++tfCount;
	else
	{
		UART_printString( "Get Time: ");
		Get_Time( tfTime, tfAddr);
		UART_print16bitNumber(  tfTime);
		UART_printString("\n");
		tfCount = 0;
	}

	_delay_ms( 50);
}

int main(void)
{
	setup();

	while(1)
	{
		loop();
	}
	return 1;
}