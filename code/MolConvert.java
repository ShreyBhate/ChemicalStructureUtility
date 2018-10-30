


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import chemaxon.formats.MolConverter;




public class MolConvert  {

	public  String doFunc(String[] args) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(args[0].getBytes());
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		MolConverter mc=new MolConverter(bis, bout,args[1],false);

		//MolConverter mc = new MolConverter(bis, bout, args[1], false);
	//	System.out.println("adf");

		mc.convert();
	//	System.out.println("adf");
		//mc.close();

		return new String(bout.toByteArray());
	}


	public  String getInchiFromMol(String mol) throws Exception
	{


		String smiles = null;
		MolConvert molC = new MolConvert();
		Integer it=new Integer(12);

		String[] strArr = {mol,"INCHI"};
		smiles =doFunc(strArr);

		String[] res=smiles.split("\n");
		return 	res[0].replace("InChI=", "");
		//return "a";

	}




}
