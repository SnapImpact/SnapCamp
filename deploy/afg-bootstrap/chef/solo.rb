file_cache_path "/tmp/bootstrap/chef"
# cookbook_path should go from least to most specific
# we consider the opscode recipes the most generic
# common contains homemade recipes for common components (e.g. jetty)
# allforgood would contain any recipes specific to us (e.g. we wanted to compile from source on the deploy box)
cookbook_path [ "/tmp/bootstrap/chef/cookbooks/opscode", "/tmp/bootstrap/chef/cookbooks/common", "/tmp/bootstrap/chef/cookbooks/allforgood" ]
