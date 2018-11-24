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

//悬浮显示用户详细信息
function showUserDetail(){
    var user = document.querySelector('#user');
    var userDetail = document.querySelector('#user_detail');
    user.addEventListener('mouseover',function(e){
        userDetail.style.display = 'block';
    },false);
    user.addEventListener('mouseout',function(e){
        userDetail.style.display = 'none';
    },false);
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