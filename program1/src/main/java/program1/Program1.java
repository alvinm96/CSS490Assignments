package program1;

import program1.WebCrawler;

public class Program1 {
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Invalid number of arguments:\n[starting url] [number of hops]");
			return;
		}
		
		WebCrawler wc = new WebCrawler();
		
		int numHops = Integer.parseInt(args[1]);
		System.out.println(wc.crawl(args[0], numHops));
	}

}
