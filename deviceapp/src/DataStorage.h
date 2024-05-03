#ifndef DataStorage_h
#define DataStorage_h

#include <Arduino.h>
#include <vector>

class DataStorage
{
public:
    DataStorage();
    void saveData(const std::vector<uint8_t> &data);
    bool hasData();
    void clearData();
    std::vector<uint8_t> getData();

private:
    std::vector<uint8_t> data;
    bool dataAvailable = false;
};

#endif
