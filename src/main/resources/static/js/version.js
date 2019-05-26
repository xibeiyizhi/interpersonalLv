function setVersion(url) {
    var version = new Date().getTime();
    if (url.indexOf('?') >= 0) {
        url = url + '&version=' + version;
    } else {
        url = url + '?version=' + version;
    }
    return url;
}