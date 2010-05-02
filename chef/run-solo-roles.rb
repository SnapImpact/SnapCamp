#!/usr/bin/env ruby

require "rubygems"
gem "json"
require "json"

puts( "Generating run_list..." )
roles = ARGV.map{|x| "role[" << x << "]" }
json = { "run_list" => roles }
open('/tmp/dna.json', 'w') { |f| f.write(JSON.generate(json)) }
puts( "Starting chef-solo...")
system( "chef-solo -c /tmp/bootstrap/chef/solo.rb -j /tmp/dna.json"  )



