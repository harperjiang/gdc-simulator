#!/usr/bin/expect
set arg0 [lindex $argv 0]
set arg1 [lindex $argv 1]
set arg2 [lindex $argv 2]
spawn ssh -oStrictHostKeyChecking=no -oCheckHostIP=no gdc@$arg0
#use correct prompt
set prompt ":|#|\\\$"
interact -o -nobuffer -re $prompt return
send "GDCscript\r"
interact -o -nobuffer -re $prompt return
send "virsh $arg1 $arg2\r"
interact -o -nobuffer -re $prompt return
send "exit\r"
interact
