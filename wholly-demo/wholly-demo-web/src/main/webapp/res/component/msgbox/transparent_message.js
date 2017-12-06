
/**
 * 提示信息.
 *
 * User: chris hsu
 * Date: 2015-01-28
 *
 * @author chris hsu
 */
var TransparentMenu = function(id, options){
	this.options = {};
	this.id = id;
	this.element = $("#"+this.id);
	this.IsShow = false;
	this.initialize(options);
};

TransparentMenu.DefaultOptions = {
  top: null,
  left: null,
  showMode: "onload",
  hideMode: "timeout",
  hideDelay: 2
};

TransparentMenu.instances = {};

TransparentMenu.hide = function(id) {
  if (TransparentMenu.instances[id])
    TransparentMenu.instances[id].hide();
};

TransparentMenu.show = function(id) {
  if (TransparentMenu.instances[id]) 
    TransparentMenu.instances[id].show();
  else 
    new TransparentMenu(id,  arguments[1]);
};

Function.prototype.bindAsEventListener = function(object) { 
	var method = this; 
	return function(event) { 
		method.call(object, event || window.event); 
	} ;
};

TransparentMenu.prototype = {
	initialize: function() {
	  this.options  = $.extend({}, TransparentMenu.DefaultOptions, arguments[0] || {});   
	  TransparentMenu.instances[this.id] = this;
  },
  
  show: function() {
	this.element.show();
	setTimeout(this.hide.bindAsEventListener(this), this.options.hideDelay*1000);
	if (this.options.hideMode == "click" || this.options.hideMode == "mousemove") {
	  this.bindEvent = this._startHideEvent.bindAsEventListener(this);
	  $("body").bind(this.options.hideMode, this.bindEvent);      
	}  
  },
    
  hide: function() {
  	this.element.hide();
  	this.IsShow = false;
  },
  
  _startHideEvent: function() {
	  if (this.IsShow == true){
		  if (this.options.hideMode == "click" || this.options.hideMode == "mousemove") {
				$("body").unbind(this.options.hideMode, this.bindEvent);      
			} 
		    setTimeout(this.hide.bindAsEventListener(this), 100);
	  } else {
		  this.IsShow = true;
	  }
  }
};
