Ext.define('GDC.node.MachineViewPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'nodeMachinePanel',
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
			src : 'image/server.png'
		}, {
			xtype : 'component',
			itemId : 'status',
			styleHtmlContent : true,
			width : 540,
			height : 120,
			style : 'float:left;' + 'margin-left:30px;' + 'margin-top:10px;',
			baseHtml : 'gdc/node/machine.html'
		}, {
			xtype : 'component',
			itemId : 'desc',
			width : 500,
			style : 'float:left;',
			margin : 10
		} ]
	}, {
		xtype : 'panel',
		itemId : 'techspec',
		title : 'Tech Specification',
		collapsible : true,
		height : 450,
		width : 320,
		rowspan : 3,
		bodyStyle : 'padding:10px'
	}, {
		itemId : 'performance',
		xtype : 'panel',
		title : 'Performance',
		bodyStyle : 'padding:10px',
		collapsible : true,
		items : [ {
			xtype : 'gdcGaugeChart',
			itemId : 'cpu',
			style : 'background:#fff',
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7,
				title : 'CPU\nUsage(%)'
			} ]
		}, {
			xtype : 'gdcGaugeChart',
			itemId : 'memory',
			style : 'background:#fff',
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7,
				title : 'Memory\nUsage(%)'
			} ]
		} ]
	}, {
		xtype : 'panel',
		itemId : 'history',
		title : 'History',
		height : 400,
		width : 700,
		bodyStyle : 'padding:10px',
		collapsible : true,
		items : [ {
			xtype : 'gdcLineChart',
			width : 680,
			height : 180,
			itemId : 'cpuHistory',
			axes : [ {
				type : 'Numeric',
				minimum : 0,
				position : 'left',
				fields : [ 'value' ],
				title : 'CPU Usage(%)',
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
		}, {
			xtype : 'gdcLineChart',
			width : 680,
			height : 180,
			itemId : 'memoryHistory',
			axes : [ {
				type : 'Numeric',
				minimum : 0,
				position : 'left',
				fields : [ 'value' ],
				title : 'Memory Usage(%)',
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
	} ],
	initComponent : function() {
		this.items.splice(this.items.length - 1, 0, {
			xtype : 'gdcVmGrid',
			itemId : 'vmGrid',
			height : 240
		});
		this.callParent();
	},
	loadData : function(datas) {
		this.datas = datas;
		this.getComponent('performance').getComponent('cpu').store
				.loadData([ [ datas.MACHINE_CPU ] ]);
		this.getComponent('performance').getComponent('memory').store
				.loadData([ [ datas.MACHINE_MEMORY ] ]);

		if (datas.history.MACHINE_CPU !== undefined) {
			this.getComponent('history').getComponent('cpuHistory').store
					.loadData(datas.history.MACHINE_CPU);
		}
		if (datas.history.MACHINE_MEMORY !== undefined) {
			this.getComponent('history').getComponent('memoryHistory').store
					.loadData(datas.history.MACHINE_MEMORY);
		}

		GDC.common.HtmlRendererInst.updateHtml(this.getComponent('info')
				.getComponent('status'), datas);
		this.getComponent('info').getComponent('desc').html = datas.desc;

		this.getComponent('banner').html = "Machine: " + datas.name;
		this.getComponent('techspec').html = datas.techSpec;

		this.getComponent('vmGrid').refresh();
	}
});