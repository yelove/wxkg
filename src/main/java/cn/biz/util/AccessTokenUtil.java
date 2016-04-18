package cn.biz.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.biz.bean.AccessToken;

/**
 * @author hasee
 *
 */
@Component
public class AccessTokenUtil {

	private final Logger logger = LogManager.getLogger(AccessTokenUtil.class.getName());

	private static AccessToken token;

	public static AccessToken getToken() {
		return token;
	}

	private static void setToken(AccessToken token) {
		AccessTokenUtil.token = token;
	}

	// 刷新凭证并更新全局凭证值
	private static void refreshToken() {
		try {
			AccessToken accessToken = MpApi.getAccessToken();
			setToken(accessToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "0 0 */1.5 * * ?")
	public void run() {
		logger.info("AccessTokenUtil trigger...");
		AccessToken tk = getToken();
		if (null == tk) {
			refreshToken();
		} else {
			if ((System.currentTimeMillis() - tk.getCreateTime()) > 200000) {
				refreshToken();
			}
		}
	}

	/**
	 * 刷新并返回新凭证
	 */
	public static String refreshAndGetToken() {
		AccessToken tk = getToken();
		// 20秒之内只刷新一次，防止并发引起的多次刷新
		if (tk == null || (System.currentTimeMillis() - tk.getCreateTime() > 20000)) {
			refreshToken();
		}
		return getTokenStr();
	}

	/**
	 * 获取凭证
	 */
	public static String getTokenStr() {
		return getToken().getAccess_token();
	}

}
