/*滑动控件*/
var Swipe = function(a, b) {
	return a ? (this.options = b || {}, 
				this.index = this.options.startSlide || 0, 
				this.speed = this.options.speed || 300, 
				this.callback = this.options.callback || function() {}, 
				this.delay = this.options.auto || 0, 
				this.container = a, 
				this.element = this.container.children[0], 
				this.container.style.overflow = "hidden", 
				this.element.style.listStyle = "none", 
				this.element.style.margin = 0, 
				this.setup(), 
				this.begin(), 
				this.element.addEventListener && (this.element.addEventListener("touchstart", this, !1), 
				this.element.addEventListener("touchmove", this, !1), 
				this.element.addEventListener("touchend", this, !1), 
				this.element.addEventListener("touchcancel", this, !1), 
				this.element.addEventListener("webkitTransitionEnd", this, !1), 
				this.element.addEventListener("msTransitionEnd", this, !1), 
				this.element.addEventListener("oTransitionEnd", this, !1), 
				this.element.addEventListener("transitionend", this, !1), 
				window.addEventListener("resize", this, !1)), void 0) : null
}
Swipe.prototype = {
	setup: function() {
		if (this.slides = this.element.children, this.length = this.slides.length, 2 > this.length) return null;
		if (this.width = Math.ceil("getBoundingClientRect" in this.container ? this.container.getBoundingClientRect().width : this.container.offsetWidth), 0 === this.width && "function" == typeof window.getComputedStyle && (this.width = window.getComputedStyle(this.container, null).width.replace("px", "")), !this.width) return null;
		var a = this.container.style.visibility;
		this.container.style.visibility = "hidden", 
		this.element.style.width = Math.ceil(this.slides.length * this.width) + "px";
		for (var b = this.slides.length; b--;) {
			var c = this.slides[b];
			c.style.width = this.width + "px", c.style.display = "table-cell", c.style.verticalAlign = "top"
		}
		this.slide(this.index, 0), 
		this.container.style.visibility = a
	},
	slide: function(a, b) {
		var c = this.element.style;
		void 0 == b && (b = this.speed), 
		c.webkitTransitionDuration = c.MozTransitionDuration = c.msTransitionDuration = c.OTransitionDuration = c.transitionDuration = b + "ms", 
		c.MozTransform = c.webkitTransform = "translate3d(" + -(a * this.width) + "px,0,0)", 
		c.msTransform = c.OTransform = "translateX(" + -(a * this.width) + "px)", 
		this.index = a
	},
	getPos: function() {
		return this.index
	},
	prev: function(a) {
		this.delay = a || 0, clearTimeout(this.interval), this.index ? this.slide(this.index - 1, this.speed) : this.slide(this.length - 1, this.speed)
	},
	next: function(a) {
		this.delay = a || 0, clearTimeout(this.interval), this.index < this.length - 1 ? this.slide(this.index + 1, this.speed) : this.slide(0, this.speed)
	},
	begin: function() {
		var a = this;
		this.interval = this.delay ? setTimeout(function() {
			a.next(a.delay)
		}, this.delay) : 0
	},
	stop: function() {
		this.delay = 0, 
		clearTimeout(this.interval)
	},
	resume: function() {
		this.delay = this.options.auto || 0,
		this.begin()
	},
	handleEvent: function(a) {
		switch (a.type) {
			case "touchstart":
				this.onTouchStart(a);
				break;
			case "touchmove":
				this.onTouchMove(a);
				break;
			case "touchcancel":
			case "touchend":
				this.onTouchEnd(a);
				break;
			case "webkitTransitionEnd":
			case "msTransitionEnd":
			case "oTransitionEnd":
			case "transitionend":
				this.transitionEnd(a);
				break;
			case "resize":
				this.setup()
		}
	},
	transitionEnd: function(a) {
		this.delay && this.begin(), 
		this.callback(a, this.index, this.slides[this.index])
	},
	onTouchStart: function(a) {
		this.start = {
			pageX: a.touches[0].pageX,
			pageY: a.touches[0].pageY,
			time: Number(new Date)
		},
		this.isScrolling = void 0, 
		this.deltaX = 0, 
		this.element.style.MozTransitionDuration = this.element.style.webkitTransitionDuration = 0, 
		a.stopPropagation()
	},
	onTouchMove: function(a) {
		a.touches.length > 1 || a.scale && 1 !== a.scale || (this.deltaX = a.touches[0].pageX - this.start.pageX,
		this.isScrolling === void 0 && (this.isScrolling = !! (this.isScrolling || Math.abs(this.deltaX) < Math.abs(a.touches[0].pageY - this.start.pageY))), 
		this.isScrolling || (a.preventDefault(), clearTimeout(this.interval), 
		this.deltaX = this.deltaX / (!this.index && this.deltaX > 0 || this.index == this.length - 1 && 0 > this.deltaX ? Math.abs(this.deltaX) / this.width + 1 : 1), 
		this.element.style.MozTransform = this.element.style.webkitTransform = "translate3d(" + (this.deltaX - this.index * this.width) + "px,0,0)", a.stopPropagation()))
	},
	onTouchEnd: function(a) {
		var b = 250 > Number(new Date) - this.start.time && Math.abs(this.deltaX) > 20 || Math.abs(this.deltaX) > this.width / 2,
			c = !this.index && this.deltaX > 0 || this.index == this.length - 1 && 0 > this.deltaX;
		this.isScrolling || this.slide(this.index + (b && !c ? 0 > this.deltaX ? 1 : -1 : 0), this.speed), 
		a.stopPropagation()
	}
};