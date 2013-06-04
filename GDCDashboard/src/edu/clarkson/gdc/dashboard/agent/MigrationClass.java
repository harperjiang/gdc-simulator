package edu.clarkson.gdc.dashboard.agent;

import java.io.*;

public class MigrationClass {

	public static void main(String[] args) {
		String srcIP = "128.153.145.179";
		String destIP = "128.153.145.175";
		String vmName = "gdcguest0-NFS-IP191";
		// migrateVM(srcIP,destIP,vmName);
		listVM(destIP);
	}

	static void migrateVM(String srcIP, String destIP, String vmName) {
		try {
			String command = "./migrate.sh";
			executeInShell(command, srcIP, destIP, vmName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void executeInShell(String command, String srcIP,
			String destIP, String vmName) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(command, srcIP, destIP,
				vmName);

		builder.redirectErrorStream(true);
		builder.redirectOutput();
		Process process = builder.start();
		InputStream iStream = process.getInputStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				iStream));

		String line;

		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}

	/*
	 * static void migrateVM(String srcIP,String destIP,String vmName) { //
	 * Runtime r = Runtime.getRuntime(); // Process p =
	 * r.exec("//home//vinay//migrate.sh "+srcIP+ " " +destIP+" "+vmName);
	 * 
	 * // OutputStream os =p.getOutputStream(); // // //OutputStreamWriter osw =
	 * new OutputStreamWriter(os); // System.out.println(os.toString()); }
	 */

	static void listVM(String srcIP) {
		try {
			ProcessBuilder builder = new ProcessBuilder("./displayVM.sh", srcIP);

			builder.redirectErrorStream(true);
			builder.redirectOutput();
			Process process = builder.start();
			InputStream iStream = process.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					iStream));

			String line;

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if (!line.matches(".*migrat.*")) {
					// Exception throwing code here
					throw new Exception();
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
