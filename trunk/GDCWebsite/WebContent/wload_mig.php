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
		<div class="subtitle">Workload Migration</div>
		<div class="text">With the time constraint of GDC node power outage,
			migrating the computation out over gigabyte network is relatively
			feasible. The computations will unlikely get interrupted and better
			than node failover and reboot. To migrate the whole data out of GDC
			is more likely unrealistic considering the migration time it needs to
			take. Moreoever, computation resource is relatively small-scale and
			easy to migrate but as disk space is cheap and easy to expand to
			Terabytes or Perabytes, user data is hard to move. So when we
			consider about the workload hosting in GDC, computation and data need
			to be treated separately. Our task is to perform a thorough
			evaluation of these migration technologies in a wide range of
			environments – between two machines in the same rack sharing a NAS,
			between two machines in the same rack not sharing a NAS, between two
			machines located in two different labs on campus with and without NAS
			and finally between an on-campus machine and an off-campus machine
			with and without NAS.</div>
		<div class="text">The goal of Task 4 is to characterize the workloads
			for which hosting in a POD datacenter would be a good match. Part of
			this task includes a detailed study of existing virtual machine
			migration technologies and an evaluation of the role virtual machine
			migration could play in shifting workloads from one POD datacenter to
			another to take advantage of available power. In addition, we would
			like to explore a number of other tools and strategies for making POD
			datacenter hosting a viable alternative for a larger range of
			workloads. We expect that there will be some workloads for which POD
			datacenter hosting is a natural fit (e.g. hosting of static content)
			and other workloads for which POD datacenter hosting may never be a
			good fit (e.g. write intensive workloads with high availability and
			coherency requirements). In addition to virtual machine migration, we
			plan to explore other tools and strategies for managing downtime at
			one POD datacenter and/or shifting work from one POD datacenter to
			another according to available power.</div>

		<div class="subtitle">Virtual Machine Migration</div>
		<div class="text">The first set of subtasks we have identified
			involves conducting a series of quantitative experiments of virtual
			machine migration. Most of the popular server class hypervisor/
			virtualization technologies including KVM, VMware, Xen, and Hyper-V
			offer a form of virtual machine migration. However, in many
			instances, it is assumed that the migration will take place between
			machines in the same datacenter and possibly even on the same network
			switch. In many cases, the machine initiating the migration must have
			access to the same network attached storage device (NAS) as the
			machine accepting the migrated VM and only the memory state is
			migrated from one machine to another. Some hypervisors also support
			live migration with storage migration where the disk state of the VM
			is transferred as well as the memory state. Our first task is to
			perform a thorough evaluation of these migration technologies in a
			wide range of environments – between two machines in the same rack
			sharing a NAS, between two machines in the same rack not sharing a
			NAS, between two machines located in two different labs on campus
			with and without NAS and finally between an on-campus machine and an
			off-campus machine with and without NAS.We are in the process of
			collecting a variety of interesting measurements of virtual machine
			migration including the total time to accomplish the migration (from
			start to finish), the downtime or time the virtual machine is
			unresponsive during migration (typically during the last stage of
			migration) and the total amount of data transferred to accomplish the
			migration. Eventually, we would also like to collect data on the
			total power required on the transmitting side to complete the
			migration. We will also collect these same measurements of two
			extreme configurations – “cold” migration and a high-availability
			pair. In cold migration, the VM is suspended, the files transferred
			to other side and the VM resumed. This represents a worst-case
			scenario for the amount of time a VM will be unresponsive but makes
			the fewest assumptions about the environment (no NAS required for
			example). In a high-availability pair, VMs run constantly on both
			machines so full VM migration is never required. In some cases, data
			will be shipped from one VM to another to keep the two VMs in sync.
			The only thing that changes is where requests are sent (to both VMs
			if available, to just one, etc.).</div>

		<div class="text">
			Progress to Date
			<ul>
				<li></li>
				<li>Surveyed advertised features/requirements of several major
					hypervisors and cloud orchestration tools, and added this
					information into a master spreadsheet</li>
				<li>Set up mini-GDC architecture in the lab with new HP servers and
					Iomega NAS. We are using this new hardware to repeat measurements
					taken on some older hardware we had available in the lab over the
					summer.</li>
				<li>Set up a similar rack of servers in one of the labs in CAMP
					which can, in part, be used for testing cross-campus migration</li>
				<li>Resolved the problem of being able to tune network bandwidth by
					using Netgear managed switch</li>
				<li>Improved GDC testbed to be compatible with all kinds of
					virtualization by removing dependencies on using the hypervisor or
					host OS for monitoring</li>
				<li>Captured a substantial amount of additional data on live
					migration with KVM (both memory migration and storage migration)
					using existing (older) hardware in our lab with VMware (both memory
					migration and storage migration)</li>
				<li>Assembled models for using GDC beyond virtual machine migration
					including rolling queries converging to a final answer</li>
			</ul>
		</div>
		<div class="subtitle">Hardware Description</div>
		<div class="text">We purchased two HP servers with two AMD Opteron
			6220 8-core 3.0G CPUs and 64G memory. These specifications are a
			better match for modern datacenter hardware than the older hardware
			we used over the summer. We install the virtualization hypervisor
			under test on each server installed Virtualization hypervisor to host
			VMs. Each site is also equipped with an 8-Terabyte Network Attached
			Storage (NAS) which will host VM images on RAID 10. Together a HP
			server and a NAS represent a small GDC in our prototype. Customer
			services are hosted in VM and can be relocated or migrated across the
			servers. The picture below is taken in Clarkson Computer Lab and
			thanks to them for allowing us to host our GDC project in their
			server room.</div>
		<div class="text">
			<img src="image/gdc_computing_setup.png" />
		</div>
		<div class="text caption">Current GDC computing setup</div>
		<div class="text">At the same time, we also have two auxiliary
			machines to accomplish the test. The GDB workload controller or
			monitor machine mentioned above is an IBM eServer xSeries345
			installed with Ubuntu. It is responsible for starting the migration,
			measuring the migration time and downtime. When testing VMware, we
			also use an additional machine to run VMware vCenter which is
			required to manage the VMware ESX servers. In this case, the workload
			controller will contact vCenter and ask vCenter to start the live
			migration. For these two additional machines, we were able to
			repurpose some exsting machine in the lab.</div>
	</div>
	<?php include 'footer.php'?>
</body>
</html>
