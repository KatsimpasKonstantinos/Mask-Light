# Mask-Light

This Project allows a user to control an LED matrix.
The ecosystem currently has a webapp, mobileapp and the microcontroller code.


## Components
- **Android Application:** 
  - Version: 10 (API Level 29)
  - Programming Language: Kotlin 1.8.0
- **LED Matrix:**
  - Controller: ESP32 (WiFi + BT + BTLE)
  - Matrix: 8x16 RGB individually adressable
- **Web Application:**
  - Programming Language: JavaScript
  - Hosted on https://masklight.konstantinos.katsimpas.com/


## Table of Contents
- [Mask-Light](#mask-light)
  - [Components](#components)
  - [Table of Contents](#table-of-contents)
  - [KFF Format](#kff-format)
    - [V0](#v0)
  - [Matrix Controller App](#matrix-controller-app)
    - [UI Components](#ui-components)


## KFF Format
The KFF (Konstis File Format) is a custom binary data format used for communication between the app, the LED matrix, and the web application.

### V0
Version 1Byte
Width 1Byte
Height 1Byte
Speed 1Byte
Data ByteArray (size is Width * Height * 3, cluster of 3 Bytes for each Pixel (rgb), compareable to a stream)

 
## Matrix Controller App
The Matrix Controller App is designed to allow users to control an LED matrix by sending data via Bluetooth to an ESP microcontroller. The app supports displaying text, animations, and other data formats.

### UI Components
- **Home:** Establishes a Bluetooth connection to the LED matrix.
- **2KFF:** Converts text to KFF format with customizable parameters (e.g., speed, color).
- **Live:** Previews and sends saved KFF files to the LED matrix.
- **Info:** Displays additional information about the application.

