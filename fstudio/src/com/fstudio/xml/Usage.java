package com.fstudio.xml;

/**
 * 单词使用
 * @author tiant5
 *
 */
public class Usage
{
	//来源类型
	int type = -1;
	//标注来源备注
	String remark = "";
	//英语例句
	private String engSent = "";
	//中文解释
	private String chsSent = "";
	
	public Usage()
	{
	}
	
	
	
	public void setEngSent(String engSent) {
		this.engSent = engSent;
	}



	public void setChsSent(String chsSent) {
		this.chsSent = chsSent;
	}



	public boolean isValideUage()
	{
		if (engSent == null)
		{
			return false;
		}
		
		if (chsSent == null)
		{
			return false;
		}
			
		return true;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getEngSent() {
		return engSent;
	}

	public String getChsSent() {
		return chsSent;
	}
}

