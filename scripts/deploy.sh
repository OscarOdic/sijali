#!/usr/bin/env bash
sbt "set test in Test := {}" clean assembly
export SSHPASS=$2
sshpass -e scp -o stricthostkeychecking=no target/scala-2.12/sijali.jar $3@$1:$4
sshpass -e ssh -f -o stricthostkeychecking=no $3@$1 "$4/deploy.sh"
