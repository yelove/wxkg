package zsytest.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import cn.biz.bean.TcpMsgHead;
import cn.biz.enmu.MsgType;
import cn.util.ByteArrayUtil;

public class TestTcp {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("119.29.151.158", 17397);
			// 向服务器端发送数据
			OutputStream os = socket.getOutputStream();
			DataOutputStream bos = new DataOutputStream(os);
			TcpMsgHead rtmh = new TcpMsgHead();
			rtmh.setClientvs(new byte[]{0,0,0,0,0,0,1,1});
			rtmh.setMsglength(ByteArrayUtil.int2byte(27));
			rtmh.setMsgtype(MsgType.regist.getValue());
			rtmh.setBxid(new byte[]{0,0,0,0,0,0,1,1,1,1});
			bos.write(ByteArrayUtil.ogByteArray(rtmh.getHeadtype(), rtmh.getApivs(), rtmh.getClientvs(),
					rtmh.getMsglength(), rtmh.getMsgtype(), rtmh.getBxid()));
			bos.flush();
			// 接收服务器端数据
			InputStream is = socket.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			byte[] bta = new byte[91];
			dis.read(bta);
			System.out.println(ByteArrayUtil.bytesToHexString(bta));
			dis.close();
			is.close();
			bos.close();
			os.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
