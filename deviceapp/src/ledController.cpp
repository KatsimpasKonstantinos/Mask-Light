#include "LedController.h"
#include <Arduino.h>

LedController::LedController(uint8_t pin, uint8_t numLedsX, uint8_t numLedsY, uint8_t defaultBrightness)
    : strip(numLedsX * numLedsY, pin, NEO_GRB + NEO_KHZ800), width(numLedsX), height(numLedsY), _defaultBrightness(defaultBrightness)
{
}

void LedController::setup()
{
    strip.begin();
    strip.show();
    noConnection();
}

void LedController::noConnection()
{
    clear();
    setColor(_defaultBrightness, _defaultBrightness, _defaultBrightness, 0, 0);
    setColor(_defaultBrightness, _defaultBrightness, _defaultBrightness, width - 1, 0);
    setColor(_defaultBrightness, _defaultBrightness, _defaultBrightness, 0, height - 1);
    setColor(_defaultBrightness, _defaultBrightness, _defaultBrightness, width - 1, height - 1);
    strip.show();
}

void LedController::yesHotSpotNoWSConnection(String ip)
{
    clear();
    for (uint8_t x = 0; x < ip.length() && x < width; x++)
    {
        if (ip[x] != '.')
        {
            uint8_t value = ip[x] - '0';
            uint8_t y = value % height;
            bool isRolledOver = value >= height;
            if (isRolledOver)
            {
                if (y == 0)
                {
                    setColor(0, 0, _defaultBrightness, x, 0);
                }
                else
                {
                    for (uint8_t j = 0; j < y; j++)
                    {
                        setColor(_defaultBrightness, 0, 0, x, j);
                    }
                }
            }
            else
            {
                if (y == 0)
                {
                    setColor(0, _defaultBrightness, 0, x, 0);
                }
                else
                {
                    for (uint8_t j = 0; j < y; j++)
                    {
                        setColor(_defaultBrightness, _defaultBrightness, _defaultBrightness, x, j);
                    }
                }
            }
        }
    }
    strip.show();
}

void LedController::allConnection()
{
    clear();
    // draw a green border
    for (uint8_t x = 0; x < width; x++)
    {
        setColor(0, _defaultBrightness, 0, x, 0);
        setColor(0, _defaultBrightness, 0, x, height - 1);
    }
    strip.show();
}

void LedController::setColor(uint8_t r, uint8_t g, uint8_t b)
{
    for (uint16_t i = 0; i < strip.numPixels(); i++)
    {
        uint16_t correctedIndex = assConverter(i);
        strip.setPixelColor(correctedIndex, strip.Color(r, g, b));
    }
}

void LedController::setColor(uint8_t r, uint8_t g, uint8_t b, uint8_t ledX, uint8_t ledY)
{
    if (ledX >= 0 && ledX < width && ledY >= 0 && ledY < height)
    {
        uint16_t led = (uint16_t)ledY * (uint16_t)width + (uint16_t)ledX;
        uint16_t correctedLed = assConverter(led);
        strip.setPixelColor(correctedLed, strip.Color(r, g, b));
    }
}

void LedController::setBrightness(uint8_t brightness)
{
    strip.setBrightness(brightness);
}

void LedController::clear()
{
    for (uint16_t i = 0; i < strip.numPixels(); i++)
    {
        uint16_t correctedIndex = assConverter(i);
        strip.setPixelColor(correctedIndex, strip.Color(0, 0, 0));
    }
}

void LedController::clear(uint8_t ledX, uint8_t ledY)
{
    if (ledX >= 0 && ledX < width && ledY >= 0 && ledY < height)
    {
        uint16_t led = ledY * width + ledX;
        uint16_t correctedLed = assConverter(led);
        strip.setPixelColor(correctedLed, strip.Color(0, 0, 0));
    }
}

void LedController::show()
{
    strip.show();
}

uint16_t LedController::assConverter(uint16_t wrongPos) // needed to remapp the LED positions, We are using Two 8x8 LED matrix
{
    uint16_t rightPos = 0;

    if (wrongPos < 8)
        rightPos = 63 - wrongPos;
    else if (wrongPos < 16)
        rightPos = 79 - wrongPos;
    else if (wrongPos < 24)
        rightPos = 32 + wrongPos;
    else if (wrongPos < 32)
        rightPos = 48 + wrongPos;

    else if (wrongPos < 40)
        rightPos = 79 - wrongPos;
    else if (wrongPos < 48)
        rightPos = 127 - wrongPos;
    else if (wrongPos < 56)
        rightPos = (-16) + wrongPos;
    else if (wrongPos < 64)
        rightPos = 32 + wrongPos;

    else if (wrongPos < 72)
        rightPos = 95 - wrongPos;
    else if (wrongPos < 80)
        rightPos = 175 - wrongPos;
    else if (wrongPos < 88)
        rightPos = (-64) + wrongPos;
    else if (wrongPos < 96)
        rightPos = 16 + wrongPos;

    else if (wrongPos < 104)
        rightPos = 111 - wrongPos;
    else if (wrongPos < 112)
        rightPos = 223 - wrongPos;
    else if (wrongPos < 120)
        rightPos = (-112) + wrongPos;
    else if (wrongPos < 128)
        rightPos = 0 + wrongPos;

    return rightPos;
}
