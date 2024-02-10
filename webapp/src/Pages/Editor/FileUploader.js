import React, { useRef } from 'react';
import Button from '@mui/material/Button';
import DriveFolderUploadIcon from '@mui/icons-material/DriveFolderUpload';

function FileUploader({ setDataBinary, buttonVariant, setConstants }) {

    const fileInputRef = useRef(null);

    function handleFileChange(event) {
        const file = event.target.files[0];
        if (!file) {
            return;
        }

        // Extract the file extension
        const fileFormat = file.name.split('.').pop();
        let weirdMode;
        if (fileFormat !== "bin") {
            weirdMode = { matrixX: 16, matrixY: 8 };
        }

        const reader = new FileReader();
        reader.onload = (e) => {
            const newData = e.target.result;
            if (newData.byteLength < 2) {
                console.error("File is too short.");
                return;
            }

            const byteView = new Uint8Array(newData.slice(newData.byteLength - 2));
            const matrixX = weirdMode ? weirdMode.matrixX : byteView[0];
            const matrixY = weirdMode ? weirdMode.matrixY : byteView[1];
            const totalPixels = (newData.byteLength - (weirdMode ? 0 : 2)) / 3;
            const pixelsPerFrame = matrixX * matrixY;
            const frames = Math.ceil(totalPixels / pixelsPerFrame);

            const updatedConstants = {
                matrixX,
                matrixY,
                frames,
                fileFormat: fileFormat,
            };

            setConstants(updatedConstants);

            // Handle the data for use
            const dataWithoutLast2Bytes = newData.slice(0, newData.byteLength - 2);
            setDataBinary(dataWithoutLast2Bytes);
        };
        reader.readAsArrayBuffer(file);
    }


    const triggerFileInput = () => {
        fileInputRef.current.click();
    };

    return (
        <>
            <input
                type="file"
                style={{ display: 'none' }}
                ref={fileInputRef}
                onChange={handleFileChange}
            />
            <Button
                variant={buttonVariant}
                component="span"
                onClick={triggerFileInput}
                startIcon={<DriveFolderUploadIcon />}
            >Upload File</Button>
        </>
    );
}

export default FileUploader;
