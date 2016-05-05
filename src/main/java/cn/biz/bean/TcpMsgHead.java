package cn.biz.bean;

public class TcpMsgHead {

	private byte[] headtype = { 0x11 };

	private byte[] apivs = { 0, 0x1 };

	private byte[] clientvs = new byte[8];

	private byte[] msglength = new byte[4];

	private byte[] msgtype = new byte[2];

	private byte[] bxid = new byte[10];

	private byte[] other;

	public TcpMsgHead(byte[] msgbuf) {
		this.headtype = checkArray(msgbuf, 0, 0);
		this.apivs = checkArray(msgbuf, 1, 2);
		this.clientvs = checkArray(msgbuf, 3, 10);
		this.msglength = checkArray(msgbuf, 11, 14);
		this.msgtype = checkArray(msgbuf, 15, 16);
		this.bxid = checkArray(msgbuf, 17, 26);
		this.other = checkArray(msgbuf, 27, msgbuf.length-1);
	}

	public TcpMsgHead() {
	}

	public static byte[] checkArray(byte[] msgbuf, int limit, int big) {
		if (msgbuf.length <= limit || msgbuf.length < big) {
			return null;
		}
		if (limit == big) {
			return new byte[] { msgbuf[limit] };
		}
		int x = 0;
		if (limit > big) {
			x = limit;
			limit = big;
			big = x;
			x = 0;
		}
		byte[] rs = new byte[(big - limit) + 1];
		for (int i = limit; i <= big; i++) {
			rs[x] = msgbuf[i];
			x++;
		}
		return rs;
	}

	public byte[] getHeadtype() {
		return headtype;
	}

	public void setHeadtype(byte[] headtype) {
		this.headtype = headtype;
	}

	public byte[] getApivs() {
		return apivs;
	}

	public void setApivs(byte[] apivs) {
		this.apivs = apivs;
	}

	public byte[] getClientvs() {
		return clientvs;
	}

	public void setClientvs(byte[] clientvs) {
		this.clientvs = clientvs;
	}

	public byte[] getMsglength() {
		return msglength;
	}

	public void setMsglength(byte[] msglength) {
		this.msglength = msglength;
	}

	public byte[] getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(byte[] msgtype) {
		this.msgtype = msgtype;
	}

	public byte[] getBxid() {
		return bxid;
	}

	public void setBxid(byte[] bxid) {
		this.bxid = bxid;
	}

	public byte[] getOther() {
		return other;
	}

	public void setOther(byte[] other) {
		this.other = other;
	}

}
