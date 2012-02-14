require 'java'

# Create Jetty Server
#
server = org.eclipse.jetty.server.Server.new

# Create and set server thread pool
#
threadPool = org.eclipse.jetty.util.thread.QueuedThreadPool.new
threadPool.setMinThreads(4)
threadPool.setMaxThreads(40)
server.setThreadPool(threadPool)

# Create and set server connector
#
webapp_port = ENV['PORT'].to_i
connector = org.eclipse.jetty.server.nio.SelectChannelConnector.new
connector.setPort(webapp_port)
server.addConnector(connector)

# Configure web application context
#
webapp_dir = "src/main/webapp"
context = org.eclipse.jetty.webapp.WebAppContext.new
context.setDescriptor("./#{webapp_dir}/WEB-INF/web.xml")
context.setResourceBase("./#{webapp_dir}")
context.setContextPath("/")
context.setParentLoaderPriority(true)

# Set server handler
#
server.setHandler(context)

# Start server
#
server.start()

# Join and prevent main exiting
#
server.join()
