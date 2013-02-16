Ext
		.define(
				'GDC.node.MachineViewPanel',
				{
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
						height : 40,
						styleHtmlContent : true,
						styleHtmlCls : 'banner',
						html : '',
						colspan : 2
					}, {
						xtype : 'panel',
						title : 'Information',
						itemId : 'information',
						collapsible : true,
						width : 700,
						bodyStyle : 'padding:10px',
						items : [ {
							xtype : 'image',
							style : 'float:left;',
							width : 100,
							height : 100,
							margin : 10,
							src : 'image/server.png'
						}, {
							xtype : 'component',
							itemId : 'description',
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
							} ]
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
					updateId : function(id) {
						this.id = id;
						Ext.ComponentManager.register(this);
					},
					loadData : function(datas) {
						this.getComponent('information').getComponent(
								'description').html = datas.description;
						this.getComponent('performance').getComponent('cpu').store
								.loadData([ [ datas.power ] ]);
						this.getComponent('performance').getComponent('memory').store
								.loadData([ [ datas.voltage ] ]);

						var myData = [ [ false, 'WT-001', 'Severe',
								'Data Center', 'Data Center is Down' ] ];
						myData.push([ false, 'ZZ-123', 'Warning', 'VM',
								'VM will be migrated' ]);

						this.getComponent('vmGrid').store.loadData(myData);
						this.getComponent('banner').html = "Machine: "
								+ datas.name;
						this.getComponent('techspec').html = datas.techSpec;
					}
				});