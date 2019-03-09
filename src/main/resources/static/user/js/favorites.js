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

addLoadEvent(showUserDetail);
addLoadEvent(findFavoritesCount);
addLoadEvent(findFavorites);
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
            url: PRO_NAME + "/user/userDetail",
            dataType: "json",
            success: function (data) {
                if(!isnull(data)){
                    user = JSON.parse(data);

                    for(let j=0; j<myProfile.length; j++){
                        myProfile[j].src = "/" + user.profilePath;
                    }
                    for(let i=0; i<loginUserNickname.length; i++){
                        loginUserNickname[i].innerHTML = user.nickname;
                    }
                    experience.innerHTML = "经验 " + user.experience;
                }
            },
            async: true
        });
    }
}
/**
 * 查找收藏夹文章数
 */
function findFavoritesCount() {
    var favoritesCount = document.querySelector("#favorites-count");
    $.ajax({
        type: "GET",
        url: PRO_NAME + "/user/myFavoritesCount",
        dataType: "json",
        success: function (data) {
            favoritesCount.innerHTML = JSON.parse(data).count;
        },
        async: true
    });
}
/**
 * 查找我的收藏夹
 */
function findFavorites() {
    var favoritesSection = document.querySelector("#favorites-section");
    var favoritesSectionTemp = "";
    loaded(favoritesSection);
    loading(favoritesSection);
    $.ajax({
        type: "GET",
        url: PRO_NAME + "/user/findFavorites",
        dataType: "json",
        success: function (data) {
            loaded(favoritesSection);
            var favoritesDetailList = JSON.parse(data);
            if(favoritesDetailList.length > 0){
                favoritesSectionTemp = "";
                console.log(favoritesDetailList);
                for(let i=0; i<favoritesDetailList.length; i++){
                    favoritesSectionTemp = favoritesSectionTemp +
                        "<div class='favorites-section-items'>" +
                        "            <article class='article'>" +
                        "                <h2><a class='articleTitle' href='/user/note?noteId="+ favoritesDetailList[i].collectNote.noteId +"'>"+ favoritesDetailList[i].collectNote.noteTitle +"</a></h2>" +
                        "                <p>点击进入详细阅读</p>" +
                        "                <div class='articleInfo'>" +
                        "                    <span class='content-type'>"+ favoritesDetailList[i].collectNote.noteType.typeName +"</span>" +
                        "                    <time>"+ favoritesDetailList[i].collectNote.createTime +"</time>" +
                        "                    <div class='readInfo'>" +
                        "                        <span><a>评论</a>"+ favoritesDetailList[i].collectNote.commentCount +"</span>" +
                        "                    </div>" +
                        "                </div>" +
                        "            </article>" +
                        "            <div class='author-info'>" +
                        "                <div class='author-info-right'>" +
                        "                    <span class='author-nickname'>"+ favoritesDetailList[i].collectNote.blogger.nickname +"</span>" +
                        "                    <time class='collect-time'>"+ favoritesDetailList[i].collectTime +"</time>" +
                        "                </div>" +
                        "                <img src='/"+ favoritesDetailList[i].collectNote.blogger.profilePath +"'>" +
                        "            </div>" +
                        "        </div>";
                }
                favoritesSection.innerHTML = favoritesSectionTemp;
            }else{
                favoritesSection.innerHTML = "<span class='iconfont icon-zhaobudaojieguo'></span><span id='no-content'>收藏夹没有内容哦</span>";
            }
        },
        async: true
    });
}