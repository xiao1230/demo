//引入自定义组件

//定义共通方法

/**
 * 格式化url
 * 
 * @param action
 */
function buildUrl(action) {
	if (!action) {
		return action;
	}
	// console.log("action:%s，类型:%s", action, typeof (action));
	var reg = new RegExp("(^(http|ftp|https|\\${app}))\S*"); // ？
	if (reg.test(action)) {
		return action;
	} else {
//		return (app + action).replace('//', '/'); // ?
		return (action).replace('//', '/');
	}
}

function alertMsg(msg, instanse, type) {
	if (isNull(instanse)) {
		alert(msg);
		return 

	}
	//
	instanse.$message({
		showClose : false,
		message : msg,
		type : type
	});
}

/**
 * 弹出提示框
 * 
 * @param title
 *            标题
 * @param msg
 *            显示内容
 * @param instanse
 *            实例对象
 * @param comfirFunc
 *            确认回调
 * @param cancelFunc
 *            取消回调
 * @param config
 *            配置参数
 * @returns
 */
function alertConfirm(title,msg, instanse,comfirFunc,cancelFunc,config) {
	var defConfig={
	        confirmButtonText: '确定',
	        cancelButtonText: '取消',
	        type: 'info'
	};	
	defConfig=$.extend(defConfig,config);
	comfirFunc=isFunction(comfirFunc)?comfirFunc:function(){
		
	};
	cancelFunc=isFunction(cancelFunc)?cancelFunc:function(){
		
	};
	instanse.$confirm(msg, title, defConfig).then(comfirFunc).catch(cancelFunc);
}

/**
 * 加載html页面
 * 
 * @param url ：
 *            html路径
 * @param contanier ：
 *            容器
 * @param success ：
 *            成功回调函数
 * @param fial ：
 *            失败回调函数
 */
function loadHtml(url, contanier, success, fial) {
	if (isNull(url) || !isFunction(success)) {
		return;
	}
	url = buildUrl(url);
	$(contanier).load(
			url,
			function(responseTxt, statusTxt, xhr) {
				if (statusTxt == "success") {
					success(responseTxt);
				} else { // 失败回调
					alertMsg("加载模板[" + url + "]失败\nstatus=" + xhr.status + ": "
							+ xhr.statusText);
				}
			});
}

/**
 * 记录错误信息
 * 
 * @param msg
 *            错误信息
 */
function errLog(msg) {
	console.error(msg);
}

/**
 * 输出调试信息
 * 
 * @param msg
 *            调试信息
 */
function debug(msg) {
	console.log(msg);
}

/**
 * 记录日志
 * 
 * @param msg
 *            日志信息
 */
function log(msg) {
	console.log(msg);
}

function isFunction(obj) {
	return isNull(obj) || typeof obj != 'function' ? false : true;
}
/**
 * 获取对象是否为null或undefined
 * 
 * @param obj
 *            obj为null或undefined返回true，否则返回true
 */
function isNull(obj) {
	return obj == undefined || obj == null ? true : false;
}
/**
 * 获取对象是否为null或undefined或空字符串
 * 
 * @param obj
 */
function isEmpty(obj) {
	return obj == undefined || obj == null || obj == "" ? true : false;
}
/**
 * ajax 共通
 * 
 * @param url
 * @param para
 * @param success
 * @param fail
 * @param config
 */
function ajax(url, para, success, fail, config) {
	if (isNull(url)) {
		alertMsg("请求URL不能为空");
		return null;
	}
	url = buildUrl(url);

	var defConf = {
		async : true,
		data : para,
		url : url,
		dataType : "json",
		type : "GET"
	};
	defConf = $.extend(defConf, config);
	if (defConf.async) {
		// 异步请求
		defConf.success = function(data, ststus) {
			if (!isFunction(success)) {
				errLog('未注册ajax执行成功回调函数');
				return;
			}
			success(data, status);
			if (!chkResponse(data)) {
				errLog("请求URL["
						+ url
						+ "]失败"
						+ (!isNull(data) && isEmpty(data.errmsg) ? ",错误信息："
								+ data.errmsg : ""));
				return;
			}
		}, function(data, status, errorThrown) {
			if (data.status && data.status === 200) {
				document.write(data.responseText);
				return;
			}
			errLog("请示URL[" + url + "]失败," + data.status + ":"
					+ data.statusText);
		};
		$.ajax(defConf);
	} else {
		// 同步请求
		var response = $.ajax(defConf);
		if (response.status == 200)
			return response.responseJSON;
		return null;
	}

}

/**
 * 检查响应结果
 * 
 * @param data
 * @returns data==null || data.errcode==null || data.errcode != '0' return false
 */
function chkResponse(data) {
	return (isNull(data) || isNull(data.errcode) || data.errcode != '0') ? false
			: true;
}

/**
 * 加载数据字典
 * 
 * @param dTypeId
 * @param dictId
 * @returns 数据字典JSON对象
 */
function loadDict(dTypeId, dictId) {
	if (isNull(dTypeId)) {
		consloe.log("获取数据字典信息必须提供[字典类型ID]");
		return null;
	}
	var paras = {
		"dictTypeId" : dTypeId,
		"dictId" : isNull(dictId) ? "" : dictId
	}
	var response = ajax("api/dict/", paras,null,null,{async:false});	 
	if (chkResponse(response))
		return response.data[0];
	else
		alertMsg(response.errMsg);
	return null;
}


/**
 * 加载数据字典
 * 
 * @param dTypeId
 * @param dictId
 * @returns 数据字典JSON对象
 */
function loadDictArray(dTypeId) {
	var result=loadDict(dTypeId);
	var resultArray=new Array();
	each(result,function(key,value){
		resultArray.push({'key':key,'value':value});
	})
	return resultArray;
}


/**
 * 加载图片
 * 
 */
function getImageUrl() {
	// TODO 获取图片URL
}

/**
 * 提交表单
 * 
 * @param form
 *            表单对象
 * @param modal
 *            模态窗口
 * @param moduleInstance
 *            类
 * @param commitSuccess
 *            表单提交成功回调
 * @param commitFail
 *            表单提交提交失败回调 returns 提交表单成功否
 */
function commitForm(form, moduleInstance, commitSuccess, commitFail) {
	if (isNotNull(moduleInstance) && isNotNull(moduleInstance.inConfirm)
			&& moduleInstance.inConfirm) {
		alertMsg('表单正在提交，请稍等', moduleInstance, "info");
		console.log('表单正在提交，请稍等');
		return;
	}
	var $form = $(form);
	if (!$form) {
		alert("未能找到表单，请检查");
		return false;
	}
	var action = build_url($form.attr('action'));
	var method = $form.attr("method");
	if (!method)
		method = "POST";
	var commitConfig = {
		type : method
	};

	var formData = $form.serializeArray();
	var data = {};
	$.each(formData, function(index, object) {
		data[object.name] = object.value;
	});
	moduleInstance.inConfirm = true;
	// 异步提交
	ajax(action, data, function(response, status) {
		if (isFunction(commitSuccess)) {
			commitSuccess(response, status);
		}
		moduleInstance.inConfirm = false;
	}, function(response, status) {
		moduleInstance.inConfirm = false;
		if (commitFail)
			return commitFail(response, status);
		alertMsg("提交信息失败：" + response);
	}, commitConfig);
}

/**
 * 同步方式提交数据
 * 
 * @param url
 * @param method
 * @param datas
 * @returns {Boolean}
 */
function commitDataSync(url,method,para){
	if(isEmpty(url)){
		alertMsg("提交数据的URL不能为空");
		return ;
	}
	method=isEmpty(method)?"POST":method;	
	//
	var result=ajax(url, para, null, null, {async:false,type:method});
	if(chkResponse(result)){
		return true;
	}else{
		return false;
	}
}


/**
 * 遍历对象
 * 
 * @param data
 * @param eachFunc
 */
function each(data, eachFunc) {
	$.each(data, eachFunc);
}

/**
 * 判断对象是否不为undefined且不为null
 * 
 * @param obj
 *            对象
 * @returns obj==null/undefined,return false,other return true
 */
function isNotNull(obj) {
	if (obj == undefined || obj == null)
		return false;
	return true;
}

/**
 * 判断对象是否为空字符串
 * 
 * @param obj
 *            字符串对象
 * @returns 对象不为空字符串：true,其他:false
 */
function isNotBlank(obj) {
	if (!isNotNull(obj))
		return false;
	if ($.trim(obj) == '')
		return false;
	return true;
}

function build_url(url) {
	if (!url) {
		return url;
	}
	// console.log("url:%s，类型:%s", url, typeof (url));
	var reg = new RegExp("(^(http|ftp|https|\\${app}))\S*");
	if (reg.test(url)) {
		return url;
	} else {
		return (app + url).replace('//', '/');
	}
}

/**
 * 检查响应结果
 * 
 * @param data
 * 
 * @returns data==null || data.errcode==null || data.errcode != '0' return false
 */
function checkResponseData(data) {
	if (!isNotNull(data) || !isNotNull(data.errcode) || data.errcode != '0') {
		// alertMsg("请求数据失败:[" + data.errmsg + "]");
		return false;
	}
	return true;
}

/**
 * 跳转画面
 * 
 * @param url
 */
function redirectURL(url,isBlank) {
	if (!isNotBlank(url)) {
		alertMsg("url不能为空");		
		return;
	}
	url=buildUrl(url);
	if(isNull(isBlank)){
		$(window.location).attr('href', url);
	}else{
		window.open(url);
	}
}

/**
 * 根据图片文件id获取图片Url
 * 
 * @param id
 * @returns
 */
function getImgUrlById(id,success) {
	if (!isNotBlank(id)) {
		alertMsg("id不能为空");		
		return;
	}
	var para={'id':id};
	if(isFunction(success)){
		var response = ajax("api/sys/file/url",para,function(response){
			if (!chkResponse(response) || isNull(response.data) || response.data.length == 0) {
				console.error("获取图片的url失败");
				return "";
			}			 
			success(response.data[0]);	
		},null,{async:true,type:"GET"});
   }
	else{
		  console.debug("未注册成功回调方法,使用同步方式获取图片地址");
		  var response = ajax("api/sys/file/url",para,null,null,{async:true,type:"GET"});
		  if (!chkResponse(response) || isNull(response.data) || response.data.length == 0) {
				console.error("获取图片的url失败");
				return "";
			}			 
			return response.data[0];	
	}
	
}

/**
 * 将对象类型转换为JSON类型
 * @param obj 对象
 * @returns JSON对象
 */
function toJSON(obj){
	return JSON.stringify(obj);
}


/**
 * 日期格式化
 */
Date.prototype.pattern=function(fmt) {           
    var o = {           
    "M+" : this.getMonth()+1, // 月份
    "d+" : this.getDate(), // 日
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, // 小时
    "H+" : this.getHours(), // 小时
    "m+" : this.getMinutes(), // 分
    "s+" : this.getSeconds(), // 秒
    "q+" : Math.floor((this.getMonth()+3)/3), // 季度
    "S" : this.getMilliseconds() // 毫秒
    };           
    var week = {           
    "0" : "\u65e5",           
    "1" : "\u4e00",           
    "2" : "\u4e8c",           
    "3" : "\u4e09",           
    "4" : "\u56db",           
    "5" : "\u4e94",           
    "6" : "\u516d"          
    };           
    if(/(y+)/.test(fmt)){           
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));           
    }           
    if(/(E+)/.test(fmt)){           
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);           
    }           
    for(var k in o){           
        if(new RegExp("("+ k +")").test(fmt)){           
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));           
        }           
    }           
    return fmt;           
}
/**
 * 依据id获取DOM对象
 * 
 * @param id
 * @returns
 */
function getDomById(id){
 var item= $("#"+id);
 if(!isNull(item)&&!isNull(item.length)){	  
	 if(item.length==1){
		 return item[0];
	   }
	 return item;
   }
 return null;	 
}

/**
 * 删除数组元素
 */
Array.prototype.remove = function(dx) {
	if (isNaN(dx) || isNull(dx) || dx > this.length) {
		return false;
	}
	for (var i = 0, n = 0; i < this.length; i++) {
		if (this[i] != this[dx]) {
			this[n++] = this[i];
		}
	}
	this.length--;
}
/**
 * 日期格式化
 */
Date.prototype.format = function(fmt) {
	var o = {
		"M+": this.getMonth() + 1, // 月份
		"d+": this.getDate(), // 日
		"h+": this.getHours(), // 小时
		"m+": this.getMinutes(), // 分
		"s+": this.getSeconds(), // 秒
		"q+": Math.floor((this.getMonth() + 3) / 3), // 季度
		"S": this.getMilliseconds() // 毫秒
	};
	if(/(y+)/.test(fmt)) {
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for(var k in o) {
		if(new RegExp("(" + k + ")").test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
}
