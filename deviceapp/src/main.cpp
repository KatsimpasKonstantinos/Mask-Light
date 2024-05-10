#include <Arduino.h>
#include <ledController.h>
#include <WebSocketServerHandler.h>
#include <DataStorage.h>

//---------------------------------------------------------------------------------Constants

const uint8_t LED_PIN = 14;   // GPIO5
const uint8_t SOUND_PIN = 13; // GPIO7
const uint8_t matrixX = 16;
const uint8_t matrixY = 8;
const uint16_t amountLED = matrixX * matrixY;
const char *ssid = "Pixel5";
const char *password = "123456789";
const bool debugMode = false;
const uint8_t defaultBrightness = 20;
const uint16_t port = 81;

void drawFrame();

//---------------------------------------------------------------------------------Setup

DataStorage dataStorage;
LedController ledController(LED_PIN, matrixX, matrixY, defaultBrightness);
WebSocketServerHandler webSocketServerHandler(port, dataStorage, ssid, password, ledController);

void setup()
{
  Serial.begin(115200);
  ledController.setup();
  webSocketServerHandler.begin();
  ledController.yesHotSpotNoWSConnection(webSocketServerHandler.getIp());
}

//---------------------------------------------------------------------------------Loop

int frameCounter = 0;

void loop()
{
  frameCounter++;
  webSocketServerHandler.loop();
  drawFrame();
}

//---------------------------------------------------------------------------------Functions

void drawFrame()
{
  if (dataStorage.hasData())
  {
    uint8_t *data = dataStorage.getData();
    for (uint8_t y = 0; y < matrixY; y++)
    {
      uint16_t column = (uint16_t)y * matrixX;
      for (uint8_t x = 0; x < matrixX; x++)
      {
        uint16_t index = (column + (uint16_t)x) * 3;
        uint8_t r = data[index];
        uint8_t g = data[index + 1];
        uint8_t b = data[index + 2];
        ledController.setColor(r, g, b, x, y);
      }
    }
    ledController.show();
  }
}