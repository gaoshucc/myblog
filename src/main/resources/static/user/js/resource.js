//跳转到顶部
$(function(){
    if($(window).scrollTop()<100){
        $("#uptoTop").fadeOut(1);   //初始化页面时，隐藏Top按钮
    }

    $(window).scroll(function(){
        if($(window).scrollTop()>100){
            $("#uptoTop").fadeIn(1000);  //当页面起点距离窗口大于100时，淡入
        }else{
            $("#uptoTop").fadeOut(1000);    //当页面起点距离窗口小于100时，淡出
        }
    });
    //跳转到顶部（动画）
    $("#uptoTop").click(function(){
        var dis = $(window).scrollTop();
        $("body,html").animate({scrollTop:0},200);  
        return false;
     });
});

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
addLoadEvent(showUserDetail);