document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('[data-confirm]').forEach(element => {
        element.addEventListener('click', event => {
            if (!window.confirm(element.getAttribute('data-confirm'))) {
                event.preventDefault();
            }
        });
    });
});

document.documentElement.classList.add('js');

(() => {
    let lastY = window.scrollY;
    const isCompact = window.matchMedia('(max-width: 980px)');
    window.addEventListener('scroll', () => {
        if (!isCompact.matches) {
            document.body.classList.remove('nav-hidden');
            return;
        }
        const y = window.scrollY;
        if (y > lastY + 8 && y > 120) {
            document.body.classList.add('nav-hidden');
        } else if (y < lastY - 8) {
            document.body.classList.remove('nav-hidden');
        }
        lastY = y;
    }, {passive: true});
})();

(() => {
    const TARGET_BYTES = 1_500_000;
    const HARD_LIMIT_BYTES = 2 * 1024 * 1024;
    const MAX_DIMENSION = 1600;
    let token = 0;

    function formatBytes(bytes) {
        if (bytes < 1024 * 1024) return Math.max(1, Math.round(bytes / 1024)) + ' KB';
        return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
    }

    function toBlob(canvas, type, quality) {
        return new Promise((resolve, reject) => {
            canvas.toBlob(blob => blob ? resolve(blob) : reject(new Error('toBlob failed')), type, quality);
        });
    }

    async function decode(file) {
        if (window.createImageBitmap) {
            try {
                return await createImageBitmap(file, {imageOrientation: 'from-image'});
            } catch (ignored) {
                try {
                    return await createImageBitmap(file);
                } catch (ignoredToo) {
                    // fall through to Image
                }
            }
        }
        return new Promise((resolve, reject) => {
            const url = URL.createObjectURL(file);
            const image = new Image();
            image.onload = () => {
                URL.revokeObjectURL(url);
                resolve(image);
            };
            image.onerror = () => {
                URL.revokeObjectURL(url);
                reject(new Error('decode failed'));
            };
            image.src = url;
        });
    }

    function setBusy(input, busy) {
        const form = input.closest('form');
        const button = form?.querySelector('button[type="submit"]');
        input.dataset.compressing = busy ? 'true' : 'false';
        if (!button) return;
        if (busy) {
            button.dataset.originalText = button.textContent;
            button.disabled = true;
            button.textContent = 'Đang nén ảnh...';
        } else {
            button.disabled = false;
            if (button.dataset.originalText) {
                button.textContent = button.dataset.originalText;
                delete button.dataset.originalText;
            }
        }
    }

    async function compress(file) {
        if (!file || !file.type.startsWith('image/') || file.size <= TARGET_BYTES) {
            return file;
        }
        const source = await decode(file);
        const sourceWidth = source.width || source.naturalWidth;
        const sourceHeight = source.height || source.naturalHeight;
        let dimension = MAX_DIMENSION;
        let output = null;

        for (let attempt = 0; attempt < 3; attempt++) {
            const scale = Math.min(1, dimension / Math.max(sourceWidth, sourceHeight));
            const width = Math.max(1, Math.round(sourceWidth * scale));
            const height = Math.max(1, Math.round(sourceHeight * scale));
            const canvas = document.createElement('canvas');
            canvas.width = width;
            canvas.height = height;
            const context = canvas.getContext('2d');
            context.fillStyle = '#fff';
            context.fillRect(0, 0, width, height);
            context.drawImage(source, 0, 0, width, height);

            let quality = 0.82;
            while (true) {
                output = await toBlob(canvas, 'image/jpeg', quality);
                if (output.size <= TARGET_BYTES || quality <= 0.45) break;
                quality -= 0.08;
            }
            if (output.size <= TARGET_BYTES) break;
            dimension = Math.round(dimension * 0.8);
        }

        if (!output || output.size > HARD_LIMIT_BYTES) {
            throw new Error('compressed image still too large');
        }
        const name = (file.name || 'anh').replace(/\.[^.]+$/, '') + '.jpg';
        return new File([output], name, {type: 'image/jpeg', lastModified: Date.now()});
    }

    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('input[type="file"][name="image"]').forEach(input => {
            const status = document.createElement('div');
            status.className = 'form-text image-compress-status';
            input.insertAdjacentElement('afterend', status);

            const form = input.closest('form');
            form?.addEventListener('submit', event => {
                if (input.dataset.compressing === 'true') {
                    event.preventDefault();
                    status.textContent = 'Đang nén ảnh, chờ một chút...';
                }
            });

            input.addEventListener('change', async () => {
                const file = input.files && input.files[0];
                if (!file) {
                    status.textContent = '';
                    return;
                }
                const current = String(++token);
                input.dataset.compressionToken = current;
                setBusy(input, true);
                status.className = 'form-text image-compress-status';
                status.textContent = 'Đang nén ảnh...';
                try {
                    const compressed = await compress(file);
                    if (input.dataset.compressionToken !== current) return;
                    if (compressed !== file) {
                        const transfer = new DataTransfer();
                        transfer.items.add(compressed);
                        input.files = transfer.files;
                        status.textContent = 'Đã nén ' + formatBytes(file.size) + ' → ' + formatBytes(compressed.size);
                    } else {
                        status.textContent = 'Ảnh ' + formatBytes(file.size) + ' — không cần nén.';
                    }
                } catch (error) {
                    console.warn('Image compression failed', error);
                    if (input.dataset.compressionToken === current) {
                        status.className = 'form-text image-compress-status text-danger';
                        status.textContent = 'Không nén được ảnh. Chọn ảnh JPEG/PNG/WebP nhỏ hơn 2 MB.';
                    }
                } finally {
                    if (input.dataset.compressionToken === current) {
                        setBusy(input, false);
                    }
                }
            });
        });
    });
})();
