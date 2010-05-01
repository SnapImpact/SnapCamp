file_cache_path "/tmp/chef-solo"
#cookbook_path should go from least to most specific
#we consider the opscode recipes the most generic
#common contains homemade recipes for common components (e.g. jetty)
#allforgood would contain any recipes specific to us (e.g. we wanted to compile from source on the deploy box)
cookbook_path [ "/tmp/bootstrap/chef/cookbooks/opscode", "/tmp/bootstrap/chef/cookbooks/common", "/tmp/bootstrap/chef/cookbooks/allforgood" ]
#roles are all specific to allforgood
# we currently have 3 roles WEB, APP, and DBMASTER.  Will add things like DBSLAVE, LOADBALANCER down the road
role_path "/tmp/bootstrap/chef/roles/allforgood"
