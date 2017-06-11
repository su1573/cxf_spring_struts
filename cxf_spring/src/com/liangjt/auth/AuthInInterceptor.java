/**
 * 
 */
package com.liangjt.auth;

import java.util.List;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Phase 阶段
 * 通过PhaseInterceptor，可以指定拦截器在哪个阶段起作用
 * @author Administrator
 *
 */
public class AuthInInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
	
	 public AuthInInterceptor() {
		//显示调用父类有参数的构造器
		//显示构造父类构造器之后，程序不会调用父类无参构造器
		 //该拦截器将会在调用之前拦截soap消息
		super(Phase.PRE_INVOKE);
	}

	//需要实现自己的拦截器，需要实现handleMessage，形参就是拦截到的消息
	//一旦程序获得了soap消息，就可以解析soap消息，或者修改soap。
	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		System.out.println("--------" + message);
		//得到soap消息所有的header
		List<Header> headers = message.getHeaders();
		
		if(headers == null || headers.size() <1){
			throw new Fault(new IllegalArgumentException("没有header，拒绝调用！	"));
		}
		//假如第一个header携带了用户名、密码信息
		Header first = headers.get(0);
		Element e = (Element) first.getObject();
		
		NodeList userIds = e.getElementsByTagName("userId");
		NodeList userPasses = e.getElementsByTagName("userPass");
		
		
		if(userIds.getLength() != 1){
			throw new Fault(new IllegalArgumentException("用户名格式不对"));
		}
		
		if(userPasses.getLength() != 1){
			throw new Fault(new IllegalArgumentException("密码格式不对"));
		}
		
		//获取此元素的文本，即用户名
		String userId = userIds.item(0).getTextContent();
		String userPass = userPasses.item(0).getTextContent();
		System.out.println(userId+":"+ userPass);
		
		//实际业务中，拿用户名和密码，比对数据库
		// ...
		
		//老铁，一定要记得不要用 == 去判断值
		if( ! "1111".equals(userId) || ! "1111".equals(userPass)){
			
			throw new Fault(new IllegalArgumentException("用户名，密码不争取"));

		}
		
	}
	
	
	

}
