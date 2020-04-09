package icesi.vip.alien.model.pertvscpm;

public enum DistributionType
{
	NORMAL("Normal"),
	BETA("Beta"),
	LOG_NORMAL("Log Normal"),
	UNIFORM("Uniform"), 
	TRIANGULAR("Triangular");
	
	private String distributionType;
	
	DistributionType(String distType)
	{
		distributionType=distType;
	}
	
	public String getDistributionType()
	{
		return distributionType;
	}
}
