#include "LedController.h"
#include <Arduino.h>

LedController::LedController(int pin, int numLedsX, int numLedsY)
    : strip(numLedsX * numLedsY, pin, NEO_GRB + NEO_KHZ800), width(numLedsX), height(numLedsY)
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
    setColor(255, 255, 255, 0, 0);
    setColor(255, 255, 255, width - 1, 0);
    setColor(255, 255, 255, 0, height - 1);
    setColor(255, 255, 255, width - 1, height - 1);
    strip.show();
}

void LedController::yesHotSpotNoWSConnection(String ip)
{
    clear();
    for (int x = 0; x < ip.length() && x < width; x++)
    {
        if (ip[x] != '.')
        {
            int value = ip[x] - '0';
            int y = value % height;
            bool isRolledOver = value >= height;
            if (isRolledOver)
            {
                if (y == 0)
                {
                    setColor(0, 0, 255, x, 0);
                }
                else
                {
                    for (int j = 0; j < y; j++)
                    {
                        setColor(255, 0, 0, x, j);
                    }
                }
            }
            else
            {
                if (y == 0)
                {
                    setColor(0, 255, 0, x, 0);
                }
                else
                {
                    for (int j = 0; j < y; j++)
                    {
                        setColor(255, 255, 255, x, j);
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
    for (int x = 0; x < width; x++)
    {
        setColor(0, 255, 0, x, 0);
        setColor(0, 255, 0, x, height - 1);
    }
    strip.show();
}

void LedController::setColor(int r, int g, int b)
{
    for (int i = 0; i < strip.numPixels(); i++)
    {
        int correctedIndex = assConverter(i);
        strip.setPixelColor(correctedIndex, strip.Color(r, g, b));
    }
}

void LedController::setColor(int r, int g, int b, int ledX, int ledY)
{
    if (ledX >= 0 && ledX < width && ledY >= 0 && ledY < height)
    {
        int led = ledY * width + ledX;
        int correctedLed = assConverter(led);
        strip.setPixelColor(correctedLed, strip.Color(r, g, b));
    }
}

void LedController::setBrightness(int brightness)
{
    strip.setBrightness(brightness);
}

void LedController::clear()
{
    for (int i = 0; i < strip.numPixels(); i++)
    {
        int correctedIndex = assConverter(i);
        strip.setPixelColor(correctedIndex, strip.Color(0, 0, 0));
    }
}

void LedController::clear(int ledX, int ledY)
{
    if (ledX >= 0 && ledX < width && ledY >= 0 && ledY < height)
    {
        int led = ledY * width + ledX;
        int correctedLed = assConverter(led);
        strip.setPixelColor(correctedLed, strip.Color(0, 0, 0));
    }
}

void LedController::show()
{
    strip.show();
}

int LedController::assConverter(int wrongPos) // needed to remapp the LED positions, We are using Two 8x8 LED matrix
{
    int rightPos = 0;

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
