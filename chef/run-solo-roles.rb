#!/usr/bin/env ruby

#run a system command
def cmd(cmd)
  system(cmd)
end

require "rubygems"
gem "json"
require "json"

puts( "Generating run_list..." )
roles = ARGV.collect { |x| "role[" << x << "]" }
json = { "run_list" => roles }
File.open('/tmp/dna.json', 'w') { |f| f.write(JSON.generate(json)) }
puts( "Starting chef-solo...")
cmd( "chef-solo -c /tmp/bootstrap/chef/solo.rb -j /tmp/dna.json"  )



