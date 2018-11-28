package com.guonl.mt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBase {
	public static Connection getConnection(String dbName){
		Connection cn=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"+dbName, "root", "abc-123456");
		}catch(Exception e){
		}
		return cn;
	}
	public static Connection getConnection(){
		return getConnection("dbmanager");
	}

	public static Connection getSqlConnection() {
		//1.JDBC所需的四个参数4个准备：user,password,url,driverClass(连接数据库所需的驱动)
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "abc-123456";
		String driverClass = "com.mysql.jdbc.Driver";//这个是mysql-connector-java-5.1.8-bin.jar包中的，不是JDK自带的Driver。

		Connection connection = null;
		try {
			//2.加载JDBC驱动程序
			Class.forName(driverClass);
			//3.创建数据库连接:三种连接方案
			//方案一：connection = DriverManager.getConnection(jdbcUrl);
			//方案二：connection = DriverManager.getConnection(url,user,password);
			//方案三：connection = DriverManager.getConnection(url,info);
			Properties info = new Properties();
			info.setProperty("user",user);//property的key必须是"user"
			info.setProperty("password",password);//property的key必须是"password"
			connection = DriverManager.getConnection(url,info);
			return connection;
		} catch (ClassNotFoundException e) {
			//log.error("Not found driver class, load driver failed.");
		} catch (SQLException e) {
			//log.error("create db connection error,",e);
		}
		return null;
	}
}
