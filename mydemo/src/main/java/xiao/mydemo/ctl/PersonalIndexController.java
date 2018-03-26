/**
 * 2017年5月2日 下午3:40:05
 * wuyp
 */
package xiao.mydemo.ctl;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

public class PersonalIndexController extends Controller{
	@ActionKey(value = "/personal/index")
	public void personalIndex() {	
		this.render("/pages/personalIndex.html");
	}

	/**
	 * 个人中心--获取用户基本信息
	 */
	@ActionKey(value = "/personal/base/info")
	public void list() {
		String userId = this.getSessionAttr("userId");
		try{
			String sql = String.format(" select * from user where id = ? and status = 1 ");
			Record record=Db.findFirst(sql,userId);
			this.renderJson("baseInfo", record);
		}catch(Exception ex){
			this.renderError(-1, "获取用户信息失败");
		}	
	}
	
	/**
	 * 个人中心--更新编辑后的用户信息
	 */
	@Before(Tx.class)
	@ActionKey(value = "/update/personal/info" )
	public void savePersonalInfo() {
		String userId = this.getSessionAttr("userId");
		try{
			Record record = Db.findFirst("select * from user where id = ?", userId);
			record.set("tel",this.getPara("tel"));
			record.set("qq", this.getPara("qq"));
			//密码未更改，则不更新此字段;不为空时，更新这个字段
			if((this.getPara("password")).length()!=0)
			{
				record.set("pwd", this.getPara("password"));
			}
			Db.update("user", record);
			this.redirect("/personal/index");
		}catch(Exception ex){//保存失败
			this.renderError(-1, "更新用户信息失败");
		};
	}
	
	
	
	/**
	 * 首页 --获取首页商品信息
	 */
	@ActionKey(value = "/get/allgoods/info")
	public void allGoodsInfo() {
		String userId=this.getSessionAttr("userId");
		try{
			String sql = String.format(" select goods.stu_id as stu_id,goods.id as id,goods.name as name,goods.price as price,goods.number as number,goods.condition as conditions,goods.introduce as introduce,user.tel as tel,user.qq as qq from goods,user where goods.number>0 and user.id=goods.stu_id and user.id<>?");
			List<Record> record=Db.find(sql,userId);
			if(record.isEmpty())//没有商品列表信息
			{
				this.renderJson("goodsInfo","");
				return;
			}
			this.renderJson("goodsInfo", record);
		}catch(Exception ex){
			this.renderError(-1, "获取用户信息失败");
		}	
	}
	
	/**
	 * 首页--获取首页查询的商品信息
	 */
	@ActionKey(value = "/get/allgoods/info2")
	public void allGoodsInfo2() {
		String userId=this.getSessionAttr("userId");
		String search=this.getPara("search");
		try{
			String sql = String.format(" select goods.stu_id as stu_id,goods.id as id,goods.name as name,goods.price as price,goods.number as number,goods.condition as conditions,goods.introduce as introduce,user.tel as tel,user.qq as qq from goods,user where goods.number>0 and user.id=goods.stu_id and user.id<>? and goods.name like ?");
			List<Record> record=Db.find(sql,userId,"%"+search+"%");			
			//System.out.println("record="+record);
			if(record.isEmpty())//没有商品列表信息
			{
				this.renderJson("goodsInfo","");
				return;
			}
			this.renderJson("goodsInfo", record);//能得到数据，但无法在首页显示，可能与页面跳转有关		
		}catch(Exception ex){
			this.renderError(-1, "获取用户信息失败");
		}	
	}
	
	/**
	 * 首页--购买商品
	 */
	@Before(Tx.class)
	@ActionKey(value = "/buy/goods" )
	public void buyGoods(){
		String number=this.getPara("number");
		String id=this.getPara("id");//goods的id
		//String sellerId=this.getPara("stu_id");
		String buyerId=this.getSessionAttr("userId");
		//根据number更新goods表
		try{
			String sql1=String.format("select number,stu_id from goods where id=?");
			Record record1=Db.findFirst(sql1,id);
			if(Integer.parseInt(number)>record1.getInt("number")){
				System.out.println("错误的数量");
			}
			else{
				//更新goods表里的库存数量
				record1.set("id",id);
				record1.set("number",record1.getInt("number")-Integer.parseInt(number));
				Db.update("goods", record1);
				//更新orders表
				Record record2=new Record();
				record2.set("goods_id", Integer.parseInt(id));
				record2.set("buyer_id",buyerId);
				record2.set("buy_time", IPersonalStoreBaseController.getCurrentDate());
				Db.save("orders", record2);					
			}
			this.redirect("/personal/index");
		}catch(Exception ex){
			this.renderError(-1, "购买失败");
		}
		
	}
		
	/**
	 * 我的商品--更新编辑后的商品信息
	 */
	@Before(Tx.class)
	@ActionKey(value = "/update/goods" )
	public void updateGoods() {
		String id=this.getPara("id");
		try{
			Record record = Db.findFirst("select * from goods where id = ?", id);
			record.set("name",this.getPara("name"));
			record.set("price", this.getPara("price"));
			record.set("condition", this.getPara("condition"));
			record.set("number",this.getPara("number"));
			record.set("introduce",this.getPara("introduce"));
			record.set("time", IPersonalStoreBaseController.getCurrentDate());			
			Db.update("goods", record);
			this.redirect("/personal/index");
		}catch(Exception ex){//更新失败
			this.renderError(-1, "更新物品信息失败");
		};
	}	
	
	
	/**
	 * 我的商品--获取商品列表信息
	 */
	@ActionKey(value = "/get/mygoods" )
	public void getPersonalStore() {
		String userId = this.getSessionAttr("userId");//当前用户id
		String sql = String.format(" SELECT * FROM goods WHERE stu_id= ? and number>0");
		try{
			List<Record> record=Db.find(sql,userId);
			if(record.isEmpty())//没有商品列表信息
			{
				this.renderJson("personalStoreItem","");
				return;
			}
			this.renderJson("personalStoreItem", record);
		}catch(Exception ex){
			this.renderError(-1, "获取商品列表失败");
		}
	}
		
	/**
	 * 我的商品--删除物品
	 */
	@Before(Tx.class)
	@ActionKey(value="/delete/goods")
	public void deleteGoods(){
		String id=this.getPara("id");
		try {
			String sql=String.format("delete from goods where id=?");
			Db.update(sql,Integer.parseInt(id));
			this.redirect("/personal/index");
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			this.renderError(-1, "删除物品信息失败");
		}
	}
	
	/**
	 * 我的商品--保存新增物品
	 */
	@Before(Tx.class)
	@ActionKey(value = "/save/new/goods" )
	public void saveNewGoods() {
		String userId = this.getSessionAttr("userId");
		try{
			//保存资源信息到资源表
			Record record = new Record();
			record.set("name",this.getPara("name"));
			record.set("price", this.getPara("price"));
			record.set("condition", this.getPara("condition"));
			record.set("number",this.getPara("number"));
			record.set("introduce",this.getPara("introduce"));
			record.set("stu_id", userId);			
			record.set("time", IPersonalStoreBaseController.getCurrentDate());
			Db.save("goods", record);
			this.redirect("/personal/index");
		}catch(Exception ex){//保存失败;
			this.renderError(-1, "保存新物品信息失败");
		};
	}
	

	
	
	/**
	 * 订单--获取订单列表信息
	 */	
	@ActionKey(value ="/get/personal/order")
	public void getPersonalOrder() {
		String userId = this.getSessionAttr("userId");//当前用户id
		String sql = String.format(" SELECT orders.id as id,goods.id as goods_id,goods.name as name,goods.img as img,goods.price as price,orders.status as status,orders.receiving_time as receiving_time,orders.buy_time as buy_time FROM orders,goods WHERE orders.buyer_id= ? and orders.goods_id=goods.id");
		try{
			List<Record> record=Db.find(sql,userId);
			if(record.isEmpty())//没有订单列表信息
			{
				this.renderJson("order","");
				return;
			}
			this.renderJson("order", record);
		}catch(Exception ex){
			this.renderError(-1, "获取订单列表失败");
		}
	}
	
	/**
	 * 订单--删除订单
	 */
	@Before(Tx.class)
	@ActionKey(value ="/delete/order")
	public void deleteOrder() {
		String userId = this.getSessionAttr("userId");//当前用户id
		String goodsId = this.getPara("goods_id");
		String sql = String.format("delete from orders where buyer_id=? and goods_id=? and status='已收货'");
		
		try{
			Db.update(sql,userId,goodsId);
			this.redirect("/personal/index");
		}catch(Exception ex){
			this.renderError(-1, "删除订单失败");
		}
	}
	
	/**
	 * 订单--确认收货
	 */
	@Before(Tx.class)
	@ActionKey(value ="/confirm/order")
	public void confirmOrder() {
		String orderId = this.getPara("order_id");
		String sql = String.format("select * from orders where id=?");
		try{
			Record record=Db.findFirst(sql,orderId);
			record.set("status", "已收货");
			record.set("receiving_time", IPersonalStoreBaseController.getCurrentDate());
			Db.update("orders",record);
			this.redirect("/personal/index");
		}catch(Exception ex){
			this.renderError(-1, "删除订单失败");
		}
	}	
	
}
