/* 
jq部分
*/
$(function () {
    if ($(window).scrollTop() < 100) {
        $("#uptoTop").fadeOut(1);   //初始化页面时，隐藏Top按钮
    }

    $(window).scroll(function () {
        if ($(window).scrollTop() > 100) {
            $("#uptoTop").fadeIn(1000);  //当页面起点距离窗口大于100时，淡入
        } else {
            $("#uptoTop").fadeOut(1000);    //当页面起点距离窗口小于100时，淡出
        }
    });
    //跳转到顶部（动画）
    $("#uptoTop").click(function () {
        var dis = $(window).scrollTop();
        $("body,html").animate({scrollTop: 0}, 300);
        return false;
    });
});

/* 
js部分
*/
function addLoadEvent(func) {
    var oldonload = window.onload;
    if (typeof window.onload != 'function') {
        window.onload = func;
    } else {
        window.onload = function () {
            oldonload();
            func();
        }
    }
}

function togglePage() {
    var nav = document.getElementsByClassName('nav');
    for (var i = 0; i < nav.length; i++) {
        nav[i].onclick = function () {
            for (var j = 0; j < nav.length; j++) {
                if (nav[j].classList.contains('on')) {
                    nav[j].classList.remove('on');
                    nav[j].classList.add('off');
                }
            }
            this.classList.remove('off');
            this.classList.add('on');

        }
    }
}

var animated = true;
var animated1 = true;

function showSetup() {
    var mine = document.getElementById("mine");

    mine.onclick = function () {
        if (!animated) {
            return;
        }
        move();
    };
}

function move() {
    var setup = document.getElementById("setup");
    var timer = null;
    var speed = 47.5;
    var target = 0;
    var num = 0;
    clearInterval(timer);
    if (parseInt(setup.style.right) == -190) {
        speed = 47.5;
        target = 0;
    } else {
        speed = -47.5;
        target = -190;
    }
    animated = false;
    num = 4;
    timer = setInterval(function () {
        if (num > 1) {
            setup.style.right = parseInt(setup.style.right) + speed + 'px';
            num--;
        } else {
            clearInterval(timer);
            setup.style.right = target + 'px';
            animated = true;
            return;
        }
    }, 50);

}

function linghtBorder() {
    var serchText = document.getElementById('serchText');
    var serch = document.getElementById('serch');
    serchText.onfocus = function () {
        serchText.style.border = 'solid 1px rgba(95, 95, 95, 70%)';
        serchText.style.borderRight = 'none';
        serchText.style.backgroundColor = 'rgba(255, 255, 255, 100%)';
        serchText.setAttribute('placeholder', '');
        serch.style.border = 'solid 1px rgba(0, 133, 235, 100%)';
        serch.style.borderLeft = 'none';
    }
    serchText.onblur = function () {
        serchText.style.border = 'solid 1px rgba(216, 216, 216, 50%)';
        serchText.style.borderRight = 'none';
        serchText.style.backgroundColor = 'rgba(246, 246, 246, 100%)';
        serchText.setAttribute('placeholder', 'java学习路线');
        serch.style.border = 'solid 1px rgba(216, 216, 216, 50%)';
        serch.style.borderLeft = 'none';
    }

}

addLoadEvent(togglePage);
addLoadEvent(showSetup);
addLoadEvent(linghtBorder);