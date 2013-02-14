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
			leaf : dc.batteries.length === 0,
			children : new Array()
		};
		for ( var btyCount = 0; btyCount < dc.batteries.length; btyCount++) {
			var bty = dc.batteries[btyCount];
			var btyNode = {
				text : bty.name,
				leaf : bty.machines.length === 0,
				children : new Array()
			};
			for ( var mcCount = 0; mcCount < bty.machines.length; mcCount++) {
				var mc = bty.machines[mcCount];
				var mcNode = {
					text : mc.name,
					leaf : mc.vms.length === 0,
					children : new Array()
				};
				for ( var vmCount = 0; vmCount < mc.vms.length; vmCount++) {
					var vm = mc.vms[vmCount];
					var vmNode = {
						text : vm.name,
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

Ext.define("GDC.StructurePanel", {
	extend : 'Ext.tree.Panel',
	title : 'Machine List',
	xtype : 'strutpanel',
	store : GDC.structureStore,
	rootVisible : false,
	width : 200,
});