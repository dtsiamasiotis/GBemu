package cpu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class disassembler {

   public static void main(String[] args) throws Exception
    {

        //FileInputStream fin = new FileInputStream("//home//dimitris//DMG_ROM.bin");

        File f = new File("/home/dimitris/tetris.gb");
        long len = f.length();
        System.out.println("Rom length:"+len);
        byte data[] = new byte[(int)len];
        int i;


        byte data2[] = new byte[(int)len];
        //Path path = new Path("\"C:\\\\Users\\\\dimitris\\\\Downloads\\\\DMG_ROM.bin\"");
        data2 = Files.readAllBytes(f.toPath());
        hexToAssembly hexToAssembly = new hexToAssembly();
        hexToAssembly.transformHexToAssembly(data);
        Integer integer = new Integer(10);
        byte curr = integer.byteValue();
        Integer pc = 0;
        instructionMapper mapOfInstructions =  new instructionMapper();
        Cpu GBcpu = new Cpu();

        String hexOpcode = "";
       /* for(pc=0;pc<data2.length;)
        {//KOLLAEI STO CP $90
            if(pc==167)
            {
                System.out.println("csdfs");
            }
            hexOpcode = String.format("%02X",data2[pc]);
            Instruction instructionToExec = mapOfInstructions.findInstructionFromOpcode(hexOpcode);
            GBcpu.executeInstruction(instructionToExec,data2,pc);
            pc = GBcpu.getPc();
            System.out.print(pc);
            if(pc==163)
            {
                System.out.println("memory cleaned");
            }
        }*/
    }

    public int[] readBootRom() throws IOException {

        File f = new File("/home/dimitris/DMG_ROM.bin");
        long len = f.length();

        System.out.println("Rom length:"+len);

        byte dataFromFile[] = Files.readAllBytes(f.toPath());
        int dataAsInt[] = new int[(int)len];

        for(int i=0; i<(int)len; i++)
            dataAsInt[i] = dataFromFile[i];

        return dataAsInt;
    }

    public int[] readFile() throws IOException {
        //File f = new File("/home/dimitris/03-op sp,hl.gb");
        File f = new File("/home/dimitris/tetris.gb");
        long len = f.length();

        System.out.println("Rom length:"+len);

        byte dataFromFile[] = Files.readAllBytes(f.toPath());
        int dataAsInt[] = new int[(int)len];

        for(int i=0; i<(int)len; i++)
            dataAsInt[i] = dataFromFile[i];

        return dataAsInt;
    }
}
