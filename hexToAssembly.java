package cpu;

public class hexToAssembly {

    public void transformHexToAssembly(byte[] data)
    {
        int i;
        String byteInHex= "";

        for(i=0;i<data.length;i++)
        {
            byteInHex = binaryToHex(data[i]);
            //switch(byteInHex) {
            //    case
            //}
        }
    }

    public static String binaryToHex(byte datum)
    {
        String hexStr = "";
        return hexStr = String.format("%02X",datum);
    }
}
