#ifndef LedController_h
#define LedController_h

#include <Adafruit_NeoPixel.h>

class LedController {
public:
    LedController(int pin, int numLedsX, int numLedsY);
    void setup();
    void setColor(int r, int g, int b);
    void setColor(int r, int g, int b, int ledX, int ledY);
    void setBrightness(int brightness);
    void clear();
    void clear(int ledX, int ledY);
    void show();
    void noConnection();
    void yesHotSpotNoWSConnection(String ip);
    void allConnection();

    int assConverter(int index);

private:
    Adafruit_NeoPixel strip;
    int width, height; // Dimensions of the matrix
};

#endif
