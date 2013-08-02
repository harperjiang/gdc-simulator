Ext.define('GDC.node.PowerViewPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'nodePowerPanel',
	closable : true,
	autoScroll : true,
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
		itemId : 'banner',
		xtype : 'panel',
		width : '100%',
		height : 45,
		styleHtmlContent : true,
		styleHtmlCls : 'banner',
		html : '',
		colspan : 2
	}, {
		xtype : 'panel',
		title : 'Information',
		itemId : 'info',
		collapsible : true,
		width : 700,
		items : [ {
			xtype : 'image',
			style : 'float:left;',
			width : 100,
			height : 100,
			margin : 10,
			src : 'image/power_socket.jpg'
		}, , {
			xtype : 'component',
			itemId : 'status',
			styleHtmlContent : true,
			width : 540,
			height : 120,
			style : 'float:left;' + 'margin-left:30px;' + 'margin-top:10px;',
			baseHtml : 'gdc/node/power.html'
		}, {
			xtype : 'component',
			itemId : 'desc',
			width : 500,
			style : 'float:left;',
			margin : 10
		} ]
	}, {
		xtype : 'panel',
		itemId : 'evaluation',
		title : 'Evaluation',
		collapsible : true,
		height : 450,
		width : 320,
		rowspan : 3,
		bodyStyle : 'padding:10px',
		items : [ {
			xtype : 'gdcEvalRadarChart',
			itemId : 'eval',
			width : 300,
			height : 200,
			store : Ext.create('Ext.data.ArrayStore', {
				// reader configs
				fields : [ 'name', {
					name : 'value',
					type : 'float'
				} ]
			})
		}, {
			xtype : 'component',
			styleHtmlContent : true,
			styleHtmlCls : 'desc',
			width : 270,
			padding : 10,
			autoLoad : 'gdc/node/power_eval.htm'
		} ]
	}, {
		xtype : 'panel',
		itemId : 'demo',
		width : 700,
		bodyStyle : 'borderWidth:0px',
		items : [ {
			xtype : 'image',
			src : 'image/power_demo.jpg',
		} ]
	}, {
		itemId : 'performance',
		xtype : 'panel',
		width : 700,
		title : 'Performance',
		bodyStyle : 'padding:10px',
		collapsible : true,
		items : [ {
			xtype : 'gdcGaugeChart2',
			itemId : 'inputCurrent',
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 40,
				steps : 5,
				margin : 7,
				title : 'Input\nCurrent(A)'
			} ]
		}, {
			xtype : 'gdcGaugeChart2',
			itemId : 'batteryCurrent',
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : -25,
				maximum : 25,
				steps : 5,
				margin : 7,
				title : 'Battery\nCurrent(A)'
			} ]
		}, {
			xtype : 'gdcGaugeChart',
			itemId : 'inverterCurrent',
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 15,
				steps : 3,
				margin : 7,
				title : 'Inverter\nCurrent(A)'
			} ]
		}, {
			xtype : 'gdcGaugeChart',
			itemId : 'batteryVoltage',
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 55,
				steps : 5,
				margin : 7,
				title : 'Battery\nVoltage(V)'
			} ]
		}, {
			xtype : 'gdcGaugeChart2',
			itemId : 'chargeStatus',
			style : 'background:#fff',
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 110,
				steps : 4,
				margin : 7,
				title : 'Charge\nStatus(%)'
			} ]
		} ]
	} ],
	loadData : function(datas) {
		this.getComponent('info').getComponent('desc').html = datas.desc;

		GDC.common.HtmlRendererInst.updateHtml(this.getComponent('info')
				.getComponent('status'), datas);
		this.getComponent('performance').getComponent('inputCurrent').store
				.loadData([ [ datas.POWER_INPUT_I ] ]);
		this.getComponent('performance').getComponent('batteryCurrent').store
				.loadData([ [ datas.POWER_BTY_I ] ]);
		this.getComponent('performance').getComponent('inverterCurrent').store
				.loadData([ [ datas.POWER_INVRT_I ] ]);
		this.getComponent('performance').getComponent('batteryVoltage').store
				.loadData([ [ datas.POWER_BTY_V ] ]);
		this.getComponent('performance').getComponent('chargeStatus').store
				.loadData([ [ datas.POWER_CHARGE ] ]);

		var data2 = [ {
			name : 'Capacity',
			value : 1
		}, {
			name : 'Stability',
			value : 2
		}, {
			name : 'Voltage',
			value : 3
		}, {
			name : 'Availability',
			value : 4
		} ];
		this.getComponent('evaluation').getComponent('eval').store
				.loadData(data2);
		this.getComponent('banner').html = "Power: " + datas.name;
	}
});