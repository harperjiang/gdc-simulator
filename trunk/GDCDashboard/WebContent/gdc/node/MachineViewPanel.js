Ext.define('GDC.node.MachineViewPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'nodeMachinePanel',
	closable : true,
	layout : {
		type : 'table',
		columns : 2,
		tdAttrs : {
			valign : 'top',
			style : {
				padding : '10 10 10 10'
			}
		}
	},
	items : [ {
		xtype : 'panel',
		width : '100%',
		height : 40,
		styleHtmlContent : true,
		styleHtmlCls : 'banner',
		html : 'Data Center',
		colspan : 2
	}, {
		xtype : 'container',
		items : [ {
			xtype : 'chart',
			id : 'machine.cpuChart',
			width : 300,
			height : 200,
			style : 'background:#fff',
			animate : {
				easing : 'bounceOut',
				duration : 500
			},
			store : Ext.create('Ext.data.ArrayStore', {
				// store configs
				storeId : 'cpustore',
				// reader configs
				fields : [ {
					name : 'data1',
					type : 'float'
				} ]
			}),
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
			id : 'machine.memChart',
			style : 'background:#fff',
			animate : {
				easing : 'bounceOut',
				duration : 500
			},
			store : Ext.create('Ext.data.ArrayStore', {
				// store configs
				storeId : 'memstore',
				// reader configs
				fields : [ {
					name : 'data1',
					type : 'float'
				} ]
			}),
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
			id : 'machine.batteryChart',
			style : 'background:#fff',
			animate : {
				easing : 'bounceOut',
				duration : 500
			},
			store : Ext.create('Ext.data.ArrayStore', {
				// store configs
				storeId : 'batterystore',
				// reader configs
				fields : [ {
					name : 'data1',
					type : 'float'
				} ]
			}),
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
	} ],
	loadData : function(datas) {
		Ext.getCmp('machine.cpuChart').store.loadData([ [ datas.cpu ] ]);

		Ext.getCmp('machine.memChart').store.loadData([ [ datas.memory ] ]);

		Ext.getCmp('machine.batteryChart').store
				.loadData([ [ datas.battery ] ]);
	}
});