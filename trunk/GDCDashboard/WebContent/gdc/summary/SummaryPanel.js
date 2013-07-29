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
		html : 'GDC Architecture',
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
		rowspan : 3,
		width : 250,
		height : 640,
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
		xtype : 'mlogGrid',
		id : 'summary.migrationLogGrid',
		width : 650,
		height : 150
	}, {
		xtype : 'gdcMapPanel',
		width : 920,
		height : 300,
		colspan : 2,
		zoom : 9,
		center : {
			x : 44.800,
			y : -74.500
		},
		markers : [ {
			x : 44.844,
			y : -74.290,
			title : "Malone"
		}, {
			x : 44.923,
			y : -74.877,
			title : "Massena"
		}, {
			x : 44.671,
			y : -74.985,
			title : "Potsdam"
		} ]
	} ],
	listeners : {
		afterrender : function(val, eopt) {
			// Load Data
			this.refresh();
		}
	},
	refresh : function() {
		structureService.getSystemSummary(function(summary) {

			GDC.common.HtmlRendererInst.updateHtml(
					Ext.getCmp('summaryContent'), summary);
			Ext.getCmp('summary.usageChart').store
					.loadData([ [ summary.usage ] ]);
			Ext.getCmp('summary.capacityChart').store
					.loadData([ [ summary.capacity ] ]);
			Ext.getCmp('summary.utilizationChart').store
					.loadData([ [ summary.utilization ] ]);
		});

		this.refreshAlert();
		this.refreshMigrationLog();
	},
	refreshAlert : function() {
		structureService.getAlerts({
			callback : function(alerts) {
				Ext.getCmp('summary.alertGrid').store.loadData(alerts);
			},
			errorHandler : function(error1, error2) {
				debugger;
				Ext.Msg
						.alert("Error",
								"Unexpected error, please contact admin");
			}
		});
	},
	refreshMigrationLog : function() {
		structureService.getMigrationLogs({
			callback : function(logs) {
				debugger;
				Ext.getCmp('summary.migrationLogGrid').store.loadData(logs);
			},
			errorHandler : function(error1, error2) {
				debugger;
				Ext.Msg
						.alert("Error",
								"Unexpected error, please contact admin");
			}
		});
	}
});