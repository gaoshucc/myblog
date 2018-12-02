$(function(){
    deleteNote();
})

function deleteNote() {
    var deleteNote = document.querySelector("#delete");
    deleteNote.addEventListener("click",function (e) {
        var ifDelete = confirm("永久删除该手记（不可恢复），是否删除？");
        if(ifDelete){
            console.log("删除成功");
            location.href = "/user/noteArticle";
        }
    })
}