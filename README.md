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
  - Hosted on [masklight.konstantinos.katsimpas.com](https://masklight.konstantinos.katsimpas.com/)


## Table of Contents
- [Mask-Light](#mask-light)
  - [Components](#components)
  - [Table of Contents](#table-of-contents)
  - [KFF Format](#kff-format)
    - [V0](#v0)
  - [Matrix Controller App](#matrix-controller-app)
    - [UI Components](#ui-components)
  - [Installation](#installation)
  - [Usage](#usage)
  - [Roadmap](#roadmap)
  - [Contributing](#contributing)
  - [License](#license)
  - [Contact](#contact)


## KFF Format
The KFF (Konstis File Format) is a custom binary data format used for communication between the app, the LED matrix, and the web application.

### V0
Version 1Byte
Width 1Byte
Height 1Byte
Speed 1Byte
Data ByteArray (size is Width * Height * 3, cluster of 3 Bytes for each Pixel (rgb), compareable to a stream)

![image](https://github.com/KatsimpasKonstantinos/Mask-Light/assets/134274002/f5200bf0-b03a-4b06-a189-e789bbbcd26e)
https://excalidraw.com/#json=NlSfO8j-Cext2qld8iBYQ,q-OGPAmQ4XPZjE9-orb1ww

 
## Matrix Controller App
The Matrix Controller App is designed to allow users to control an LED matrix by sending data via Bluetooth to an ESP microcontroller. The app supports displaying text, animations, and other data formats.

### UI Components
- **Home:** Establishes a Bluetooth connection to the LED matrix.
- **2KFF:** Converts text to KFF format with customizable parameters (e.g., speed, color).
- **Live:** Previews and sends saved KFF files to the LED matrix.
- **Info:** Displays additional information about the application.

## Installation
TODO

## Usage
TODO

## Roadmap
A Roadmap of features that will be added can be found in the [TODO List](TODO.md)

## Contributing
If you'd like to contribute to this project, please follow the [contributing guidelines](CONTRIBUTING.md).

## License
This project is licensed under the [MIT License](LICENSE).

## Contact
For any questions or feedback, please reach out to [konstantinoskatsimpas02@gmail.com](mailto:konstantinoskatsimpas02@gmail.com).
