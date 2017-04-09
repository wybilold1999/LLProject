package com.cyanbirds.lljy.db;

import android.content.Context;

import com.cyanbirds.lljy.db.base.DBManager;
import com.cyanbirds.lljy.entity.IMessage;
import com.cyanbirds.lljy.greendao.IMessageDao;
import com.cyanbirds.lljy.listener.MessageUnReadListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collections;
import java.util.List;

/**
 * 作者：wangyb
 * 时间：2016/8/30 14:08
 * 描述：
 */
public class IMessageDaoManager extends DBManager {

	private static IMessageDaoManager mInstance;
	private IMessageDao mIMessageDao;

	private IMessageDaoManager(Context context) {
		super(context);
		mIMessageDao = getDaoSession().getIMessageDao();
	}

	/**
	 * 获取单例引用
	 *
	 * @param context
	 * @return
	 */
	public static IMessageDaoManager getInstance(Context context) {
		if (mInstance == null) {
			synchronized (IMessageDaoManager.class) {
				if (mInstance == null) {
					mInstance = new IMessageDaoManager(context);
				}
			}
		}
		return mInstance;
	}
	
	/**
	 * 插入一条记录
	 * @param message
	 */
	public long insertIMessage(IMessage message) {
		long id = mIMessageDao.insertOrReplace(message);
		MessageUnReadListener.getInstance().notifyDataSetChanged(0);
		return id;
	}
	
	/**
	 * 插入消息集合
	 * @param messages
	 */
	public void insertIMessageList(List<IMessage> messages) {
		if (messages == null || messages.isEmpty()) {
			return;
		}
		MessageUnReadListener.getInstance().notifyDataSetChanged(0);
		mIMessageDao.insertInTx(messages);
	}
	
	/**
	 * @param message
	 */
	public void deleteIMessage(IMessage message) {
	}

	/**
	 * 根据会话id删除消息
	 * @param conversationId
	 */
	public void deleteIMessageByConversationId(long conversationId) {
		QueryBuilder<IMessage> qb = mIMessageDao.queryBuilder();
		qb.where(IMessageDao.Properties.ConversationId.eq(conversationId));
		List<IMessage> messageList = qb.list();
		mIMessageDao.deleteInTx(messageList);
	}
	
	/**
	 * 更新一条记录
	 *
	 * @param message
	 */
	public void updateIMessage(IMessage message) {
		mIMessageDao.update(message);
	}

	/**
	 * 根据msgId查询消息
	 * @param msgId
	 * @return
	 */
	public long queryIMessageByMsgId(String msgId) {
		QueryBuilder<IMessage> qb = mIMessageDao.queryBuilder();
		qb.where(IMessageDao.Properties.MsgId.eq(msgId));
		return qb.count();
	}
	
	/**
	 * 查询消息列表
	 */
	public List<IMessage> queryAllIMessageList() {
		/*QueryBuilder<IMessage> qb = mIMessageDao.queryBuilder();
		List<IMessage> list = qb.list();
		return list;*/
		return null;
	}

	/**
	 * 查询最近pageSize条消息
	 */
	public List<IMessage> queryIMessageList(String conversationId, int pageSize, String time) {
		QueryBuilder<IMessage> qb = mIMessageDao.queryBuilder();
		qb.where(IMessageDao.Properties.Create_time.lt(time),
				IMessageDao.Properties.ConversationId.eq(conversationId))
				.orderDesc(IMessageDao.Properties.Create_time).limit(pageSize);
		List<IMessage> list = qb.list();
		Collections.reverse(list);
		return list;
	}

	/**
	 * 退出的时候调用此方法
	 */
	public static void reset() {
		release();
		mInstance = null;
	}
}
