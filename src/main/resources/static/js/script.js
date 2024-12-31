document.addEventListener("DOMContentLoaded", function() {
    const url = new URL(window.location.href);
    if (url.searchParams.has('success')) {
        url.searchParams.delete('success');
        window.history.replaceState({}, document.title, url.toString());
    }
});

document.addEventListener("DOMContentLoaded", function() {
    const url = new URL(window.location.href);
    if (url.searchParams.has('error')) {
        url.searchParams.delete('error');
        window.history.replaceState({}, document.title, url.toString());
    }
});
