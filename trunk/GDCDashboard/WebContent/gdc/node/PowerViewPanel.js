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
	items : [
			{
				itemId : 'banner',
				xtype : 'panel',
				width : '100%',
				height : 45,
				styleHtmlContent : true,
				styleHtmlCls : 'banner',
				html : '',
				colspan : 2
			},
			{
				xtype : 'panel',
				title : 'Information',
				itemId : 'info',
				collapsible : true,
				width : 700,
				items : [
						{
							xtype : 'image',
							style : 'float:left;',
							width : 100,
							height : 100,
							margin : 10,
							src : 'image/battery.jpg'
						},
						,
						{
							xtype : 'component',
							itemId : 'status',
							styleHtmlContent : true,
							width : 540,
							height : 120,
							style : 'float:left;' + 'margin-left:30px;'
									+ 'margin-top:10px;',
							html : "<div class='slabeled_text'>"
									+ "	<label>Status:</label>"
									+ "	<img src='image/{image}'"
									+ "		style='width: 14px; "
									+ "		height: 14px; "
									+ "		display: inline;' />" + "</div>"
									+ "<div class='slabeled_text'>"
									+ "	<label>Manufacture:</label>"
									+ "	{manufacture}" + "</div>"
									+ "<div class='slabeled_text'>"
									+ "	<label>Model:</label>" + "	{model}"
									+ "</div>" + "<div class='slabeled_text'>"
									+ "	<label>Designed Capacity:</label>"
									+ "	{designed_capacity}" + "</div>"
									+ "<div class='slabeled_text'>"
									+ "	<label>Designed Output:</label>"
									+ "	{designed_output}" + "</div>"
									+ "<div class='slabeled_text'>"
									+ "	<label>Output Voltage:</label>"
									+ "	{output_voltage}" + "</div>"
						}, {
							xtype : 'component',
							itemId : 'desc',
							width : 500,
							style : 'float:left;',
							margin : 10
						} ]
			},
			{
				xtype : 'panel',
				itemId : 'evaluation',
				title : 'Evaluation',
				collapsible : true,
				height : 450,
				width : 320,
				rowspan : 3,
				bodyStyle : 'padding:10px',
				items : [
						{
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
						},
						{
							xtype : 'component',
							styleHtmlContent : true,
							styleHtmlCls : 'desc',
							width : 270,
							padding : 10,
							html : 'This is an evaluation of the UPS.'
									+ '<ul><li><label>' + '</label></li>'
									+ '</ul>'
						} ]
			}, {
				itemId : 'performance',
				xtype : 'panel',
				title : 'Performance',
				bodyStyle : 'padding:10px',
				collapsible : true,
				tbar : [ {
					text : 'Reload',
					handler : function() {
					}
				} ],
				items : [ {
					xtype : 'gdcGaugeChart',
					itemId : 'power',
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
						title : 'Power\nPercentage'
					} ]
				}, {
					xtype : 'gdcGaugeChart',
					itemId : 'voltage',
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
						title : 'Output\nVoltage'
					} ]
				}, {
					xtype : 'gdcGaugeChart',
					itemId : 'temperature',
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
						title : 'Temperature'
					} ]
				} ]
			}, {
				xtype : 'panel',
				itemId : 'history',
				title : 'History',
				height : 340,
				width : 700,
				bodyStyle : 'padding:10px',
				collapsible : true,
				items : [ {
					xtype : 'gdcLineChart',
					width : 650,
					height : 250,
					itemId : 'powerHistory',
					store : Ext.create('Ext.data.ArrayStore', {
						// reader configs
						fields : [ 'time', {
							name : 'value',
							type : 'float'
						} ]
					}),
					axes : [ {
						type : 'Numeric',
						minimum : 0,
						position : 'left',
						fields : [ 'value' ],
						title : 'Percentage(%)',
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
					} ],
					series : [ {
						type : 'line',
						highlight : {
							size : 7,
							radius : 7
						},
						axis : 'left',
						xField : 'time',
						yField : 'value',
						markerConfig : {
							type : 'cross',
							size : 4,
							radius : 4,
							'stroke-width' : 0
						}
					} ]
				} ]
			} ],
	loadData : function(datas) {
		this.getComponent('info').getComponent('desc').html = datas.desc;

		GDC.common.HtmlRendererInst.updateHtml(this.getComponent('info')
				.getComponent('status'), datas);
		this.getComponent('performance').getComponent('power').store
				.loadData([ [ datas.s_power ] ]);
		this.getComponent('performance').getComponent('voltage').store
				.loadData([ [ datas.s_voltage ] ]);
		this.getComponent('performance').getComponent('temperature').store
				.loadData([ [ datas.s_temperature ] ]);

		var data = [ {
			time : '12:00',
			value : 240
		}, {
			time : '12:10',
			value : 500
		}, {
			time : '12:20',
			value : 350
		}, {
			time : '12:30',
			value : 900
		}, {
			time : '12:40',
			value : 275
		}, {
			time : '12:50',
			value : 275
		}, {
			time : '13:00',
			value : 423
		}, {
			time : '13:10',
			value : 765
		}, {
			time : '13:20',
			value : 324
		} ];

		this.getComponent('history').getComponent('powerHistory').store
				.loadData(data);

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
		this.getComponent('banner').html = "Battery: " + datas.name;
	}
});