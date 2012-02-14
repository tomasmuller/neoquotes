configure :production do 
  set :reload_templates, false
  set :protection, true
  set :dump_errors, false
  set :raise_errors, false
  set :show_exceptions, false
end
