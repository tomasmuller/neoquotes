package com.herokuapp.neoquotes.models;

/**
 * Results from a YQL query, or Nokogiri scrap through XPath.
 */
public class YqlResult {

	private String symbol;

	private String stockName;

	private String companyName;

	private String price;

	/**
	 * Validate symbol data.
	 * 
	 * @return boolean
	 */
	public boolean validate() {
		try {
			return (!stockName.equals("")) 
					&& (!companyName.equals(""))
					&& (!price.equals(""));
		} catch (Exception e) {
			return false;
		}
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol.trim().toUpperCase();
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName.trim().toUpperCase();
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName.trim().toUpperCase();
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price.trim();
	}

	@Override
	public String toString() {
		return "YqlResult [symbol=" + symbol + ", stockName=" + stockName
				+ ", companyName=" + companyName + ", price=" + price + "]";
	}

}
