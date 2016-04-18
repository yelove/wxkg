package cn.biz.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.biz.bean.OPID_AccessToken;
import cn.biz.util.MpApi;
import cn.common.CommonStr;

@Controller
public class ViewAction {
	
	
	@RequestMapping(value = "wxview", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
	public String wxlogview(HttpServletRequest request){
		String code = request.getParameter(CommonStr.CODE);
		OPID_AccessToken opidtk = MpApi.getOPIDtoken(code);
		request.setAttribute(CommonStr.OPENID, opidtk.getOpenid());
		return "";
	}

}
