Ext.define('GDC.node.UPSViewPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'nodeUpsPanel',
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
			src : 'image/ups.jpg'
		}, , {
			xtype : 'component',
			itemId : 'status',
			styleHtmlContent : true,
			width : 540,
			height : 120,
			style : 'float:left;' + 'margin-left:30px;' + 'margin-top:10px;',
			baseHtml : 'gdc/node/ups.html'
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
			autoLoad : 'gdc/node/ups_eval.htm'
		} ]
	}, {
		itemId : 'performance',
		xtype : 'panel',
		title : 'Performance',
		bodyStyle : 'padding:10px',
		collapsible : true,
		items : [ {
			xtype : 'gdcGaugeChart2',
			itemId : 'power',
			style : 'background:#fff',
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7,
				title : 'Power\nPercentage'
			} ]
		}, {
			xtype : 'gdcGaugeChart2',
			itemId : 'voltage',
			style : 'background:#fff',
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7,
				title : 'Output\nVoltage(V)'
			} ]
		}, {
			xtype : 'gdcGaugeChart',
			itemId : 'temperature',
			style : 'background:#fff',
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7,
				title : 'Temperature(°F)'
			} ]
		} ]
	} ],
	loadData : function(datas) {
		this.getComponent('info').getComponent('desc').html = datas.desc;

		GDC.common.HtmlRendererInst.updateHtml(this.getComponent('info')
				.getComponent('status'), datas);
		this.getComponent('performance').getComponent('power').store
				.loadData([ [ datas.UPS_POWER ] ]);
		this.getComponent('performance').getComponent('voltage').store
				.loadData([ [ datas.UPS_VOLTAGE ] ]);
		this.getComponent('performance').getComponent('temperature').store
				.loadData([ [ datas.UPS_TEMPERATURE ] ]);

		var data2 = [ {
			name : 'Capacity',
			value : 1
		}, {
			name : 'Ampere',
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
		this.getComponent('banner').html = "UPS: " + datas.name;
	}
});