const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 8080 });

wss.on('connection', ws => {
    console.log('New connection!');
    ws.on('message', message => {
        console.log(`Message received: ${message}`);
        if (message == 'getNextFrame') {
            var pixelData = new ArrayBuffer(384);
            ws.send(pixelData);
        } else if (message instanceof Buffer) {
            console.log('Received Buffer: ', message.byteLength);
            ws.send('Buffer received!')
        } else ws.send(`Unknown Command!: ${message}`);
    });
});