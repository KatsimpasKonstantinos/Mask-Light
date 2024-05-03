#ifndef WebSocketServerHandler_h
#define WebSocketServerHandler_h

#include <ESP8266WiFi.h> // For ESP8266, use <WiFi.h> for ESP32
#include <WebSocketsServer.h>
#include "DataStorage.h"
#include "LedController.h"

class WebSocketServerHandler
{
public:
    WebSocketServerHandler(uint16_t port, DataStorage &dataStorage, const char *ssid, const char *password, LedController &ledController);
    void begin();
    void loop();
    String getIp();
    static void onWebSocketEvent(uint8_t client_num, WStype_t type, uint8_t *payload, size_t length);

private:
    WebSocketsServer _webSocketServer;
    DataStorage &_dataStorage; // Reference to the DataStorage instance
    LedController &_ledController; // Reference to the LedController instance
    const char *_ssid;
    const char *_password;
    uint16_t _port;                           // Store the port number
    static WebSocketServerHandler *_instance; // For accessing instance methods in static callback

    void handleEvent(uint8_t client_num, WStype_t type, uint8_t *payload, size_t length);
};

#endif // WebSocketServerHandler_h
