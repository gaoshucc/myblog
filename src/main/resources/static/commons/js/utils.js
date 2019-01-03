function isnull(val) {

    var str = val.replace(/(^\s*)|(\s*$)/g, '');//去除空格;

    if (str == '' || str == undefined || str == null) {
        return true;
    } else {
        return false;
    }
}

function hasClass(cla, element) {
    if(element.className.trim().length === 0) return false;
    var allClass = element.className.trim().split(" ");
    return allClass.indexOf(cla) > -1;
}

function addClass(cla,element){
    if(!hasClass(cla,element)){
        if(element.setAttribute){
            element.setAttribute("class",element.getAttribute("class")+" "+cla);
        }else{
            element.className = element.className+" "+cla;
        }

    }
}

/*
* 是否已登录
* */
function hasLogin() {
    console.log("进入hasLogin了");
    var isLogin = false;
    $.ajax({
        type: "GET",
        url: "/user/whetherTheLogin",
        dataType: "json",
        success: function (data) {
            var whetherTheLogin = JSON.parse(data);
            if(whetherTheLogin.loginStatus == "1"){
                //已登录
                console.log("已登录");
                isLogin = true;
            }else{
                //未登录
                console.log("未登录");
                isLogin = false;
            }
        },
        async: false
    });

    return isLogin;
}

var popup;
function showPopup(content,width,height) {
    body = document.body;
    popup = document.createElement("div");
    popup.setAttribute("id","commons-popup");
    popup.innerHTML = "<div id='commons-popup-content'>" +
        "<span class='iconfont icon-guanbi' id='commons-close' title='关闭'></span>" +
        content +
        "</div>" +
        "<div id='commons-popup-bg'></div>";
    body.appendChild(popup);
    var popupContent = document.querySelector("#commons-popup-content");
    console.log("width:"+width + ";height:"+height);
    popupContent.style.width = width + "px";
    popupContent.style.height = height + "px";
    popupContent.style.marginLeft = -width/2 + "px";
    popupContent.style.marginTop = -height/2 + "px";

    var commonsClose = document.querySelector("#commons-close");
    commonsClose.addEventListener("click",function (e) {
        body.removeChild(popup);
    });
    var popupCancel = document.querySelector("#popup-cancel");
    popupCancel.addEventListener("click",function () {
        body.removeChild(popup);
    });

    addClass("show",popup);
}