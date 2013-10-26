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
		<div class="subtitle">Emulator Block</div>
		<div class="text">Development of the emulator: This is a tool that
			generates a power profile typical of a specified wind turbine
			generator from a given wind profile. By acquiring real wind speed
			measurements, an identical power profile from a real generator
			(inclusive of aerodynamic, mechanical, and electrical losses) can be
			emulated.</div>
		<div class="text">
			<img src="image/emulator_renew_res.png" />
		</div>
		<div class="text caption">Renewable Resources Emulator</div>
		<div class="text">The renewable resource emulator imports and
			pre-processes the data, compute the values of voltage and current
			that a wind turbine can produce from the wind speed measurements, and
			send voltage/current profile to the programmable DC power supply. The
			programmable DC power supply will in turn feed power to the POD. A
			slave controller takes charge of the plant, turbine, and POD. It
			measures the electric transients and the steady state conditions, all
			recorded internally in the hardware, which that can be used for
			control algorithm development. The power can be determined from the
			DC voltage and current which are measured in the laboratory setup.
			Fluctuations of current and voltage are also taken in consideration
			based on the specs of specific wind turbine. A power drop will have
			more influence on current than voltage. For instance, if the wind
			speed decreases, the wind turbine will not be able to run several
			servers, due to a current drop. In those instances, a storage bank is
			required to support the POD operations.</div>
		<div class="subtitle">Distributed Control System (DCS)</div>
		<div class="text">The first control strategy investigated was to be
			implemented by controlling different sites from a centralized control
			system acquiring electric and weather measurements, and then proceeds
			to forecast for weather availability. Depending on the weather
			forecast, the controller decides to shift workload to the desired
			location. However, this procedure keeps the controller continuously
			to be on with the disadvantage that it might not be able to respond
			to an external alert or interruption. To address this issue, an
			alternative solution was proposed. The proposed architecture is a
			D.C.S that divides the tasks in order to supplement the central
			controller and rapidly provide decisions. Each individual location
			has its own controller, denoted “Slave Controller”, that controls and
			monitors the (solar or wind) site where it is located. Then, these
			slave controllers communicate with a central controller, denoted
			“Master Controller,” that helps taking decisions. Once the master
			controller determines the available power distribution, it supplies
			these data to the workload manager that decides the best approach to
			shift data from one location to another.</div>
		<div class="text">
			<img src="image/emulator_dcs.png" />
		</div>
		<div class="text caption">DCS Architecture</div>

		<div class="text">Characterization of the Subsystems and the Assembly
			to Determine the Correlation between Input Power (to POD) and Wind
			Variability – in the second quarter report an algorithm was presented
			to meet the demand and schedule the power commitment. The program
			gives a schedule of running several electricity units in order to
			meet a pre-deterministic workload. In electricity market operation,
			for the same power load and same plants or network, electricity price
			computed based on UC1 algorithm increases when constraints related to
			network transmission lines are taken into consideration (NCUC2). The
			constraints related to outage of transmission lines or generators are
			also included; when these constraints are added to the NCUC the price
			increases. These constraints are also included in the SCUC3
			algorithm. Significant improvements were made during this quarter.</div>
		<div class="subtitle">Unit Commitment (U.C) problem:</div>
		<div class="text">Workload and power allocation strategy was proposed
			based on minimizing the price of available energy. Such criterion is
			subjected to reserve limitations, generator capabilities, and power
			bus limitations, and other constraints.</div>
		<div class="text">
			<img src="image/emulator_gdc_arch.png" />
		</div>
		<div class="text caption">GDC Architecture</div>

		<div class="text">
			Based on available power in each location and associated price, the
			code generates a schedule and a power distribution that can meet the
			desired load demand. As described in Task1-I, the strategy adopted
			relies on individual sites sending data to the master controller
			where a Unit Commitment (UC) algorithm is used to make data energy
			scheduling decisions. If the demand profile can be met, then the
			Workload Manger will make final decision on how the data should be
			scheduled. The master controller also sends out alerts about grid
			outage, drop of wind power, etc. as needed. The algorithm has been
			further developed to extend its capability to manage 24 hours instead
			of 8 hours. Available periods of the day are taken into consideration
			to evaluate the availability of power. Some basic conditions were
			also implemented (for example the controller avoids relying on solar
			sites during night cycle).The economic model that represents our
			scenario includes:
			<ul>
				<li>The price objective function</li>
				<li>The bus capability that is the cumulative power that a bus can
					get from a generator plus the storage that is coupled to compensate
					the power drop.</li>
				<li>The generators limitations, that is the minimum power and
					maximum power that a generator can produce.</li>
				<li>The reserve capability that is the limitations of the storage
					bank.</li>
			</ul>
			Extra constraints, as listed below, were added to better model the
			site where the grid unit is supplying power to a POD:
			<ul>
				<li>Start Up cost and its constraints</li>
				<li>Shut Down cost and its constraints</li>
				<li>Maximum sustained rate</li>
				<li>Quick Start Capability</li>
				<li>Minimum Up Time</li>
				<li>Minimum Down Time</li>
				<li>Ramp Up rate</li>
				<li>Ramp Down rate</li>
			</ul>
		</div>

		<div class="subtitle">UC algorithm simulation</div>
		<div class="text">Some initial simulations are provided, using the UC
			algorithm to show its capability. A 24 hours UC scheduling is
			presented. The power requirement of the system is provided in Fig. 5
			with power demand and desired reserve. Figure 6 shows the results of
			a feasible scenario. Eventually, if the renewable resources cannot
			meet the power demand, the grid will be used to compensate. If all
			locations cannot meet the demand, an infeasibility flag is triggered
			and workload manager will look for other sites. The results of this
			simulation are presented in the following example. The figures 5 and
			6 represent the scenario reported in table 2.</div>
		<div class="text">
			<img src="image/power_requirement.png" />
		</div>
		<div class="text caption">Figure 5 - Power requirement</div>
		<div class="text">
			<img src="image/power_production.png" />
		</div>
		<div class="text caption">Figure 6 - Power production scenario</div>


	</div>
	<?php include 'footer.php'?>
</body>
</html>
