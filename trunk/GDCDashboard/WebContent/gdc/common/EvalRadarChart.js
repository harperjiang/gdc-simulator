Ext.define('GDC.common.EvalRadarChart', {
	extend : 'Ext.chart.Chart',
	xtype : 'gdcEvalRadarChart',
	style : 'background:#fff',
	theme : 'Category2',
	insetPadding : 20,
	animate : true,
	axes : [ {
		type : 'Radial',
		position : 'radial',
		minimum : 0,
		maximum : 5,
		steps : 5,
		label : {
			display : false
		}
	} ],
	series : [ {
		showInLegend : false,
		type : 'radar',
		xField : 'name',
		yField : 'value',
		style : {
			opacity : 0.8
		},
		colorSets : [ '#0dfd38' ]
	} ]
});