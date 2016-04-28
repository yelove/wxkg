package cn.biz.action;

import java.io.IOException;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.biz.bean.OPID_AccessToken;
import cn.biz.util.MpApi;
import cn.common.CommonStr;
import cn.consts.WxConfig;
import cn.util.SHA1;

@Controller
public class ViewAction {

	private Logger log = LogManager.getLogger(ViewAction.class);

	@RequestMapping(value = "wxview", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "text/html;charset=UTF-8")
	public String wxlogview(HttpServletRequest request) {
		String code = request.getParameter(CommonStr.CODE);
		log.info("code is {}", code);
		OPID_AccessToken opidtk = MpApi.getOPIDtoken(code);
		if (null != opidtk) {
			request.setAttribute(CommonStr.OPENID, opidtk.getOpenid());
		} else {
			request.setAttribute(CommonStr.OPENID, -1);
		}

		return "firstpg";
	}

	@RequestMapping(value = "wxcheck", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "text/html;charset=UTF-8")
	public void wxcheck(HttpServletRequest req, HttpServletResponse resp) {
		String echostr = req.getParameter("echostr");
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		if (StringUtils.isEmpty(echostr)) {
			echostr = "";
		}
		if (StringUtils.isEmpty(signature)) {
			signature = "";
		}
		if (StringUtils.isEmpty(timestamp)) {
			timestamp = "";
		}
		if (StringUtils.isEmpty(nonce)) {
			nonce = "";
		}
		log.info("param is {},{},{},{}", echostr, signature, timestamp, nonce);
		if (checkSignature(signature, timestamp, nonce, echostr)) {
			out(echostr, resp);
		}
		out("checkfail", resp);
	}

	public static boolean checkSignature(String signature, String timestamp, String nonce, String echostr) {
		List<String> params = new ArrayList<>();
		params.add(WxConfig.TOKEN);
		params.add(timestamp);
		params.add(nonce);

		// 1. 将token、timestamp、nonce三个参数进行字典序排序
		Collections.sort(params, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
		// String temp = SHA1_WX.Instance.encode2String(params.get(0) +
		// params.get(1) + params.get(2));
		String temp = "";
		try {
			temp = SHA1.gen(WxConfig.TOKEN, timestamp, nonce);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("加密结果：" + temp);
		if (temp.equals(signature)) {
			System.out.println("微信认证成功，返回 echostr：" + echostr);
			return true;
		}
		System.out.println("微信认证失败！");
		return false;
	}

	/**
	 * 输出字符串
	 */
	protected void out(String str, HttpServletResponse response) {
		Writer out = null;
		try {
			response.setContentType("text/xml;charset=UTF-8");
			out = response.getWriter();
			out.append(str);
			out.flush();
		} catch (IOException e) {
			// ignore
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
	
	/**
	 * 创建自定义菜单
	 */
	public static void createMenu() throws Exception {
		// 创建菜单
		JSONObject buttonOn = new JSONObject();
		buttonOn.put("type", "click");
		buttonOn.put("name", "点灯");
//		buttonOn.put("key", CallbackService.V1001_LIGHT_ON);

		JSONObject buttonOff = new JSONObject();
		buttonOff.put("type", "click");
		buttonOff.put("name", "灭灯");
//		buttonOff.put("key", CallbackService.V1002_LIGHT_OFF);

		JSONArray buttons = new JSONArray();
		buttons.add(buttonOn);
		buttons.add(buttonOff);

		JSONObject menu = new JSONObject();
		menu.put("button", buttons);

		System.out.println("菜单：" + menu.toString());
		System.out.println("创建菜单返回：" + MpApi.menuCreate(menu.toString()));
		System.out.println("查询菜单：" + MpApi.menuQuery());
	}

	// public static void main (String args[]){
	// checkSignature("874567935632867327","14148702a2f2722eecd0e10e062b91c3b304b6ae","1461055279","973947297");
	// }

}
