package cn.biz.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.biz.bean.OPID_AccessToken;
import cn.biz.service.CallbackService;
import cn.biz.util.MpApi;
import cn.common.CommonStr;
import cn.consts.WxConfig;
import cn.util.SHA1;

@Controller
public class ViewAction {

	private Logger log = LogManager.getLogger(ViewAction.class);

	@Autowired
	private CallbackService callbackService;

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

	@RequestMapping(value = "wxcheck", method = { RequestMethod.GET }, produces = "text/html;charset=UTF-8")
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

	@RequestMapping(value = "wxcheck", method = { RequestMethod.POST }, produces = "text/html;charset=UTF-8")
	public void wxcheckpost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		if (StringUtils.isEmpty(signature)) {
			signature = "";
		}
		if (StringUtils.isEmpty(timestamp)) {
			timestamp = "";
		}
		if (StringUtils.isEmpty(nonce)) {
			nonce = "";
		}
		log.info("param is {},{},{}", signature, timestamp, nonce);
		if (checkSignature(signature, timestamp, nonce, "")) {
			// 解析xml
			Map<String, String> reqMap = parseXml(req.getInputStream());
			System.out.println("reqMap=" + reqMap);

			// 处理请求
			String xmlStr = callbackService.handle(reqMap);

			System.out.println("xmlStr=" + xmlStr);

			// null 转为空字符串
			xmlStr = xmlStr == null ? "" : xmlStr;

			out(xmlStr, resp);
		}
		out("", resp);
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
		// buttonOn.put("key", CallbackService.V1001_LIGHT_ON);

		JSONObject buttonOff = new JSONObject();
		buttonOff.put("type", "click");
		buttonOff.put("name", "灭灯");
		buttonOff.put("key", CallbackService.V1002_LIGHT_OFF);

		JSONObject wenduquery = new JSONObject();
		wenduquery.put("type", "view");
		wenduquery.put("name", "温度查询");
		wenduquery.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + "wxc921557dbb941a02"
				+ "&redirect_uri=http%3A%2F%2Fwww.zsy666.com%2Fwxkg%2Fwxview&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
		JSONArray querysb = new JSONArray();
		querysb.add(wenduquery);
		JSONObject query = new JSONObject();
		query.put("name", "查询");
		query.put("sub_button", querysb);

		JSONArray buttons = new JSONArray();
		buttons.add(query);
		buttons.add(buttonOff);

		JSONObject menu = new JSONObject();
		menu.put("button", buttons);

		System.out.println("菜单：" + menu.toString());
		System.out.println("创建菜单返回：" + MpApi.menuCreate(menu.toString()));
		// System.out.println("查询菜单：" + MpApi.menuQuery());
	}

	@RequestMapping(value = "delmenu", method = { RequestMethod.GET }, produces = "text/html;charset=UTF-8")
	public void deletMenu() {
		MpApi.menuDelete();
		log.info("menuDelete");
	}

	public static void main(String args[]) {
//		MpApi.menuDelete();
		try {
			createMenu();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// checkSignature("874567935632867327","14148702a2f2722eecd0e10e062b91c3b304b6ae","1461055279","973947297");
	}

	/**
	 * 解析请求中的xml元素为Map
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> parseXml(InputStream in) throws DocumentException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		Document document = reader.read(in);
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		return map;
	}

}
