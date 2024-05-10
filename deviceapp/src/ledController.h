#ifndef LedController_h
#define LedController_h

#include <Adafruit_NeoPixel.h>

class LedController {
public:
    LedController(uint8_t pin, uint8_t numLedsX, uint8_t numLedsY);
    void setup();
    void setColor(uint8_t r, uint8_t g, uint8_t b);
    void setColor(uint8_t r, uint8_t g, uint8_t b, uint8_t ledX, uint8_t ledY);
    void setBrightness(uint8_t brightness);
    void clear();
    void clear(uint8_t ledX, uint8_t ledY);
    void show();
    void noConnection();
    void yesHotSpotNoWSConnection(String ip);
    void allConnection();

    uint16_t assConverter(uint16_t index);

private:
    Adafruit_NeoPixel strip;
    uint8_t width, height; // Dimensions of the matrix
};

#endif
