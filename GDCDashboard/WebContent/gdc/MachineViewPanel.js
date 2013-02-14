Ext.define('GDC.MachineViewPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'centerpanel',
	items : [ {
		xtype : 'panel',
		width : '100%',
		height : 40,
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
		store : 'store',
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
		store : 'store',
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
		store : 'store',
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
	} ],
	loadData : function(datas) {
		var cpuStore = Ext.create('Ext.data.ArrayStore', {
			// store configs
			storeId : 'cpustore',
			// reader configs
			fields : [ {
				name : 'data1',
				type : 'float'
			} ]
		});
		cpuStore.loadData([ [ datas.cpu ] ]);
		this.items.items[1].store = cpuStore;

		var memoryStore = Ext.create('Ext.data.ArrayStore', {
			// store configs
			storeId : 'memorystore',
			// reader configs
			fields : [ {
				name : 'data1',
				type : 'float'
			} ]
		});
		memoryStore.loadData([ [ datas.memory ] ]);
		this.items.items[2].store = memoryStore;

		var batteryStore = Ext.create('Ext.data.ArrayStore', {
			// store configs
			storeId : 'batterystore',
			// reader configs
			fields : [ {
				name : 'data1',
				type : 'float'
			} ]
		});
		batteryStore.loadData([ [ datas.battery ] ]);
		this.items.items[3].store = batteryStore;
	}
});