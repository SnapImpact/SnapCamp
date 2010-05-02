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

service "jetty" do
 supports :restart => true, :reload => true
 action [ :enable, :start ]
end
