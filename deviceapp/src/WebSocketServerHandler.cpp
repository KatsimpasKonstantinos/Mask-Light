#include "WebSocketServerHandler.h"

// Initialize the static instance pointer
WebSocketServerHandler *WebSocketServerHandler::_instance = nullptr;

WebSocketServerHandler::WebSocketServerHandler(uint16_t port, DataStorage &dataStorage, const char *ssid, const char *password, LedController &ledController)
    : _webSocketServer(port), _dataStorage(dataStorage), _ssid(ssid), _password(password), _port(port), _ledController(ledController)
{
    _instance = this; // Set the instance pointer to this object
}

void WebSocketServerHandler::begin()
{
    WiFi.begin(_ssid, _password);
    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
        Serial.print(".");
    }
    Serial.println("");
    Serial.print("Connected to WiFi. IP address: ");
    Serial.print(WiFi.localIP());
    Serial.print(":");
    Serial.println(_port);

    _webSocketServer.begin();
    _webSocketServer.onEvent(onWebSocketEvent);
}

void WebSocketServerHandler::loop()
{
    _webSocketServer.loop();
}

String WebSocketServerHandler::getIp()
{
    Serial.println("WebSocketServerHandler::getIp");
    Serial.println(WiFi.localIP().toString());
    return WiFi.localIP().toString();
}

void WebSocketServerHandler::onWebSocketEvent(uint8_t client_num, WStype_t type, uint8_t *payload, size_t length)
{
    if (_instance)
    {
        _instance->handleEvent(client_num, type, payload, length);
    }
}

void WebSocketServerHandler::handleEvent(uint8_t client_num, WStype_t type, uint8_t *payload, size_t length)
{
    switch (type)
    {
    case WStype_DISCONNECTED:
        Serial.printf("[%u] Disconnected!\n", client_num);
        _dataStorage.clearData();
        _ledController.yesHotSpotNoWSConnection(getIp());
        break;
    case WStype_CONNECTED:
    {
        IPAddress ip = WiFi.localIP(); // Assuming you want to log the ESP's IP
        Serial.printf("[%u] Connection from %d.%d.%d.%d\n", client_num, ip[0], ip[1], ip[2], ip[3]);
        _ledController.allConnection();
    }
    break;
    case WStype_TEXT:
    {
        Serial.printf("[%u] Text: %s\n", client_num, payload);
    }
    break;
    case WStype_BIN:
    {
        // Encapsulate the variable initialization in a block
        std::vector<uint8_t> binData(payload, payload + length);
        _dataStorage.saveData(binData);
        Serial.printf("[%u] Binary data received and stored.\n", client_num);
    }
    break;
    default:
        // Handle any other types that are not explicitly handled above
        Serial.printf("[%u] Received unhandled message type: %u\n", client_num, (unsigned int)type);
        break;
    }
}
