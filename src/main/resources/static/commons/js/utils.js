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