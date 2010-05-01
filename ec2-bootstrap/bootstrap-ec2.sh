#!/bin/bash
## THe above line must be the first line of this script
## AMI: ami-6743ae0e
## log all commands, and exit on any errors (to aid debugging)
## all stdout/stderr will be piped to /var/log/syslog
set -e -x
##get us up to date
apt-get -y update
##make sure we have openssl for ruby
apt-get install libopenssl-ruby
##and install ruby
apt-get -y install ruby
##install ruby dev
aptitude -y update
aptitude -y install irb ri rdoc libshadow-ruby1.8 ruby1.8-dev gcc g++ curl
##pull down rubygems
curl -L 'http://rubyforge.org/frs/download.php/45905/rubygems-1.3.1.tgz' | tar xvzf -
cd rubygems* && ruby setup.rb --no-ri --no-rdoc
##make it our system gem executable
ln -sfv /usr/bin/gem1.8 /usr/bin/gem
##install chef
gem install rdoc chef ohai --no-ri --no-rdoc --source http://gems.opscode.com --source http://gems.rubyforge.org
##bootstrap chef with scripts from our chef repo
#TODO: grab from S3 bucket
#for now, install git and use git-clone
##install git
aptitude -y install git-core
cd /tmp
git clone git://github.com/ryanschneider/chef-sample.git

## ???
# only need these if we are mounting some new EBS volumes
#yes | mkfs -t ext3 /dev/sdq1
#yes | mkfs -t ext3 /dev/sdq2
## (non-)PROFIT!
