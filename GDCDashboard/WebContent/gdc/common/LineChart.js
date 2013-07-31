function gdcLineChartDateConvert(value, record) {
	var date = Ext.Date.parse(value, 'M j, Y g:i:s A');
	return Ext.Date.format(date, 'H:i');
}

Ext.define('Ext.chart.theme.LineChartTheme', {
	extend : 'Ext.chart.theme.Base',
	constructor : function(config) {
		this.callParent([ Ext.apply({
			background : false,
			axis : {
				stroke : '#444',
				'stroke-width' : 3
			},
			axisLabelTop : {
				fill : '#444',
				font : '12px Arial, Helvetica, sans-serif',
				spacing : 2,
				padding : 5,
				renderer : function(v) {
					return v;
				}
			},
			axisLabelRight : {
				fill : '#444',
				font : '12px Arial, Helvetica, sans-serif',
				spacing : 2,
				padding : 5,
				renderer : function(v) {
					return v;
				}
			},
			axisLabelBottom : {
				fill : '#444',
				font : '12px Arial, Helvetica, sans-serif',
				spacing : 2,
				padding : 5,
				renderer : function(v) {
					return v;
				}
			},
			axisLabelLeft : {
				fill : '#444',
				font : '12px Arial, Helvetica, sans-serif',
				spacing : 2,
				padding : 5,
				renderer : function(v) {
					return v;
				}
			},
			axisTitleTop : {
				font : 'bold 18px Arial',
				fill : '#444'
			},
			axisTitleRight : {
				font : 'bold 18px Arial',
				fill : '#444',
				rotate : {
					x : 0,
					y : 0,
					degrees : 270
				}
			},
			axisTitleBottom : {
				font : 'bold 12px Arial',
				fill : '#444'
			},
			axisTitleLeft : {
				font : 'bold 12px Arial',
				fill : '#444',
				rotate : {
					x : 0,
					y : 0,
					degrees : 270
				}
			},
			series : {
				'stroke-width' : 0
			},
			seriesLabel : {
				font : '12px Arial',
				fill : '#333'
			},
			marker : {
				stroke : '#555',
				fill : '#000',
				radius : 3,
				size : 3
			},
			seriesThemes : [ {
				fill : "#92EC00"
			}, {
				fill : "#0772A1"
			}, {
				fill : "#FFD200"
			}, {
				fill : "#D30068"
			}, {
				fill : "#7EB12C"
			}, {
				fill : "#225E79"
			}, {
				fill : "#BFA630"
			}, {
				fill : "#9E2862"
			} ],
			markerThemes : [ {
				fill : "#115fa6",
				type : 'plus'
			}, {
				fill : "#94ae0a",
				type : 'cross'
			}, {
				fill : "#a61120",
				type : 'plus'
			} ]
		}, config) ]);
	}
});

Ext.define('GDC.common.LineChart', {
	extend : 'Ext.chart.Chart',
	style : 'background:#fff',
	animate : true,
	shadow : true,
	theme : 'LineChartTheme',
	xtype : 'gdcLineChart',
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
			size : 2,
			radius : 2,
			'stroke-width' : 0
		}
	} ],
	initComponent : function() {
		this.store = Ext.create('Ext.data.ArrayStore', {
			// reader configs
			fields : [ {
				name : 'time',
				convert : gdcLineChartDateConvert
			}, {
				name : 'value',
				type : 'float'
			} ]
		});
		this.callParent();
	}
});