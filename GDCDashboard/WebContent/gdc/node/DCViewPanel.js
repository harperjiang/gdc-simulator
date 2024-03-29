function updateHtml(component, data) {
	var html = component.html;
	if (html == undefined) {
		html = Ext.get(component.id).dom.innerHTML;
	}
	for ( var i in data) {
		html = html.replace('{' + i + '}', data[i]);
	}
	component.html = html;
}

Ext.define('GDC.node.DCViewPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'nodeDcpanel',
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
			src : 'image/data_center.jpg'
		}, {
			xtype : 'component',
			itemId : 'status',
			styleHtmlContent : true,
			width : 540,
			height : 120,
			style : 'float:left;' + 'margin-left:30px;' + 'margin-top:10px;',
			baseHtml : 'gdc/node/dc.html'
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
		height : 500,
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
			autoLoad : 'gdc/node/dc_eval.htm'
		} ]
	}, {
		itemId : 'performance',
		xtype : 'panel',
		title : 'Performance',
		bodyStyle : 'padding:10px',
		collapsible : true,
		items : [ {
			xtype : 'gdcGaugeChart2',
			itemId : 'healthChart',
			style : 'background:#fff',
			store : Ext.create('Ext.data.ArrayStore', {
				// reader configs
				fields : [ {
					name : 'data1',
					type : 'float'
				} ]
			}),
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				title : 'Health',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7
			} ]
		}, {
			xtype : 'gdcGaugeChart2',
			itemId : 'powerChart',
			style : 'background:#fff',
			store : Ext.create('Ext.data.ArrayStore', {
				// reader configs
				fields : [ {
					name : 'data1',
					type : 'float'
				} ]
			}),
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7,
				title : 'Power\nGeneration'
			} ]
		}, {
			xtype : 'gdcGaugeChart',
			itemId : 'consumptionChart',
			style : 'background:#fff',
			store : Ext.create('Ext.data.ArrayStore', {
				// reader configs
				fields : [ {
					name : 'data1',
					type : 'float'
				} ]
			}),
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7,
				title : 'Consumption'
			} ]
		} ]
	}, {
		xtype : 'panel',
		itemId : 'history',
		title : 'History',
		height : 340,
		width : 700,
		rowspan : 2,
		bodyStyle : 'padding:10px',
		collapsible : true,
		items : [ {
			xtype : 'gdcLineChart',
			width : 650,
			height : 250,
			itemId : 'powerHistory',
			axes : [ {
				type : 'Numeric',
				minimum : 0,
				position : 'left',
				fields : [ 'value' ],
				title : 'Power Consumption(kW)',
				minorTickSteps : 1,
				grid : {
					odd : {
						opacity : 1,
						fill : '#ddd',
						stroke : '#bbb',
						'stroke-width' : 0.5
					}
				}
			}, {
				type : 'Category',
				position : 'bottom',
				fields : [ 'time' ],
				title : 'Time'
			} ]
		} ]
	}, {
		xtype : 'gdcMapPanel',
		width : 320,
		height : 240
	} ],
	loadData : function(datas) {
		this.getComponent('info').getComponent('desc').html = datas.desc;
		GDC.common.HtmlRendererInst.updateHtml(this.getComponent('info')
				.getComponent('status'), datas);
		this.getComponent('performance').getComponent('healthChart').store
				.loadData([ [ datas.DC_HEALTH ] ]);
		this.getComponent('performance').getComponent('consumptionChart').store
				.loadData([ [ datas.DC_CONSUMPTION ] ]);
		this.getComponent('performance').getComponent('powerChart').store
				.loadData([ [ datas.DC_GENERATION ] ]);

		this.getComponent('history').getComponent('powerHistory').store
				.loadData(datas.history.DC_CONSUMPTION);

		var data2 = [ {
			name : 'Stablity',
			value : 1
		}, {
			name : 'Efficiency',
			value : 2
		}, {
			name : 'Capacity',
			value : 3
		}, {
			name : 'Availability',
			value : 4
		}, {
			name : 'MTBF',
			value : 3
		} ];
		this.getComponent('evaluation').getComponent('eval').store
				.loadData(data2);
		this.getComponent('banner').html = "Data Center: " + datas.name;
	}
});