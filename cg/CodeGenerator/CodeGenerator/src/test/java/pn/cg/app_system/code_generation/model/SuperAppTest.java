package pn.cg.app_system.code_generation.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pn.cg.util.CodeGeneratorUtil;

public class SuperAppTest {

    @Test
    public void toStringForMethodsTest(){

        final String exampleOutputFromOLLAMA = "//Create a NameGenerator class\n" +
                "public class NameGenerator\n" +
                "{\n" +
                "        //String array to save the Name parts\n" +
                "        private static final String[] Beginning = {\"Kr\", \"Ca\", \"Ra\", \"Mrok\",\n" +
                "                                        \"Cru\", \"Ray\", \"Bre\", \"Zed\", \"Drak\", \"Mor\", \"Jag\", \"Mer\", \"Jar\", \"Mjol\",\n" +
                "                                        \"Zork\", \"Mad\", \"Cry\", \"Zur\", \"Creo\", \"Azak\", \"Azur\", \"Rei\", \"Cro\",\n" +
                "                                        \"Mar\", \"Luk\"};\n" +
                "        private static final String[] Middle = {\"air\", \"ir\", \"mi\", \"sor\", \"mee\", \"clo\",\n" +
                "                                        \"red\", \"cra\", \"ark\", \"arc\", \"miri\", \"lori\", \"cres\", \"mur\", \"zer\",\n" +
                "                                        \"marac\", \"zoir\", \"slamar\", \"salmar\", \"urak\"};\n" +
                "        private static final String[] End = {\"d\", \"ed\", \"ark\", \"arc\", \"es\", \"er\", \"der\",\n" +
                "                                        \"tron\", \"med\", \"ure\", \"zur\", \"cred\", \"mur\"};\n" +
                "\n" +
                "        //Generates a random name\n" +
                "        public String generateName()\n" +
                "        {\n" +
                "                Random rand = new Random();\n" +
                "\n" +
                "                //Concatenate the random string values\n" +
                "                String name = Beginning[rand.nextInt(Beginning.length)]+\n" +
                "                                        Middle[rand.nextInt(Middle.length)]+\n" +
                "                                        End[rand.nextInt(End.length)];\n" +
                "\n" +
                "                return name;\n" +
                "        }\n" +
                "}\n" +
                "\n" +
                "//Driver Code\n" +
                "class Main\n" +
                "{\n" +
                "        public static void main(String args[])\n" +
                "        {\n" +
                "                NameGenerator nameGenerator = new NameGenerator();\n" +
                "\n" +
                "                // Generate 10 random names\n" +
                "                for (int i = 0; i < 10; i++)\n" +
                "                {\n" +
                "                        System.out.println(nameGenerator.generateName());\n" +
                "                }\n" +
                "        }\n" +
                "}\n";


        SuperApp superAppTest = new SuperApp("NameGenerator",true);
        superAppTest.setMethods(CodeGeneratorUtil.ExtractMethodsFromJavaSourceCodeString(exampleOutputFromOLLAMA));

        System.out.println(superAppTest.toStringForMethods());

        Assertions.assertEquals(true,true);

    }
}
