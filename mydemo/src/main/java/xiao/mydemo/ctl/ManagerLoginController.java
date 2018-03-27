package xiao.mydemo.ctl;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/**
 * 管理员登录controller
 * @author xiaorenqi
 *
 */
public class ManagerLoginController extends Controller{
	/**
	 * 进入管理员登录界面
	*/
		@ActionKey(value = "/managerlogin/index")
		public void login() {
			this.render("/pages/managerLogin.html");
		}
		

	    /**
	     * 提交登录，保存数据，进入管理中心
	     */
	    @ActionKey(value = "/managerlogin/verification")
	    public void loginVerif() {
	    	String Id = this.getPara("Id");
	    	String password = this.getPara("password");
	    	String sql = String.format(" SELECT * FROM manager WHERE id = ? AND pwd = ?");
	    	//验证用户信息，成功则放置用户id到session
	    	Record record=Db.findFirst(sql,Id,password);	
	    	System.out.println("record="+record);
	    	if(null==record){//验证失败
	    		String errmsg = "账号或密码错误";
	    		this.setAttr("errmsg", errmsg);
	    		this.render("/pages/managerlogin.html");
	    		return;
	    	}  
	    	else
	    	{
	    		String managerId = record.get("id"); 
	    		this.setSessionAttr("managerId", managerId);
	    		this.redirect("/manager/index");//登录成功，跳转到管理中心
	    	}
	    }
	
}
