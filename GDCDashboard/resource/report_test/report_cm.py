#!/usr/bin/python3
import subprocess
import http.client, urllib.parse

#Configurations
machine_id="dc1-bty1-mc1"
host="localhost:8080"
url="/GDCDashboard/update"

#CPU Usage
lines = subprocess.check_output('vmstat').splitlines()
values = lines[2].split()
cpu_use = 100 - int(values[14])

#Memory Usage
lines = subprocess.check_output('free').splitlines();
values = lines[1].split()
memory_use = int(int(values[3])*100 /int(values[1]))

#VM Count
vm_count = int(subprocess.check_output('./vm_count.sh'))

#Send information
vals = {machine_id+'.MACHINE_CPU':cpu_use,\
        machine_id+'.MACHINE_MEMORY':memory_use,\
        machine_id+'.MACHINE_VMCOUNT':vm_count}
params =','.join(['%s=%s'%(key,val) for (key,val) in vals.items()])
headers = {'Content-Type':'application/x-www-form-urlencoded', 'Accept':'text/plain'}

conn = http.client.HTTPConnection(host)

conn.request("POST", url, params, headers)
conn.close()
