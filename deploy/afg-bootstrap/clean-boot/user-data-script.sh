#!/bin/bash
## For Alestic AMIs (I'm using ami-6743ae0e)
## details on how this script works: http://alestic.com/2009/06/ec2-user-data-scripts

## TO USE:
#
## Edit the following:
##
## 1. point BOOTSTRAP_TARBALL_URL to your bootstrap tarball
## 2. point DNA's war variable to your WAR file
##
export BOOTSTRAP_TARBALL_URL="http://github.com/ryanschneider/afg-bootstrap/tarball/master"
# MAKE SURE YOU USE DOUBLE QUOTES WHEN EDITTING DNA
export DNA='
{
  "recipes": [ "afg_web", "afg_app", "afg_dbmaster" ],
  "war": "http://allforgood-bootstrap.s3.amazonaws.com/all_for_good-0.1-SNAPSHOT.war"
}
'
## NOTHING BELOW HERE SHOULD NEED EDITTING

## log all commands, and exit on any errors (to aid debugging)
## all stdout/stderr will be piped to /var/log/syslog
set -e -x
##get us up to date
apt-get -y update
## set up working dir
cd /tmp
mkdir bootstrap
cd bootstrap
## save the DNA
echo $DNA > /tmp/bootstrap/dna.json
## pull down the bootstrap code
curl -L $BOOTSTRAP_TARBALL_URL | tar xvzf -
## tarfile may have our stuff at root, or in a subdir
## if chef isn't here, then (hopefully) everything is in a single subdir
if [ ! -d  chef ];
then
  mv */* .
fi
## launch bootstrap script
./scripts/bootstrap.sh -r /tmp/bootstrap/dna.json
