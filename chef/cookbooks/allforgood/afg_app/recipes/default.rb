include_recipe "jetty"

#install the war as root
remote_file "allforgood_war" do
  path "/usr/share/jetty/webapps/root.war"
  owner "jetty"
  mode  0640
  #be lame and hardcode it for now, just to make sure jetty works
  source "http://allforgood-bootstrap.s3.amazonaws.com/all_for_good-0.1-SNAPSHOT.war"  #should be node[:war]
end

#delete the sample root directory if it exists
directory "/usr/share/jetty/webapps/root" do
  action :delete
  only_if "test -d /usr/share/jetty/webapps/root"
end

#restart jetty
service "jetty" do
  #can i say supports [ :restart => false ] and then use :restart?
  action [ :stop, :start]
end
