include_recipe "postgresql::server"

#hopefully all we need to do is setup local access.
#the lift app will actually be the one to create the DB/tables.

#stop postgres
service "postgresql-8.3" do
  action :stop
end

#copy the pg_hba.conf to the proper location
remote_file "pg_hba_conf" do
    source "pg_hba.conf"
    path "/etc/postgresql/8.3/main/pg_hba.conf"
    mode 0600
    owner "postgres"
end

#and restart the service
service "postgresql-8.3" do
  action :start
end
