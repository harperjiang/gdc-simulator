#!/usr/bin/expect

set domain [lindex $argv 2]
set source [lindex $argv 0]
set operation [lindex $argv 1]

set prompt "(%|#|\\$) $"
set password "(.*password):"

spawn ssh -oStrictHostKeyChecking=no -oCheckHostIP=no gdc@$source
expect -re $password
send "GDCscript\r"
expect -re $prompt
send "virsh $operation $domain\r"
set timeout -1
expect -re $prompt
send "exit\r"
expect eof
