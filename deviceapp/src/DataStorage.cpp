#include "DataStorage.h"

DataStorage::DataStorage() {}

void DataStorage::saveData(uint8_t *newData, size_t size)
{
    delete[] data;
    data = new uint8_t[size];
    memcpy(data, newData, size);
    dataAvailable = true;

    Serial.println("Free Heap Size: " + String(ESP.getFreeHeap()));
    Serial.println("Data Size: " + String(size));
}

bool DataStorage::hasData()
{
    return dataAvailable;
}

void DataStorage::clearData()
{
    dataAvailable = false;
}

uint8_t* DataStorage::getData()
{
    dataAvailable = false;
    return data;
}
