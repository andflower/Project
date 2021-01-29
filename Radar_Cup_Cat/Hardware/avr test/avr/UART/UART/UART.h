#ifndef	_UART_H_
#define	_UART_H_

#include <avr/io.h>

void UART_INIT(void);							// UART�ʱ�ȭ
unsigned char UART_receive(void);				// 1����Ʈ ����
void UART_transmit(unsigned char data);			// 1����Ʈ �۽�
void UART_printString(char *str);				// ���ڿ� �۽�
void UART_print8bitNumber(uint8_t no);			// ���ڸ� ���ڿ��� ��ȯ�Ͽ� �۽�(8��Ʈ)
void UART_print16bitNumber(uint16_t no);		// ���ڸ� ���ڿ��� ��ȯ�Ͽ� �۽�(16��Ʈ)
void UART_print32bitNumber(uint32_t no);		// ���ڸ� ���ڿ��� ��ȯ�Ͽ� �۽�(32��Ʈ)

#endif	//__UART_H__