#!/bin/bash
## Install chef and its dependencies

##make sure we have openssl for ruby
echo 'Installing Chef...'
echo 'installing rubygems...'
apt-get -y install libopenssl-ruby
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
echo 'rubygems installed, proceeding with chef...'
gem install rdoc chef ohai json_pure --no-ri --no-rdoc --source http://gems.opscode.com --source http://gems.rubyforge.org
mkdir /tmp/chef-solo #for cache files, etc.
echo 'Chef Installed!'