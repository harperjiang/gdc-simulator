#!/usr/bin/python

import sys
import os
import re
import subprocess

hosts = ['128.153.145.179','128.153.145.175','128.153.145.188'];

for host in hosts:
	output = subprocess.check_output(["./display.sh", host]);
	lines = re.split('[\r\n]+',output);
	for line in lines:
		section = line.split()
		if len(section) == 3 and section[1] == sys.argv[1]:
			print(host) 
	