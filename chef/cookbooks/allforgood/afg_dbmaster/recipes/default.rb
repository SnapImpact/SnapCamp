include_recipe "postgresql::server"

#hopefully all we need to do is setup local access.
#the lift app will actually be the one to create the DB/tables.

#copy the pg_hba.conf to the proper location
remote_file "trust_localhost_pg_hba_conf" do
    source "pg_hba.conf"
    path "/etc/postgresql/8.3/main/pg_hba.conf"
    mode 0600
    owner "postgres"
    group "postgres"
end

#make sure postgresql.conf has the proper owner
file "/etc/postgresql/8.3/main/postgresql.conf" do
  owner "postgres"
  group "postgres"
  mode 0600
end

#stop postgres
service "postgresql-8.3" do
  action :stop
end

#and start the service
service "postgresql-8.3" do
  action :start
end

# Not sure why, but postgres isn't picking up my changes until I restart..
service "postgresql-8.3" do
  action :restart
end

#create our jetty and ubuntu roles
bash "psql CREATE ROLEs" do
  user "root"
  code <<-EOH
    psql -h localhost -d postgres -U postgres -c "CREATE ROLE \"jetty\" LOGIN PASSWORD '';"
    psql -h localhost -d postgres -U postgres -c "CREATE ROLE \"ubuntu\" LOGIN PASSWORD '';"
  EOH
end
