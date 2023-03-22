package com.ds.screw.auth.sso.util;


import com.ds.screw.auth.AuthManager;
import com.ds.screw.auth.sso.SsoManager;
import com.ds.screw.auth.util.AuthFoxUtils;

/**
 * <p>
 *      单点登录模块 工具类
 * </p>
 *
 * @author dongsheng
 */
public class SsoTicketUtils {
	private SsoTicketUtils(){}

	// ---------------------- Ticket 操作 ---------------------- 
	/**
	 * 根据 账号id 创建一个 Ticket码 
	 * @param ssoAccount 账号id
	 * @return Ticket码
	 */
	public static String createTicket(String ssoAccount) {
		// 创建 Ticket
		String ticket = randomTicket();

		// 保存 Ticket
		saveTicket(ticket, ssoAccount);
		saveTicketIndex(ticket, ssoAccount);

		// 返回 Ticket
		return ticket;
	}

	/**
	 * 保存 Ticket 
	 * @param ticket ticket码
	 * @param ssoAccount 账号id
	 */
	public static void saveTicket(String ticket, String ssoAccount) {
		long ticketTimeout = SsoManager.getConfig().getTicketTimeout();
		AuthManager.getAuthRepository().set(splicingTicketSaveKey(ticket), String.valueOf(ssoAccount), ticketTimeout);
	}

	/**
	 * 保存 Ticket 索引 
	 * @param ticket ticket码
	 * @param ssoAccount 账号id
	 */
	public static void saveTicketIndex(String ticket, String ssoAccount) {
		long ticketTimeout = SsoManager.getConfig().getTicketTimeout();
		AuthManager.getAuthRepository().set(splicingTicketIndexKey(ssoAccount), String.valueOf(ticket), ticketTimeout);
	}

	/**
	 * 删除 Ticket
	 * @param ticket Ticket码
	 */
	public static void deleteTicket(String ticket) {
		if(ticket == null) {
			return;
		}
		AuthManager.getAuthRepository().delete(splicingTicketSaveKey(ticket));
	}

	/**
	 * 删除 Ticket索引
	 * @param ssoAccount 账号id
	 */
	public static void deleteTicketIndex(String ssoAccount) {
		if(ssoAccount == null) {
			return;
		}
		AuthManager.getAuthRepository().delete(splicingTicketIndexKey(ssoAccount));
	}

	/**
	 * 根据 Ticket码 获取账号id，如果Ticket码无效则返回null
	 * @param ticket Ticket码
	 * @return 账号id
	 */
	public static String getSsoAccount(String ticket) {
		if(AuthFoxUtils.isEmpty(ticket)) {
			return null;
		}
		return (String) AuthManager.getAuthRepository().get(splicingTicketSaveKey(ticket));
	}

	/**
	 * 校验ticket码，获取账号id，如果此ticket是有效的，则立即删除
	 * @param ticket Ticket码
	 * @return 账号id
	 */
	public static String checkTicket(String ticket) {
		String ssoAccount = getSsoAccount(ticket);
		if(ssoAccount != null) {
			deleteTicket(ticket);
			deleteTicketIndex(ssoAccount);
		}
		return ssoAccount;
	}

	/**
	 * 随机一个 Ticket码 
	 * @return Ticket码
	 */
	public static String randomTicket() {
		return AuthFoxUtils.getRandomString(64);
	}


	// ------------------- 返回相应key -------------------

	/**
	 * 拼接key：Ticket 查 账号Id
	 * @param ticket ticket值
	 * @return key
	 */
	private static String splicingTicketSaveKey(String ticket) {
		return AuthManager.getConfig().getTokenName() + ":ticket:" + ticket;
	}

	/**
	 * 拼接key：账号Id 反查 Ticket 
	 * @param ssoAccount 账号id
	 * @return key
	 */
	private static String splicingTicketIndexKey(String ssoAccount) {
		return AuthManager.getConfig().getTokenName() + ":sso-account-ticket:" + ssoAccount;
	}

}
