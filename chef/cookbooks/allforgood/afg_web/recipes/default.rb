include_recipe "nginx"

#copy cookbook's nginx.conf -> $NGINX_HOME/sites-available/allforgood.conf
remote_file "nginx_conf" do
    source "nginx.conf"
    path "/etc/nginx/sites-available/allforgood.conf"
    mode 0644
    owner "root"
end

#symlink to our config
link "/etc/nginx/sites-enabled/allforgood.conf" do
  to "/etc/nginx/sites-available/allforgood.conf"
end

#delete the default link if there
link "/etc/nginx/sites-enabled/default" do
  action :delete
  only_if "test -L /etc/nginx/sites-enabled/default"
end

#restart nginx
service "nginx" do
  action :restart
end
