package com.fstudio.servlet.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.fstudio.DateTool;

/**
 * 用户访问单词的行为
 * @author user
 *
 */
public class UsrAccessInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2472081306986990302L;

	private Date currentDate; //只考虑当前的时间, 时间变化, 则整个数据清空
	
	private Map<String, Integer> wordMap; //单词访问的数据统计
	
	private int times;
	
	public UsrAccessInfo(Date date, Map<String, Integer> wordMap)
	{
		this.currentDate = date;
		this.wordMap = wordMap;
	}
	
	
	public void putWordWithIndex(String word, int index)
	{
		wordMap.put(word, index);
		times++;
	}
	
	public void updateDate(Date newDate)
	{
		//如果是今天的数据, 则不更新
		if(needClear(currentDate, newDate))
		{
			clean();
		}
		currentDate = newDate;
	}
	
	private void clean()
	{
		times = 0;
		wordMap.clear();
	}
	
	/**
	 * 什么时候需要清理数据
	 * @param oldD
	 * @param newD
	 * @return
	 */
	private boolean needClear(Date oldD, Date newD)
	{
		String  FORMAT_TIME_EN_TIRE = "yyyy-MM-dd";
		String oString = DateTool.Date2String(oldD, FORMAT_TIME_EN_TIRE);
		String nString = DateTool.Date2String(newD, FORMAT_TIME_EN_TIRE);
		if (!oString.equals(nString))
		{
			return true;
		}
		return false;
	}
	
	
	public Map<String, Integer> getWordMap() {
		return wordMap;
	}
	
	public Integer getIndex4word(String word)
	{
		return wordMap.get(word);
	}

	public int getTimes()
	{
		return times;
	}

}
