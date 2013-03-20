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
		<div class="subtitle">Computation Migration</div>
		<div class="subtitle2">Live Migration Requirements</div>
		<div class="text">
			Current leading enterprise-level virtualizations are
			<ul>
				<li>Redhat KVM</li>
				<li>VMware vSphere</li>
				<li>Microsoft HyperV</li>
				<li>Citrix XenServer</li>
			</ul>
		</div>
		<div class="text">All of them have many successful cases
			deployed in IT infrastructure. VMware vSphere has a rich experience
			in live migration while HyperV just released their share-nothing live
			migration in Oct 2012.</div>
		<div class="text">
			To migrate a virtual machine, there are different states that could
			be transferred over between servers:
			<ul>
				<li>CPU state</li>
				<li>Memory state</li>
				<li>VM image</li>
			</ul>
		</div>
		<div class="text">While services are running inside a vritual
			machine, hypervisor needs to keep syncing the state changes between
			the source and destination servers when does the live migration.</div>
		<div class="text">
			In general, there are two different types of live migration to each
			of Virtualization technologies: memory migration and storage
			migration:
			<ul>
				<li>Memory migration: CPU and Memory states are migrated but VM
					image stays on shared datastore. <br />For memory migration, each
					virtualization requires VM's image hosted on a shared storage like
					NFS or Samba instead of local disk so that even VM instance is
					running on a different server but VM is still able to access the
					remote VM image. CPU and Server architecture needs to match on both
					source and destination server.
				</li>
				<li>Storage migration: VM image is also migrated from source
					datastore to destination datastore. <br />For storage migration,
					VM image can be hosted on a local disk and VM image needs to be
					migrated to the destination local disk when storage migration
					happens. But for source and destination server needs to have the
					same type of file system and same file folder for hosting VM image.
				</li>
			</ul>
		</div>
	</div>
	<?php include 'footer.php'?>
</body>
</html>
