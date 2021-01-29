#include "UART.h"
#define F_CPU 16000000UL

void UART_INIT(void)
{
	// USART Control and Status Register 0 A
	//  8비트 UCSR0A(7:RXC0, 6:TXC0, 5:UDRE0, 4:FE0, 3:DOR0, 2:UPE0, 1:U2X0, 0:MPCM0)
	UCSR0A |= _BV(U2X0);									// 2배속 - 1배속일 경우 0x00
	
	// USART Baud Rate Register
	//  4비트 비어있음
	// 12비트 UBRR0H[11:8], UBRR0L[7:0] - 전송 속도(보율)
	UBRR0H = 0x00;											// 통신 속도(보율) 설정
	UBRR0L = (uint64_t)(((F_CPU/(8*115200))-1) + 0.5);			// 표 10-6 비동기 2배속 참조 baud(9600)

	
	// USART Control and Status Register 0 C
	//  8비트 UCSR0C(7:UMSEL01, 6:UMSEL00, 5:UPM01, 4:UPM00, 3:USBS0, 2:UCSZ01, 1:UCSZ00, 0:UCPOL0)
	UCSR0C = _BV(UCSZ01) | _BV(UCSZ00);						// 비동기, 8비트 데이터, 패리티 없음, 1비트 정지 비트 모드
	
	// USART Control and Status Register 0 B
	//  8비트 UCSR0B(7:RXCIE0, 6:TXCIE0, 5:UDRIE0, 4:RXEN0, 3:TXEN0, 2:UCSZ02, 1:RXB81, 0:TXB80)
	UCSR0B = _BV(RXEN0) | _BV(TXEN0);						// RXEN0(수신), TXEN0(송신)
}

unsigned char UART_receive(void)							// 1바이트 수신
{
	while( !(UCSR0A & (1<<RXC0)) );							// 데이터 수신 대기
	
	// USART Data Register
	//  8비트 RXB[7:0]
	//  8비트 TXB[7:0]
	return UDR0;											// 데이터 수신
}

void UART_transmit(unsigned char data)						// 1바이트 송신
{
	while( !(UCSR0A & (1<<UDRE0)) );						// 데이터 송신 대기
	UDR0 = data;											// 데이터 송신
}

void UART_printString(char *str)							// 문자열 송신
{
	for(int i = 0; str[i]; i++)								// '\0'문자를 만날 때까지 반복
		UART_transmit(str[i]);								// 바이트 단위 출력
}

void UART_print8bitNumber(uint8_t no)						// 숫자를 문자열로 변환하여 송신(8비트)
{
	char numString[4] = "0";
	int i, index = 0;
	
	if(no > 0)												// 문자열 변환
	{
		for(i = 0; no != 0; i++)
		{
			numString[i] = no % 10 + '0';
			no = no / 10;
		}
		numString[i] = '\0';
		index = i - 1;
	}
	
	for(i = index; i >= 0; i--)								// 변환된 문자열 출력
		UART_transmit(numString[i]);
}

void UART_print16bitNumber(uint16_t no)						// 숫자를 문자열로 변환하여 송신(16비트)
{
	char numString[6] = "0";
	int i, index = 0;
		
	if(no > 0)												// 문자열 변환
	{
		for(i = 0; no != 0; i++)
		{
			numString[i] = no % 10 + '0';
			no = no / 10;
		}
		numString[i] = '\0';
		index = i - 1;
	}
		
	for(i = index; i >= 0; i--)								// 변환된 문자열 출력
	UART_transmit(numString[i]);
}

void UART_print32bitNumber(uint32_t no)						// 숫자를 문자열로 변환하여 송신(32비트)
{
		char numString[11] = "0";
		int i, index = 0;
		
		if(no > 0)											// 문자열 변환
		{
			for(i = 0; no != 0; i++)
			{
				numString[i] = no % 10 + '0';
				no = no / 10;
			}
			numString[i] = '\0';
			index = i - 1;
		}
		
		for(i = index; i >= 0; i--)							// 변환된 문자열 출력
		UART_transmit(numString[i]);
}