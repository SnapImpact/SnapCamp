include_recipe "jetty"

#install the war as root
remote_file "allforgood_war" do
  path "/usr/share/jetty/webapps/root.war"
  owner "jetty"
  mode  0640
  source node[:war]
end

#delete the sample root directory if it exists
directory "/usr/share/jetty/webapps/root" do
  action :delete
  recursive true
  only_if "test -d /usr/share/jetty/webapps/root"
end

#restart jetty
service "jetty" do
  #can i say supports [ :restart => false ] and then use :restart?
  action [ :stop, :start]
end
