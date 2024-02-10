import React, { useState } from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import FileUploader from './FileUploader';
import AddIcon from '@mui/icons-material/Add';

function Initializer(props) {
    const { setConstants, setDataBinary } = props;
    const [matrixX, setMatrixX] = useState('');
    const [matrixY, setMatrixY] = useState('');
    const [frames, setFrames] = useState('');
    const [name, setName] = useState(''); // State for the name
    const [errorX, setErrorX] = useState(false);
    const [errorY, setErrorY] = useState(false);
    const [errorFrames, seterrorFrames] = useState(false);
    const [errorName, setErrorName] = useState(false); // Error state for the name
    const [open, setOpen] = useState(false);

    const minDimensionNumber = 1;
    const maxDimensionNumber = 32;
    const minFramesNumber = 1;
    const maxFramesNumber = 64;
    const bytesPerPixel = 3;

    const validateDimensionInput = (value) => {
        return value && value >= minDimensionNumber && value <= maxDimensionNumber;
    };

    const validateFramesInput = (value) => {
        return value && value >= minFramesNumber && value <= maxFramesNumber;
    };

    const validateNameInput = (value) => {
        return value.trim() !== '';
    };

    const handleSubmit = () => {
        const isValidX = validateDimensionInput(matrixX);
        const isValidY = validateDimensionInput(matrixY);
        const isValidFrames = validateFramesInput(frames);
        const isValidName = validateNameInput(name);

        setErrorX(!isValidX);
        setErrorY(!isValidY);
        seterrorFrames(!isValidFrames);
        setErrorName(!isValidName);

        if (isValidX && isValidY && isValidFrames && isValidName) {
            const totalSize = matrixX * matrixY * bytesPerPixel * frames + 3;
            var buffer = new ArrayBuffer(totalSize);
            var dataView = new DataView(buffer);
            dataView.setUint8(totalSize - 2, parseInt(matrixX, 10));
            dataView.setUint8(totalSize - 1, parseInt(matrixY, 10));

            setDataBinary(buffer);
            setConstants({
                name,
                matrixX: parseInt(matrixX, 10),
                matrixY: parseInt(matrixY, 10),
                frames: parseInt(frames, 10),
                fileFormat: "bin"
            });
        }
    };

    function dialogInitialize() {
        return (
            <Dialog open={true} onClose={() => { }} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Initialize Matrix Dimensions</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="name"
                        label="Name"
                        type="text"
                        fullWidth
                        variant="outlined"
                        required
                        error={errorName}
                        helperText={errorName ? "Name cannot be empty" : "This can not be changed later"}
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />
                    <TextField
                        margin="dense"
                        id="matrixX"
                        label="Matrix X Dimension"
                        type="number"
                        fullWidth
                        variant="outlined"
                        required
                        error={errorX}
                        helperText={errorX ? `Enter a value between ${minDimensionNumber} and ${maxDimensionNumber}` : "This can not be changed later"}
                        value={matrixX}
                        onChange={(e) => setMatrixX(e.target.value)}
                    />
                    <TextField
                        margin="dense"
                        id="matrixY"
                        label="Matrix Y Dimension"
                        type="number"
                        fullWidth
                        variant="outlined"
                        required
                        error={errorY}
                        helperText={errorY ? `Enter a value between ${minDimensionNumber} and ${maxDimensionNumber}` : "This can not be changed later"}
                        value={matrixY}
                        onChange={(e) => setMatrixY(e.target.value)}
                    />
                    <TextField
                        margin="dense"
                        id="frames"
                        label="Frames"
                        type="number"
                        fullWidth
                        variant="outlined"
                        required
                        error={errorFrames}
                        helperText={errorFrames ? `Enter a value between ${minFramesNumber} and ${maxFramesNumber}` : "This can not be changed later"}
                        value={frames}
                        onChange={(e) => setFrames(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setOpen(false)} variant="contained" color="primary">
                        Cancel
                    </Button>
                    <Button onClick={handleSubmit} variant="contained" color="primary">
                        Initialize
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }

    function startMode() {
        return (
            <>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => setOpen(true)}
                    style={{ marginRight: 16 }}
                    startIcon={<AddIcon />}
                >
                    Create new Matrix
                </Button>
                <FileUploader buttonVariant="contained" setDataBinary={setDataBinary} setConstants={setConstants} />
            </>
        );
    }

    return (
        <>
            {open ? dialogInitialize() : startMode()}
        </>
    );
}

export default Initializer;
