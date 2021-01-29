#include "I2C.h"
#define F_CPU 16000000UL

void I2C_INIT(void)
{
	// The Port C Data Direction Register
	//  8��Ʈ DDRC(7:DDC6, 6:DDC5, 5:DDC4, 4:DDC3, 3:DDC3, 2:DDC2, 1:DDC1, 0:DDC0)
	DDRC |= _BV(I2C_SCL);
	DDRC |= _BV(I2C_SDA);
	
	// TWI Bit Rate Register
	//  8��Ʈ TWBR[7:0]
	TWBR = (uint64_t)( F_CPU / (2 * 100000) ) - 8;					// P.316 ����, ���ֺ� 1, 100KHz
	
	//TWI Control Register
	//  8��Ʈ TWCR(7:TWINT, 6:TWEA, 5:TWSTA, 4:TWSTO, 3:TWWC, 2:TWEN, 1:�����, 0:TWIE)
	TWCR = _BV(TWEN) | _BV(TWEA);									//I2C Ȱ��ȭ(TWEN), ACK ���(TWEA)
}

void I2C_START(void)												// I2C ����
{
	TWCR = _BV(TWINT) | _BV(TWSTA) | _BV(TWEN) | _BV(TWEA);			// ���ͷ�Ʈ(TWINT), ��Ʈ ���� ���(TWSTA)
	while ( !(TWCR & (1 << TWINT)) );									// ���� �Ϸ� ���
}

void I2C_transmit(uint8_t data)									// ������ ����
{
	//TWI Data Register
	//  8��Ʈ TWDR(7:TWD7, 6:TWD6, 5:TWD5, 4:TWD4, 3:TWD3, 2:TWD2, 1:TWD1, 0:TWD0)
	TWDR = data;
	TWCR = _BV(TWINT) | _BV(TWEN) | _BV(TWEA);
	while ( !(TWCR & (1 << TWINT)) );									// ���� �Ϸ� ���
}

uint8_t I2C_receive_ACK(void)										// ������ ����(ACT)
{
	TWCR = _BV(TWINT) | _BV(TWEN) | _BV(TWEA);
	while ( !(TWCR & (1 << TWINT)) );									// ���� �Ϸ� ���(ACK)
	return TWDR;
}

uint8_t I2C_receive_NACK(void)										// ������ ����(NACK)
{
	TWCR = _BV(TWINT) | _BV(TWEN);
	while ( !(TWCR & (1 << TWINT)) );									// ���� �Ϸ� ���(NACK)
	return TWDR;
}

void I2C_STOP(void)
{
	TWCR = _BV(TWINT) | _BV(TWSTO) | _BV(TWEN) | _BV(TWEA);
}