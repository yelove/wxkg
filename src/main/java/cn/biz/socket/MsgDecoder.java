/**
 * 报文解码器
 */
package cn.biz.socket;

import java.util.List;

import org.apache.log4j.Logger;

import cn.biz.bean.TcpMsgHead;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author zhangsy
 * 
 */
public final class MsgDecoder extends ByteToMessageDecoder {

	/**
	 * logger
	 */
	private final Logger logger = Logger.getLogger(MsgDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int length = in.readableBytes();
		logger.info("length = " + length);
		if(length==0){
			return;
		}
		byte[] readBytes = null;
		readBytes = new byte[length];
		in.readBytes(readBytes);
		out.add(new TcpMsgHead(readBytes));
	}

}
