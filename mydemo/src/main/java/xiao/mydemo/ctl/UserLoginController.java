
package xiao.mydemo.ctl;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/**
 * 跳蚤市场用户登录controller
 * @author xiaorenqi
 *
 */
public class UserLoginController extends Controller{

	/**
	 * 进入用户登录界面
	 */
	@ActionKey(value = "/userlogin/index")
	public void login() {
		this.render("/pages/userLogin.html");
	}
	

    /**
     * 提交登录，保存数据，进入个人中心
     */
    @ActionKey(value = "/userlogin/verification")
    public void loginVerif() {
    	String Id = this.getPara("userId");
    	String password = this.getPara("password");
    	String sql = String.format(" SELECT * FROM user WHERE id = ? AND pwd = ?");
    	//验证用户信息，成功则放置用户id到session
    	Record record=Db.findFirst(sql,Id,password);	
    	if(null==record){//验证失败
    		String errmsg = "学号或密码错误";
    		this.setAttr("errmsg", errmsg);
    		this.render("/pages/userLogin.html");
    		return;
    	}  
    	else
    	{
    		String userId = record.get("id"); 
    		this.setSessionAttr("userId", userId);
    		this.redirect("/user/index");//登录成功，跳转到个人中心
    	}
    }
}
