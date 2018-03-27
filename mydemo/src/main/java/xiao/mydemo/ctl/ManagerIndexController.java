package xiao.mydemo.ctl;

import java.util.List;

import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ManagerIndexController extends Controller{
	
	@ActionKey(value = "/manager/index")
	public void userIndex() {	
		this.render("/pages/managerIndex.html");
	}
	/**
	 * 获取用户账号的信息
	 */
	@ActionKey(value = "/manage/user/info")
	public void ManageUserInfo() {
		String id=this.getPara("id");
		try{
			String sql = String.format(" select  * from user where id=?");
			Record record=Db.findFirst(sql,id);
			if(null==record)
			{
				record=new Record();
				record.set("id", id);
				record.set("pwd", "123456");
				record.set("status","未注册");
				this.renderJson("userInfo",record);
				return;
			}
			else
			{	
				this.renderJson("userInfo", record);
			}
		}catch(Exception ex){
			this.renderError(-1, "获取用户账号信息失败");
		}	
	}
	/**
	 * 添加用户账号
	 */
	@ActionKey(value = "/manage/add/user")
	public void ManageAddUser(){
		String id=this.getPara("id");
		try{
			Record record=new Record();
			record.set("id", id);
			Db.save("user", record);
			this.render("/manager/index");
		}catch(Exception ex){
			this.renderError(-1, "注册用户信息失败");
		}	
	}
	/**
	 * 重置用户账号
	 */
	@ActionKey(value="/manage/user/pwd")
	public void ManageUserPwd(){
		String id=this.getPara("id");
		try{
			String sql = String.format(" select  * from user where id=?");
			Record record=Db.findFirst(sql,id);
			record=new Record();
			record.set("pwd", "123456");
			Db.update("user",record);
		}catch(Exception ex){
			this.renderError(-1, "重置密码用户信息失败");
		}	
	}
	
	/**
	 * 删除用户账号
	 */
	@ActionKey(value="/manage/delete/user")
	public void ManageDeleteUser(){
		String id=this.getPara("id");
		try{
			String sql = String.format(" select * from user where id like ?");;
			List<Record> record=Db.find(sql,id+"%");
			if(record.isEmpty()){//若无包含此字段的账号，返回相应信息
				this.renderJson("message", "不存在包含"+id+"字段的账号");
			}
			else{
				String sql2 = String.format(" delete  from user where id like ?");;
				Db.update(sql2,id+"%");
				this.renderJson("message", "包含"+id+"字段的账号已删除");
			}
			
		}catch(Exception ex){
			this.renderError(-1, "删除用户账号信息失败");
		}	
	}
	
	/**
	 * 查询管理者权限
	 */
	@ActionKey(value="/manage/power")
	public void ManagerPower(){
		String manager=this.getSessionAttr("managerId");
		String sql=String.format("select power from manager where id=?");
		Record record=Db.findFirst(sql, manager);
		this.renderJson("power",record.get("power"));
	}
	/**
	 * 获取管理员账号的信息
	 */
	@ActionKey(value = "/manage/manager/info")
	public void ManageManagerInfo() {
		String id=this.getPara("id");
		try{
			String sql = String.format(" select  * from manager where id=?");
			Record record=Db.findFirst(sql,id);
			if(null==record)//
			{
				record=new Record();
				record.set("id", id);
				record.set("pwd", "123456");
				record.set("status","未注册");
				this.renderJson("managerInfo",record);
				return;
			}
			else
			{	
				this.renderJson("managerInfo", record);
			}
		}catch(Exception ex){
			this.renderError(-1, "获取管理员账号信息失败");
		}	
	}
	
	/**
	 * 添加管理员账号
	 */
	@ActionKey(value = "/manage/add/manager")
	public void ManageAddManager(){
		String id=this.getPara("id");
		try{
			Record record=new Record();
			record.set("id", id);
			Db.save("manager", record);
			this.render("/manager/index");
		}catch(Exception ex){
			this.renderError(-1, "注册管理员信息失败");
		}	
	}
	
	/**
	 * 删除管理员账号
	 */
	@ActionKey(value="/manage/delete/manager")
	public void ManageDeleteManager(){
		String id=this.getPara("id");
		try{
			System.out.println("id="+id);
			if(id.equals("001")){
				this.renderJson("message", "001账号为系统管理员账号，无法删除");
			}
			else{
				String sql2 = String.format(" delete from manager where id=?");
				Db.update(sql2,id);
				this.renderJson("message", "管理员账号"+id+"已删除");
			}			
		}catch(Exception ex){
			this.renderError(-1, "删除管理员账号信息失败");
		}	
	}
}
