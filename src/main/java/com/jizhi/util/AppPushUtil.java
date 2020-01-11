package com.jizhi.util;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import org.springframework.stereotype.Component;


@Component
public class AppPushUtil {
	
	private static String appId ="TldvlUfij37JmGB7OqK6w5";
    private static String appKey ="Oo4YVrGWg672Nppw4HUZi3";
    private static String masterSecret ="JaD0hR0m1e8EPLpSh6aVz5";
    //别名推送方式
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";
    //主函数  入口
    /*****
     * @param CID   需要发送给谁，，，也就是那个用户的应用id
     * @param title     标题
     * @param content   文本，内容
     * @return String   返回的结果
     * @throws Exception
     * @author daju
     */
    public String pushMsg(String CID,String title,String content) {
	     String pushResult="pushFail";
		 IGtPush push = new IGtPush(host, appKey, masterSecret);
		 LinkTemplate template = linkTemplateDemo(title,content);
	     SingleMessage message = new SingleMessage();
	     // 离线有效时间，单位为毫秒，可选
	     message.setOffline(true);
	     message.setOfflineExpireTime(24 * 3600 * 1000);
	     message.setData(template);
	     // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
	     message.setPushNetWorkType(0);
		 Target target = new Target();
	     target.setAppId(appId);
	     target.setClientId(CID);
		 IPushResult ret = null;
		 try {
	         ret = push.pushMessageToSingle(message, target);
	     } catch (RequestException e) {
	         e.printStackTrace();
	         ret = push.pushMessageToSingle(message, target, e.getRequestId());
	     }
	     if (ret != null) {
	         if("ok".equals(ret.getResponse().get("result"))){
	        	 pushResult="pushSuccess";
	         }
	     }else {
	         System.out.println("服务器响应异常");
	     }
         return pushResult;
    }
    
    
    /*****
     * @param title   群发的标题
     * @param content   群发的文本，内容
     * @return LinkTemplate 返回设置后的需要发送的对象 
     * @throws Exception
     * @author daju
     */
    public LinkTemplate linkTemplateDemo(String title,String content) {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(title);
        style.setText(content);
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);
        //设置打开的网址地址
        template.setUrl("#");
        return template;
    }
    
    
    
}
