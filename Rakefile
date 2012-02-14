#!/usr/bin/env target/bin/jruby -S rake
require 'rake/testtask'

task :default => [:usage]

task :usage do
  puts "\n"
  puts "*** Usage of this rakefile ***"
  puts "-- rake test: run unit tests."
  puts "\n"
end

Rake::TestTask.new do |t|
  t.libs << "test"
  t.test_files = FileList['./test/unit/*_test.rb']
  t.verbose = true
end

desc "Another task description."
task :another do
  puts "Running another task."
end
