//注册个人中心-基本信息组件
Vue.component('i-c-personal-info',{
	template:'#i-c-personal-info-tpl',
	props:{		
		type:{type:String,default:function(){return "S";}}
		,applyNo:{type:String,default:function(){return "";}}
	}, 
	data:function(){
		return {

		}
	},
	methods:{		

	},
	watch:{
		
	}
});