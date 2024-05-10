#ifndef DataStorage_h
#define DataStorage_h

#include <Arduino.h>
#include <vector>

class DataStorage
{
public:
    DataStorage();
    void saveData(uint8_t *newData, size_t size);
    bool hasData();
    void clearData();
    uint8_t* getData();

private:
    static const size_t frameSize = 16 * 8 * 3;
    uint8_t* data;
    bool dataAvailable = false;
    size_t allDataSize = 0;
};

#endif
