#ifndef _I2C_H_
#define _I2C_H_
#define I2C_SCL		PC5
#define I2C_SDA		PC4
#include <avr/io.h>

void I2C_INIT(void);						// I2C 초기화
void I2C_START(void);						// I2C 시작
void I2C_transmit(uint8_t data);			// 1바이트 송신
uint8_t I2C_receive_ACK(void);				// 1바이트 수신 & ACK
uint8_t I2C_receive_NACK(void);				// 1바이트 수신 & NACK
void I2C_STOP(void);						// I2C 정지

#endif