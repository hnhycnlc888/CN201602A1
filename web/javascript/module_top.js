// JavaScript Document
var _zc={};
_zc.addEventListener=function(el,type,fn,useCapture){   //事件监听方法
	if(window.addEventListener){
		  el.addEventListener(type,function(){
			  fn.apply(el,arguments);  //始终将this指向当前DOM元素
			},useCapture);   
		  }else if(window.attachEvent){
			  el.attachEvent('on'+type,function(){
				  fn.apply(el,arguments);    //始终将this指向当前DOM元素
				  });
		   }
 }	 
	
	
	 
_zc.stopBubble=function(e){    //取消冒泡
	if(e&&e.stopPropagation){
		//阻止W3C冒泡
		e.stopPropagation();
		}else{
	    //阻止IE冒泡	
		window.event.cancelBubble=true;
		}
 }
 
 
 
 
_zc.stopDefault=function(e){    //阻止默认行为
	if(e&&e.preventDefault){
		//阻止W3C动作
		e.preventDefault();
		}else{
		//阻止IE默认动作	
		window.event.returnValue=true;
		return false;	
	   }
	}  
_zc.hasClass=function(name,type){  //通过ClassName获取样式 name代表className type代表TagName
	var arr=[];
	//var reg=new RegExp('(^|\\s)'+name+'(\\s|$)');
	var reg=new RegExp(name);
	var el=document.getElementsByTagName(type||'*');
    for(var i=0;i<el.length;i++){
		 if(reg.test(el[i].className)) arr.push(el[i]);
		}
	return arr;		
} 
_zc.getStyle=function(el,name){   //取出最终样式 el代表obj,name范例:borderLeftWidth
   
   if(el.style[name]) return el.style[name];
   
   else if(el.currentStyle) return el.currentStyle[name];
   
   else if(document.defaultView&&document.defaultView.getComputedStyle){
	  name=name.replace(/([A-Z])/g,'-$1');
	  name=name.toLowerCase();
	  var s=document.defaultView.getComputedStyle(el,null);
	  return s&&s.getPropertyValue(name); 
	   }else{
		return null;   
		}	
 } 
_zc.move=function(obj,json,endFn){  //缓冲运动  例子：_zc.move(obj,{'left':'100','opacity':'50'},callBack)
  		clearInterval(obj.timer);
		
		obj.timer = setInterval(function(){
			
			var bBtn = true;
			
			for(var attr in json){
				
				var iCur = 0;
			
				if(attr == 'opacity'){
					if(Math.round(parseFloat(_zc.getStyle(obj,attr))*100)==0){
					iCur = Math.round(parseFloat(_zc.getStyle(obj,attr))*100);
					
					}
					else{
						iCur = Math.round(parseFloat(_zc.getStyle(obj,attr))*100) || 100;
					}	
				}
				else{
					iCur = parseInt(_zc.getStyle(obj,attr)) || 0;
				}
				
				var iSpeed = (json[attr] - iCur)/8;
			        iSpeed = iSpeed >0 ? Math.ceil(iSpeed) : Math.floor(iSpeed);
				if(iCur!=json[attr]){
					bBtn = false;
				}
				
				if(attr == 'opacity'){
					obj.style.filter = 'alpha(opacity=' +(iCur + iSpeed)+ ')';
					obj.style.opacity = (iCur + iSpeed)/100;
					
				}
				else{
					obj.style[attr] = iCur + iSpeed + 'px';
				}
				
				
			}
			
			if(bBtn){
				clearInterval(obj.timer);
				
				if(endFn){
					endFn.call(obj);
					//endFn.apply(obj,arguments)
				}
			}
			
		},30);	
}

_zc.domReady=function(fn){   //判断DOM元素是否加载完毕  例子：_zc.domReady(function(){obj.style.color='red'});

  if(_zc.domReady.done) return fn();
  
  if(_zc.domReady.timer){
	 _zc.domReady.ready.push(fn); 
	  }else{
	   _zc.addEventListener(window,'load',isDomReady,false);
	   _zc.domReady.ready=[fn];
	   _zc.domReady.timer=setInterval(_zc.domReady,13);	  	
	 }
	 
  
  function isDomReady(){
	  
	 if(_zc.domReady.done) return false;
	 
	 if(document&&document.getElementsByName&&document.getElementById&&document.body){
		 clearInterval(_zc.domReady.timer);
		 _zc.domReady.timer=null;
		 
		 for(var i=0;i<_zc.domReady.ready.length;i++){
			 _zc.domReady.ready[i]();
			 _zc.domReady.ready[i]=null;
			 _zc.domReady.done=true;
			 }
		 }  
	  }	    	
}

_zc.inputFocus=function(obj,txt){  //input获取焦点,obj代表input本身,txt代表input的val;
		if(arguments.length==2){
		    if (obj.value == txt) {
		        obj.value = '';
		    } 
			obj.style.backgroundColor = "#fffee9";
			obj.style.color = "#0e8bcd";
		}
	}
	
_zc.inputBlur=function(obj,txt){ //input失去焦点,obj代表input本身,txt代表input的val;
		if(arguments.length==2){
		    if (obj.value == '') {
		        obj.value = txt;
		        obj.style.color = "#b5b5b5";

		    } else {
		        obj.style.color = "#eb6877";
		    }
			obj.style.backgroundColor = "white"; 
		}		
} 
_zc.conNum=function(str,num){   //控制字符的长度
		var sub_length = num ;
		var temp1 = str.replace(/[^\x00-\xff]/g,"**");//精髓   
		var temp2 = temp1.substring(0,sub_length);   
		//找出有多少个*   
		var x_length = temp2.split("\*").length - 1 ;   
		var hanzi_num = x_length /2 ;   
		sub_length = sub_length - hanzi_num ;//实际需要sub的长度是总长度-汉字长度   
		var res = str.substring(0,sub_length);   
		if(sub_length < str.length ){   
			var end  =res+"……" ;   
		}else{    
			var end  = res ;   
		}   
		return end ; 
    }
_zc.passWordFocus=function(obj){
	  var lable=document.getElementById('labelpwd');
	      if(!obj.value){
	          lable.innerHTML = ''; 
	      }
	      obj.style.backgroundColor = "#fffee9";
	      obj.style.color = "#0e8bcd";
	}
	
_zc.passWordBlur=function(obj){
	  var lable=document.getElementById('labelpwd'); 
	  if (obj.value == '') {
	      lable.innerHTML = '密码';
	      obj.style.color = "#b5b5b5";
	  } else {
	      obj.style.color = "#eb6877";
	  }
	   obj.style.backgroundColor = "white";
	   
	}			
_zc.titleCheck = function (obj,id,tabName) {
    var obj2 = document.getElementById(id);

    if (id != "title_msg") {
        document.getElementById("title_bottom_style").style.left = "205px";
    } else {
        document.getElementById("title_bottom_style").style.left = "50px";
    }
    document.getElementById("account_tab").style.display = "none";
    document.getElementById("msg_tab").style.display = "none";
    document.getElementById(tabName).style.display = "block";
    obj.style.color = "#eb6877";
    obj2.style.color = "#b5b5b5";

    var objClassStr = obj.getAttribute("class").split("title_unChecked")[0] + " title_checked";
    obj.setAttribute("class", objClassStr);

    var objClassStr = obj2.getAttribute("class").split("title_checked")[0] + " title_unChecked";
    obj2.setAttribute("class", objClassStr);
}
 
