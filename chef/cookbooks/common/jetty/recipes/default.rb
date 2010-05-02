include_recipe "java"
# Needed by jetty extra & jsp
%w{ ant libgnujaf-java libgnumail-java }.each do |pkg|
 package pkg do
 action :install
 end
end

jetty_version = "6.1.16_all"
jetty_debs = %w{libjetty6-java libjetty6-jsp-java libjetty6-extra-java jetty6}

jetty_debs.each do |deb|
 remote_file deb do
 path "/tmp/#{deb}_#{jetty_version}.deb"
 source "http://dist.codehaus.org/jetty/jetty-6.1.16/debs/#{deb}_#{jetty_version}.deb"
 end
end

bash "install-jetty" do
 code "cd /tmp && dpkg --install *jetty6*.deb"
end

bash "enable-jetty" do
 code "sed s/NO_START=1/NO_START=0/ -i /etc/default/jetty6"
end

service "jetty6" do
 supports :restart => true, :reload => true
 action [ :enable, :start ]
end
