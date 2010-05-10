#!/bin/bash
## THe above line MUST be the first line of this script
## AMI: ami-6743ae0e
## log all commands, and exit on any errors (to aid debugging)
## all stdout/stderr will be piped to /var/log/syslog
set -e -x

## STEP 0: Pull args
while getopts :r: o
do case "$o" in
  r) DNA="$OPTARG"
  esac
done

if [ ! -f "${DNA}" ];
then
  echo "ERROR: DNA file not found!"
  exit 1
fi

## STEP 1: Setup Chef
./scripts/setup/chef.sh

## STEP 2: Run Chef for our roles (TODO: use chef server instead of solo)
echo "Starting chef with DNA:"
cat ${DNA}
chef-solo -c /tmp/bootstrap/chef/solo.rb -j ${DNA}



