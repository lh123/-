package com.lh.flux.model.entity;
import com.alibaba.fastjson.annotation.*;

public class WelfareEnvelopEntity
{
	@JSONField(name="__v")
	private int v;
	@JSONField(name="_id")
	private String id;
	private String description;
	private boolean enabled;
	private String itemId;
	private String logoUrl;
	private String name;
	private int prizeValue;
	private String slogan;
	private String type;

	public void setV(int v)
	{
		this.v = v;
	}

	public int getV()
	{
		return v;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setItemId(String itemId)
	{
		this.itemId = itemId;
	}

	public String getItemId()
	{
		return itemId;
	}

	public void setLogoUrl(String logoUrl)
	{
		this.logoUrl = logoUrl;
	}

	public String getLogoUrl()
	{
		return logoUrl;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setPrizeValue(int prizeValue)
	{
		this.prizeValue = prizeValue;
	}

	public int getPrizeValue()
	{
		return prizeValue;
	}

	public void setSlogan(String slogan)
	{
		this.slogan = slogan;
	}

	public String getSlogan()
	{
		return slogan;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
}
