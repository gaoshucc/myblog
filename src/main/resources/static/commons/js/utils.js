/*const PRO_NAME = "/funnycode";*/
const PRO_NAME = "";
function isnull(val) {

    var str = val.replace(/(^\s*)|(\s*$)/g, '');//去除空格;

    if (str == '' || str == undefined || str == null) {
        return true;
    } else {
        return false;
    }
}
/**
 * 判断某元素是否属于某个类
 * @param cla 类名
 * @param element 元素
 * @return true表示属于，false表示不属于
 */
function hasClass(cla, element) {
    if(element.className.trim().length === 0) return false;
    var allClass = element.className.trim().split(" ");
    return allClass.indexOf(cla) > -1;
}
/**
 * 为元素添加类
 * @param cla 类名
 * @param element 元素
 */
function addClass(cla,element){
    if(!hasClass(cla,element)){
        if(element.setAttribute){
            element.setAttribute("class",element.getAttribute("class")+" "+cla);
        }else{
            element.className = element.className+" "+cla;
        }
    }
}
/**
 * 判断是否已登录
 */
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
 * 添加加载动画
 * @param element 需要添加加载动画的元素
 */
function loading(element) {
    var loadElement = document.createElement("div");
    addClass("lds-css", loadElement);
    addClass("ng-scope", loadElement);
    loadElement.innerHTML = "<div style='width:100%;height:100%;' class='lds-double-ring'>" +
                                "<div></div>" +
                                "<div></div>" +
                            "</div>";
    element.appendChild(loadElement);
    console.log("加载中");
}
/**
 * 移除加载动画
 * @param element 需要移除加载动画的元素
 */
function loaded(element) {
    element.innerHTML = "";
    console.log("加载完");
}

/**
 * 自动关闭弹出层
 * @param content 显示内容
 * @param width 宽度
 * @param height 高度
 */
var autoPopup;
function showAutoPopup(content,width,height,time) {
    body = document.body;
    autoPopup = document.createElement("div");
    autoPopup.setAttribute("id","commons-popup");
    autoPopup.innerHTML = "<div id='commons-autoPopup-content'>" +
        content +
        "</div>" +
        "<div id='commons-autoPopup-bg'></div>";
    body.appendChild(autoPopup);
    var popupContent = document.querySelector("#commons-autoPopup-content");
    console.log("width:"+width + ";height:"+height);
    popupContent.style.width = width + "px";
    popupContent.style.height = height + "px";
    popupContent.style.marginLeft = -width/2 + "px";
    popupContent.style.marginTop = -height/2 + "px";
    addClass("show",autoPopup);
    setTimeout(function () {
        body.removeChild(autoPopup);
    },time);
}

/**
 * 弹出层util
 * @param content 显示内容
 * @param width 宽度
 * @param height 高度
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
    //关闭按钮
    var commonsClose = document.querySelector("#commons-close");
    commonsClose.addEventListener("click",function (e) {
        body.removeChild(popup);
    });
    //取消按钮
    var popupCancel = document.querySelector("#popup-cancel");
    popupCancel.addEventListener("click",function () {
        body.removeChild(popup);
    });
    //点击背景关闭弹窗
    var commonsPopupBg = document.querySelector("#commons-popup-bg");
    commonsPopupBg.addEventListener("click",function (e) {
        if(e.target == e.currentTarget){
            body.removeChild(popup);
        }
    });

    addClass("show",popup);
}

/**
 * 判断当前关注状态
 * @param attentionId 某个用户的ID
 * @return true表示已关注，false表示未关注
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
 * @param attentionBtn 关注按钮
 */
function attention(attentionBtn) {
    var attentionId;
    attentionBtn.addEventListener("click",function (e) {
        if(hasLogin()){
            attentionId = attentionBtn.getAttribute("data-followee-id");
            if(payAttentionTo(attentionId)){
                if(hasAttention(attentionId)){
                    updateAttentionBtnStyle(attentionBtn,true);
                    showAutoPopup("<span>关注成功</span>",100,60,2000);
                    console.log("关注成功");
                }else{
                    updateAttentionBtnStyle(attentionBtn,false);
                    showAutoPopup("<span>已取消关注</span>",120,60,2000);
                    console.log("未关注");
                }
                console.log("操作成功");
            }else{
                console.log("操作失败");
            }
        }else{
            showPopup("<span id='popup-login-title'>小主，要登录才能关注哦<br>(,,・ω・,,)</span><a href='/user/loginpage' id='popup-login'>登录</a><a id='popup-cancel'>取消</a>",200,200);
        }
    });
}
/**
 * 改变关注按钮样式
 * @param element 按钮
 * @param flag 关注状态
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
/**
 * 关注ta
 * @param attentionId 某个用户的ID
 */
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
