const BarcodeScanner = (() => {
    const ROI = {left: 0.10, top: 0.35, width: 0.80, height: 0.30};
    const SCAN_INTERVAL_MS = 120;
    const CONFIRM_WINDOW_MS = 700;

    let running = false;
    let token = 0;
    let stream = null;
    let timer = null;
    let reader = null;
    let canvas = null;
    let context = null;
    let onResult = null;
    let lastCode = null;
    let lastSeenAt = 0;
    let seenCount = 0;

    function start(elementId, onDetected) {
        if (running) {
            return;
        }
        const target = document.getElementById(elementId);
        if (!target) {
            return;
        }
        if (typeof ZXingBrowser === 'undefined' || !ZXingBrowser.BrowserMultiFormatOneDReader) {
            alert('Trình duyệt chưa tải được bộ quét mã. Bạn nhập mã tay giúp tiệm nhé.');
            return;
        }
        if (!window.isSecureContext) {
            alert('Camera chỉ hoạt động trên HTTPS hoặc localhost. Hãy mở trang bằng HTTPS.');
            return;
        }
        if (!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia) {
            alert('Trình duyệt không hỗ trợ camera. Bạn nhập mã tay giúp tiệm nhé.');
            return;
        }

        running = true;
        const current = ++token;
        onResult = onDetected;
        lastCode = null;
        lastSeenAt = 0;
        seenCount = 0;

        target.innerHTML = '';
        target.classList.add('is-live');

        const video = document.createElement('video');
        video.setAttribute('playsinline', '');
        video.setAttribute('autoplay', '');
        video.muted = true;
        target.appendChild(video);

        const guide = document.createElement('div');
        guide.className = 'scan-guide';
        guide.innerHTML = '<div class="scan-band"></div><div class="scan-line"></div>' +
            '<div class="scan-hint">Căn mã nằm ngang trên vạch đỏ</div>';
        target.appendChild(guide);

        canvas = document.createElement('canvas');
        context = canvas.getContext('2d', {willReadFrequently: true});
        reader = new ZXingBrowser.BrowserMultiFormatOneDReader();

        startCamera(video, current).catch(error => {
            console.error('Camera error', error);
            stop();
            alert('Không mở được camera. Bạn nhập mã tay giúp tiệm nhé.');
        });
    }

    async function startCamera(video, current) {
        stream = await navigator.mediaDevices.getUserMedia({
            audio: false,
            video: {
                facingMode: {ideal: 'environment'},
                width: {ideal: 1280},
                height: {ideal: 720},
                focusMode: 'continuous'
            }
        });
        if (!running || current !== token) {
            stopStream();
            return;
        }
        video.srcObject = stream;
        await video.play();
        if (!running || current !== token) {
            stopStream();
            return;
        }
        scheduleFrame(current);
    }

    function scheduleFrame(current) {
        timer = window.setTimeout(() => scanFrame(current), SCAN_INTERVAL_MS);
    }

    function scanFrame(current) {
        if (!running || current !== token) {
            return;
        }
        const video = document.querySelector('.scan-box.is-live video');
        if (!video || video.readyState < 2 || !video.videoWidth || !video.videoHeight) {
            scheduleFrame(current);
            return;
        }

        const sx = Math.round(video.videoWidth * ROI.left);
        const sy = Math.round(video.videoHeight * ROI.top);
        const sw = Math.round(video.videoWidth * ROI.width);
        const sh = Math.round(video.videoHeight * ROI.height);
        if (canvas.width !== sw || canvas.height !== sh) {
            canvas.width = sw;
            canvas.height = sh;
        }
        context.drawImage(video, sx, sy, sw, sh, 0, 0, sw, sh);

        let code = null;
        try {
            const result = reader.decodeFromCanvas(canvas);
            code = result ? result.getText() : null;
        } catch (ignored) {
            // ZXing throws NotFoundException on most frames; that is expected.
        }

        if (code) {
            code = code.trim().replace(/\s+/g, '').toUpperCase();
            const now = performance.now();
            if (code === lastCode && now - lastSeenAt <= CONFIRM_WINDOW_MS) {
                seenCount += 1;
            } else {
                lastCode = code;
                seenCount = 1;
            }
            lastSeenAt = now;
            if (seenCount >= 2) {
                const callback = onResult;
                stop();
                if (navigator.vibrate) {
                    navigator.vibrate(120);
                }
                if (callback) {
                    callback(code);
                }
                return;
            }
        }

        scheduleFrame(current);
    }

    function stopStream() {
        if (stream) {
            stream.getTracks().forEach(track => track.stop());
            stream = null;
        }
    }

    function stop() {
        running = false;
        token += 1;
        onResult = null;
        lastCode = null;
        seenCount = 0;
        if (timer) {
            window.clearTimeout(timer);
            timer = null;
        }
        stopStream();
        const target = document.querySelector('.scan-box.is-live');
        if (target) {
            target.classList.remove('is-live');
            target.innerHTML = '';
        }
        if (canvas && context) {
            context.clearRect(0, 0, canvas.width, canvas.height);
        }
        canvas = null;
        context = null;
        reader = null;
    }

    return {start, stop};
})();
