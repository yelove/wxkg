package cn.biz.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import cn.biz.bean.TcpMsgHead;
import cn.biz.enmu.MsgType;
import cn.util.ByteArrayUtil;
import cn.util.UUID32;

@Service
public class MessageService {

	private static ConcurrentHashMap<byte[], byte[]> bxidcodemap = new ConcurrentHashMap<byte[], byte[]>();

	private static ConcurrentHashMap<byte[], byte[]> bxidtokenmap = new ConcurrentHashMap<byte[], byte[]>();

	public TcpMsgHead testService(TcpMsgHead tmh) {
		TcpMsgHead rtmh = new TcpMsgHead();
		if (MsgType.regist.getIntValue() == ByteArrayUtil.byteArrayToInt(tmh.getMsgtype())) {
			cpTmh(rtmh, tmh);
			rtmh.setMsgtype(MsgType.regist_rs.getValue());
			byte[] bxcode = UUID32.generate().getBytes();
			byte[] token = UUID32.generate().getBytes();
			bxidcodemap.put(tmh.getBxid(), bxcode);
			bxidtokenmap.put(tmh.getBxid(), token);
			rtmh.setOther(ByteArrayUtil.ogByteArray(bxcode, token));
			rtmh.setMsglength(ByteArrayUtil.intToByteArray(91, 4));

		} else if (MsgType.connect.getIntValue() == ByteArrayUtil.byteArrayToInt(tmh.getMsgtype())) {
			cpTmh(rtmh, tmh);
			rtmh.setMsgtype(MsgType.connect_rs.getValue());
			byte[] bxcode = bxidcodemap.get(tmh.getBxid()) == null ? UUID32.generate().getBytes()
					: bxidcodemap.get(tmh.getBxid());
			byte[] token = bxidtokenmap.get(tmh.getBxid()) == null ? UUID32.generate().getBytes()
					: bxidtokenmap.get(tmh.getBxid());
			rtmh.setOther(ByteArrayUtil.ogByteArray(bxcode, token));
			rtmh.setMsglength(ByteArrayUtil.intToByteArray(91, 4));
		} else if (MsgType.heartbeat.getIntValue() == ByteArrayUtil.byteArrayToInt(tmh.getMsgtype())) {
			cpTmh(rtmh, tmh);
			rtmh.setMsgtype(MsgType.heartbeat_rs.getValue());
			byte[] bxcode = bxidcodemap.get(tmh.getBxid()) == null ? UUID32.generate().getBytes()
					: bxidcodemap.get(tmh.getBxid());
			byte[] token = bxidtokenmap.get(tmh.getBxid()) == null ? UUID32.generate().getBytes()
					: bxidtokenmap.get(tmh.getBxid());
			rtmh.setOther(ByteArrayUtil.ogByteArray(bxcode, token));
			rtmh.setMsglength(ByteArrayUtil.intToByteArray(91, 4));
		}
		return rtmh;
	}


	private void cpTmh(TcpMsgHead rtmh, TcpMsgHead tmh) {
		rtmh.setHeadtype(tmh.getHeadtype());
		rtmh.setClientvs(tmh.getClientvs());
		rtmh.setApivs(tmh.getApivs());
		rtmh.setBxid(tmh.getBxid());
	}

	public static void main(String args[]) {
		// UUID uuid = UUID.randomUUID();
		// byte [] a =
		// (String.valueOf(System.currentTimeMillis())+uuid).getBytes();
		byte[] a = UUID32.generate().getBytes();
		System.out.println(a.length);

	}

}
