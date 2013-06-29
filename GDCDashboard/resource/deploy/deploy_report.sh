#!/usr/bin/expect

set host [lindex $argv 0]
set prompt "(%|#|\\$) $"
set password "(.*password):"

send "echo 'Start'\r"
spawn ssh -oStrictHostKeyChecking=no -oCheckHostIP=no gdc@$host
expect -re $password
send "GDCscript\r"
expect -re $prompt
send "mkdir -p ~/deploy/GDCDashboard\r"
expect -re $prompt
send "exit\r"
expect eof
set spawn_id $user_spawn_id
expect -re $prompt
send "scp ../report_script/* gdc@$host:~/deploy/GDCDashboard\r"
expect -re $password
interact
