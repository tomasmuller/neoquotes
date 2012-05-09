#
# Trinidad looks for a "rackup" file named config.ru
# in config/config.ru.
#

BASEDIR = File.expand_path("../../", __FILE__)
puts "BASEDIR is: #{BASEDIR}"

ENV['BUNDLE_GEMFILE'] = "#{BASEDIR}/Gemfile"
ENV['GEM_HOME'] = "#{BASEDIR}/vendor/bundle"
ENV['GEM_PATH'] = ENV['GEM_HOME']
ENV['PATH'] = "#{ENV['GEM_PATH']}/bin:#{ENV['PATH']}"

require 'rubygems'

require 'bundler'
Bundler.setup(:default, ENV.fetch("RACK_ENV", :development))
Bundler.require

require 'sinatra'

# Global application settings
#
set :run, false
set :environment, ENV['RACK_ENV']
set :root, "#{BASEDIR}/app"
set :views, "#{BASEDIR}/app/views"
set :public_folder, "#{BASEDIR}/public"

# Enable session support.
#
set :sessions, false

# Enable logging.
#
set :logging, true

# Serve static files from the public directory.
#
set :static, true
set :default_encoding, "UTF-8"
set :locale, "en"

# environment specific settings
#
require "#{BASEDIR}/config/environments/#{settings.environment.to_s}"

# Add ruby app directories to load path.
#
$LOAD_PATH.tap do |path|
  path << File.expand_path("#{BASEDIR}/app", __FILE__)
end

# Load all initializers. Files will be loaded in alphabetical order.
#
Dir["#{BASEDIR}/config/initializers/**/*.rb"].each do |file|
  require file
end

require "#{BASEDIR}/app/application"
run Sinatra::Application
