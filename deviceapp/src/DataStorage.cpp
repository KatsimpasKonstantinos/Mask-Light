#include "DataStorage.h"

DataStorage::DataStorage() {}

void DataStorage::saveData(const std::vector<uint8_t> &data)
{
    // Store the incoming binary data
    this->data = data;
    dataAvailable = true;
    // log all the data
    Serial.println("DataStorage::saveData");
}

bool DataStorage::hasData()
{
    return dataAvailable;
}

void DataStorage::clearData()
{
    data.clear();
    dataAvailable = false;
}

std::vector<uint8_t> DataStorage::getData()
{
    return data;
}
