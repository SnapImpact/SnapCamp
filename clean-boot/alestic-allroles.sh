#!/bin/bash
## For Alestic AMIs (I'm using ami-6743ae0e)
## What are our roles?
export ROLES=( WEB APP DBMASTER )
## log all commands, and exit on any errors (to aid debugging)
## all stdout/stderr will be piped to /var/log/syslog
set -e -x
##get us up to date
apt-get -y update
##grab the tarball
## TODO: grab from S3 bucket
## FORNOW: grab github tarball
cd /tmp
mkdir bootstrap
cd bootstrap
curl -L 'http://github.com/ryanschneider/afg-bootstrap/tarball/master' | tar xvzf -
## tarfile may have our stuff at root, or in a subdir
## if chef isn't here, then (hopefully) everything is in a single subdir
if [ ! -d  chef ];
then
  mv */* .
fi
## launch bootstrap script
./scripts/bootstrap.sh

