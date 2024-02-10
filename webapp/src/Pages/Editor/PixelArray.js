import './PixelArray.css';

import React, { useState } from 'react';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';

function PixelArray(props) {
    const { constants, selectedColor, defaultColor, dataBinary, setDataBinary, currentFrame } = props;
    const matrixX = constants.matrixX;
    const matrixY = constants.matrixY;
    const bytesPerPixel = 3;

    const handlePixelClick = (x, y) => {
        const currentColor = getColorForPixel(x, y);
        const newColor = currentColor === selectedColor ? defaultColor : selectedColor;

        setDataBinary((prevDataBinary) => {
            const newBinary = new ArrayBuffer(prevDataBinary.byteLength);
            const view = new Uint8Array(newBinary);
            const prevUint8Array = new Uint8Array(prevDataBinary);
            const frameOffset = matrixX * matrixY * bytesPerPixel * currentFrame;
            const pixelOffset = (y * matrixX + x) * bytesPerPixel;
            view.set(prevUint8Array);
            view[frameOffset + pixelOffset] = parseInt(newColor.slice(1, 3), 16);
            view[frameOffset + pixelOffset + 1] = parseInt(newColor.slice(3, 5), 16);
            view[frameOffset + pixelOffset + 2] = parseInt(newColor.slice(5, 7), 16);
            return newBinary;
        });
    }

    const getColorForPixel = (x, y) => {
        const frameOffset = matrixX * matrixY * bytesPerPixel * currentFrame;
        const pixelOffset = (y * matrixX + x) * bytesPerPixel;

        const view = new Uint8Array(dataBinary);
        const r = view[frameOffset + pixelOffset];
        const g = view[frameOffset + pixelOffset + 1];
        const b = view[frameOffset + pixelOffset + 2];

        // Convert RGB values to a hex string
        if (r === undefined || g === undefined || b === undefined) return "#000000";
        return `#${r.toString(16).padStart(2, '0')}${g.toString(16).padStart(2, '0')}${b.toString(16).padStart(2, '0')}`;
    };


    const buildPixelArray = () => {
        let rows = [];
        for (let y = 0; y < matrixY; y++) {
            let row = [];
            for (let x = 0; x < matrixX; x++) {
                const color = getColorForPixel(x, y) || defaultColor; // Fallback to defaultColor if needed
                row.push(
                    <Button
                        key={`${x}-${y}`}
                        variant="outlined"
                        onClick={() => handlePixelClick(x, y)}
                        sx={{
                            width: 64,
                            height: 64,
                            borderRadius: 0,
                            margin: 0,
                            padding: 0,
                            lineHeight: 1,
                            backgroundColor: color, // Use color from dataBinary
                            border: '1px solid rgba(0, 0, 0, 0.12)',
                            '&:hover': {
                                backgroundColor: color === selectedColor ? defaultColor : selectedColor,
                            },
                        }}
                    >
                    </Button>
                );
            }
            rows.push(<Box key={y} sx={{ display: 'flex' }}>{row}</Box>);
        }
        return rows;
    };


    return (
        <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', overflow: 'hidden' }}>
            {buildPixelArray()}
        </Box>
    );
}

export default PixelArray;