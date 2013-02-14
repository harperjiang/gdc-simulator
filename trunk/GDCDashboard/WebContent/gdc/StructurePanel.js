Ext.namespace("GDC");
GDC.structureStore = Ext.create('Ext.data.TreeStore');

structureService.getDataCenters(function(dcs) {
	debugger;
	var root = {};
	root.expanded = true;
	root.children = new Array();
	for ( var dcCount = 0; dcCount < dcs.length; dcCount++) {
		var dc = dcs[dcCount];
		var dcNode = {
			text : dc.name,
			id : dc.id,
			leaf : dc.batteries.length === 0,
			children : new Array()
		};
		for ( var btyCount = 0; btyCount < dc.batteries.length; btyCount++) {
			var bty = dc.batteries[btyCount];
			var btyNode = {
				text : bty.name,
				id : bty.id,
				leaf : bty.machines.length === 0,
				children : new Array()
			};
			for ( var mcCount = 0; mcCount < bty.machines.length; mcCount++) {
				var mc = bty.machines[mcCount];
				var mcNode = {
					text : mc.name,
					id : mc.id,
					leaf : mc.vms.length === 0,
					children : new Array()
				};
				for ( var vmCount = 0; vmCount < mc.vms.length; vmCount++) {
					var vm = mc.vms[vmCount];
					var vmNode = {
						text : vm.name,
						id : vm.id,
						leaf : true
					};
					mcNode.children.push(vmNode);
				}
				btyNode.children.push(mcNode);
			}
			dcNode.children.push(btyNode);
		}
		root.children.push(dcNode);
	}
	GDC.structureStore.setRootNode(root);
});

function loadNode(id) {
	nodeService.getData(id, function(data) {
		var newView = undefined;
		data = {
			title : 'Machine View',
			cpu : 40,
			memory : 30,
			battery : 80
		};
		switch (data.type) {
		case 'machine':
		default:
			newView = Ext.create('GDC.MachineViewPanel');
		}
		newView.loadData(data);
		newView.title = data.title;

		var mainTabPanel = Ext.getCmp('maintab');
		mainTabPanel.add(newView);
		// Set it as the active one
		mainTabPanel.setActiveTab(mainTabPanel.items.length - 1);
	});

}

Ext.define("GDC.StructurePanel", {
	extend : 'Ext.tree.Panel',
	title : 'Machine List',
	xtype : 'strutpanel',
	store : GDC.structureStore,
	rootVisible : false,
	width : 200,
	listeners : {
		select : function(tree, record, row, opt) {
			loadNode(record.raw.id);
		}
	}
});