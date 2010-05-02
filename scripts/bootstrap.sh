#!/bin/bash
## THe above line MUST be the first line of this script
## AMI: ami-6743ae0e
## log all commands, and exit on any errors (to aid debugging)
## all stdout/stderr will be piped to /var/log/syslog
set -e -x

## STEP 0: Pull args
while getopts :r: o
do case "$o" in
  r) ROLES="$OPTARG"
  esac
done

if [ -z "${ROLES}" ];
then
  echo "ERROR: No roles defined!"
  exit 1
fi

## STEP 1: Setup Chef
./scripts/setup/chef.sh

## STEP 2: Run Chef for our roles (TODO: use chef server instead of solo)
echo "Starting chef with roles: ${ROLES[*]}"
./chef/run-solo-roles.rb ${ROLES[*]}



