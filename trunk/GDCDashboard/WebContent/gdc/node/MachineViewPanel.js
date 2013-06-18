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
					+ "	<label>Architecture:</label>"
					+ "	{arch}" + "</div>"
					+ "<div class='slabeled_text'>"
					+ "	<label>CPU:</label>" + "	{cpu}"
					+ "</div>" + "<div class='slabeled_text'>"
					+ "	<label>Memory:</label>" + "	{memory}"
					+ "</div>" + "<div class='slabeled_text'>"
					+ "  <label>IP:</label>" + "  {ip}"
					+ "</div>"
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
				title : 'CPU Usage'
			} ],
			initComponent: function() {
				if(this.series === undefined)
					alert('Undefined');
				else
					this.series[0].biggerBetter = true;
			}
		}, {
			xtype : 'gdcGaugeChart',
			itemId : 'memory',
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
				title : 'Memory Usage'
			} ]
		} ]
	}, {
		xtype : 'gdcVmGrid',
		itemId : 'vmGrid',
		height : 240
	} ],
	loadData : function(datas) {
		this.datas = datas;
		this.getComponent('performance').getComponent('cpu').store
				.loadData([ [ datas.s_cpu ] ]);
		this.getComponent('performance').getComponent('memory').store
				.loadData([ [ datas.s_memory ] ]);
		GDC.common.HtmlRendererInst.updateHtml(this.getComponent('info')
				.getComponent('status'), datas);
		this.getComponent('info').getComponent('desc').html = datas.desc;

		this.getComponent('banner').html = "Machine: " + datas.name;
		this.getComponent('techspec').html = datas.techSpec;

		this.getComponent('vmGrid').refresh();
	}
});