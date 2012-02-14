require 'java'
require 'helpers/yql_helper'

# Application main page
#
get '/' do
  erb :index
end

# Symbol lookup through GUI
#
post '/:symbol' do
  # Get informations about this symbol from Yahoo Finance 
  #
  results = Yql.lookup_symbol params[:symbol]
  yqlResult = com.herokuapp.neoquotes.models.YqlResult.new
  yqlResult.symbol = params[:symbol].to_java
  yqlResult.stockName = results[:stock_name].to_java
  yqlResult.companyName = results[:company_name].to_java
  yqlResult.price = results[:price].to_java
  
  # Create or Update symbol information
  #
  symbol = com.herokuapp.neoquotes.models.Symbol.new
  message = symbol.search(yqlResult)
  logger.info "Returning: #{message}"
  message 
end

# Symbol lookup through VOICE
#
get '/voice/index.vxml' do
  erb :'/voice/index', :layout => :'voice/layout'
end

post '/voice/lookup.vxml' do
  logger.info "Searching for symbol: #{params[:stringSymbol]}"
  
  # Get informations about this symbol from Yahoo Finance 
  #
  @results = Yql.lookup_symbol params[:stringSymbol]
  yqlResult = com.herokuapp.neoquotes.models.YqlResult.new
  yqlResult.symbol = @results[:symbol].to_java
  yqlResult.stockName = @results[:stock_name].to_java
  yqlResult.companyName = @results[:company_name].to_java
  yqlResult.price = @results[:price].to_java

  # Create or Update symbol information
  #
  symbol = com.herokuapp.neoquotes.models.Symbol.new
  @message = symbol.search(yqlResult)

  # Send user to vxml with lookup results.
  #
  erb :'/voice/lookup', :layout => :'voice/layout'
end

# Return Neo4j data in Arbor.js JSON format
#
get '/graph.json' do
  graph = com.herokuapp.neoquotes.models.Graph.new
  graph.toJson
end

error 404 do
  erb :http_404, :layout => false
end

error 422 do
  erb :http_422, :layout => false
end

error 500 do
  erb :http_500, :layout => false
end
