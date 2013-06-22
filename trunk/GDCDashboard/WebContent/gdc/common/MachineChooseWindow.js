Ext.define('GDC.common.MachineChooseWindow', {
	extend : 'Ext.window.Window',
	xtype : 'gdcMcWindow',
	title : 'Choose a Machine',
	width : 300,
	height : 180,
	modal : true,
	layout : {
		type : 'table',
		columns : 1,
		tdAttrs : {
			valign : 'top',
			style : {
				padding : '10 10 10 10'
			}
		}
	},
	listeners : {
		afterrender : function() {
			this.down('#dsCombo').store.loadData(GDC.dataCenters);
		}
	},
	items : [
			{
				xtype : 'combo',
				itemId : 'dsCombo',
				fieldLabel : 'Data Center',
				queryMode : 'local',
				displayField : 'text',
				valueField : 'id',
				store : {
					xtype : 'store',
					fields : [ 'text', 'id' ]
				},
				listeners : {
					change : function(oldval, newval, eopts) {
						var machineCombo = this.up('gdcMcWindow').down(
								'#machineCombo');
						machineCombo.clearValue();
						if (null != newval) {
							machineCombo.store
									.loadData(GDC.machinesUnderDs[newval]);
						}
					}
				}
			}, {
				xtype : 'combo',
				itemId : 'machineCombo',
				fieldLabel : 'Machine',
				queryMode : 'local',
				displayField : 'text',
				valueField : 'id',
				store : {
					xtype : 'store',
					fields : [ 'text', 'id' ]
				}
			}, {
				xtype : 'button',
				text : 'Select',
				handler : function() {
					debugger;
					var window = this.up('gdcMcWindow');
					var machineCombo = window.down('#machineCombo');
					window['machineId'] = machineCombo.getValue();
					window.down('#dsCombo').clearValue();
					window.hide();
				}
			} ]
});