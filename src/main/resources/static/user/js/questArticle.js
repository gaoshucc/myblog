function addLoadEvent(func){
    var oldonload = window.onload;
    if(typeof window.onload != 'function'){
        window.onload = func;
    }else{
        window.onload = function(){
            oldonload();
            func();
        }
    }
}

/**
 * 悬浮显示用户详细信息
 */
var user = null;
function showUserDetail(){
    var userinfo = document.querySelector('#user');
    var userDetail = document.querySelector('#user_detail');

    var myProfile = document.querySelectorAll(".myProfile");
    var loginUserNickname = document.querySelectorAll(".loginUserNickname");
    var experience = document.querySelector("#experience");

    findUser();
    userinfo.addEventListener('mouseover',function(e){
        userDetail.style.display = 'block';
        //判断鼠标是否已经悬浮，放置重复发送请求
        let event = e || event;        //兼容处理
        let from = event.fromElement || event.relatedTarget;//兼容处理
        if(from && this.contains(from)){      //如果在里面则返回
            return;
        }
        findUser();
    },false);
    userinfo.addEventListener('mouseout',function(e){
        userDetail.style.display = 'none';
    },false);

    //获取登录用户
    function findUser() {
        //向后台发送ajax请求获取登录用户详细信息
        $.ajax({
            type: "GET",
            url: "/user/userDetail",
            dataType: "json",
            success: function (data) {
                user = JSON.parse(data);

                for(let j=0; j<myProfile.length; j++){
                    myProfile[j].src = "http://localhost:8080/user/image/profile/" + user.profilePath;
                }
                for(let i=0; i<loginUserNickname.length; i++){
                    loginUserNickname[i].innerHTML = user.nickname;
                }
                experience.innerHTML = "经验 " + user.experience;
            },
            cache: true,
            async: true
        });
    }
}

function showWin(){
    var write = document.querySelector("#write");
    var askingbg = document.querySelector("#askingbg");
    var askbox = document.querySelector("#askbox");
    
    askingbg.style.display = "none";
    write.addEventListener("click",function(e){
        askingbg.style.display = "block";
        askingbg.style.opacity = "1";
    },"false");

    askbox.addEventListener("click",function(e){
        if(e.target == e.currentTarget){
            askingbg.style.display = "none";
        }
    },"false");
}

function hideWin(){
    var askingbg = document.querySelector("#askingbg");
    var cancel = document.querySelector("#cancel");

    cancel.addEventListener("click",function(){
        var confirmCancel = confirm("是否取消编辑？");
        if(confirmCancel){
            askingbg.style.display = "none";
        }
    },"false");
}

function dynamicLoadCss(url) {
    var head = document.getElementsByTagName('head')[0];
    var link = document.createElement('link');
    link.type='text/css';
    link.rel = 'stylesheet';
    link.href = url;
    head.appendChild(link);
}

addLoadEvent(showUserDetail);
addLoadEvent(showWin);
addLoadEvent(hideWin);