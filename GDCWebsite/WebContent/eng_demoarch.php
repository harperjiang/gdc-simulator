<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Clarkson GDC</title>
<link rel="stylesheet" type="text/css" href="style/main.css" />
</head>
<body>
	<?php include 'header.php';?>
	<?php include 'menu.php'; ?>
	<div id="content" class="content">

		<div class="subtitle">Proposed Demo Architecture</div>
		<div class="text">With different characteristics of Renewable Energy,
			we can decompose a datacenter into Computation, Network Flow, and
			Data access. Computing servers consume most of the power but can be
			leveraged by Virtualization or other Computer Techniques to shift the
			services across datacenters while network and storage equipment take
			less power. This is critical to the users, but needs long-term access
			so computing servers can be supplied with wind turbines or hydro dams
			which provide the power at KWs level, while network and storage
			equipment can be supplied with Solar panels which provides the power
			at 100Ws. For computing servers, as the renewable energy is not
			dynamic and inconstant, Virtualization can be leveraged in both
			shortage and outage cases and Service Scheduling Components will be
			in place to manage the computing services: 1. In power shortage
			cases, Virtualization can dynamically consolidate the services in
			fewer Servers while shutting down some servers 2. In power outage
			cases, Virtualization can dynamically migrate the services to another
			Datacenter to escape the shutdown datacenter For storage equipment,
			the low-powered but long-lasting renewable energy can still maintain
			the data access through network. So the proposed Demo Architecture
			will look like the diagram as below:</div>
		<div class="text">
			<img src="image/propose_demo_arch.png" />
		</div>
		<div class="text caption">Proposed Demo Architecture</div>
		<div class="text">
			In the above proposed architecture:
			<ul>
				<li>Each GDC center has been equipped with Networked Attached
					storage.</li>
				<li>Power Predicator (Power management) will keep updates of the
					green energy stats information to the Service Scheduler. It has two
					main roles: one is notifying the Service Scheduler in advance when
					Datacenter’s power outage is coming and the other is predicting the
					power supply strength.</li>
				<li>Based upon the updates from the Power predicator, the Service
					Scheduler (Service Management) will make a decision on how to move
					the services across datacenters and what strategies will pertain
					for each service in the power shortage and outage.</li>
			</ul>
			Based on the proposed architecture, the protocol between Service
			Scheduler and Power Predicator is focused on how quickly the power
			predictor can notify the Service Scheduler of the power
			outage/shortage and how long the power supply can last in current
			strength. Those two statistics are critical for enabling the Service
			Scheduler to make a good decision about migrating the computing
			service.
		</div>
		<div class="subtitle2">Proposed Hardware Specifications</div>
		<div class="text">
			With the current project budget, we plan the following: each
			rack-mount server represents a mini-datacenter with virtualization
			deployed. One mini-datacenter supplied with Green Energy, and the
			other mini-datacenter supplied with Grid Power. Power Predictor will
			predict the power of Green-energy-driven datacenter and Service
			scheduler will decide when to shift the computing service onto and
			off GDC. This system will include:
			<ul>
				<li>1. 1x HP Proliant DL385 with 1200w DC as Green-DC</li>
				<li>2. 1x HP Proliant DL165 with 750w AC as Grid-DC
					<ul>
						<li>Both of them include two AMD Operon 6220 3.G CPUs and 64G
							Memory.</li>
						<li>AMD Operon 6220 supports AMD-V Virtualization Technologies and
							VMware EVC mode.</li>
					</ul>
				</li>
				<li>3. 1x Iomega StorCenter px4-300r Server Class NAS
					<ul>
						<li>Px4-300r supports both NFS, Samba, and iSCSI</li>
						<li>Maximum Power Assumption at 170w</li>
						<li>px4-300r will be equipped with GDC, hosting VM images</li>
				
				</li>
			</ul>
		</div>
	</div>
	<?php include 'footer.php'?>
</body>
</html>
