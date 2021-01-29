#ifndef	_UART_H_
#define	_UART_H_

#include <avr/io.h>

void UART_INIT(void);							// UART초기화
unsigned char UART_receive(void);				// 1바이트 수신
void UART_transmit(unsigned char data);			// 1바이트 송신
void UART_printString(char *str);				// 문자열 송신
void UART_print8bitNumber(uint8_t no);			// 숫자를 문자열로 변환하여 송신(8비트)
void UART_print16bitNumber(uint16_t no);		// 숫자를 문자열로 변환하여 송신(16비트)
void UART_print32bitNumber(uint32_t no);		// 숫자를 문자열로 변환하여 송신(32비트)

#endif	//__UART_H__