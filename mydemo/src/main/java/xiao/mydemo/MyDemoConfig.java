
package xiao.mydemo;
import xiao.mydemo.ctl.UserLoginController;
import xiao.mydemo.ctl.ManagerIndexController;
import xiao.mydemo.ctl.ManagerLoginController;
import xiao.mydemo.ctl.UserIndexController;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

public class MyDemoConfig extends JFinalConfig {

	public static void main(String[] arg) {
		JFinal.start("src/main/webapp", 8080, "/", 5);
	}

	@Override
	public void configConstant(Constants arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configEngine(Engine arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers arg0) {
		// TODO Auto-generated method stub

		// 配置Oracle方言
		// arp.setDialect(new OracleDialect());
	}

	@Override
	public void configInterceptor(Interceptors arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configPlugin(Plugins arg0) {
		// TODO Auto-generated method stub
		DruidPlugin cp = new DruidPlugin(
				"jdbc:mysql://127.0.0.1:3306/mydb?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull",
				"root", "1234");
		// 配置Oracle驱动
		cp.setDriverClass("com.mysql.jdbc.Driver");
		arg0.add(cp);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		Dialect mysqlDialect = new MysqlDialect();
		arg0.add(arp);
	}

	@Override
	public void configRoute(Routes arg0) {
		arg0.add("/userlogin", UserLoginController.class);
		arg0.add("/managerlogin", ManagerLoginController.class);
		arg0.add("/user",UserIndexController.class);
		arg0.add("/manager", ManagerIndexController.class);
	}

}
