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