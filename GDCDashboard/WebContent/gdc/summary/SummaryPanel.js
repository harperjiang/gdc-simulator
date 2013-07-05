Ext.define('GDC.summary.SummaryPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'summaryPanel',
	title : 'Summary',
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
		xtype : 'panel',
		width : '100%',
		height : 45,
		styleHtmlContent : true,
		styleHtmlCls : 'banner',
		html : 'Summary',
		colspan : 2
	}, {
		xtype : 'panel',
		title : 'Overview',
		collapsible : true,
		width : 650,
		height : 300,
		items : [ {
			xtype : 'image',
			style : 'float:left;',
			src : 'image/data_center.jpg',
			width : 100,
			height : 100,
			margin : 10
		}, {
			xtype : 'container',
			id : 'summaryContent',
			style : 'float:left;',
			margin : 10,
			width : 350,
			height : 120,
			baseHtml : 'gdc/summary/summary.html'
		}, {
			xtype : 'container',
			style : 'float:left',
			width : 60,
			height : 120
		}, {
			xtype : 'gdcGaugeChart2',
			id : 'summary.usageChart',
			width : 180,
			height : 120,
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				title : 'System\n' + 'Health',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7
			} ]
		}, {
			xtype : 'gdcGaugeChart',
			id : 'summary.capacityChart',
			width : 210,
			height : 120,
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				title : 'System\nCapacity',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7
			} ]
		}, {
			xtype : 'gdcGaugeChart2',
			id : 'summary.utilizationChart',
			width : 210,
			height : 120,
			axes : [ {
				type : 'gauge',
				position : 'gauge',
				title : 'Green Power\nUtilization',
				minimum : 0,
				maximum : 100,
				steps : 4,
				margin : 7
			} ]
		} ]
	}, {
		xtype : 'panel',
		rowspan : 2,
		width : 250,
		height : 470,
		collapsible : true,
		bodyStyle : 'padding:10px;',
		title : 'Introduction',
		html : 'This is a bunch of introduction text to the system'
	}, {
		xtype : 'alertGrid',
		id : 'summary.alertGrid',
		width : 650,
		height : 150
	}, {
		xtype : 'gdcMapPanel',
		width : 920,
		height : 300,
		colspan : 2
	} ],
	listeners : {
		afterrender : function(val, eopt) {
			// Load Data
			structureService.getSystemSummary(function(summary) {
				var data = {};
				data['dcCount'] = summary.dcCount;
				data['vmRunning'] = summary.vmRunning;
				data['gpUtil'] = summary.utilization;
				data['mtbm'] = summary.mtbm;

				GDC.common.HtmlRendererInst.updateHtml(Ext
						.getCmp('summaryContent'), data);

				Ext.getCmp('summary.usageChart').store
						.loadData([ [ summary.usage ] ]);
				Ext.getCmp('summary.capacityChart').store
						.loadData([ [ summary.capacity ] ]);
				Ext.getCmp('summary.utilizationChart').store
						.loadData([ [ summary.utilization ] ]);
			});

			this.refreshAlert();
		}
	},
	refreshAlert : function() {
		structureService.getAlerts(function(alerts) {
			debugger;
			Ext.getCmp('summary.alertGrid').store.loadData(alerts);
		});
	}
});