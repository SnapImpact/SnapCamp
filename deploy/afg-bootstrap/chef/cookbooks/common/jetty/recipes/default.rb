include_recipe "java"
# Needed by jetty extra & jsp
%w{ libjetty6-java libjetty6-extra-java jetty }.each do |pkg|
 package pkg do
  action :install
 end
end

bash "enable-jetty" do
 code "sed s/NO_START=1/NO_START=0/ -i /etc/default/jetty"
end

#jetty blows up if this directory doesn't exist on ubuntu
directory "/usr/share/java/webapps" do
  owner "root"
  group "root"
  mode "0755"
  action :create
  recursive true
  not_if "test -d /usr/share/java/webapps"
end

service "jetty" do
 supports :restart => true, :reload => true
 action [ :enable, :start ]
end
