ENV["RACK_ENV"] = "test"

# Add app directories to load path.
#
$LOAD_PATH.tap do |path|
  path << File.expand_path("../../app", __FILE__)
end

require 'test/unit'

class Test::Unit::TestCase
  # Add helper methods to be used by all tests here...
end
