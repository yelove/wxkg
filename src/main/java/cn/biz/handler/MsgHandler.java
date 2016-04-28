/**
 * 
 */
package cn.biz.handler;

import org.apache.log4j.Logger;

import cn.biz.bean.TcpMsgHead;
import cn.biz.service.MessageService;
import cn.util.ByteArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhanght.fnst
 * 
 */
public final class MsgHandler extends ChannelHandlerAdapter {

	/**
	 * logger
	 */
	private final Logger logger = Logger.getLogger(MsgHandler.class);

	/**
	 * MessageService
	 */
	private MessageService messageService;

	/**
	 */
	public MsgHandler(MessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		logger.info("There is a new connection...");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		TcpMsgHead message = (TcpMsgHead) msg;
		TcpMsgHead rtmh = messageService.testService(message);
		final ByteBuf bb = ctx.alloc().buffer(ByteArrayUtil.byte2int(rtmh.getMsglength()));
		bb.writeBytes(ByteArrayUtil.ogByteArray(rtmh.getHeadtype(), rtmh.getApivs(), rtmh.getClientvs(),
				rtmh.getMsglength(), rtmh.getMsgtype(), rtmh.getBxid(), rtmh.getOther()));
		ctx.writeAndFlush(bb);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		logger.warn("Error From = " + ctx.channel().remoteAddress().toString() + "-->" + cause.getMessage());
		ctx.close();
	}

}
