configure :test do
  set :raise_errors, true
  set :show_exceptions, true
  set :dump_errors, true

  set :bind, 'localhost' # 0.0.0.0 to listen on all available interfaces.
  # set :port, 9292

  set :reload_templates, true
  set :protection, true
end
