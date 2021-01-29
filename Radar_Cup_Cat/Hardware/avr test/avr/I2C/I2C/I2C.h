#ifndef _I2C_H_
#define _I2C_H_
#define I2C_SCL		PC5
#define I2C_SDA		PC4
#include <avr/io.h>

void I2C_INIT(void);						// I2C �ʱ�ȭ
void I2C_START(void);						// I2C ����
void I2C_transmit(uint8_t data);			// 1����Ʈ �۽�
uint8_t I2C_receive_ACK(void);				// 1����Ʈ ���� & ACK
uint8_t I2C_receive_NACK(void);				// 1����Ʈ ���� & NACK
void I2C_STOP(void);						// I2C ����

#endif