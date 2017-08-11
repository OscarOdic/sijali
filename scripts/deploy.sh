#!/usr/bin/env bash
git stash --all
sbt "set test in assembly := {}" clean assembly
export SSHPASS=$2
sshpass -e scp target/scala-2.11/sijali.jar $3@$1:$4
sshpass -e ssh $3@$1 $4/deploy.sh