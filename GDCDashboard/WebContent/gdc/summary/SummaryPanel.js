Ext.define('GDC.summary.SummaryPanel', {
	extend : 'Ext.panel.Panel',
	xtype : 'summaryPanel',
	title : 'Summary',
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
	items : [
			{
				xtype : 'panel',
				width : '100%',
				height : 40,
				styleHtmlContent : true,
				styleHtmlCls : 'banner',
				html : 'Summary',
				colspan : 2
			},
			{
				xtype : 'panel',
				title : 'Overview',
				collapsible : true,
				width : 550,
				height : 300,
				items : [
						{
							xtype : 'image',
							style : 'float:left;',
							src : 'image/data_center.jpg',
							width : 100,
							height : 100,
							margin : 10
						},
						{
							xtype : 'container',
							style : 'float:left;',
							margin : 10,
							width : 350,
							height : 120,
							html : "<div class='labeled_text'>" + "<label>"
									+ "Green Data Center Under Managed:"
									+ "</label>"
									+ "<a id='summary.gdcCount' href='#'></a>"
									+ "</div>" + "<div class='labeled_text'>"
									+ "<label>" + "Green Data Center Running:"
									+ "</label>"
									+ "<span id='summary.gdcRunning'/>"
									+ "</div>" + "<div class='labeled_text'>"
									+ "<label>" + "Green Power Utilization:"
									+ "</label>"
									+ "<span id='summary.gpUtil'/>" + "</div>"
									+ "<div class='labeled_text'>" + "<label>"
									+ "Mean time between migration:"
									+ "</label>" + "<span id='summary.mtbm'/>"
									+ "</div>"
						}, {
							xtype : 'container',
							style : 'float:left',
							width : 60,
							height : 120
						}, {
							xtype : 'gdcGaugeChart',
							id : 'summary.usageChart',
							width : 210,
							height : 120,
							store : Ext.create('Ext.data.ArrayStore', {
								// store configs
								storeId : 'summary.usageStore',
								// reader configs
								fields : [ {
									name : 'data1',
									type : 'float'
								} ]
							}),
							axes : [ {
								type : 'gauge',
								position : 'gauge',
								title : 'System\n' + 'Health',
								minimum : 0,
								maximum : 100,
								steps : 4,
								margin : 7
							} ],
						}, {
							xtype : 'gdcGaugeChart',
							id : 'summary.capacityChart',
							width : 210,
							height : 120,
							store : Ext.create('Ext.data.ArrayStore', {
								// store configs
								storeId : 'summary.capacityStore',
								// reader configs
								fields : [ {
									name : 'data1',
									type : 'float'
								} ]
							}),
							axes : [ {
								type : 'gauge',
								position : 'gauge',
								title : 'System\nCapacity',
								minimum : 0,
								maximum : 60,
								steps : 4,
								margin : 7
							} ]
						} ]
			}, {
				xtype : 'panel',
				rowspan : 2,
				width : 350,
				height : 470,
				collapsible : true,
				bodyStyle : 'padding:10px;',
				title : 'Introduction',
				html : 'This is a bunch of introduction text to the system'
			}, {
				xtype : 'alertGrid',
				width : 550,
				height : 150
			}, ],
	listeners : {
		afterrender : function(val, eopt) {
			Ext.get("summary.gdcCount").setHTML("4");
			Ext.get("summary.gdcRunning").setHTML("2");
			Ext.get("summary.gpUtil").setHTML("30" + "%");
			Ext.get("summary.mtbm").setHTML("250" + "min");

			Ext.getCmp('summary.usageChart').store.loadData([ [ 19 ] ]);
			Ext.getCmp('summary.capacityChart').store.loadData([ [ 40 ] ]);
		}
	}
});