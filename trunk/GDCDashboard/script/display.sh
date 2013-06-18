#!/usr/bin/expect
set arg0 [lindex $argv 0]
spawn ssh -oStrictHostKeyChecking=no -oCheckHostIP=no gdc@$arg0
#use correct prompt
set prompt ":|#|\\\$"
interact -o -nobuffer -re $prompt return
send "GDCscript\r"
interact -o -nobuffer -re $prompt return
send "virsh list --all\r"
interact -o -nobuffer -re $prompt return
send "exit\r"
interact
