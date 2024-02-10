import React, { useEffect, useState } from 'react';
import ColorPicker from "./ColorPicker";
import PixelArray from "./PixelArray";
import Initializer from './Initializer';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import DownloadIcon from '@mui/icons-material/Download';
import AddIcon from '@mui/icons-material/Add';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import PauseIcon from '@mui/icons-material/Pause';
import RemoveIcon from '@mui/icons-material/Remove';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import SpeedIcon from '@mui/icons-material/Speed';
import { ButtonGroup, Slider, Typography } from '@mui/material';
import FileUploader from './FileUploader';

function Editor() {
    const [constants, setConstants] = useState();
    const [selectedColor, setSelectedColor] = useState("#0000ff");
    const [dataBinary, setDataBinary] = useState();
    const [currentFrame, setCurrentFrame] = useState(0);
    const [playing, setPlaying] = useState(false);
    const [speed, setSpeed] = useState(1);
    const defaultColor = "black";
    const buttonVariant = "outlined";

    // Function to download dataBinary as a file
    const downloadDataBinary = () => {
        const blob = new Blob([dataBinary], { type: "text/plain" });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        console.log(constants);
        const filename = `${constants.matrixX}-${constants.matrixY}-${constants.frames}-${constants.name}.${constants.fileFormat}`
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    };

    const incrementFrame = () => {
        if (!constants) return;
        if (currentFrame < constants.frames - 1) {
            setCurrentFrame(currentFrame + 1);
        } else {
            setCurrentFrame(0);
        }
    };

    const decrementFrame = () => {
        if (!constants) return;
        if (currentFrame > 0) {
            setCurrentFrame(currentFrame - 1);
        } else {
            setCurrentFrame(constants.frames - 1);
        }
    };

    const togglePlay = () => {
        setPlaying(!playing);
    }

    useEffect(() => {
        let interval = null;
        if (playing && constants) {
            interval = setInterval(() => {
                setCurrentFrame(currentFrame => (currentFrame + 1) % constants.frames);
            }, 1000 / speed);
        } else {
            clearInterval(interval);
        }
        return () => clearInterval(interval); // Cleanup interval on component unmount
    }, [playing, constants, speed]);

    const copyPreviousFrameData = () => {
        // Ensure dataBinary, constants, and dimensions are defined
        if (!dataBinary || !constants || !constants.frames || constants.frames < 2) {
            console.error("Insufficient data or frames to perform copy.");
            return;
        }

        const pixelsPerFrame = constants.matrixX * constants.matrixY;
        const bytesPerFrame = pixelsPerFrame * 3;
        const previousFrameIndex = currentFrame === 0 ? constants.frames - 1 : currentFrame - 1;

        const currentFrameOffset = currentFrame * bytesPerFrame;
        const previousFrameOffset = previousFrameIndex * bytesPerFrame;

        const newDataBinary = dataBinary.slice(0);
        const dataView = new Uint8Array(newDataBinary);
        for (let i = 0; i < bytesPerFrame; i++) {
            dataView[currentFrameOffset + i] = dataView[previousFrameOffset + i];
        }
        setDataBinary(newDataBinary);
    };

    const handleSpeedChange = (event, newValue) => {
        setSpeed(newValue);
    };





    function normalMode() {
        return (
            <Box display="flex" flexDirection="column" alignItems="center" marginY={2}>
                <Box marginBottom={2} display="flex" alignItems="center" gap={2}>
                    <ColorPicker selectedColor={selectedColor} onColorChange={setSelectedColor} />
                    <SpeedIcon />
                    <Slider
                        value={speed}
                        onChange={handleSpeedChange}
                        aria-labelledby="input-slider"
                        min={1}
                        max={20}
                        step={1}
                        marks={true}
                        sx={{
                            height: 8,
                            width: 200,
                            alignSelf: 'center'
                        }}
                    />
                </Box>
                <PixelArray
                    constants={constants}
                    dataBinary={dataBinary}
                    setDataBinary={setDataBinary}
                    selectedColor={selectedColor}
                    defaultColor={defaultColor}
                    currentFrame={currentFrame}
                />
                <Box display="flex" alignItems="center" gap={2} marginY={2}>

                    <ButtonGroup>
                        <Button
                            variant={buttonVariant}
                            onClick={decrementFrame}
                            startIcon={<RemoveIcon />}
                        />
                        <Button
                            variant={buttonVariant}
                            onClick={togglePlay}
                            startIcon={playing ? <PauseIcon /> : <PlayArrowIcon />}
                        >
                            {currentFrame + 1}/{constants ? constants.frames : 0}
                        </Button>
                        <Button
                            variant={buttonVariant}
                            color="primary"
                            onClick={incrementFrame}
                            startIcon={<AddIcon />}
                        />
                        <Button
                            variant={buttonVariant}
                            color="primary"
                            startIcon={<ContentCopyIcon />}
                            onClick={copyPreviousFrameData}
                        >Copy Previous</Button>
                        <Button
                            variant={buttonVariant}
                            color="primary"
                            startIcon={<DownloadIcon />}
                            onClick={downloadDataBinary}
                        >Download Binary</Button>
                        <FileUploader buttonVariant={buttonVariant} onFileUpload={setDataBinary} />
                    </ButtonGroup>
                </Box>

            </Box>
        );
    }

    return (
        <div className="Editor">
            {constants ? normalMode() : <Initializer setConstants={setConstants} setDataBinary={setDataBinary} />}
        </div>
    );
}

export default Editor;
