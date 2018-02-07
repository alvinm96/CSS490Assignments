package program3;

/**
 * 
 * @author amana
 *
 */
public class Program3 {
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Invalid number of arguments:\n[path] [name (optional)]");
			System.exit(1);
		}
		DirectoryTraverser dt = new DirectoryTraverser(args[0]);
		
		if (args.length == 2) {
			dt.setBucketName(args[1]);
		}
		
		dt.backupDirectory();
	}
}
