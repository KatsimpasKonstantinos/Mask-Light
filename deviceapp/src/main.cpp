#include <Arduino.h>
#include <ledController.h>
#include <WebSocketServerHandler.h>
#include <DataStorage.h>

//---------------------------------------------------------------------------------Constants

const int LED_PIN = 14;   // GPIO5
const int SOUND_PIN = 13; // GPIO7
const int matrixX = 16;
const int matrixY = 8;
const int amountLED = matrixX * matrixY;
const int animationDelay = 10;
const char *ssid = "Pixel5";
const char *password = "123456789";
const bool debugMode = false;
const uint16_t port = 81;

void drawFrame();

//---------------------------------------------------------------------------------Setup

DataStorage dataStorage;
LedController ledController(LED_PIN, matrixX, matrixY);
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
  for (int y = 0; y < matrixY; y++)
  {
    for (int x = 0; x < matrixX; x++)
    {
      if (dataStorage.hasData())
      {
        int index = y * matrixX + x;
        int r = dataStorage.getData()[index * 3];
        int g = dataStorage.getData()[index * 3 + 1];
        int b = dataStorage.getData()[index * 3 + 2];
        ledController.setColor(r, g, b, x, y);
      }
    }
  }
  ledController.show();
}