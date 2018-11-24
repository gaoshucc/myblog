/* //显示侧边栏
function showAside(){
    var account_funcbox = document.querySelector("#account-funcbox");
    var switch_box = document.querySelector("#switch-box");
    var ani_tags = false;

    switch_box.addEventListener("click",function(){
        if(!ani_tags){
            account_funcbox.classList.add("off");
            switch_box.classList.add("fold");
        }
        ani_tags = true;
        account_funcbox.classList.toggle("on");
        switch_box.classList.toggle("unfold");
    });
} */

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

//显示侧边栏
function showAside(){
    var switch_box = document.querySelector("#switch-box");
    var close_aside = document.querySelector("#close-aside");
    var account_funcbox = document.querySelector("#account-funcbox");
    var ani_tags = false;

    switch_box.addEventListener('click',function(){
        if(!ani_tags){
            account_funcbox.classList.add("hideaside");
        }
        ani_tags = true;
        account_funcbox.classList.toggle("showaside");
    });

    close_aside.addEventListener('click',function(){
        ani_tags = false;
        account_funcbox.classList.toggle("showaside");
    });
}

addLoadEvent(showUserDetail);
addLoadEvent(showAside);


//监听点击事件，显示或隐藏“关注的人”列表
function showfollweelist(){
    var $followee = $("#followee");
    var $followeelist = $("#list-followee");
    $followee.click(function(){
        $followee.toggleClass("pulldown");
        $followeelist.toggle(200,function(){});
    });
}

function showpasswordmanage(){
    var $mdfpwdbtn = $("#mdfpwd-btn");
    var $password_manage = $("#password-manage");
    var $confirm = $("#confirm");
    var $close_password_manage = $("#close-password-manage");

    $mdfpwdbtn.click(function(){
        $password_manage.toggle(200,function(){});
    });
    $confirm.click(function(){
        $password_manage.toggle(200,function(){});
    });
    $close_password_manage.click(function(){
        $password_manage.toggle(200,function(){});
    });
}

$(function(){
    showfollweelist();
    showpasswordmanage();
})

