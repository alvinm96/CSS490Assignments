package program1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {
	private Set<String> pagesVisited;
	
	public WebCrawler() {
		pagesVisited = new HashSet<String>();
	}
	
	/**
	 * Crawls the page for the first unvisited link goes to that link. Repeats
	 * operation until numHops times or a link is not found.
	 * @param page page to go to
	 * @param numHops number of page hops
	 * @return the last page seen
	 */
	public String crawl(String page, int numHops) {
		if (numHops == 0) {
			return page;
		}
		
		pagesVisited.add(page);
		System.out.println(page);
		
		Elements links = getLinks(page);
		
		for (Element link : links) {
			String nextLink = link.attr("abs:href");
			if (nextLink.contains("http") && !pagesVisited.contains(nextLink)) {
				return crawl(nextLink, --numHops);
			}
		}
		
		return page; // when no links are found
	}
	
	/**
	 * Gets the links found in an html
	 * @param page 
	 * @return An array of links found on the page
	 */
	public Elements getLinks(String page) {
		String html = getHtml(page);
		Document doc = Jsoup.parse(html);
		return doc.select("a");
	}
	
	/**
	 * Gets the HTML of a page
	 * @param page the page to view
	 * @return a String value of the html
	 */
	public String getHtml(String page) {
		try {
			URL url = new URL(page);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			
			InputStream is = urlConn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
						
			String html = "";
			String line;
			while ((line = br.readLine()) != null) {
				html += line;
			}
			return html;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}
}
