package cpu;

public class instructionMapper {
    private Instruction[] instructionsMap;

    public instructionMapper()
    {
        instructionsMap = new Instruction[256];
        populateInstructionsMap();
    }

    public Instruction findInstructionFromOpcode(String opCode)
    {
        int i = 0;
        Instruction toRet = null;
        for(i=0; i<instructionsMap.length; i++)
        {
            if(instructionsMap[i].getOpCode().equals(opCode)) {
                toRet = instructionsMap[i];
                break;
            }
        }

        return toRet;
    }

    public void populateInstructionsMap()
    {
        int i;
        for(i=0;i<256;i++)
        {
            instructionsMap[i] = new Instruction();
        }

        instructionsMap[0].setDescription("NOP");
        instructionsMap[0].setOpCode("00");
        instructionsMap[0].setByteLength(1);
        instructionsMap[0].setCycles(4);

        instructionsMap[1].setDescription("LD");
        instructionsMap[1].setOpCode("01");
        instructionsMap[1].setOperand1("BC");
        instructionsMap[1].setOperand2("d16");
        instructionsMap[1].setByteLength(3);
        instructionsMap[1].setCycles(12);

        instructionsMap[2].setDescription("LD");
        instructionsMap[2].setOpCode("02");
        instructionsMap[2].setOperand1("(BC)");
        instructionsMap[2].setOperand2("A");
        instructionsMap[2].setByteLength(1);
        instructionsMap[2].setCycles(8);

        instructionsMap[3].setDescription("INC");
        instructionsMap[3].setOpCode("03");
        instructionsMap[3].setOperand1("BC");
        instructionsMap[3].setByteLength(1);
        instructionsMap[3].setCycles(8);

        instructionsMap[4].setDescription("INC");
        instructionsMap[4].setOpCode("04");
        instructionsMap[4].setOperand1("B");
        instructionsMap[4].setByteLength(1);
        instructionsMap[4].setCycles(4);

        instructionsMap[5].setDescription("DEC");
        instructionsMap[5].setOpCode("05");
        instructionsMap[5].setOperand1("B");
        instructionsMap[5].setByteLength(1);
        instructionsMap[5].setCycles(4);

        instructionsMap[6].setDescription("LD");
        instructionsMap[6].setOpCode("06");
        instructionsMap[6].setOperand1("B");
        instructionsMap[6].setOperand2("d8");
        instructionsMap[6].setByteLength(2);
        instructionsMap[6].setCycles(8);

        instructionsMap[7].setDescription("RLCA");
        instructionsMap[7].setOpCode("07");
        instructionsMap[7].setByteLength(1);
        instructionsMap[7].setCycles(4);

        instructionsMap[8].setDescription("LD");
        instructionsMap[8].setOpCode("08");
        instructionsMap[8].setOperand1("(a16)");
        instructionsMap[8].setOperand2("SP");
        instructionsMap[8].setByteLength(3);
        instructionsMap[8].setCycles(20);

        instructionsMap[9].setDescription("ADD");
        instructionsMap[9].setOpCode("09");
        instructionsMap[9].setOperand1("HL");
        instructionsMap[9].setOperand2("BC");
        instructionsMap[9].setByteLength(1);
        instructionsMap[9].setCycles(8);

        instructionsMap[10].setDescription("LD");
        instructionsMap[10].setOpCode("0A");
        instructionsMap[10].setOperand1("A");
        instructionsMap[10].setOperand2("(BC)");
        instructionsMap[10].setByteLength(1);
        instructionsMap[10].setCycles(8);

        instructionsMap[11].setDescription("DEC");
        instructionsMap[11].setOpCode("0B");
        instructionsMap[11].setOperand1("BC");
        instructionsMap[11].setByteLength(1);
        instructionsMap[11].setCycles(8);

        instructionsMap[12].setDescription("INC");
        instructionsMap[12].setOpCode("0C");
        instructionsMap[12].setOperand1("C");
        instructionsMap[12].setByteLength(1);
        instructionsMap[12].setCycles(4);

        instructionsMap[13].setDescription("DEC");
        instructionsMap[13].setOpCode("0D");
        instructionsMap[13].setOperand1("C");
        instructionsMap[13].setByteLength(1);
        instructionsMap[13].setCycles(4);

        instructionsMap[14].setDescription("LD");
        instructionsMap[14].setOpCode("0E");
        instructionsMap[14].setOperand1("C");
        instructionsMap[14].setOperand2("d8");
        instructionsMap[14].setByteLength(2);
        instructionsMap[14].setCycles(8);

        instructionsMap[15].setDescription("RRCA");
        instructionsMap[15].setOpCode("0F");
        instructionsMap[11].setCycles(1);
        instructionsMap[15].setCycles(4);

        instructionsMap[16].setDescription("STOP");
        instructionsMap[16].setOpCode("10");
        instructionsMap[16].setOperand1("0");
        instructionsMap[16].setByteLength(2);
        instructionsMap[16].setCycles(4);

        instructionsMap[17].setDescription("LD");
        instructionsMap[17].setOpCode("11");
        instructionsMap[17].setOperand1("DE");
        instructionsMap[17].setOperand2("d16");
        instructionsMap[17].setByteLength(3);
        instructionsMap[17].setCycles(12);

        instructionsMap[18].setDescription("LD");
        instructionsMap[18].setOpCode("12");
        instructionsMap[18].setOperand1("(DE)");
        instructionsMap[18].setOperand2("A");
        instructionsMap[18].setByteLength(1);
        instructionsMap[18].setCycles(8);

        instructionsMap[19].setDescription("INC");
        instructionsMap[19].setOpCode("13");
        instructionsMap[19].setOperand1("DE");
        instructionsMap[19].setByteLength(1);
        instructionsMap[19].setCycles(8);

        instructionsMap[20].setDescription("INC");
        instructionsMap[20].setOpCode("14");
        instructionsMap[20].setOperand1("D");
        instructionsMap[20].setByteLength(1);
        instructionsMap[20].setCycles(4);


        instructionsMap[21].setDescription("DEC");
        instructionsMap[21].setOpCode("15");
        instructionsMap[21].setOperand1("D");
        instructionsMap[21].setByteLength(1);
        instructionsMap[21].setCycles(4);

        instructionsMap[22].setDescription("LD");
        instructionsMap[22].setOpCode("16");
        instructionsMap[22].setOperand1("D");
        instructionsMap[22].setOperand2("d8");
        instructionsMap[22].setByteLength(2);
        instructionsMap[22].setCycles(8);

        instructionsMap[23].setDescription("RLA");
        instructionsMap[23].setOpCode("17");
        instructionsMap[23].setByteLength(1);
        instructionsMap[23].setCycles(4);

        instructionsMap[24].setDescription("JR");
        instructionsMap[24].setOpCode("18");
        instructionsMap[24].setOperand1("r8");
        instructionsMap[24].setByteLength(2);
        instructionsMap[24].setCycles(12);

        instructionsMap[25].setDescription("ADD");
        instructionsMap[25].setOpCode("19");
        instructionsMap[25].setOperand1("HL");
        instructionsMap[25].setOperand2("DE");
        instructionsMap[25].setByteLength(1);
        instructionsMap[25].setCycles(8);

        instructionsMap[26].setDescription("LD");
        instructionsMap[26].setOpCode("1A");
        instructionsMap[26].setOperand1("A");
        instructionsMap[26].setOperand2("(DE)");
        instructionsMap[26].setByteLength(1);
        instructionsMap[26].setCycles(8);

        instructionsMap[27].setDescription("DEC");
        instructionsMap[27].setOpCode("1B");
        instructionsMap[27].setOperand1("DE");
        instructionsMap[27].setByteLength(1);
        instructionsMap[27].setCycles(8);

        instructionsMap[28].setDescription("INC");
        instructionsMap[28].setOpCode("1C");
        instructionsMap[28].setOperand1("E");
        instructionsMap[28].setByteLength(1);
        instructionsMap[28].setCycles(4);

        instructionsMap[29].setDescription("DEC");
        instructionsMap[29].setOpCode("1D");
        instructionsMap[29].setOperand1("E");
        instructionsMap[29].setByteLength(1);
        instructionsMap[29].setCycles(4);

        instructionsMap[30].setDescription("LD");
        instructionsMap[30].setOpCode("1E");
        instructionsMap[30].setOperand1("E");
        instructionsMap[30].setOperand2("d8");
        instructionsMap[30].setByteLength(2);
        instructionsMap[30].setCycles(8);

        instructionsMap[31].setDescription("RRA");
        instructionsMap[31].setOpCode("1F");
        instructionsMap[31].setByteLength(1);
        instructionsMap[31].setCycles(4);

        instructionsMap[32].setDescription("JR");
        instructionsMap[32].setOpCode("20");
        instructionsMap[32].setOperand1("NZ");
        instructionsMap[32].setOperand2("r8");
        instructionsMap[32].setByteLength(2);
        instructionsMap[32].setCycles(8);

        instructionsMap[33].setDescription("LD");
        instructionsMap[33].setOpCode("21");
        instructionsMap[33].setOperand1("HL");
        instructionsMap[33].setOperand2("d16");
        instructionsMap[33].setByteLength(3);
        instructionsMap[33].setCycles(12);

        instructionsMap[34].setDescription("LD");
        instructionsMap[34].setOpCode("22");
        instructionsMap[34].setOperand1("(HL+)");
        instructionsMap[34].setOperand2("A");
        instructionsMap[34].setByteLength(1);
        instructionsMap[34].setCycles(8);

        instructionsMap[35].setDescription("INC");
        instructionsMap[35].setOpCode("23");
        instructionsMap[35].setOperand1("HL");
        instructionsMap[35].setByteLength(1);
        instructionsMap[35].setCycles(8);

        instructionsMap[36].setDescription("INC");
        instructionsMap[36].setOpCode("24");
        instructionsMap[36].setOperand1("H");
        instructionsMap[36].setByteLength(1);
        instructionsMap[36].setCycles(4);

        instructionsMap[37].setDescription("DEC");
        instructionsMap[37].setOpCode("25");
        instructionsMap[37].setOperand1("H");
        instructionsMap[37].setByteLength(1);
        instructionsMap[37].setCycles(4);

        instructionsMap[38].setDescription("LD");
        instructionsMap[38].setOpCode("26");
        instructionsMap[38].setOperand1("H");
        instructionsMap[38].setOperand2("d8");
        instructionsMap[38].setByteLength(2);
        instructionsMap[38].setCycles(8);

        instructionsMap[39].setDescription("DAA");
        instructionsMap[39].setOpCode("27");
        instructionsMap[39].setByteLength(1);
        instructionsMap[39].setCycles(4);


        instructionsMap[40].setDescription("JR");
        instructionsMap[40].setOpCode("28");
        instructionsMap[40].setOperand1("Z");
        instructionsMap[40].setOperand2("r8");
        instructionsMap[40].setByteLength(2);
        instructionsMap[40].setCycles(12);

        instructionsMap[41].setDescription("ADD");
        instructionsMap[41].setOpCode("29");
        instructionsMap[41].setOperand1("HL");
        instructionsMap[41].setOperand2("HL");
        instructionsMap[41].setByteLength(1);
        instructionsMap[41].setCycles(8);

        instructionsMap[42].setDescription("LD");
        instructionsMap[42].setOpCode("2A");
        instructionsMap[42].setOperand1("A");
        instructionsMap[42].setOperand2("(HL+)");
        instructionsMap[42].setByteLength(1);
        instructionsMap[42].setCycles(8);

        instructionsMap[43].setDescription("DEC");
        instructionsMap[43].setOpCode("2B");
        instructionsMap[43].setOperand1("HL");
        instructionsMap[43].setByteLength(1);
        instructionsMap[43].setCycles(8);

        instructionsMap[44].setDescription("INC");
        instructionsMap[44].setOpCode("2C");
        instructionsMap[44].setOperand1("L");
        instructionsMap[44].setByteLength(1);
        instructionsMap[44].setCycles(4);


        instructionsMap[45].setDescription("DEC");
        instructionsMap[45].setOpCode("2D");
        instructionsMap[45].setOperand1("L");
        instructionsMap[45].setByteLength(1);
        instructionsMap[45].setCycles(4);

        instructionsMap[46].setDescription("LD");
        instructionsMap[46].setOpCode("2E");
        instructionsMap[46].setOperand1("L");
        instructionsMap[46].setOperand2("d8");
        instructionsMap[46].setByteLength(2);
        instructionsMap[46].setCycles(8);

        instructionsMap[47].setDescription("CPL");
        instructionsMap[47].setOpCode("2F");
        instructionsMap[47].setCycles(4);
        instructionsMap[47].setByteLength(1);

        instructionsMap[48].setDescription("JR");
        instructionsMap[48].setOpCode("30");
        instructionsMap[48].setOperand1("NC");
        instructionsMap[48].setOperand2("r8");
        instructionsMap[48].setByteLength(2);
        instructionsMap[48].setCycles(12);

        instructionsMap[49].setDescription("LD");
        instructionsMap[49].setOpCode("31");
        instructionsMap[49].setOperand1("SP");
        instructionsMap[49].setOperand2("d16");
        instructionsMap[49].setByteLength(3);
        instructionsMap[49].setCycles(12);

        instructionsMap[50].setDescription("LD");
        instructionsMap[50].setOpCode("32");
        instructionsMap[50].setOperand1("(HL-)");
        instructionsMap[50].setOperand2("A");
        instructionsMap[50].setByteLength(1);
        instructionsMap[50].setCycles(8);

        instructionsMap[51].setDescription("INC");
        instructionsMap[51].setOpCode("33");
        instructionsMap[51].setOperand1("SP");
        instructionsMap[51].setByteLength(1);
        instructionsMap[51].setCycles(8);


        instructionsMap[52].setDescription("INC");
        instructionsMap[52].setOpCode("34");
        instructionsMap[52].setOperand1("(HL)");
        instructionsMap[52].setByteLength(1);
        instructionsMap[52].setCycles(12);


        instructionsMap[53].setDescription("DEC");
        instructionsMap[53].setOpCode("35");
        instructionsMap[53].setOperand1("(HL)");
        instructionsMap[53].setByteLength(1);
        instructionsMap[53].setCycles(12);

        instructionsMap[54].setDescription("LD");
        instructionsMap[54].setOpCode("36");
        instructionsMap[54].setOperand1("(HL)");
        instructionsMap[54].setOperand2("d8");
        instructionsMap[54].setByteLength(2);
        instructionsMap[54].setCycles(12);


        instructionsMap[55].setDescription("SCF");
        instructionsMap[55].setOpCode("37");
        instructionsMap[55].setByteLength(1);
        instructionsMap[55].setCycles(4);


        instructionsMap[56].setDescription("JR");
        instructionsMap[56].setOpCode("38");
        instructionsMap[56].setOperand1("C");
        instructionsMap[56].setOperand2("r8");
        instructionsMap[56].setByteLength(2);
        instructionsMap[56].setCycles(12);

        instructionsMap[57].setDescription("ADD");
        instructionsMap[57].setOpCode("39");
        instructionsMap[57].setOperand1("HL");
        instructionsMap[57].setOperand2("SP");
        instructionsMap[57].setByteLength(1);
        instructionsMap[57].setCycles(8);

        instructionsMap[58].setDescription("LD");
        instructionsMap[58].setOpCode("3A");
        instructionsMap[58].setOperand1("A");
        instructionsMap[58].setOperand2("(HL-)");
        instructionsMap[58].setByteLength(1);
        instructionsMap[58].setCycles(8);

        instructionsMap[59].setDescription("DEC");
        instructionsMap[59].setOpCode("3B");
        instructionsMap[59].setOperand1("SP");
        instructionsMap[59].setByteLength(1);
        instructionsMap[59].setCycles(8);


        instructionsMap[60].setDescription("INC");
        instructionsMap[60].setOpCode("3C");
        instructionsMap[60].setOperand1("A");
        instructionsMap[60].setByteLength(1);
        instructionsMap[60].setCycles(4);


        instructionsMap[61].setDescription("DEC");
        instructionsMap[61].setOpCode("3D");
        instructionsMap[61].setOperand1("A");
        instructionsMap[61].setByteLength(1);
        instructionsMap[61].setCycles(4);


        instructionsMap[62].setDescription("LD");
        instructionsMap[62].setOpCode("3E");
        instructionsMap[62].setOperand1("A");
        instructionsMap[62].setOperand2("d8");
        instructionsMap[62].setByteLength(2);
        instructionsMap[62].setCycles(8);

        instructionsMap[63].setDescription("CCF");
        instructionsMap[63].setOpCode("3F");
        instructionsMap[63].setByteLength(1);
        instructionsMap[63].setCycles(4);


        instructionsMap[64].setDescription("LD");
        instructionsMap[64].setOpCode("40");
        instructionsMap[64].setOperand1("B");
        instructionsMap[64].setOperand2("B");
        instructionsMap[64].setByteLength(1);
        instructionsMap[64].setCycles(4);

        instructionsMap[65].setDescription("LD");
        instructionsMap[65].setOpCode("41");
        instructionsMap[65].setOperand1("B");
        instructionsMap[65].setOperand2("C");
        instructionsMap[65].setByteLength(1);
        instructionsMap[65].setCycles(4);

        instructionsMap[66].setDescription("LD");
        instructionsMap[66].setOpCode("42");
        instructionsMap[66].setOperand1("B");
        instructionsMap[66].setOperand2("D");
        instructionsMap[66].setByteLength(1);
        instructionsMap[66].setCycles(4);

        instructionsMap[67].setDescription("LD");
        instructionsMap[67].setOpCode("43");
        instructionsMap[67].setOperand1("B");
        instructionsMap[67].setOperand2("E");
        instructionsMap[67].setByteLength(1);
        instructionsMap[67].setCycles(4);

        instructionsMap[68].setDescription("LD");
        instructionsMap[68].setOpCode("44");
        instructionsMap[68].setOperand1("B");
        instructionsMap[68].setOperand2("H");
        instructionsMap[68].setByteLength(1);
        instructionsMap[68].setCycles(4);

        instructionsMap[69].setDescription("LD");
        instructionsMap[69].setOpCode("45");
        instructionsMap[69].setOperand1("B");
        instructionsMap[69].setOperand2("L");
        instructionsMap[69].setByteLength(1);
        instructionsMap[69].setCycles(4);

        instructionsMap[70].setDescription("LD");
        instructionsMap[70].setOpCode("46");
        instructionsMap[70].setOperand1("B");
        instructionsMap[70].setOperand2("(HL)");
        instructionsMap[70].setByteLength(1);
        instructionsMap[70].setCycles(8);

        instructionsMap[71].setDescription("LD");
        instructionsMap[71].setOpCode("47");
        instructionsMap[71].setOperand1("B");
        instructionsMap[71].setOperand2("A");
        instructionsMap[71].setByteLength(1);
        instructionsMap[71].setCycles(4);

        instructionsMap[72].setDescription("LD");
        instructionsMap[72].setOpCode("48");
        instructionsMap[72].setOperand1("C");
        instructionsMap[72].setOperand2("B");
        instructionsMap[72].setByteLength(1);
        instructionsMap[72].setCycles(4);

        instructionsMap[73].setDescription("LD");
        instructionsMap[73].setOpCode("49");
        instructionsMap[73].setOperand1("C");
        instructionsMap[73].setOperand2("C");
        instructionsMap[73].setByteLength(1);
        instructionsMap[73].setCycles(4);

        instructionsMap[74].setDescription("LD");
        instructionsMap[74].setOpCode("4A");
        instructionsMap[74].setOperand1("C");
        instructionsMap[74].setOperand2("D");
        instructionsMap[74].setByteLength(1);
        instructionsMap[74].setCycles(4);

        instructionsMap[75].setDescription("LD");
        instructionsMap[75].setOpCode("4B");
        instructionsMap[75].setOperand1("C");
        instructionsMap[75].setOperand2("E");
        instructionsMap[75].setByteLength(1);
        instructionsMap[75].setCycles(4);

        instructionsMap[76].setDescription("LD");
        instructionsMap[76].setOpCode("4C");
        instructionsMap[76].setOperand1("C");
        instructionsMap[76].setOperand2("H");
        instructionsMap[76].setByteLength(1);
        instructionsMap[76].setCycles(4);

        instructionsMap[77].setDescription("LD");
        instructionsMap[77].setOpCode("4D");
        instructionsMap[77].setOperand1("C");
        instructionsMap[77].setOperand2("L");
        instructionsMap[77].setByteLength(1);
        instructionsMap[77].setCycles(4);

        instructionsMap[78].setDescription("LD");
        instructionsMap[78].setOpCode("4E");
        instructionsMap[78].setOperand1("C");
        instructionsMap[78].setOperand2("(HL)");
        instructionsMap[78].setByteLength(1);
        instructionsMap[78].setCycles(8);

        instructionsMap[79].setDescription("LD");
        instructionsMap[79].setOpCode("4F");
        instructionsMap[79].setOperand1("C");
        instructionsMap[79].setOperand2("A");
        instructionsMap[79].setByteLength(1);
        instructionsMap[79].setCycles(4);

        instructionsMap[80].setDescription("LD");
        instructionsMap[80].setOpCode("50");
        instructionsMap[80].setOperand1("D");
        instructionsMap[80].setOperand2("B");
        instructionsMap[80].setByteLength(1);
        instructionsMap[80].setCycles(4);

        instructionsMap[81].setDescription("LD");
        instructionsMap[81].setOpCode("51");
        instructionsMap[81].setOperand1("D");
        instructionsMap[81].setOperand2("C");
        instructionsMap[81].setByteLength(1);
        instructionsMap[81].setCycles(4);

        instructionsMap[82].setDescription("LD");
        instructionsMap[82].setOpCode("52");
        instructionsMap[82].setOperand1("D");
        instructionsMap[82].setOperand2("D");
        instructionsMap[82].setByteLength(1);
        instructionsMap[82].setCycles(4);

        instructionsMap[83].setDescription("LD");
        instructionsMap[83].setOpCode("53");
        instructionsMap[83].setOperand1("D");
        instructionsMap[83].setOperand2("E");
        instructionsMap[83].setByteLength(1);
        instructionsMap[83].setCycles(4);

        instructionsMap[84].setDescription("LD");
        instructionsMap[84].setOpCode("54");
        instructionsMap[84].setOperand1("D");
        instructionsMap[84].setOperand2("H");
        instructionsMap[84].setByteLength(1);
        instructionsMap[84].setCycles(4);

        instructionsMap[85].setDescription("LD");
        instructionsMap[85].setOpCode("55");
        instructionsMap[85].setOperand1("D");
        instructionsMap[85].setOperand2("L");
        instructionsMap[85].setByteLength(1);
        instructionsMap[85].setCycles(4);

        instructionsMap[86].setDescription("LD");
        instructionsMap[86].setOpCode("56");
        instructionsMap[86].setOperand1("D");
        instructionsMap[86].setOperand2("(HL)");
        instructionsMap[86].setByteLength(1);
        instructionsMap[86].setCycles(8);

        instructionsMap[87].setDescription("LD");
        instructionsMap[87].setOpCode("57");
        instructionsMap[87].setOperand1("D");
        instructionsMap[87].setOperand2("A");
        instructionsMap[87].setByteLength(1);
        instructionsMap[87].setCycles(4);

        instructionsMap[88].setDescription("LD");
        instructionsMap[88].setOpCode("58");
        instructionsMap[88].setOperand1("E");
        instructionsMap[88].setOperand2("B");
        instructionsMap[88].setByteLength(1);
        instructionsMap[88].setCycles(4);

        instructionsMap[89].setDescription("LD");
        instructionsMap[89].setOpCode("59");
        instructionsMap[89].setOperand1("E");
        instructionsMap[89].setOperand2("C");
        instructionsMap[89].setByteLength(1);
        instructionsMap[89].setCycles(4);

        instructionsMap[90].setDescription("LD");
        instructionsMap[90].setOpCode("5A");
        instructionsMap[90].setOperand1("E");
        instructionsMap[90].setOperand2("D");
        instructionsMap[90].setByteLength(1);
        instructionsMap[90].setCycles(4);

        instructionsMap[91].setDescription("LD");
        instructionsMap[91].setOpCode("5B");
        instructionsMap[91].setOperand1("E");
        instructionsMap[91].setOperand2("E");
        instructionsMap[91].setByteLength(1);
        instructionsMap[91].setCycles(4);

        instructionsMap[92].setDescription("LD");
        instructionsMap[92].setOpCode("5C");
        instructionsMap[92].setOperand1("E");
        instructionsMap[92].setOperand2("H");
        instructionsMap[92].setByteLength(1);
        instructionsMap[92].setCycles(4);

        instructionsMap[93].setDescription("LD");
        instructionsMap[93].setOpCode("5D");
        instructionsMap[93].setOperand1("E");
        instructionsMap[93].setOperand2("L");
        instructionsMap[93].setByteLength(1);
        instructionsMap[93].setCycles(4);

        instructionsMap[94].setDescription("LD");
        instructionsMap[94].setOpCode("5E");
        instructionsMap[94].setOperand1("E");
        instructionsMap[94].setOperand2("(HL)");
        instructionsMap[94].setByteLength(1);
        instructionsMap[94].setCycles(8);

        instructionsMap[95].setDescription("LD");
        instructionsMap[95].setOpCode("5F");
        instructionsMap[95].setOperand1("E");
        instructionsMap[95].setOperand2("A");
        instructionsMap[95].setByteLength(1);
        instructionsMap[95].setCycles(4);

        instructionsMap[96].setDescription("LD");
        instructionsMap[96].setOpCode("60");
        instructionsMap[96].setOperand1("H");
        instructionsMap[96].setOperand2("B");
        instructionsMap[96].setByteLength(1);
        instructionsMap[96].setCycles(4);

        instructionsMap[97].setDescription("LD");
        instructionsMap[97].setOpCode("61");
        instructionsMap[97].setOperand1("H");
        instructionsMap[97].setOperand2("C");
        instructionsMap[97].setByteLength(1);
        instructionsMap[97].setCycles(4);

        instructionsMap[98].setDescription("LD");
        instructionsMap[98].setOpCode("62");
        instructionsMap[98].setOperand1("H");
        instructionsMap[98].setOperand2("D");
        instructionsMap[98].setByteLength(1);
        instructionsMap[98].setCycles(4);

        instructionsMap[99].setDescription("LD");
        instructionsMap[99].setOpCode("63");
        instructionsMap[99].setOperand1("H");
        instructionsMap[99].setOperand2("E");
        instructionsMap[99].setByteLength(1);
        instructionsMap[99].setCycles(4);

        instructionsMap[100].setDescription("LD");
        instructionsMap[100].setOpCode("64");
        instructionsMap[100].setOperand1("H");
        instructionsMap[100].setOperand2("H");
        instructionsMap[100].setByteLength(1);
        instructionsMap[100].setCycles(4);

        instructionsMap[101].setDescription("LD");
        instructionsMap[101].setOpCode("65");
        instructionsMap[101].setOperand1("H");
        instructionsMap[101].setOperand2("L");
        instructionsMap[101].setByteLength(1);
        instructionsMap[101].setCycles(4);

        instructionsMap[102].setDescription("LD");
        instructionsMap[102].setOpCode("66");
        instructionsMap[102].setOperand1("H");
        instructionsMap[102].setOperand2("(HL)");
        instructionsMap[102].setByteLength(1);
        instructionsMap[102].setCycles(8);

        instructionsMap[103].setDescription("LD");
        instructionsMap[103].setOpCode("67");
        instructionsMap[103].setOperand1("H");
        instructionsMap[103].setOperand2("A");
        instructionsMap[103].setByteLength(1);
        instructionsMap[103].setCycles(4);

        instructionsMap[104].setDescription("LD");
        instructionsMap[104].setOpCode("68");
        instructionsMap[104].setOperand1("L");
        instructionsMap[104].setOperand2("B");
        instructionsMap[104].setByteLength(1);
        instructionsMap[104].setCycles(4);

        instructionsMap[105].setDescription("LD");
        instructionsMap[105].setOpCode("69");
        instructionsMap[105].setOperand1("L");
        instructionsMap[105].setOperand2("C");
        instructionsMap[105].setByteLength(1);
        instructionsMap[105].setCycles(4);

        instructionsMap[106].setDescription("LD");
        instructionsMap[106].setOpCode("6A");
        instructionsMap[106].setOperand1("L");
        instructionsMap[106].setOperand2("D");
        instructionsMap[106].setByteLength(1);
        instructionsMap[106].setCycles(4);

        instructionsMap[107].setDescription("LD");
        instructionsMap[107].setOpCode("6B");
        instructionsMap[107].setOperand1("L");
        instructionsMap[107].setOperand2("E");
        instructionsMap[107].setByteLength(1);
        instructionsMap[107].setCycles(4);

        instructionsMap[108].setDescription("LD");
        instructionsMap[108].setOpCode("6C");
        instructionsMap[108].setOperand1("L");
        instructionsMap[108].setOperand2("H");
        instructionsMap[108].setByteLength(1);
        instructionsMap[108].setCycles(4);

        instructionsMap[109].setDescription("LD");
        instructionsMap[109].setOpCode("6D");
        instructionsMap[109].setOperand1("L");
        instructionsMap[109].setOperand2("L");
        instructionsMap[109].setByteLength(1);
        instructionsMap[109].setCycles(4);

        instructionsMap[110].setDescription("LD");
        instructionsMap[110].setOpCode("6E");
        instructionsMap[110].setOperand1("L");
        instructionsMap[110].setOperand2("(HL)");
        instructionsMap[110].setByteLength(1);
        instructionsMap[110].setCycles(8);

        instructionsMap[111].setDescription("LD");
        instructionsMap[111].setOpCode("6F");
        instructionsMap[111].setOperand1("L");
        instructionsMap[111].setOperand2("A");
        instructionsMap[111].setByteLength(1);
        instructionsMap[111].setCycles(4);

        instructionsMap[112].setDescription("LD");
        instructionsMap[112].setOpCode("70");
        instructionsMap[112].setOperand1("(HL)");
        instructionsMap[112].setOperand2("B");
        instructionsMap[112].setByteLength(1);
        instructionsMap[112].setCycles(8);

        instructionsMap[113].setDescription("LD");
        instructionsMap[113].setOpCode("71");
        instructionsMap[113].setOperand1("(HL)");
        instructionsMap[113].setOperand2("C");
        instructionsMap[113].setByteLength(1);
        instructionsMap[113].setCycles(8);

        instructionsMap[114].setDescription("LD");
        instructionsMap[114].setOpCode("72");
        instructionsMap[114].setOperand1("(HL)");
        instructionsMap[114].setOperand2("D");
        instructionsMap[114].setByteLength(1);
        instructionsMap[114].setCycles(8);

        instructionsMap[115].setDescription("LD");
        instructionsMap[115].setOpCode("73");
        instructionsMap[115].setOperand1("(HL)");
        instructionsMap[115].setOperand2("E");
        instructionsMap[115].setByteLength(1);
        instructionsMap[115].setCycles(8);


        instructionsMap[116].setDescription("LD");
        instructionsMap[116].setOpCode("74");
        instructionsMap[116].setOperand1("(HL)");
        instructionsMap[116].setOperand2("H");
        instructionsMap[116].setByteLength(1);
        instructionsMap[116].setCycles(8);

        instructionsMap[117].setDescription("LD");
        instructionsMap[117].setOpCode("75");
        instructionsMap[117].setOperand1("(HL)");
        instructionsMap[117].setOperand2("L");
        instructionsMap[117].setByteLength(1);
        instructionsMap[117].setCycles(8);

        instructionsMap[118].setDescription("HALT");
        instructionsMap[118].setOpCode("76");
        instructionsMap[118].setByteLength(1);
        instructionsMap[118].setCycles(4);


        instructionsMap[119].setDescription("LD");
        instructionsMap[119].setOpCode("77");
        instructionsMap[119].setOperand1("(HL)");
        instructionsMap[119].setOperand2("A");
        instructionsMap[119].setByteLength(1);
        instructionsMap[119].setCycles(8);

        instructionsMap[120].setDescription("LD");
        instructionsMap[120].setOpCode("78");
        instructionsMap[120].setOperand1("A");
        instructionsMap[120].setOperand2("B");
        instructionsMap[120].setByteLength(1);
        instructionsMap[120].setCycles(4);

        instructionsMap[121].setDescription("LD");
        instructionsMap[121].setOpCode("79");
        instructionsMap[121].setOperand1("A");
        instructionsMap[121].setOperand2("C");
        instructionsMap[121].setByteLength(1);
        instructionsMap[121].setCycles(4);

        instructionsMap[122].setDescription("LD");
        instructionsMap[122].setOpCode("7A");
        instructionsMap[122].setOperand1("A");
        instructionsMap[122].setOperand2("D");
        instructionsMap[122].setByteLength(1);
        instructionsMap[122].setCycles(4);

        instructionsMap[123].setDescription("LD");
        instructionsMap[123].setOpCode("7B");
        instructionsMap[123].setOperand1("A");
        instructionsMap[123].setOperand2("E");
        instructionsMap[123].setByteLength(1);
        instructionsMap[123].setCycles(4);

        instructionsMap[124].setDescription("LD");
        instructionsMap[124].setOpCode("7C");
        instructionsMap[124].setOperand1("A");
        instructionsMap[124].setOperand2("H");
        instructionsMap[124].setByteLength(1);
        instructionsMap[124].setCycles(4);

        instructionsMap[125].setDescription("LD");
        instructionsMap[125].setOpCode("7D");
        instructionsMap[125].setOperand1("A");
        instructionsMap[125].setOperand2("L");
        instructionsMap[125].setByteLength(1);
        instructionsMap[125].setCycles(4);

        instructionsMap[126].setDescription("LD");
        instructionsMap[126].setOpCode("7E");
        instructionsMap[126].setOperand1("A");
        instructionsMap[126].setOperand2("(HL)");
        instructionsMap[126].setByteLength(1);
        instructionsMap[126].setCycles(8);

        instructionsMap[127].setDescription("LD");
        instructionsMap[127].setOpCode("7D");
        instructionsMap[127].setOperand1("A");
        instructionsMap[127].setOperand2("A");
        instructionsMap[127].setByteLength(1);
        instructionsMap[127].setCycles(4);

        instructionsMap[128].setDescription("ADD");
        instructionsMap[128].setOpCode("80");
        instructionsMap[128].setOperand1("A");
        instructionsMap[128].setOperand2("B");
        instructionsMap[128].setByteLength(1);
        instructionsMap[128].setCycles(4);

        instructionsMap[129].setDescription("ADD");
        instructionsMap[129].setOpCode("81");
        instructionsMap[129].setOperand1("A");
        instructionsMap[129].setOperand2("C");
        instructionsMap[129].setByteLength(1);
        instructionsMap[129].setCycles(4);

        instructionsMap[130].setDescription("ADD");
        instructionsMap[130].setOpCode("82");
        instructionsMap[130].setOperand1("A");
        instructionsMap[130].setOperand2("D");
        instructionsMap[130].setByteLength(1);
        instructionsMap[130].setCycles(4);

        instructionsMap[131].setDescription("ADD");
        instructionsMap[131].setOpCode("83");
        instructionsMap[131].setOperand1("A");
        instructionsMap[131].setOperand2("E");
        instructionsMap[131].setByteLength(1);
        instructionsMap[131].setCycles(4);

        instructionsMap[132].setDescription("ADD");
        instructionsMap[132].setOpCode("84");
        instructionsMap[132].setOperand1("A");
        instructionsMap[132].setOperand2("H");
        instructionsMap[132].setByteLength(1);
        instructionsMap[132].setCycles(4);

        instructionsMap[133].setDescription("ADD");
        instructionsMap[133].setOpCode("85");
        instructionsMap[133].setOperand1("A");
        instructionsMap[133].setOperand2("L");
        instructionsMap[133].setByteLength(1);
        instructionsMap[133].setCycles(4);

        instructionsMap[134].setDescription("ADD");
        instructionsMap[134].setOpCode("86");
        instructionsMap[134].setOperand1("A");
        instructionsMap[134].setOperand2("(HL)");
        instructionsMap[134].setByteLength(1);
        instructionsMap[134].setCycles(8);

        instructionsMap[135].setDescription("ADD");
        instructionsMap[135].setOpCode("87");
        instructionsMap[135].setOperand1("A");
        instructionsMap[135].setOperand2("A");
        instructionsMap[135].setByteLength(1);
        instructionsMap[135].setCycles(4);

        instructionsMap[136].setDescription("ADC");
        instructionsMap[136].setOpCode("88");
        instructionsMap[136].setOperand1("A");
        instructionsMap[136].setOperand2("B");
        instructionsMap[136].setByteLength(1);
        instructionsMap[136].setCycles(4);

        instructionsMap[137].setDescription("ADC");
        instructionsMap[137].setOpCode("89");
        instructionsMap[137].setOperand1("A");
        instructionsMap[137].setOperand2("C");
        instructionsMap[137].setByteLength(1);
        instructionsMap[137].setCycles(4);

        instructionsMap[138].setDescription("ADC");
        instructionsMap[138].setOpCode("8A");
        instructionsMap[138].setOperand1("A");
        instructionsMap[138].setOperand2("D");
        instructionsMap[138].setByteLength(1);
        instructionsMap[138].setCycles(4);

        instructionsMap[139].setDescription("ADC");
        instructionsMap[139].setOpCode("8B");
        instructionsMap[139].setOperand1("A");
        instructionsMap[139].setOperand2("E");
        instructionsMap[139].setByteLength(1);
        instructionsMap[139].setCycles(4);

        instructionsMap[140].setDescription("ADC");
        instructionsMap[140].setOpCode("8C");
        instructionsMap[140].setOperand1("A");
        instructionsMap[140].setOperand2("H");
        instructionsMap[140].setByteLength(1);
        instructionsMap[140].setCycles(4);

        instructionsMap[141].setDescription("ADC");
        instructionsMap[141].setOpCode("8D");
        instructionsMap[141].setOperand1("A");
        instructionsMap[141].setOperand2("L");
        instructionsMap[141].setByteLength(1);
        instructionsMap[141].setCycles(4);

        instructionsMap[142].setDescription("ADC");
        instructionsMap[142].setOpCode("8E");
        instructionsMap[142].setOperand1("A");
        instructionsMap[142].setOperand2("(HL)");
        instructionsMap[142].setByteLength(1);
        instructionsMap[142].setCycles(8);

        instructionsMap[143].setDescription("ADC");
        instructionsMap[143].setOpCode("8F");
        instructionsMap[143].setOperand1("A");
        instructionsMap[143].setOperand2("A");
        instructionsMap[143].setByteLength(1);
        instructionsMap[143].setCycles(4);

        instructionsMap[144].setDescription("SUB");
        instructionsMap[144].setOpCode("90");
        instructionsMap[144].setOperand1("B");
        instructionsMap[144].setByteLength(1);
        instructionsMap[144].setCycles(4);


        instructionsMap[145].setDescription("SUB");
        instructionsMap[145].setOpCode("91");
        instructionsMap[145].setOperand1("C");
        instructionsMap[145].setByteLength(1);
        instructionsMap[145].setCycles(4);


        instructionsMap[146].setDescription("SUB");
        instructionsMap[146].setOpCode("92");
        instructionsMap[146].setOperand1("D");
        instructionsMap[146].setByteLength(1);
        instructionsMap[146].setCycles(4);


        instructionsMap[147].setDescription("SUB");
        instructionsMap[147].setOpCode("93");
        instructionsMap[147].setOperand1("E");
        instructionsMap[147].setByteLength(1);
        instructionsMap[147].setCycles(4);


        instructionsMap[148].setDescription("SUB");
        instructionsMap[148].setOpCode("94");
        instructionsMap[148].setOperand1("H");
        instructionsMap[148].setByteLength(1);
        instructionsMap[148].setCycles(4);

        instructionsMap[149].setDescription("SUB");
        instructionsMap[149].setOpCode("95");
        instructionsMap[149].setOperand1("L");
        instructionsMap[149].setByteLength(1);
        instructionsMap[149].setCycles(4);

        instructionsMap[150].setDescription("SUB");
        instructionsMap[150].setOpCode("96");
        instructionsMap[150].setOperand1("(HL)");
        instructionsMap[150].setByteLength(1);
        instructionsMap[150].setCycles(8);

        instructionsMap[151].setDescription("SUB");
        instructionsMap[151].setOpCode("97");
        instructionsMap[151].setOperand1("A");


        instructionsMap[152].setDescription("SBC");
        instructionsMap[152].setOpCode("98");
        instructionsMap[152].setOperand1("A");
        instructionsMap[152].setOperand2("B");

        instructionsMap[153].setDescription("SBC");
        instructionsMap[153].setOpCode("99");
        instructionsMap[153].setOperand1("A");
        instructionsMap[153].setOperand2("C");

        instructionsMap[154].setDescription("SBC");
        instructionsMap[154].setOpCode("9A");
        instructionsMap[154].setOperand1("A");
        instructionsMap[154].setOperand2("D");

        instructionsMap[155].setDescription("SBC");
        instructionsMap[155].setOpCode("9B");
        instructionsMap[155].setOperand1("A");
        instructionsMap[155].setOperand2("E");

        instructionsMap[156].setDescription("SBC");
        instructionsMap[156].setOpCode("9C");
        instructionsMap[156].setOperand1("A");
        instructionsMap[156].setOperand2("H");

        instructionsMap[157].setDescription("SBC");
        instructionsMap[157].setOpCode("9D");
        instructionsMap[157].setOperand1("A");
        instructionsMap[157].setOperand2("L");

        instructionsMap[158].setDescription("SBC");
        instructionsMap[158].setOpCode("9E");
        instructionsMap[158].setOperand1("A");
        instructionsMap[158].setOperand2("(HL)");

        instructionsMap[159].setDescription("SBC");
        instructionsMap[159].setOpCode("9F");
        instructionsMap[159].setOperand1("A");
        instructionsMap[159].setOperand2("A");

        instructionsMap[160].setDescription("AND");
        instructionsMap[160].setOpCode("A0");
        instructionsMap[160].setOperand1("B");
        instructionsMap[160].setByteLength(1);
        instructionsMap[160].setCycles(4);

        instructionsMap[161].setDescription("AND");
        instructionsMap[161].setOpCode("A1");
        instructionsMap[161].setOperand1("C");
        instructionsMap[161].setByteLength(1);
        instructionsMap[161].setCycles(4);

        instructionsMap[162].setDescription("AND");
        instructionsMap[162].setOpCode("A2");
        instructionsMap[162].setOperand1("D");
        instructionsMap[162].setByteLength(1);
        instructionsMap[162].setCycles(4);

        instructionsMap[163].setDescription("AND");
        instructionsMap[163].setOpCode("A3");
        instructionsMap[163].setOperand1("E");
        instructionsMap[163].setByteLength(1);
        instructionsMap[163].setCycles(4);

        instructionsMap[164].setDescription("AND");
        instructionsMap[164].setOpCode("A4");
        instructionsMap[164].setOperand1("H");
        instructionsMap[164].setByteLength(1);
        instructionsMap[164].setCycles(4);

        instructionsMap[165].setDescription("AND");
        instructionsMap[165].setOpCode("A5");
        instructionsMap[165].setOperand1("L");
        instructionsMap[165].setByteLength(1);
        instructionsMap[165].setCycles(4);

        instructionsMap[166].setDescription("AND");
        instructionsMap[166].setOpCode("A6");
        instructionsMap[166].setOperand1("(HL)");
        instructionsMap[166].setByteLength(1);
        instructionsMap[166].setCycles(8);

        instructionsMap[167].setDescription("AND");
        instructionsMap[167].setOpCode("A7");
        instructionsMap[167].setOperand1("A");
        instructionsMap[167].setByteLength(1);
        instructionsMap[167].setCycles(4);

        instructionsMap[168].setDescription("XOR");
        instructionsMap[168].setOpCode("A8");
        instructionsMap[168].setOperand1("B");
        instructionsMap[168].setByteLength(1);
        instructionsMap[168].setCycles(4);

        instructionsMap[169].setDescription("XOR");
        instructionsMap[169].setOpCode("A9");
        instructionsMap[169].setOperand1("C");
        instructionsMap[169].setByteLength(1);
        instructionsMap[169].setCycles(4);

        instructionsMap[170].setDescription("XOR");
        instructionsMap[170].setOpCode("AA");
        instructionsMap[170].setOperand1("D");
        instructionsMap[170].setByteLength(1);
        instructionsMap[170].setCycles(4);

        instructionsMap[171].setDescription("XOR");
        instructionsMap[171].setOpCode("AB");
        instructionsMap[171].setOperand1("E");
        instructionsMap[171].setByteLength(1);
        instructionsMap[171].setCycles(4);

        instructionsMap[172].setDescription("XOR");
        instructionsMap[172].setOpCode("AC");
        instructionsMap[172].setOperand1("H");
        instructionsMap[172].setByteLength(1);
        instructionsMap[172].setCycles(4);

        instructionsMap[173].setDescription("XOR");
        instructionsMap[173].setOpCode("AD");
        instructionsMap[173].setOperand1("L");
        instructionsMap[173].setByteLength(1);
        instructionsMap[173].setCycles(4);

        instructionsMap[174].setDescription("XOR");
        instructionsMap[174].setOpCode("AE");
        instructionsMap[174].setOperand1("(HL)");
        instructionsMap[174].setByteLength(1);
        instructionsMap[174].setCycles(8);

        instructionsMap[175].setDescription("XOR");
        instructionsMap[175].setOpCode("AF");
        instructionsMap[175].setOperand1("A");
        instructionsMap[175].setByteLength(1);
        instructionsMap[175].setCycles(4);

        instructionsMap[176].setDescription("OR");
        instructionsMap[176].setOpCode("B0");
        instructionsMap[176].setOperand1("B");
        instructionsMap[176].setByteLength(1);
        instructionsMap[176].setCycles(4);

        instructionsMap[177].setDescription("OR");
        instructionsMap[177].setOpCode("B1");
        instructionsMap[177].setOperand1("C");
        instructionsMap[177].setByteLength(1);
        instructionsMap[177].setCycles(4);

        instructionsMap[178].setDescription("OR");
        instructionsMap[178].setOpCode("B2");
        instructionsMap[178].setOperand1("D");
        instructionsMap[178].setByteLength(1);
        instructionsMap[178].setCycles(4);

        instructionsMap[179].setDescription("OR");
        instructionsMap[179].setOpCode("B3");
        instructionsMap[179].setOperand1("E");
        instructionsMap[179].setByteLength(1);
        instructionsMap[179].setCycles(4);

        instructionsMap[180].setDescription("OR");
        instructionsMap[180].setOpCode("B4");
        instructionsMap[180].setOperand1("H");
        instructionsMap[180].setByteLength(1);
        instructionsMap[180].setCycles(4);

        instructionsMap[181].setDescription("OR");
        instructionsMap[181].setOpCode("B5");
        instructionsMap[181].setOperand1("L");
        instructionsMap[181].setByteLength(1);
        instructionsMap[181].setCycles(4);

        instructionsMap[182].setDescription("OR");
        instructionsMap[182].setOpCode("B6");
        instructionsMap[182].setOperand1("(HL)");
        instructionsMap[182].setByteLength(1);
        instructionsMap[182].setCycles(8);

        instructionsMap[183].setDescription("OR");
        instructionsMap[183].setOpCode("B7");
        instructionsMap[183].setOperand1("A");
        instructionsMap[183].setByteLength(1);
        instructionsMap[183].setCycles(4);

        instructionsMap[184].setDescription("CP");
        instructionsMap[184].setOpCode("B8");
        instructionsMap[184].setOperand1("B");
        instructionsMap[184].setByteLength(1);
        instructionsMap[184].setCycles(4);

        instructionsMap[185].setDescription("CP");
        instructionsMap[185].setOpCode("B9");
        instructionsMap[185].setOperand1("C");
        instructionsMap[185].setByteLength(1);
        instructionsMap[185].setCycles(4);

        instructionsMap[186].setDescription("CP");
        instructionsMap[186].setOpCode("BA");
        instructionsMap[186].setOperand1("D");
        instructionsMap[186].setByteLength(1);

        instructionsMap[187].setDescription("CP");
        instructionsMap[187].setOpCode("BB");
        instructionsMap[187].setOperand1("E");
        instructionsMap[187].setByteLength(1);

        instructionsMap[188].setDescription("CP");
        instructionsMap[188].setOpCode("BC");
        instructionsMap[188].setOperand1("H");
        instructionsMap[188].setByteLength(1);

        instructionsMap[189].setDescription("CP");
        instructionsMap[189].setOpCode("BD");
        instructionsMap[189].setOperand1("L");
        instructionsMap[189].setByteLength(1);

        instructionsMap[190].setDescription("CP");
        instructionsMap[190].setOpCode("BE");
        instructionsMap[190].setOperand1("(HL)");
        instructionsMap[190].setByteLength(1);
        instructionsMap[190].setCycles(8);

        instructionsMap[191].setDescription("CP");
        instructionsMap[191].setOpCode("BF");
        instructionsMap[191].setOperand1("A");
        instructionsMap[191].setByteLength(1);
        instructionsMap[191].setCycles(4);

        instructionsMap[192].setDescription("RET");
        instructionsMap[192].setOpCode("C0");
        instructionsMap[192].setOperand1("NZ");
        instructionsMap[192].setByteLength(1);
        instructionsMap[192].setCycles(20);

        instructionsMap[193].setDescription("POP");
        instructionsMap[193].setOpCode("C1");
        instructionsMap[193].setOperand1("BC");
        instructionsMap[193].setByteLength(1);
        instructionsMap[193].setCycles(12);

        instructionsMap[194].setDescription("JP");
        instructionsMap[194].setOpCode("C2");
        instructionsMap[194].setOperand1("NZ");
        instructionsMap[194].setOperand2("a16");
        instructionsMap[194].setByteLength(3);
        instructionsMap[194].setCycles(12);

        instructionsMap[195].setDescription("JP");
        instructionsMap[195].setOpCode("C3");
        instructionsMap[195].setOperand1("a16");
        instructionsMap[195].setByteLength(3);
        instructionsMap[195].setCycles(16);

        instructionsMap[196].setDescription("CALL");
        instructionsMap[196].setOpCode("C4");
        instructionsMap[196].setOperand1("NZ");
        instructionsMap[196].setOperand2("a16");
        instructionsMap[196].setCycles(12);
        instructionsMap[196].setByteLength(3);

        instructionsMap[197].setDescription("PUSH");
        instructionsMap[197].setOpCode("C5");
        instructionsMap[197].setOperand1("BC");
        instructionsMap[197].setByteLength(1);
        instructionsMap[197].setCycles(16);

        instructionsMap[198].setDescription("ADD");
        instructionsMap[198].setOpCode("C6");
        instructionsMap[198].setOperand1("A");
        instructionsMap[198].setOperand2("d8");
        instructionsMap[198].setByteLength(2);
        instructionsMap[198].setCycles(8);

        instructionsMap[199].setDescription("RST");
        instructionsMap[199].setOpCode("C7");
        instructionsMap[199].setOperand1("00");
        instructionsMap[199].setByteLength(1);

        instructionsMap[200].setDescription("RET");
        instructionsMap[200].setOpCode("C8");
        instructionsMap[200].setOperand1("Z");
        instructionsMap[200].setByteLength(1);
        instructionsMap[200].setCycles(20);

        instructionsMap[201].setDescription("RET");
        instructionsMap[201].setOpCode("C9");
        instructionsMap[201].setByteLength(1);
        instructionsMap[201].setCycles(16);

        instructionsMap[202].setDescription("JP");
        instructionsMap[202].setOpCode("CA");
        instructionsMap[202].setOperand1("Z");
        instructionsMap[202].setOperand2("a16");
        instructionsMap[202].setByteLength(3);
        instructionsMap[202].setCycles(12);


        instructionsMap[203].setDescription("PREFIX CB");
        instructionsMap[203].setOpCode("CB");
        instructionsMap[203].setByteLength(2);
        instructionsMap[203].setCycles(4);

        instructionsMap[204].setDescription("CALL");
        instructionsMap[204].setOpCode("CC");
        instructionsMap[204].setOperand1("Z");
        instructionsMap[204].setOperand2("a16");
        instructionsMap[204].setByteLength(3);

        instructionsMap[205].setDescription("CALL");
        instructionsMap[205].setOpCode("CD");
        instructionsMap[205].setOperand1("a16");
        instructionsMap[205].setByteLength(3);
        instructionsMap[205].setCycles(24);

        instructionsMap[206].setDescription("ADC");
        instructionsMap[206].setOpCode("CE");
        instructionsMap[206].setOperand1("A");
        instructionsMap[206].setOperand2("d8");
        instructionsMap[206].setByteLength(2);
        instructionsMap[206].setCycles(8);

        instructionsMap[207].setDescription("RST");
        instructionsMap[207].setOpCode("CF");
        instructionsMap[207].setOperand1("08");
        instructionsMap[207].setByteLength(1);

        instructionsMap[208].setDescription("RET");
        instructionsMap[208].setOpCode("D0");
        instructionsMap[208].setOperand1("NC");
        instructionsMap[208].setByteLength(1);
        instructionsMap[208].setCycles(20);

        instructionsMap[209].setDescription("POP");
        instructionsMap[209].setOpCode("D1");
        instructionsMap[209].setOperand1("DE");
        instructionsMap[209].setByteLength(1);
        instructionsMap[209].setCycles(12);

        instructionsMap[210].setDescription("JP");
        instructionsMap[210].setOpCode("D2");
        instructionsMap[210].setOperand1("NC");
        instructionsMap[210].setOperand2("a16");
        instructionsMap[210].setByteLength(3);

        instructionsMap[211].setDescription("");
        instructionsMap[211].setOpCode("D3");

        instructionsMap[212].setDescription("CALL");
        instructionsMap[212].setOpCode("D4");
        instructionsMap[212].setOperand1("NC");
        instructionsMap[212].setOperand2("a16");
        instructionsMap[212].setByteLength(3);
        instructionsMap[212].setCycles(12);

        instructionsMap[213].setDescription("PUSH");
        instructionsMap[213].setOpCode("D5");
        instructionsMap[213].setOperand1("DE");
        instructionsMap[213].setByteLength(1);
        instructionsMap[213].setCycles(16);


        instructionsMap[214].setDescription("SUB");
        instructionsMap[214].setOpCode("D6");
        instructionsMap[214].setOperand1("d8");
        instructionsMap[214].setByteLength(2);
        instructionsMap[214].setCycles(8);

        instructionsMap[215].setDescription("RST");
        instructionsMap[215].setOpCode("D7");
        instructionsMap[215].setOperand1("10");
        instructionsMap[215].setByteLength(1);

        instructionsMap[216].setDescription("RET");
        instructionsMap[216].setOpCode("D8");
        instructionsMap[216].setOperand1("C");
        instructionsMap[216].setByteLength(1);
        instructionsMap[216].setCycles(20);

        instructionsMap[217].setDescription("RETI");
        instructionsMap[217].setOpCode("D9");
        instructionsMap[217].setByteLength(1);

        instructionsMap[218].setDescription("JP");
        instructionsMap[218].setOpCode("DA");
        instructionsMap[218].setOperand1("C");
        instructionsMap[218].setOperand2("a16");
        instructionsMap[218].setByteLength(3);

        instructionsMap[219].setDescription("");
        instructionsMap[219].setOpCode("DB");

        instructionsMap[220].setDescription("CALL");
        instructionsMap[220].setOpCode("DC");
        instructionsMap[220].setOperand1("C");
        instructionsMap[220].setOperand2("a16");
        instructionsMap[220].setByteLength(3);

        instructionsMap[221].setDescription("");
        instructionsMap[221].setOpCode("DD");

        instructionsMap[222].setDescription("SBC");
        instructionsMap[222].setOpCode("DE");
        instructionsMap[222].setOperand1("A");
        instructionsMap[222].setOperand2("d8");
        instructionsMap[222].setByteLength(2);

        instructionsMap[223].setDescription("RST");
        instructionsMap[223].setOpCode("DF");
        instructionsMap[223].setOperand1("18");
        instructionsMap[223].setByteLength(1);

        instructionsMap[224].setDescription("LD");
        instructionsMap[224].setOpCode("E0");
        instructionsMap[224].setOperand1("($FF00+a8)");
        instructionsMap[224].setOperand2("A");
        instructionsMap[224].setByteLength(2);
        instructionsMap[224].setCycles(12);

        instructionsMap[225].setDescription("POP");
        instructionsMap[225].setOpCode("E1");
        instructionsMap[225].setOperand1("HL");
        instructionsMap[225].setByteLength(1);
        instructionsMap[225].setCycles(12);

        instructionsMap[226].setDescription("LD");
        instructionsMap[226].setOpCode("E2");
        instructionsMap[226].setOperand1("($FF00+C)");
        instructionsMap[226].setOperand2("A");
        instructionsMap[226].setByteLength(1);
        instructionsMap[226].setCycles(8);

        instructionsMap[227].setDescription("");
        instructionsMap[227].setOpCode("E3");

        instructionsMap[228].setDescription("");
        instructionsMap[228].setOpCode("E4");

        instructionsMap[229].setDescription("PUSH");
        instructionsMap[229].setOpCode("E5");
        instructionsMap[229].setOperand1("HL");
        instructionsMap[229].setByteLength(1);
        instructionsMap[229].setCycles(16);

        instructionsMap[230].setDescription("AND");
        instructionsMap[230].setOpCode("E6");
        instructionsMap[230].setOperand1("d8");
        instructionsMap[230].setByteLength(2);
        instructionsMap[230].setCycles(8);

        instructionsMap[231].setDescription("RST");
        instructionsMap[231].setOpCode("E7");
        instructionsMap[231].setOperand1("20");
        instructionsMap[231].setByteLength(1);

        instructionsMap[232].setDescription("ADD");
        instructionsMap[232].setOpCode("E8");
        instructionsMap[232].setOperand1("SP");
        instructionsMap[232].setOperand2("r8");
        instructionsMap[232].setByteLength(2);

        instructionsMap[233].setDescription("JP");
        instructionsMap[233].setOpCode("E9");
        instructionsMap[233].setOperand1("(HL)");
        instructionsMap[233].setByteLength(1);
        instructionsMap[233].setCycles(4);

        instructionsMap[234].setDescription("LD");
        instructionsMap[234].setOpCode("EA");
        instructionsMap[234].setOperand1("(a16)");
        instructionsMap[234].setOperand2("A");
        instructionsMap[234].setByteLength(3);
        instructionsMap[234].setCycles(16);

        instructionsMap[235].setDescription("");
        instructionsMap[235].setOpCode("EB");


        instructionsMap[236].setDescription("");
        instructionsMap[236].setOpCode("EC");

        instructionsMap[237].setDescription("");
        instructionsMap[237].setOpCode("ED");


        instructionsMap[238].setDescription("XOR");
        instructionsMap[238].setOpCode("EE");
        instructionsMap[238].setOperand1("d8");
        instructionsMap[238].setByteLength(2);
        instructionsMap[238].setCycles(8);

        instructionsMap[239].setDescription("RST");
        instructionsMap[239].setOpCode("EF");
        instructionsMap[239].setOperand1("28");
        instructionsMap[239].setByteLength(1);
        instructionsMap[239].setCycles(16);

        instructionsMap[240].setDescription("LD");
        instructionsMap[240].setOpCode("F0");
        instructionsMap[240].setOperand1("A");
        instructionsMap[240].setOperand2("($FF00+a8)");
        instructionsMap[240].setByteLength(2);
        instructionsMap[240].setCycles(12);

        instructionsMap[241].setDescription("POP");
        instructionsMap[241].setOpCode("F1");
        instructionsMap[241].setOperand1("AF");
        instructionsMap[241].setByteLength(1);
        instructionsMap[241].setCycles(12);

        instructionsMap[242].setDescription("LD");
        instructionsMap[242].setOpCode("F2");
        instructionsMap[242].setOperand1("A");
        instructionsMap[242].setOperand2("(C)");
        instructionsMap[242].setByteLength(2);

        instructionsMap[243].setDescription("DI");
        instructionsMap[243].setOpCode("F3");
        instructionsMap[243].setByteLength(1);
        instructionsMap[243].setCycles(4);

        instructionsMap[244].setDescription("");
        instructionsMap[244].setOpCode("F4");

        instructionsMap[245].setDescription("PUSH");
        instructionsMap[245].setOpCode("F5");
        instructionsMap[245].setOperand1("AF");
        instructionsMap[245].setByteLength(1);
        instructionsMap[245].setCycles(16);

        instructionsMap[246].setDescription("OR");
        instructionsMap[246].setOpCode("F6");
        instructionsMap[246].setOperand1("d8");
        instructionsMap[246].setByteLength(2);

        instructionsMap[247].setDescription("RST");
        instructionsMap[247].setOpCode("F7");
        instructionsMap[247].setOperand1("30");
        instructionsMap[247].setByteLength(1);

        instructionsMap[248].setDescription("LD");
        instructionsMap[248].setOpCode("F8");
        instructionsMap[248].setOperand1("HL");
        instructionsMap[248].setOperand2("SP+r8");
        instructionsMap[248].setByteLength(2);
        instructionsMap[248].setCycles(12);

        instructionsMap[249].setDescription("LD");
        instructionsMap[249].setOpCode("F9");
        instructionsMap[249].setOperand1("SP");
        instructionsMap[249].setOperand2("HL");
        instructionsMap[249].setByteLength(1);

        instructionsMap[250].setDescription("LD");
        instructionsMap[250].setOpCode("FA");
        instructionsMap[250].setOperand1("A");
        instructionsMap[250].setOperand2("(a16)");
        instructionsMap[250].setByteLength(3);
        instructionsMap[250].setCycles(16);

        instructionsMap[251].setDescription("EI");
        instructionsMap[251].setOpCode("FB");
        instructionsMap[251].setByteLength(1);
        instructionsMap[251].setCycles(4);

        instructionsMap[252].setDescription("");
        instructionsMap[252].setOpCode("FC");


        instructionsMap[253].setDescription("");
        instructionsMap[253].setOpCode("FD");


        instructionsMap[254].setDescription("CP");
        instructionsMap[254].setOpCode("FE");
        instructionsMap[254].setOperand1("d8");
        instructionsMap[254].setByteLength(2);
        instructionsMap[254].setCycles(8);

        instructionsMap[255].setDescription("RST");
        instructionsMap[255].setOpCode("FF");
        instructionsMap[255].setOperand1("38");
        instructionsMap[255].setByteLength(1);
        instructionsMap[255].setCycles(16);



    }
}
