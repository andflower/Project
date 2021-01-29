#include "UART.h"
#define F_CPU 16000000UL

void UART_INIT(void)
{
	// USART Control and Status Register 0 A
	//  8��Ʈ UCSR0A(7:RXC0, 6:TXC0, 5:UDRE0, 4:FE0, 3:DOR0, 2:UPE0, 1:U2X0, 0:MPCM0)
	UCSR0A |= _BV(U2X0);									// 2��� - 1����� ��� 0x00
	
	// USART Baud Rate Register
	//  4��Ʈ �������
	// 12��Ʈ UBRR0H[11:8], UBRR0L[7:0] - ���� �ӵ�(����)
	UBRR0H = 0x00;											// ��� �ӵ�(����) ����
	UBRR0L = (uint64_t)(((F_CPU/(8*115200))-1) + 0.5);			// ǥ 10-6 �񵿱� 2��� ���� baud(9600)

	
	// USART Control and Status Register 0 C
	//  8��Ʈ UCSR0C(7:UMSEL01, 6:UMSEL00, 5:UPM01, 4:UPM00, 3:USBS0, 2:UCSZ01, 1:UCSZ00, 0:UCPOL0)
	UCSR0C = _BV(UCSZ01) | _BV(UCSZ00);						// �񵿱�, 8��Ʈ ������, �и�Ƽ ����, 1��Ʈ ���� ��Ʈ ���
	
	// USART Control and Status Register 0 B
	//  8��Ʈ UCSR0B(7:RXCIE0, 6:TXCIE0, 5:UDRIE0, 4:RXEN0, 3:TXEN0, 2:UCSZ02, 1:RXB81, 0:TXB80)
	UCSR0B = _BV(RXEN0) | _BV(TXEN0);						// RXEN0(����), TXEN0(�۽�)
}

unsigned char UART_receive(void)							// 1����Ʈ ����
{
	while( !(UCSR0A & (1<<RXC0)) );							// ������ ���� ���
	
	// USART Data Register
	//  8��Ʈ RXB[7:0]
	//  8��Ʈ TXB[7:0]
	return UDR0;											// ������ ����
}

void UART_transmit(unsigned char data)						// 1����Ʈ �۽�
{
	while( !(UCSR0A & (1<<UDRE0)) );						// ������ �۽� ���
	UDR0 = data;											// ������ �۽�
}

void UART_printString(char *str)							// ���ڿ� �۽�
{
	for(int i = 0; str[i]; i++)								// '\0'���ڸ� ���� ������ �ݺ�
		UART_transmit(str[i]);								// ����Ʈ ���� ���
}

void UART_print8bitNumber(uint8_t no)						// ���ڸ� ���ڿ��� ��ȯ�Ͽ� �۽�(8��Ʈ)
{
	char numString[4] = "0";
	int i, index = 0;
	
	if(no > 0)												// ���ڿ� ��ȯ
	{
		for(i = 0; no != 0; i++)
		{
			numString[i] = no % 10 + '0';
			no = no / 10;
		}
		numString[i] = '\0';
		index = i - 1;
	}
	
	for(i = index; i >= 0; i--)								// ��ȯ�� ���ڿ� ���
		UART_transmit(numString[i]);
}

void UART_print16bitNumber(uint16_t no)						// ���ڸ� ���ڿ��� ��ȯ�Ͽ� �۽�(16��Ʈ)
{
	char numString[6] = "0";
	int i, index = 0;
		
	if(no > 0)												// ���ڿ� ��ȯ
	{
		for(i = 0; no != 0; i++)
		{
			numString[i] = no % 10 + '0';
			no = no / 10;
		}
		numString[i] = '\0';
		index = i - 1;
	}
		
	for(i = index; i >= 0; i--)								// ��ȯ�� ���ڿ� ���
	UART_transmit(numString[i]);
}

void UART_print32bitNumber(uint32_t no)						// ���ڸ� ���ڿ��� ��ȯ�Ͽ� �۽�(32��Ʈ)
{
		char numString[11] = "0";
		int i, index = 0;
		
		if(no > 0)											// ���ڿ� ��ȯ
		{
			for(i = 0; no != 0; i++)
			{
				numString[i] = no % 10 + '0';
				no = no / 10;
			}
			numString[i] = '\0';
			index = i - 1;
		}
		
		for(i = index; i >= 0; i--)							// ��ȯ�� ���ڿ� ���
		UART_transmit(numString[i]);
}