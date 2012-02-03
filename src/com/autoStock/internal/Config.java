package com.autoStock.internal;

/**
 * @author Kevin Kowalewski
 *
 */
public class Config {
	public static int comListenPort = 8888;
	public static int comSocketTimeout = 30;
	public static int dbPort = 3306;
	public static String dbHost = "199.19.173.146";
	public static String dbDatabase = "autoStock";
	public static int dbConnectionPartitions = 1;
	public static int dbMinConnectionsPerParition = 1;
	public static int dbMaxConnectionsPerParition = 16;
	public static String dbUsername = "autoStock";
	public static String dbPassword = "SSmxynk";
	public static String plIbUsername = "cmiha621";
	public static String plIbPassword = "paper123";
	public static int plIbTwsPort = 888;
	public static String plIbTwsHost = "127.0.0.1";
}
