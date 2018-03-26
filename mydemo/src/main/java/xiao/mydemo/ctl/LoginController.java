/**
 * 2017年4月13日 下午7:47:05
 * wuyp
 */
package xiao.mydemo.ctl;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/**
 * 跳蚤市场登录controller
 * @author xiaorenqi
 *
 */
public class LoginController extends Controller{

	/**
	 * 进入用户登录界面
	 */
	@ActionKey(value = "/login/index")
	public void login() {
		this.render("/pages/login.html");
	}
	
	/**
	 *退出登录
	 */
	@ActionKey(value = "/login/out")
	public void exitLogin() {
		String currentUser = this.getPara("userId");
		if (currentUser != null) {
			this.removeSessionAttr("userId");
			this.removeSessionAttr("pwd");
			this.redirect("/index");
		} else {
			redirect("/index");
		}
	}

    /**
     * 提交登录，保存数据，进入个人中心
     */
    @ActionKey(value = "/login/verification")
    public void loginVerif() {
    	String Id = this.getPara("userId");
    	String password = this.getPara("password");
    	String sql = String.format(" SELECT * FROM user WHERE id = ? AND pwd = ?");
    	//验证用户信息，成功则放置用户id到session
    	Record record=Db.findFirst(sql,Id,password);	
    	if(null==record){//验证失败
    		String errmsg = "学号或密码错误";
    		this.setAttr("errmsg", errmsg);
    		this.render("/pages/login.html");
    		return;
    	}  
    	else
    	{
    		String userId = record.get("id"); 
    		this.setSessionAttr("userId", userId);
    		this.redirect("/personal/index");//登录成功，跳转到个人中心
    	}
    }
}
