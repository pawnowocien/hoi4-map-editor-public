package src.java.main;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        String provincesFilePath = "src/resources/files/map/definition.csv";
        String provincesMapPath = "src/resources/files/map/provinces.bmp";
        String hisStatesDirPath = "src/resources/files/history/states";
        String hisCountriesDirPath = "src/resources/files/history/countries";
        String comCountryTagsDirPath = "src/resources/files/common/country_tags";
        String comCountriesDirPath = "src/resources/files/common/countries";

        File provincesFile = new File(provincesFilePath);
        File provincesMap = new File(provincesMapPath);
        File hisStatesDir = new File(hisStatesDirPath);
        File hisCountriesDir = new File(hisCountriesDirPath);
        File comCountryTagsDir = new File(comCountryTagsDirPath);
        File comCountriesDir = new File(comCountriesDirPath);

//        ProvinceData[] pd = ParadoxParser.provinceParser(provincesFile);
//        for (ProvinceData p : pd) {
//            System.out.println(p.getId() + " " + p.getR() + ";" + p.getG() + ";" + p.getB());
//        }
//
//        System.out.println(ParadoxParser.load(new File("src/resources/files/common/country_tags/00_countries.txt")));
//        new MultiGenerator(provincesFile, provincesMap, hisStatesDir, hisCountriesDir, comCountryTagsDir, comCountriesDir);
//        String[] s = hisStatesDir.list();
//        for (int i = 0; i < s.length; i++) {
//            System.out.println(s[i]);
//        }

//        System.out.println(ParadoxParser.load(new File("src/resources/files/history/countries/COG - Congo.txt")));
//        String[] s1 = new String[]{"{","A","=","A","B","C","{","P","{","L","{","}","}","Q","}","D","}"};
//        String[] s2 = new String[]{"{","A","=","B","}"};

//        System.out.println(ParadoxParser.load(new File("src/resources/testfilecolor")));
    }
}
