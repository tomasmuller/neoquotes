require 'test_helper'

require 'nokogiri'
require 'open-uri'

require 'helpers/yql_helper'

class YqlStockTest < Test::Unit::TestCase

  def test_aapl_query
    symbol = "AAPL"
    results = Yql.get_stock_name symbol

    assert_equal "1", results[:count]
    assert_equal "NasdaqGS", results[:name]

    puts "Total count: #{results[:count]}. Stock name: #{results[:name]}"
  end
  
  def test_appl_company_name
    symbol = "AAPL"
    results = Yql.get_company_name symbol
    
    assert_equal "1", results[:count]
    assert_equal "Apple Inc. (AAPL)", results[:name]

    puts "Total count: #{results[:count]}. Company name: #{results[:name]}"
  end

  def test_appl_price
    symbol = "AAPL"
    results = Yql.get_price symbol
    
    assert_equal "1", results[:count]
    assert_not_nil results[:price]

    puts "Total count: #{results[:count]}. Price: #{results[:price]}"
  end

  def test_appl_related_sumbols
    symbol = "AAPL"
    results = Yql.get_related_symbols symbol
    
    assert_equal results[:count].to_i, results[:symbols].size
    
    puts "Total count: #{results[:count]}. Related: #{results[:symbols].join(', ')}"
  end
  
  def test_get_all_with_one_request
    symbol = "AAPL"
    results = Yql.lookup_symbol symbol

    assert_equal "AAPL", results[:symbol]
    assert_equal "-NasdaqGS", results[:stock_name]
    assert_equal "Apple Inc. (AAPL)", results[:company_name]
    assert_not_nil results[:price]
    assert_not_nil results[:related_symbols]
    
    puts "Stock: #{results[:stock_name]}. Company: #{results[:company_name]}. Price: #{results[:price]}. Related: #{results[:related_symbols].join(', ')}."
  end

end
