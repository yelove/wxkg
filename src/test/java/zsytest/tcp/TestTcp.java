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
			System.out.println("发起注册请求："+System.currentTimeMillis());
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
			TcpMsgHead restmh = new TcpMsgHead(bta);
			System.out.println("注册响应："+ByteArrayUtil.bytesToHexString(bta));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			restmh.setMsgtype(MsgType.heartbeat.getValue());
			System.out.println("发送心跳请求："+System.currentTimeMillis());
			bos.write(ByteArrayUtil.ogByteArray(restmh.getHeadtype(), restmh.getApivs(), restmh.getClientvs(),
					restmh.getMsglength(), restmh.getMsgtype(), restmh.getBxid(),restmh.getOther()));
			byte[] btax = new byte[91];
			dis.read(btax);
//			TcpMsgHead restmh2 = new TcpMsgHead(btax);
			System.out.println("心跳响应："+ByteArrayUtil.bytesToHexString(btax));
			bos.flush();
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
