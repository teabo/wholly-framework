/** 判断字符串长度 包括汉字类型长度 */
function getByteLen(val) {
        var len = 0;
        for (var i = 0; i < val.length; i++) {
             var a = val.charAt(i);
             if (a.match(/[^\x00-\xff]/ig) != null) 
            {
                len += 3;//如果你的数据库用的是UTF8编码，那么一个汉字将占用3个字节;  如果你的数据库用的是GBK编码，那么一个汉字将占用2个字节;
            }
            else
            {
                len += 1;
            }
        }
        return len;
}

/** 正则表达式去除空格 */
function trimSpace(obj){

	return obj.replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
	
}

/** 检测元素长度 */
	function isOverLength(eleName,length){
		var ele = document.getElementsByName(eleName);
		if(ele){
			var val = ele[0].value;
			if(getByteLen(val) > length){
				return true;
			}else{
				return false;
			}
		}
		return true;
	}

/** 检查元素是否包含特殊字符 */
function containsUnnormal(eleName){
//		var ele = document.getElementsByName(eleName);
//		if(ele){
//			var pat=new RegExp("[^a-zA-Z0-9\_\u4e00-\u9fa5]","i");
//			var str = ele[0].value;
//			if(pat.test(str)){   
//				return true;   
//			}
//		}
		return false;
}

/** 检查是否为空 */
function checkIsEmpty(eleName){
	var ele = document.getElementsByName(eleName);
	if(ele){
		var str = trimSpace(ele[0].value);
		if(str.length==0){  
			return true;   
		}
	}
	return false;
}