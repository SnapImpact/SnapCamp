#!/bin/bash
## THe above line MUST be the first line of this script
## AMI: ami-6743ae0e
## log all commands, and exit on any errors (to aid debugging)
## all stdout/stderr will be piped to /var/log/syslog
set -e -x

## STEP 1: Setup Chef
./scripts/setup/chef.sh

## STEP 2: Run Chef for our roles (TODO: use chef server instead of solo)
./scripts/chef/solo.sh


