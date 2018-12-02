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
addLoadEvent(findMyNotes);
addLoadEvent(noteRecycleBin);
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
                if(!isnull(data)){
                    user = JSON.parse(data);

                    for(let j=0; j<myProfile.length; j++){
                        myProfile[j].src = "http://localhost:8080/user/image/profile/" + user.profilePath;
                    }
                    for(let i=0; i<loginUserNickname.length; i++){
                        loginUserNickname[i].innerHTML = user.nickname;
                    }
                    experience.innerHTML = "经验 " + user.experience;
                }
            },
            cache: true,
            async: true
        });
    }
}
/**
 * 查找“我的手记”并显示
 */
function findMyNotes() {
    var $noteSection = $("#noteSection");
    $noteSection.empty();
    $.ajax({
        type: "GET",
        url: "/user/findMyNotes",
        dataType: "json",
        success: function (data) {
            if(!isnull(data)){
                let notes = JSON.parse(data);
                console.log(notes);
                for(let i=0; i<notes.length; i++){
                    $noteSection.append("<article class='article'><h2><a class='noteTitle' href='/user/note?noteId=" + notes[i].noteId + "'>" + notes[i].noteTitle + "</a><a class='deleteNote' data-noteId='"+ notes[i].noteId +"'>删除</a></h2><p>"+"Struts2的核心部分是拦截器模块"+"</p><div class='articleInfo'><span class='content-type'>"+ notes[i].noteType.typeName +"</span><time>"+ notes[i].createTime +"</time><div class='readInfo'><span><a href='#'>评论</a>99</span><span><a href='#'>浏览</a>99</span></div></article>");
                }
                deleteNote();
            }
        },
        async: true
    });
}
/**
 * 删除“我的手记”并重新加载
 */
function deleteNote() {
    var deleteList = document.querySelectorAll(".deleteNote");
    for(let i=0; i<deleteList.length; i++){
        deleteList[i].addEventListener("click",function (e) {
            var confirmDel = confirm("确认删除该手记？（可通过回收站还原）");
            if(confirmDel){
                var noteId = deleteList[i].getAttribute("data-noteId");
                console.log("noteId:"+noteId);
                $.ajax({
                    type: "GET",
                    url: "/user/deleteNote",
                    data:{"noteId":noteId},
                    dataType: "json",
                    success: function (data) {
                        if(!isnull(data)){
                            if(data == "1"){
                                confirm("删除成功");
                                location.href = "/user/myNote";
                            }else {
                                confirm("删除失败");
                            }
                        }
                    },
                    async: true
                });
            }
        });
    }
}
/**
 * 查找“我的手记”回收站
 */
function noteRecycleBin() {
    var $noteSection = $("#noteSection");
    var note_recycle_bin = document.querySelector("#note-recycle-bin");
    note_recycle_bin.addEventListener("click",function (e) {
        $noteSection.empty();
        $.ajax({
            type: "GET",
            url: "/user/noteRecycleBin",
            dataType: "json",
            success: function (data) {
                if(!isnull(data)){
                    let notes = JSON.parse(data);
                    console.log(notes);
                    for(let i=0; i<notes.length; i++){
                        $noteSection.append("<article class='article'><h2><a class='noteTitle' href='/user/note?noteId=" + notes[i].noteId + "'>" + notes[i].noteTitle + "</a><a class='recycleNote' data-noteId='"+ notes[i].noteId +"'>还原</a><a class='completelyDel' data-noteId='"+ notes[i].noteId +"'>彻底删除</a></h2><p>"+"Struts2的核心部分是拦截器模块"+"</p><div class='articleInfo'><span class='content-type'>"+ notes[i].noteType.typeName +"</span><time>"+ notes[i].createTime +"</time><div class='readInfo'><span><a href='#'>评论</a>99</span><span><a href='#'>浏览</a>99</span></div></article>");
                    }
                    recycleNote();
                    completelyDelNote();
                }
            },
            async: true
        });
    });
}
/**
 * 还原“我的手记”并重新加载
 */
function recycleNote() {
    var recycleList = document.querySelectorAll(".recycleNote");
    for(let i=0; i<recycleList.length; i++){
        recycleList[i].addEventListener("click",function (e) {
            var confirmRecycle = confirm("确认还原该手记？");
            if(confirmRecycle){
                var noteId = recycleList[i].getAttribute("data-noteId");
                console.log("noteId:"+noteId);
                $.ajax({
                    type: "GET",
                    url: "/user/recycleNote",
                    data:{"noteId":noteId},
                    dataType: "json",
                    success: function (data) {
                        if(!isnull(data)){
                            if(data == "1"){
                                confirm("还原成功");
                                location.href = "/user/myNote";
                            }else {
                                confirm("还原失败");
                            }
                        }
                    },
                    async: true
                });
            }
        });
    }
}
/**
 * 彻底删除“我的手记”并重新加载
 */
function completelyDelNote() {
    var completelyDelList = document.querySelectorAll(".completelyDel");
    for(let i=0; i<completelyDelList.length; i++){
        completelyDelList[i].addEventListener("click",function (e) {
            var confirmDel = confirm("确认彻底删除该手记？（该操作不可恢复）");
            if(confirmDel){
                var noteId = completelyDelList[i].getAttribute("data-noteId");
                console.log("noteId:"+noteId);
                $.ajax({
                    type: "GET",
                    url: "/user/completelyDelNote",
                    data:{"noteId":noteId},
                    dataType: "json",
                    success: function (data) {
                        if(!isnull(data)){
                            if(data == "1"){
                                confirm("已彻底删除");
                                location.href = "/user/myNote";
                            }else {
                                confirm("彻底删除失败");
                            }
                        }
                    },
                    async: true
                });
            }
        });
    }
}