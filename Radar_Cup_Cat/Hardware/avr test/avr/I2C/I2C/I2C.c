#include "I2C.h"
#define F_CPU 16000000UL

void I2C_INIT(void)
{
	// The Port C Data Direction Register
	//  8비트 DDRC(7:DDC6, 6:DDC5, 5:DDC4, 4:DDC3, 3:DDC3, 2:DDC2, 1:DDC1, 0:DDC0)
	DDRC |= _BV(I2C_SCL);
	DDRC |= _BV(I2C_SDA);
	
	// TWI Bit Rate Register
	//  8비트 TWBR[7:0]
	TWBR = (uint64_t)( F_CPU / (2 * 100000) ) - 8;					// P.316 참조, 분주비 1, 100KHz
	
	//TWI Control Register
	//  8비트 TWCR(7:TWINT, 6:TWEA, 5:TWSTA, 4:TWSTO, 3:TWWC, 2:TWEN, 1:예약됨, 0:TWIE)
	TWCR = _BV(TWEN) | _BV(TWEA);									//I2C 활성화(TWEN), ACK 허용(TWEA)
}

void I2C_START(void)												// I2C 시작
{
	TWCR = _BV(TWINT) | _BV(TWSTA) | _BV(TWEN) | _BV(TWEA);			// 인터럽트(TWINT), 비트 전송 대기(TWSTA)
	while ( !(TWCR & (1 << TWINT)) );									// 시작 완료 대기
}

void I2C_transmit(uint8_t data)									// 데이터 전송
{
	//TWI Data Register
	//  8비트 TWDR(7:TWD7, 6:TWD6, 5:TWD5, 4:TWD4, 3:TWD3, 2:TWD2, 1:TWD1, 0:TWD0)
	TWDR = data;
	TWCR = _BV(TWINT) | _BV(TWEN) | _BV(TWEA);
	while ( !(TWCR & (1 << TWINT)) );									// 전송 완료 대기
}

uint8_t I2C_receive_ACK(void)										// 데이터 수신(ACT)
{
	TWCR = _BV(TWINT) | _BV(TWEN) | _BV(TWEA);
	while ( !(TWCR & (1 << TWINT)) );									// 수신 완료 대기(ACK)
	return TWDR;
}

uint8_t I2C_receive_NACK(void)										// 데이터 수신(NACK)
{
	TWCR = _BV(TWINT) | _BV(TWEN);
	while ( !(TWCR & (1 << TWINT)) );									// 수신 완료 대기(NACK)
	return TWDR;
}

void I2C_STOP(void)
{
	TWCR = _BV(TWINT) | _BV(TWSTO) | _BV(TWEN) | _BV(TWEA);
}