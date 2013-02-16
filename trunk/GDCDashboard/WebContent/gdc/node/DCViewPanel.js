function updateHtml(component, data) {
	debugger;
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
	items : [
			{
				itemId : 'banner',
				xtype : 'panel',
				width : '100%',
				height : 40,
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
							src : 'image/data_center.jpg'
						},
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
									+ "<img src='image/{image}'"
									+ "	style='width: 14 px; "
									+ "height: 14px; " + "display: inline;' />"
									+ "</div>" + "<div class='slabeled_text'>"
									+ "	<label>Power Source:</label>"
									+ "{power_source}" + "</div>"
									+ "<div class='slabeled_text'>"
									+ "	<label>Designed Capacity:</label>"
									+ "{designed_capacity}" + "</div>"
									+ "<div class='slabeled_text'>"
									+ "	<label>Peak Capacity:</label>"
									+ "{peak_capacity}" + "</div>"
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
				height : 550,
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
							html : "<div>This is an evaluation "
									+ "of the data center.</div>" + "<ul>"
									+ "<li>" + "<label>" + "Stability:"
									+ "</label>"
									+ "<div>The minimal power generation that "
									+ "can be guaranteed by the station</div>"
									+ "</li>" + "<li>" + "<label>"
									+ "Efficiency:" + "</label>"
									+ "The green power used vs."
									+ " The green power generated " + "</li>"
									+ "<li>" + "<label>" + "Capacity:"
									+ "</label>"
									+ "Power generating peak value" + "</li>"
									+ "<li>" + "<label>" + "Availability:"
									+ "</label>" + "The time that data center "
									+ "can provide service." + "</li>" + "<li>"
									+ "<label>" + "MTBM:" + "</label>"
									+ "Mean time between Migrations." + "</li>"
									+ "</ul>"
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
					xtype : 'gdcGaugeChart',
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
					itemId : 'capacityChart',
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
						title : 'Capacity'
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
						title : 'Power(MW)',
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
		debugger;
		this.getComponent('info').getComponent('desc').html = datas.desc;
		GDC.common.HtmlRendererInst.updateHtml(this.getComponent('info')
				.getComponent('status'), datas);
		this.getComponent('performance').getComponent('healthChart').store
				.loadData([ [ datas.health ] ]);
		this.getComponent('performance').getComponent('capacityChart').store
				.loadData([ [ datas.capacity ] ]);
		this.getComponent('performance').getComponent('powerChart').store
				.loadData([ [ datas.power ] ]);
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