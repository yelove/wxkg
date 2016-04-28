package cn.biz.enmu;

import cn.util.ByteArrayUtil;

public enum MsgType {
	regist(new byte[] { 0, 0x02 }), 
	regist_rs(new byte[] { 0, 0x03 }), 
	connect(new byte[] { 0, 0x04 }), 
	connect_rs(new byte[] { 0, 0x05 }), 
	heartbeat(new byte[] { 0, 0x06 }), 
	heartbeat_rs(new byte[] { 0, 0x07 }), 
	deviceopen_s2c(new byte[] { 0, 0x21 }), 
	deviceclose_s2c(new byte[] { 0, 0x23 }), 
	devicetype_c2s(new byte[] { 0, 0x26 });
	
	


	private byte[] value;

	private MsgType(byte[] vs) {
		this.value = vs;
	}

	public byte[] getValue() {
		return value;
	}
	
	public int getIntValue() {
		return ByteArrayUtil.byteArrayToInt(value);
	}

}
