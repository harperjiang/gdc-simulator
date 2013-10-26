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
		<div class="subtitle" id="part1">Components</div>
		<div class="text">
			From an electric and control stand point, this project will be
			divided into seven major tasks(sub modules).
			<ul>
				<li>Selecting the subsystem architecture and topology capable of
					providing high quality electricity to servers that are co-located
					is the first major undertake (task 1.1).</li>
				<li>A description of different strategies of monitoring the
					available power, from different intermittent sources of power will
					be considered (task 1.2).</li>
				<li>In addition, the robustness of the selected architecture will be
					verified (task 1.3).</li>
				<li>On the other hand, shifting data from one server to another with
					both servers under sufficient power availability conditions and
					subjected to willful electric defaults will be considered (task
					1.4).As a result, the system should demonstrate its ability to
					overcome electric failures and ensure transparent data shifting.</li>
				<li>Then, unpredictable events due to real-world weather constraints
					will be gradually introduce to the system (task 1.5). For instance,
					the system has to provide a fast response to an eventual lack of
					solar power, wind power, and other sources of energy.</li>
				<li>A nonstop run of the system will be monitored in order to detect
					eventual anomalies (task 1.6). Data from different seasons will be
					used for a period to be determined.</li>
				<li>Finally, a scaling up design in order to implement a field test
					will be developed (task 1.7).</li>
			</ul>
		</div>
		<div class="text">
			A system emulator will be used to simulate the behavior of real
			renewable energy sources. A short description of additional
			components is given below:
			<ul>
				<li>a. RES: Renewable Energy Sources: wind turbines and solar panels
					will be used to respectively transform kinetic motion and light
					intensity to electricity (respectively 3 phases AC voltage and one
					DC voltage line);</li>
				<li>b. CC: Charge Controller is the device responsible for
					rectifying the AC voltage and providing continuous DC voltage. It
					is also responsible for controlling the output DC intensity current
					to avoid electric components failure (including servers and other
					subsystems);</li>
				<li>c. EMU: Energy Management Unit is managing and monitoring energy
					input/output, state of charge of the storage unit. It is also
					responsible for managing energy resources in peak times and other
					critical periods of the day;</li>
				<li>d. SCU: Signal Conditioning Units are a series of power
					electronic devices such as rectifiers, inverters and UPS.
					Generally, these components cannot be all used at the same time,
					however, if the application is critical, they can be combined;</li>
				<li>e. PDU: Power Distribution Unit is responsible for intelligently
					distributing the power available at the source to the servers based
					on their computing needs and efficient operations.</li>
				<li>f. Load: The HP ProLiant DL385 G7 servers are currently being
					selected for this application. It has multi-core processors (4/8/16
					µP), each server is dedicated for highly mission critical
					applications; this server includes 12-core AMD Opteron 6100 Series.</li>
			</ul>
		</div>
		<div class="subtitle" id="part2">Test Bench Organization</div>
		<div class="text">Two electric panels will be used, one with 48VDC and
			the other with 208 VAC. Two experiments will be realized as shown in
			the below figures.</div>
		<div class="text">
			<img src="image/ac_arch.png" width="90%" />
		</div>
		<div class="text caption">AC Server Architecture</div>
		<div class="text">
			<img src="image/dc_arch.png" width="90%" />
		</div>
		<div class="text caption">DC Server Architecture</div>

		<div class="text">The below figure below explains a possible way
			datacenter can be managed. First, the total data to be process during
			a time slot is estimated. Second, the Workload Energy Manager (WEM)
			evaluates the necessary amount of electric energy needed to meet the
			demand and prepares different scenarios that are sent to a UC solver
			which prepares a schedule for each scenario. The WEM receives back
			all solutions and stores them in a database. When one of these
			scenarios occurs, the WEM orders the switch to move data from one
			server to another accordingly.</div>
		<div class="text">
			<img src="image/workload_manage.png" />
		</div>
		<div class="text caption">The synopsis of the workload commitment
			builder</div>

		<div class="subtitle2">Testbed Architecture</div>
		<div class="text">In our testbed, the actual migration traffic is
			isolated from the network we use to control the tests and to monitor
			the state of the VM being migrated. A third machine simulates switch
			connecting two Hosts on a private network. We can use either an
			actual physical switch or a third machine running the CORE network
			emulation software to represent a variety of network conditions.</div>

		<div class="text">
			<img src="image/testbed_arch.png" />
		</div>
		<div class="text caption">Testbed architecture</div>
	</div>
	<?php include 'footer.php'?>
</body>
</html>
