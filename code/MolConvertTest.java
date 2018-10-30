

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;








public class MolConvertTest {

	private static PrintStream ps1=null;
	private static PrintStream ps2=null;
	private static PrintStream ps3=null;
	private static PrintStream ps4=null;

	private static ArrayList<String>std_compounds_usedList=new ArrayList<String>();
	private static String projectDatabaseName="MEDCHEM_PROJECT_NEW";
	private static String databaseName="MEDCHEM";
	private static final String STD_COMPOUND_TABLE="STD_COMPOUNDS";
	private static final String STD_COMPOUND_JNP_TABLE="STD_COMPOUNDS_JNP";


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{

		MolConvert molConert=new MolConvert();
		String outputPath="./../Output";
		ps1=new PrintStream(new FileOutputStream(outputPath+"/AcrossPmid.txt"));
		ps2=new PrintStream(new FileOutputStream(outputPath+"/WithinPmid.txt"));
		ps3=new PrintStream(new FileOutputStream(outputPath+"/StdCompoundCheck.xls"));
		               System.out.println("MolconvertTestOutput>>>>>>>>>>>>>>>>>> "+outputPath);

        databaseName=args[0];
		String pmidlist=args[1];

	/*	String pmidlist="10.1021/ml100170e,22172704,22560474,22575050,10.1021/ml300047h,10.1021/ml300083p,10.1021/ml3001215,21779519,21549456,21413800,21978284,10.1021/jm100064d,10.1021/jm101168r,10.1021/jm101199t,21755948,21599003,21824690,21664827,22995621,22999416,22995619,22995624,22995623,22989532,22981333,22981334,22975302,22981330,22989530,22995617,22963763,22975299,22967765,22981335,22981328,22963764,22975301,22981332,22967764,22981331,22995620,22959246,22995622,22672799,22087535,22352782,22932312,22954737,22951255,22917858,21955455,21571530,21928824,21774525,21291235,21280635,21800826,21797275,21780776,21851089,21875807,21450376,21497424,21353728,21531054,21802798,21664729,21959232,21568322,21661758,21684744,21919481,22071520,22039920,22999415,22985854,10.1021/ml100170e,22989912,22938093,22959249,22548342,22537153,22676247,22642300,22621689,22578073,22642365,22686608,22676210,22650244,22591402,22642259,22642319,22691057,22686657,23085140,23123727,23123728,23124210,23124211,23124212,23124214,23124215,23124217,23124218,23085771,23124219,23127987,23127989,23131541,23131542,23085772,23137448,23149297,23142674,23142675,23149298,23149299,23153810,23151320,23153812,23153813,23151321,23151322,23159805,23159806,23164656,23164657,23164660,23088933,23092906,23108363,23123726,10.1021/ml300221t,10.1021/ml300194f,10.1021/ml300195b,10.1021/ml300199d,10.1021/ml300200p,10.1021/ml300201e,10.1021/ml300074j,10.1021/ml300097g,10.1021/ml300129b,10.1021/ml300133f,10.1021/ml3001352,10.1021/ml300134b,10.1021/ml300149z,10.1021/ml300168e,10.1021/ml3001616,10.1021/ml300175c,10.1021/ml3001787,23066712,23057874,23046414,23046341,23043498,23039861,23035772,23031087,23030848,23030826,23025417,23025386,23025282,23013356,23013292,23002924,23002902,23006147,10.1021/ml300234y,10.1021/ml300144n,10.1021/ml300172p,10.1021/ml3001775,10.1021/ml3001984,10.1021/ml300212a,10.1021/ml3000148,10.1021/ml3001165,10.1021/ml3001825,10.1021/ml300256p,10.1021/ml300316d,10.1021/ml300317n,10.1021/ml300318y,10.1021/ml300323y,10.1021/ml300324q,10.1021/ml3003378,10.1021/ml300338u,10.1021/ml300339s,10.1021/ml300340r,10.1021/ml300341j,21899370,22014751,22537178,22691154,22612268,22725979,22694121,10.1021/ml3002488";*/

		String [] pmids=pmidlist.split(",");
		Vector comp_act_kine_list =null;
		Map<String, String> duplicateCompoundnameMap=null;
		Map<String ,String> inchiresultMap=null;
		for(String pmid:pmids){
System.out.println("pmid::\t"+pmid);
			comp_act_kine_list = compound_activitydata(pmid);

			String res=checkCompoundInchiDuplicate(comp_act_kine_list,molConert);
			if(!"".equals(res))
				ps2.println(pmid+"-->"+res);
		}


		duplicateCompoundnameMap=getSameCompoundNameAcrossPMID(pmids);
		System.out.println("No of Duplicate compounds acrossPMID:\t"+duplicateCompoundnameMap.size());

		CheckInchiForDuplicateNames(duplicateCompoundnameMap, molConert);
		CheckWithStandardCompound(pmids, STD_COMPOUND_TABLE, ps3);
		System.out.println("No of Std compounds found matching are:\t"+std_compounds_usedList.size());
		std_compounds_usedList.clear();
		if(databaseName.equals("MEDCHEM")){
			ps4=new PrintStream(new FileOutputStream(outputPath+"/StdCompoundCheck_JNP.xls"));
		CheckWithStandardCompound(pmids, STD_COMPOUND_JNP_TABLE, ps4);
		System.out.println("No of Std compounds jnp found matching are:\t"+std_compounds_usedList.size());

		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(ps1!=null)
				    ps1.close();

				if(ps2!=null)
				   ps2.close();

				if(ps3!=null)
				   ps3.close();

				if(ps4!=null)
				  ps4.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void CheckWithStandardCompound(String []pmids, String stdCompoundTable, PrintStream ps){


		try{

			ps.println("pmid\tcompoundID\tcompoundName\tcompoundKey\tMessage");
			for(String pmid:pmids){

			System.out.println("pmid>>>>>>>>>>>>>>>>"+pmid)	;		
	ArrayList<Compound> compoundList=getCompoundList(pmid);

				for(Compound compound:compoundList){



					String inchiCompare=CheckStdCompound(compound, stdCompoundTable);

					if(inchiCompare.equals("different")){

					ps.println(compound.getPmid()+"\t"+compound.getCompoundID()+"\t"+compound.getCompoundName()+"\t"+compound.getCompoundKey()+"\tStructure mismatched with the standard database");


					}
				}
			}



		}catch (Exception e) {
			e.printStackTrace();
		}


		}

		public static String CheckStdCompound(Compound compound, String stdCompoundTable){
			ResultSet rs = null;
			Statement stmt=null;
			Connection con=null;
			String res="";
			String sql="";
			MolConvert molConvert=new MolConvert();
			try{
				Class.forName("com.mysql.jdbc.Driver");
				con=DriverManager.getConnection("jdbc:mysql://178.79.186.74:3306/MEDCHEM_PROJECT_NEW","root","2k51125_MCPL");
				if(!compound.getCompoundName().equals("NA")){

					 sql="SELECT inchi,compound_name  FROM MEDCHEM_PROJECT_NEW."+stdCompoundTable+" where (compound_name='"+compound.getCompoundName().replaceAll("'", "''")+"')";

					 stmt=con.createStatement();
					rs=stmt.executeQuery(sql);

					if(rs.next()){


						if(!rs.getString("inchi").equals(molConvert.getInchiFromMol(compound.getMolStructure()))){
							res="different";
							//System.out.println(sql);
							//System.out.println(compound.getCompoundName());

						}else if(!std_compounds_usedList.contains(rs.getString("compound_name"))){
							std_compounds_usedList.add(rs.getString("compound_name"));
						}


					}
				}
				if(!compound.getCompoundKey().equals("NA")){
					sql="SELECT inchi,compound_name FROM MEDCHEM_PROJECT_NEW."+stdCompoundTable+" where (compound_name='"+compound.getCompoundKey().replaceAll("'", "''")+"')";
					stmt=con.createStatement();
					rs=stmt.executeQuery(sql);
					if(rs.next()){

						if(!rs.getString("inchi").equals(molConvert.getInchiFromMol(compound.getMolStructure()))){
							res="different";
						//	System.out.println(sql);

							//System.out.println(compound.getCompoundName());
						}else if(!std_compounds_usedList.contains(rs.getString("compound_name"))){
							std_compounds_usedList.add(rs.getString("compound_name"));
						}
					}

				}


			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					if(con!=null)
					con.close();
					if(stmt!=null)
					stmt.close();
					if(rs!=null)
					rs.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			return res;
		}
		public static ArrayList<Compound> getCompoundList(String pmid){

			ResultSet rs = null;
			Statement stmt=null;
			Connection con=null;
			Compound compound=null;
			ArrayList<Compound> compoundList=new ArrayList<Compound>();
			try{

				Class.forName("com.mysql.jdbc.Driver");
				con=DriverManager.getConnection("jdbc:mysql://178.79.186.74:3306/"+databaseName,"root","2k51125_MCPL");
				String sql="SELECT compound_id,pubmed_id,name,structure,reference  FROM "+databaseName+".Compound_Master where pubmed_id='"+pmid+"'";

				stmt=con.createStatement();
				rs=stmt.executeQuery(sql);
				while(rs.next()){
					compound=new Compound();
					compound.setCompoundID(Integer.valueOf(rs.getString("compound_id")));
					compound.setPmid(rs.getString("pubmed_id"));
					compound.setCompoundName(rs.getString("name"));

					compound.setCompoundKey(rs.getString("reference"));
					compound.setMolStructure(rs.getString("structure"));
					compoundList.add(compound);
				}

			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					if(con!=null)
						con.close();
						if(stmt!=null)
						stmt.close();
						if(rs!=null)
						rs.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}








			return compoundList;
		}


	 public static String checkCompoundInchiDuplicate(Vector comp_act_kine_list,MolConvert molConvert) {
	    	String res="";
	    	String inchi="";
	    	Map<String, String> map=new HashMap<String, String>();
	    	List<String> list=new ArrayList<String>();

	    	for(int j = 0 ; j < comp_act_kine_list.size(); j++){
	            //  RETERIVE THE COMPOUND_ACTIVITY_KINETICS OBJECT FROM THE COMP_ACT_KINE VECTOR.

	           Map<String,String> comp_act_kine = (Map <String,String> )comp_act_kine_list.elementAt(j);
	           Set<String> mapset=comp_act_kine.keySet();

	           for(String key:mapset){
	        	   try{
                        System.out.println("Key::\t"+key);

	        	 inchi=molConvert.getInchiFromMol(comp_act_kine.get(key));

	        	   }catch (Exception e) {
					System.out.println("Error"+key);
				}
	        	  // System.out.println(inchi);
	        	   //if(list.contains(inchi))
	        		   //System.out.println("iiii"+inchi);
	        	   //list.add(inchi);
		            if(!map.containsKey(inchi))
		            	map.put(inchi,key);
		            else
		            	map.put(inchi,map.get(inchi)+"--"+key);
	           }




}
	    	Iterator<String> it=map.keySet().iterator();
			String key="";
			while(it.hasNext()){
				key=it.next();
				if(map.get(key).contains("--")){
					if("".equals(res))
					res=map.get(key);
					else
						res=res+","+map.get(key);

				}

			}

	    	return res;
	    }


 public static void CheckInchiForDuplicateNames(Map<String, String> duplicateCompoundnameMap, MolConvert molConvert){
		 ResultSet rs = null;
		Statement stmt=null;
		Connection con=null;
		StringBuffer result=null;
		String name="";
		String compoundIDs="";
		String res="";
		String_Utility string_Utility=new String_Utility();
		Map<String, String> map=null;
		String inchi="";
		String compoundID="";
		ps1.println("compoundName\tcompoundID\tpmid");
		Map<String,String> compoundIdPmidMap=new HashMap<String, String>();
		 try{
				Class.forName("com.mysql.jdbc.Driver");
				con=DriverManager.getConnection("jdbc:mysql://178.79.186.74:3306/"+databaseName,"root","2k51125_MCPL");
			 for(Map.Entry<String, String> entry: duplicateCompoundnameMap.entrySet()){
				 result=new StringBuffer();
				 name=entry.getKey();
				 compoundIDs=entry.getValue();
				 map=new LinkedHashMap<String, String>();
					String sql="SELECT compound_id, pubmed_id,name,structure FROM "+databaseName+".Compound_Master where compound_id IN ("+string_Utility.getQuerySting(compoundIDs)+")";
					stmt=con.createStatement();
					rs=stmt.executeQuery(sql);
					while(rs.next()){

						compoundID=rs.getString("compound_id");
						inchi=molConvert.getInchiFromMol(rs.getString("structure"));
						compoundIdPmidMap.put(compoundID, rs.getString("pubmed_id"));
						//System.out.println(inchi +compoundID);
						if(!map.containsKey(inchi))
							map.put(inchi,compoundID);
				        else
				          	map.put(inchi,map.get(inchi)+"--"+compoundID);




					}
					//result.append(rs.getString("name")+"\t"+compoundID+"\t"+rs.getString("pubmed_id")+"\n");

					Set<String> set=map.keySet();
					if(set.size()>1){
						ps1.println(name);
						for(String key:set){
							ps1.println(" ");
							ps1.println(key);
							String [] compoundids=map.get(key).split("\\-\\-");
							for(String compoundid:compoundids){
								ps1.println(compoundid+"\t"+compoundIdPmidMap.get(compoundid));
							}

						}
						ps1.println("--------------------");
					}





			 }


		}catch (Exception e) {
			e.printStackTrace();
		}finally{

			try{
			if(con!=null)
				con.close();
				if(stmt!=null)
				stmt.close();
				if(rs!=null)
				rs.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}



	 }

 public static Map<String, String> getSameCompoundNameAcrossPMID(String [] pmids){
	 ResultSet rs = null;
	Statement stmt=null;
		Connection con=null;
		Map<String ,String> comp_data_obj=null;
		String_Utility string_Utility=new String_Utility();
		String pmid_list="";
		Map<String, String>compound_idNameMap=new LinkedHashMap<String, String>();
		Map<String, String>duplicateCompoundnameMap=new LinkedHashMap<String, String>();
		String compound_id="";
		String name="";
		try{
			for(String pmid:pmids){
				if(pmid_list.equals("")){
					pmid_list=pmid;
				}else{
					pmid_list+=","+pmid;
				}
			}
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://178.79.186.74:3306/"+databaseName,"root","2k51125_MCPL");
			String sql="SELECT compound_id, pubmed_id,name,structure FROM "+databaseName+".Compound_Master where pubmed_id IN("+string_Utility.getQuerySting(pmid_list)+")";
			stmt=con.createStatement();
			rs=stmt.executeQuery(sql);
			while(rs.next()){

				compound_id=rs.getString("compound_id");
				name=rs.getString("name");
				if(!name.equals("NA")){
				if(compound_idNameMap.containsKey(name)){

					if(duplicateCompoundnameMap.containsKey(name)){
						duplicateCompoundnameMap.put(name,duplicateCompoundnameMap.get(name)+","+compound_id);

					}else{
						duplicateCompoundnameMap.put(name,compound_idNameMap.get(name)+","+compound_id);
					}



				}else{
					compound_idNameMap.put(name,compound_id);
				}
			}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}finally{try{
			if(con!=null)
				con.close();
				if(stmt!=null)
				stmt.close();
				if(rs!=null)
				rs.close();
			}catch (Exception e) {
				e.printStackTrace();
			}}

return duplicateCompoundnameMap;

}

	public static Vector compound_activitydata(String pmid){

		ResultSet rs = null;
		PreparedStatement ptat=null;
		Connection con=null;
		Map<String ,String> comp_data_obj=null;

		Vector comp_act_data_vector=new Vector();
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://178.79.186.74:3306/"+databaseName,"root","2k51125_MCPL");



			String sql="SELECT * FROM "+databaseName+".Compound_Master  where pubmed_id=? ";//where ID='305746'
			ptat=con.prepareStatement(sql);
			ptat.setString(1, pmid);
			rs=ptat.executeQuery();
			while(rs.next()){

				comp_data_obj=new HashMap<String, String>();
				comp_data_obj.put(rs.getString("compound_id"), rs.getString("structure"));
				if(!comp_act_data_vector.contains("comp_data_obj")){
					comp_act_data_vector.addElement(comp_data_obj);
				}


			}


		}catch(Exception e){
			System.out.println("Error @compound_activitydata()\t" + e);
		}finally{try{
			if(con!=null)
				con.close();
				if(ptat!=null)
					ptat.close();
				if(rs!=null)
				rs.close();
			}catch (Exception e) {
				e.printStackTrace();
			}}
		return comp_act_data_vector;
	}
}

