Ext.define('GDC.node.VMModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'select',
		type : 'bool'
	}, {
		name : 'name'
	}, {
		name : 'status'
	}, {
		name : 'desc'
	} ],
	idProperty : 'name'
});

Ext.define('GDC.node.VMGrid', {
	extend : 'Ext.grid.Panel',
	title : 'Virtual Machines',
	xtype : 'gdcVmGrid',
	listeners : {
		'selectionchange' : function(view, records) {
			var running = false;
			var shutdown = false;
			if (records.length != 0) {
				var data = records[0].data;
				if (!(undefined == data || null == data)) {
					running = (data.status == 'running');
					shutdown = (data.status == 'shut off');		
				}
			}
			this.down('#migrateButton').setDisabled(!running);
			this.down('#restartButton').setDisabled(!running);
			this.down('#startButton').setDisabled(!shutdown);
			this.down('#stopButton').setDisabled(!running);
		}
	},
	tbar : [ {
		itemId : 'migrateButton',
		text : 'Migrate VM',
		handler : function() {
			var gridPanel = this.up('gdcVmGrid');
			var model = gridPanel.getSelectionModel().getSelection()[0];
			var vmName = model.get('name');
			var machinePanel = this.up('nodeMachinePanel');
			var srcId = machinePanel.datas.id;
			var mcWindow = Ext.create('GDC.common.MachineChooseWindow');
			gridPanel.getSelectionModel().deselectAll();
			mcWindow.show();
			mcWindow.on('hide', function() {
				debugger;
				var destId = this.machineId;
				if (destId == undefined || destId == null || srcId == destId)
					return;
				Ext.MessageBox.show({
					msg : 'Scheduling migration...',
					width : 300,
					wait : true,
					waitConfig : {
						interval : 200
					}
				});
				vmService.migrate(vmName, srcId, destId, {
					callback : function() {
						Ext.MessageBox.hide();
						Ext.Msg
						.alert(
								'Migration in progress',
								'Migration may take'+ 
								'some time, please wait');
					},
					errorHandler : function() {

					}
				});
			});
		},
		disabled : true
	}, {
		text : 'Create New VM',
		handler : function() {
			Ext.Msg.alert('Warning', 'Not implemented yet');
		}
	}, {
		text : 'Refresh',
		handler : function() {
			this.up('gdcVmGrid').refresh();
		}
	}, {
		text : 'Start',
		itemId : 'startButton',
		icon : 'resource/icon/start_small.png',
		handler : function() {
			this.up('gdcVmGrid').operate('START');
		},
		disabled : true
	}, {
		text : 'Stop',
		itemId : 'stopButton',
		icon : 'resource/icon/stop_small.png',
		handler : function() {
			this.up('gdcVmGrid').operate('STOP');
		},
		disabled : true
	}, {
		text : 'Restart',
		icon : 'resource/icon/reset_small.png',
		itemId : 'restartButton',
		handler : function() {
			this.up('gdcVmGrid').operate('RESTART');
		},
		disabled : true
	} ],
	collapsible : true,
	multiSelect : false, // This seems not working?
	columns : [ {
		text : 'Name',
		width : 120,
		sortable : false,
		dataIndex : 'name'
	}, {
		text : 'Status',
		width : 90,
		sortable : false,
		dataIndex : 'status'
	}, {
		text : 'Description',
		flex : 1,
		sortable : false,
		dataIndex : 'desc'
	} ],
	viewConfig : {
		stripeRows : true,
		enableTextSelection : true
	},
	initComponent : function() {
		this.store = Ext.create('Ext.data.ArrayStore', {
			model : 'GDC.node.VMModel'
		});
		this.selModel = Ext.create('Ext.selection.CheckboxModel');
		this.callParent();
	},
	refresh : function() {
		// Do not refresh if under selection
		if (this.getSelectionModel().getSelection().length != 0)
			return;
		var machinePanel = this.up('nodeMachinePanel');
		var srcId = machinePanel.datas.id;
		vmService.list(srcId, {
			callback:function(vmBean) {
				debugger;
				var ownerId = vmBean.ownerId;
				var vmGrid = Ext.getCmp('tab' + ownerId).down('gdcVmGrid');
				var dataArray = new Array();
				for ( var i = 0; i < vmBean.vms.length; i++) {
					dataArray[i] = new Array();
					dataArray[i][0] = false;
					dataArray[i][1] = vmBean.vms[i].name;
					dataArray[i][2] = vmBean.vms[i].attributes.status;
					dataArray[i][3] = ' ';
				}
				vmGrid.store.loadData(dataArray);
			},errorHandler:function(errorString, exception) {
				console.log(errorString);
			}
		});
	},
	operate : function(operation) {
		debugger;
		var gridPanel = this;
		var model = gridPanel.getSelectionModel().getSelection()[0];
		var vmName = model.get('name');
		var machinePanel = this.up('nodeMachinePanel');
		var srcId = machinePanel.datas.id;
		var oprText = '';
		switch (operation) {
		case 'START':
			oprText = "Starting...";
			break;
		case 'STOP':
			oprText = 'Stopping...';
			break;
		case 'RESTART':
			oprText = 'Restarting...';
			break;
		}
		Ext.MessageBox.show({
			msg : oprText,
			width : 300,
			wait : true,
			waitConfig : {
				interval : 200
			}
		});
		gridPanel.getSelectionModel().deselectAll();
		vmService.operate(srcId, vmName, operation, {
			callback : function() {
				Ext.getCmp('maintab').down('gdcVmGrid').refresh();
				Ext.MessageBox.hide();
			},
			errorHandler : function(errorString, exception) {
				console.log(errorString);
				console.log(exception);
				Ext.MessageBox.hide();
			}
		});
	}
});