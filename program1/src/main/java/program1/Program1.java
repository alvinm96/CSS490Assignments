package program1;

import program1.WebCrawler;

/**
 * 
 * @author Alvin Manalastas
 * CSS490 Program 1
 * 18 January 2018
 *
 */

public class Program1 {
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Invalid number of arguments:\n[starting url] [number of hops]");
			return;
		}
		
		WebCrawler wc = new WebCrawler();
		
		int numHops = Integer.parseInt(args[1]);
		
		String finalPage = "";
		System.out.println(String.format("Final page: %s", finalPage = wc.crawl(args[0], numHops)));
		System.out.println(wc.getHtml(finalPage));
	}

}
