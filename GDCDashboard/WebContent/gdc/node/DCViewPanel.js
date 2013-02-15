Ext.define('GDC.node.DCViewPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'nodeDcpanel',
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
		html : 'Data Center: ' + "<span id='nodeDc.title.{#id}'/>",
		colspan : 2
	}, {
		xtype : 'container',
		items : [ {
			xtype : 'chart',
			width : 300,
			height : 200,
			id : 'nodeDc.cpuChart.{#id}',
			style : 'background:#fff',
			animate : {
				easing : 'bounceOut',
				duration : 500
			},
			store : Ext.create('Ext.data.ArrayStore', {
				// store configs
				storeId : 'nodeDc.cpuStore.{#id}',
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
			id : 'nodeDc.memChart.{#id}',
			style : 'background:#fff',
			animate : {
				easing : 'bounceOut',
				duration : 500
			},
			store : Ext.create('Ext.data.ArrayStore', {
				// store configs
				storeId : 'nodeDc.memStore.{#id}',
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
			id : 'nodeDc.batteryChart.{#id}',
			style : 'background:#fff',
			animate : {
				easing : 'bounceOut',
				duration : 500
			},
			store : Ext.create('Ext.data.ArrayStore', {
				// store configs
				storeId : 'nodeDc.batteryStore.{#id}',
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
	updateId : function(id) {
		this.id = id;
		var ele0 = this.items.items[0];
		ele0.html = ele0.html.replace("{#id}", id);
		var ele1 = this.items.items[1];
		ele1.items.items[0].id = ele1.items.items[0].id.replace("{#id}", id);
		ele1.items.items[0].store.storeId = ele1.items.items[0].store.storeId
				.replace("{#id}", id);
		ele1.items.items[1].id = ele1.items.items[1].id.replace("{#id}", id);
		ele1.items.items[1].store.storeId = ele1.items.items[1].store.storeId
				.replace("{#id}", id);
		ele1.items.items[2].id = ele1.items.items[2].id.replace("{#id}", id);
		ele1.items.items[2].store.storeId = ele1.items.items[2].store.storeId
				.replace("{#id}", id);

		Ext.ComponentManager.register(this);
	},
	loadData : function(datas) {
		// Ext.getCmp('nodeDc.cpuChart').store
		// .loadData([ [ datas.cpu ] ]);
		//
		// Ext.getCmp('nodeDc.memChart').store
		// .loadData([ [ datas.memory ] ]);
		//
		// Ext.getCmp('nodeDc.batteryChart').store
		// .loadData([ [ datas.battery ] ]);
	},
	listeners : {
		afterrender : function(val, opt) {
			//			
			// Ext.get('nodeDc.title').setHTML("Data Center 1");
		}
	}
});