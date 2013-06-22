#!/usr/bin/expect

set arg0 [lindex $argv 0]
set prompt "(%|#|\\$) $"
set password "(.*password):"

spawn ssh -oStrictHostKeyChecking=no -oCheckHostIP=no gdc@$arg0
expect -re $password
send "GDCscript\r"
expect -re $prompt
send "virsh list --all\r"
expect -re $prompt
send "exit\r"
interact
