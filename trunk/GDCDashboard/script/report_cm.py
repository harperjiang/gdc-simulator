#!/usr/bin/python3

import subprocess
import http.client, urllib.parse

machine_id="dc1-bty1-mc2"
host="localhost:8080"
url="/GDCDashboard/update"
lines = subprocess.check_output('vmstat').splitlines()
values = lines[2].split()
cpu_use = 100 - int(values[14])
lines = subprocess.check_output('free').splitlines();
values = lines[1].split()
memory_use = int(values[3])*100 /int(values[1])

vals = {machine_id+'.MACHINE_CPU':cpu_use,machine_id+'.MACHINE_MEMORY':int(memory_use)}
params =','.join(['%s=%s'%(key,val) for (key,val) in vals.items()])
headers = {'Content-Type':'application/x-www-form-urlencoded', 'Accept':'text/plain'}

conn = http.client.HTTPConnection(host)

conn.request("POST", url, params, headers)
conn.close()
