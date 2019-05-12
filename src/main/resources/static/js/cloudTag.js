window.onload = function() {
    var oDiv = document.getElementById('cloudTag');
    var aA = document.getElementsByTagName('a');
    var i = 0;
    for (i = 0; i < aA.length; i++) {
        aA[i].pause = 1;
        aA[i].time = null;
        initialize(aA[i]);
        aA[i].onmouseover = function() {
            this.pause = 0;
        };
        aA[i].onmouseout = function() {
            this.pause = 1;
        };
    }
    setInterval(starmove, 150);

    function starmove() {
        for (i = 0; i < aA.length; i++) {
            if (aA[i].pause) {
                domove(aA[i]);
            }
        }
    }

    function domove(obj) {
        if (obj.offsetTop <= -obj.offsetHeight) {
            obj.style.display = "none";
            obj.style.top = oDiv.offsetHeight + "px";
            obj.style.display = "block";
            initialize(obj);
        } else {
            obj.style.top = (obj.offsetTop - obj.ispeed) + "px";
            console.log('offsetTop=' + obj.offsetTop)
            console.log('ispeed=' + obj.ispeed)
        }
    }

    function initialize(obj) {
        var iLeft = parseInt(Math.random() * oDiv.offsetWidth);
        var scale = Math.random() * 1 + 1;
        var iTimer = parseInt(Math.random() * 3000);
        var baseColor = 254;
        var initColor = 1;
        var op = parseFloat(Math.random() * 0.5 + 0.5);
        var r = parseInt(Math.random() * baseColor + initColor);
        var g = parseInt(Math.random() * baseColor + initColor);
        var b = parseInt(Math.random() * baseColor + initColor);
        var r1= parseInt(Math.random() * baseColor + initColor);
        var g1 = parseInt(Math.random() * baseColor + initColor);
        var b1 = parseInt(Math.random() * baseColor + initColor);

        obj.style.background = "-webkit-linear-gradient(top right, rgba(" + r + "," + g+ "," + b +"," + op + "), rgba(" + r1 + "," + g1+ "," + b1 +",1))";
        obj.style.background = "-o-linear-gradient(top right, rgba(" + r + "," + g+ "," + b +"," + op + "), rgba(" + r1 + "," + g1+ "," + b1 +",1))";
        obj.style.background = "-moz-linear-gradient(top right, rgba(" + r + "," + g+ "," + b +"," + op + "), rgba(" + r1 + "," + g1+ "," + b1 +",1))";
        obj.style.background = "linear-gradient(to top right, rgba(" + r + "," + g+ "," + b +"," + op + "), rgba(" + r1 + "," + g1+ "," + b1 +",1))";

        obj.pause = 0;

        obj.style.fontSize = 35 * scale + 'px';

        if ((iLeft - obj.offsetWidth) > 0) {
            obj.style.left = iLeft - obj.offsetWidth + "px";
        } else {
            obj.style.left = iLeft + "px";
        }

        clearTimeout(obj.time);
        obj.time = setTimeout(function() {
            obj.pause = 1;
        }, iTimer);
        obj.ispeed = Math.ceil(Math.random() * 5) + 1;
    }
};