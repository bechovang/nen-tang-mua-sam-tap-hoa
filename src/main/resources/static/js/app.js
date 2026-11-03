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
