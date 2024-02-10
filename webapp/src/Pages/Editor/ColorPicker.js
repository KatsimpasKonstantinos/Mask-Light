import React from 'react';

function ColorPicker({ selectedColor, onColorChange }) {

    var cooldown = true;
    var cache = selectedColor;

    setTimeout(() => {
        cooldown = false;
    }, 350);

    function handleChange(e) {
        cache = e.target.value;
        if (cooldown) {
            setTimeout(() => {
                if (!cooldown) onColorChange(cache);
            }, 400);
        } else onColorChange(cache);
    }

    return (
        <input
            type="color"
            defaultValue={selectedColor} // Use defaultValue for controlled component
            onBlur={(e) => onColorChange(e.target.value)} // Update color on blur
            onMouseLeave={(e) => onColorChange(e.target.value)} // Update color on blur
            onChange={(e) => handleChange(e)} // Update color on change
        />
    );
}



export default ColorPicker;
