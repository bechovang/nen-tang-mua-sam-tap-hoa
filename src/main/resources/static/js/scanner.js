const BarcodeScanner = (() => {
    let running = false;
    let handler = null;

    function start(elementId, onDetected) {
        if (running) {
            return;
        }
        const target = document.getElementById(elementId);
        if (!target || typeof Quagga === 'undefined') {
            alert('Trình duyệt chưa hỗ trợ quét mã. Bạn nhập mã tay giúp tiệm nhé.');
            return;
        }
        Quagga.init({
            inputStream: {
                name: 'Live',
                type: 'LiveStream',
                target: target,
                constraints: {
                    facingMode: 'environment',
                    width: {min: 640},
                    height: {min: 480}
                }
            },
            locator: {patchSize: 'medium', halfSample: true},
            numOfWorkers: 2,
            frequency: 10,
            decoder: {
                readers: ['ean_reader', 'ean_8_reader', 'upc_reader', 'code_128_reader']
            },
            locate: true
        }, error => {
            if (error) {
                console.error('Camera error', error);
                alert('Không mở được camera. Bạn nhập mã tay giúp tiệm nhé.');
                return;
            }
            running = true;
            Quagga.start();
        });

        if (handler) {
            Quagga.offDetected(handler);
        }
        handler = result => {
            if (!running) {
                return;
            }
            const code = result?.codeResult?.code;
            if (!code) {
                return;
            }
            stop();
            onDetected(code);
        };
        Quagga.onDetected(handler);
    }

    function stop() {
        if (running && typeof Quagga !== 'undefined') {
            try {
                Quagga.stop();
            } catch (ignored) {
            }
        }
        running = false;
    }

    return {start, stop};
})();
