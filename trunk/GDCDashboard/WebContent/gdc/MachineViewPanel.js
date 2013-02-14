var cpuStore = Ext.create('Ext.data.ArrayStore', {
	// store configs
	storeId : 'cpustore',
	// reader configs
	fields : [ {
		name : 'data1',
		type : 'float'
	} ]
});
cpuStore.loadData([ [ 30 ] ]);

var memoryStore = Ext.create('Ext.data.ArrayStore', {
	// store configs
	storeId : 'memorystore',
	// reader configs
	fields : [ {
		name : 'data1',
		type : 'float'
	} ]
});
memoryStore.loadData([ [ 50 ] ]);

var batteryStore = Ext.create('Ext.data.ArrayStore', {
	// store configs
	storeId : 'batterystore',
	// reader configs
	fields : [ {
		name : 'data1',
		type : 'float'
	} ]
});
batteryStore.loadData([ [ 20 ] ]);

Ext.define('GDC.MachineViewPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'centerpanel',
	items : [ {
		xtype : 'panel',
		width : '100%',
		height : 30,
		styleHtmlContent : true,
		styleHtmlCls : 'banner',
		html : 'Brief'
	}, {
		xtype : 'chart',
		width : 300,
		height : 200,
		style : 'background:#fff',
		animate : {
			easing : 'bounceOut',
			duration : 500
		},
		store : cpuStore,
		insetPadding : 25,
		flex : 1,
		axes : [ {
			type : 'gauge',
			position : 'gauge',
			title : 'CPU Usage',
			minimum : 0,
			maximum : 100,
			steps : 10,
			margin : 7
		} ],
		series : [ {
			type : 'gauge',
			field : 'data1',
			donut : 60,
			colorSet : [ '#11EE22', '#ddd' ]
		} ]
	}, {
		xtype : 'chart',
		width : 300,
		height : 200,
		style : 'background:#fff',
		animate : {
			easing : 'bounceOut',
			duration : 500
		},
		store : memoryStore,
		insetPadding : 25,
		flex : 1,
		axes : [ {
			type : 'gauge',
			position : 'gauge',
			minimum : 0,
			maximum : 100,
			steps : 10,
			margin : 7,
			title : 'Memory Usage'
		} ],
		series : [ {
			type : 'gauge',
			field : 'data1',
			donut : 60,
			colorSet : [ '#3AA8CB', '#ddd' ]
		} ]
	}, {
		xtype : 'chart',
		width : 300,
		height : 200,
		style : 'background:#fff',
		animate : {
			easing : 'bounceOut',
			duration : 500
		},
		store : batteryStore,
		insetPadding : 25,
		flex : 1,
		axes : [ {
			type : 'gauge',
			position : 'gauge',
			minimum : 0,
			maximum : 100,
			steps : 10,
			margin : 7,
			title : 'Battery Usage'
		} ],
		series : [ {
			type : 'gauge',
			field : 'data1',
			donut : 60,
			colorSet : [ '#DD0011', '#ddd' ]
		} ]
	} ]
});