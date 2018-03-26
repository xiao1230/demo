/**
 * 2017年5月4日 下午15:40:05
 * wuyp
 */
package xiao.mydemo.ctl;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.jfinal.core.Controller;

public class IPersonalStoreBaseController extends Controller{
	/**
	 * 获取当前登录用户方法
	 */
/*	public Record getCurrentUser() {
		Record user = this.getSessionAttr(PersonalStoreConstant.SES_NAME_LOGIN_USER);
		return user;
	}*/
	
	 /**
	   * 获取现在时间
	   *
	   * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	   */
	  public static String getCurrentDate() {
	    Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String dateString = formatter.format(currentTime);
	    return dateString;
	  }
}
