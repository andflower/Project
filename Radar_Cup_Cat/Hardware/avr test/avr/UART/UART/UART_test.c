#define F_CPU 16000000UL
#include <avr/io.h>
#include <util/delay.h>
#include "UART.h"

int main(int argc, char *argv[])
{
	unsigned char data;
	
	UART_INIT();
	while (1)
	{
		data = UART_receive();
		UART_transmit(data);
	}
	
	return 0;
}