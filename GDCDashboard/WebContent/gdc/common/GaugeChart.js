Ext.define("GDC.common.GaugeChart", {
	xtype : 'gdcGaugeChart',
	extend : 'Ext.chart.Chart',
	style : 'background:#fff',
	animate : {
		easing : 'bounceOut',
		duration : 500
	},
	width : 210,
	height : 120,
	insetPadding : 25,
	flex : 1,
	axes : [ {
		type : 'gauge',
		position : 'gauge',
		minimum : 0,
		maximum : 100,
		steps : 10,
		margin : 7,
		title : 'Usage'
	} ],
	biggerBetter : false,
	series : [ {
		type : 'gauge',
		field : 'data1',
		donut : 60,
		colorList : [ '#00ff00', '#fbf80b', '#fd8c12', '#f65007', '#f81409' ],
		colorList2 : [ '#f81409', '#f65007', '#fd8c12', '#fbf80b', '#00ff00' ],
		renderer : function(sprite, storeItem, attr, i, store) {
			var data = storeItem.raw[0];
			var colors = this.biggerBetter ? this.colorList2 : this.colorList;
			return Ext.apply(attr, {
				fill : i === 0 ? colors[Math.floor(data / 20)] : '#ddd'
			});
		}
	} ]
}, function(clazz) {
//	debugger;
//	clazz.series.biggerBetter = clazz.biggerBetter;
});