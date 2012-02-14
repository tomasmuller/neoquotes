require 'nokogiri'
require 'open-uri'

module Yql
  
  YAHOO_API_BASE_URL = "http://query.yahooapis.com/v1/public/yql?q="
 
  YAHOO_FINANCE_QUOTES_URL = "http://finance.yahoo.com/q?s=" 

  STOCK_NAME_XPATH = "/html/body/div[3]/div/div[3]/div[2]/div/div/div/div/span"
  
  COMPANY_NAME_XPATH = "/html/body/div[3]/div/div[3]/div[2]/div/div/div/div/h2"
  
  PRICE_XPATH = "//*[@id=\"yfs_l84_@SYMBOL_DOWNCASE@\"]"
  
  RELATED_SYMBOLS_XPATH = "//*[@id=\"yfi_related_tickers\"]/span/a/strong"
  
  
  def self.get_stock_name(symbol)
    yql_query = build_query(symbol, STOCK_NAME_XPATH)
    doc = execute(YAHOO_API_BASE_URL + yql_query, :xml)

    {
      :count => doc.xpath('//@yahoo:count').text.strip,
      :name => doc.xpath('//results/span[1]').text.strip 
    }
  end
  
  def self.get_company_name(symbol) 
    yql_query = build_query(symbol, COMPANY_NAME_XPATH)
    doc = execute(YAHOO_API_BASE_URL + yql_query, :xml)

    {
      :count => doc.xpath('//@yahoo:count').text.strip,
      :name => doc.xpath('//results/h2[1]').text.strip 
    }
  end
  
  def self.get_price(symbol) 
    yql_query = build_query(symbol, build_price_xpath(symbol))
    doc = execute(YAHOO_API_BASE_URL + yql_query, :xml)

    {
      :count => doc.xpath('//@yahoo:count').text.strip,
      :price => doc.xpath('//results/span[1]').text.strip 
    }
  end
  
  def self.get_related_symbols(symbol) 
    yql_query = build_query(symbol, RELATED_SYMBOLS_XPATH)
    doc = execute(YAHOO_API_BASE_URL + yql_query, :xml)
    
    related_symbols = []
    doc.xpath('//results/strong').map do |node|
      related_symbols << node.text.strip
    end
    
    {
      :count => doc.xpath('//@yahoo:count').text.strip,
      :symbols => related_symbols
    }    
  end
  
  def self.lookup_symbol(symbol) 
    doc = execute(YAHOO_FINANCE_QUOTES_URL + symbol, :html)
    
    related_symbols = []
    doc.xpath(RELATED_SYMBOLS_XPATH).map do |node|
      related_symbols << node.text.strip
    end
    
    {
      :symbol => symbol,
      :stock_name => doc.xpath(STOCK_NAME_XPATH).text.strip,
      :company_name => doc.xpath(COMPANY_NAME_XPATH).text.strip,
      :price => doc.xpath(build_price_xpath(symbol)).text.strip,
      :related_symbols => related_symbols
    }
  end
  
  def self.build_query(symbol, xpath_condition) 
    "select content from html where url='#{YAHOO_FINANCE_QUOTES_URL + symbol}' and xpath='#{xpath_condition}'"
  end
  
  def self.execute(url, format)
    url = URI.escape(url)
    puts "Executing: #{url}"
    case format
    when :xml
      doc = Nokogiri::XML(open(url))
    when :html
      doc = Nokogiri::HTML(open(url))
    end
    doc
  end

  def self.build_price_xpath(symbol)
    PRICE_XPATH.gsub('@SYMBOL_DOWNCASE@', symbol.downcase)
  end

end
