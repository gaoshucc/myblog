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
/**
 * 弹出层
 */
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

/**
 * 判断当前关注状态
 */
function hasAttention(attentionId){
    var isAttention = false;
    $.ajax({
        type: "GET",
        url: "/user/hasAttention",
        data:{"attentionId":attentionId},
        dataType: "json",
        success: function (data) {
            var attentionSuccess = JSON.parse(data);
            if(attentionSuccess.status == 1){
                isAttention = true;
                console.log("hasAttention");
            }else{
                isAttention = false;
                console.log("notAttention");
            }
        },
        async: false
    });

    return isAttention;
}
/**
 * 关注操作
 */
function attention(attentionBtn) {
    var attentionId;
    attentionBtn.addEventListener("click",function (e) {
        attentionId = attentionBtn.getAttribute("data-followee-id");
        if(payAttentionTo(attentionId)){
            if(hasAttention(attentionId)){
                updateAttentionBtnStyle(attentionBtn,true);
                console.log("已关注");
            }else{
                updateAttentionBtnStyle(attentionBtn,false);
                console.log("未关注");
            }
            console.log("操作成功");
        }else{
            console.log("操作失败");
        }
    });
}
/**
 * 改变关注按钮样式
 */
function updateAttentionBtnStyle(element, flag) {
    if(flag){
        element.innerHTML = "已关注";
        element.style.backgroundColor = "rgba(216, 216, 216, 1)";
    }else{
        element.innerHTML = "关注ta";
        element.style.backgroundColor = "rgba(0, 133, 235, 1)";
    }
}
/*
* 关注ta
* */
function payAttentionTo(attentionId) {
    var success;
    $.ajax({
        type: "POST",
        url: "/user/payAttentionTo",
        data:{"attentionId":attentionId},
        dataType: "json",
        success: function (data) {
            var attentionSuccess = JSON.parse(data);
            if(attentionSuccess.status == "1"){
                success = true;
            }else{
                success = false;
            }
        },
        async: false
    });

    return success;
}