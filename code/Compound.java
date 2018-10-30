

public class Compound {

	private String molStructure="";
	private String [] compoundNames=null;
	private String compoundName="";
	private int compoundID=0;
	private String compoundKey="";
	private String inchi="";
	private String pmid="";
	public String getPmid() {
		return pmid;
	}
	public void setPmid(String pmid) {
		this.pmid = pmid;
	}
	public int getCompoundID() {
		return compoundID;
	}
	public void setCompoundID(int compoundID) {
		this.compoundID = compoundID;
	}
	public String getCompoundKey() {
		return compoundKey;
	}
	public void setCompoundKey(String compoundKey) {
		this.compoundKey = compoundKey;
	}
	public String[] getCompoundNames() {
		return compoundNames;
	}
	public void setCompoundNames(String [] compoundNames) {
		this.compoundNames = compoundNames;
	}
	public String getInchi() {
		return inchi;
	}
	public void setInchi(String inchi) {
		this.inchi = inchi;
	}
	public String getMolStructure() {
		return molStructure;
	}
	public void setMolStructure(String molStructure) {
		this.molStructure = molStructure;
	}
	public String getCompoundName() {
		return compoundName;
	}
	public void setCompoundName(String compoundName) {
		this.compoundName = compoundName;
	}

	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer("id::-"+this.compoundID);
		if(this.compoundNames!=null){
			for(String str: this.compoundNames){
				sb.append(str+"; ");
			}
		}
		return sb.toString();
	}
}
