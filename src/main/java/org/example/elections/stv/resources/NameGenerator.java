package org.example.elections.stv.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class NameGenerator {
    private static String[] maleNames = new String[]{
            "Alexander","Arthur","Adam","Andrew","Austin","Alan","Anthony","Aaron",
            "Brandon","Benjamin","Bobby","Bob","Brian",
            "Charles","Christopher",
            "Daniel","David","Donald","Douglas","Dylan","Dennis",
            "Edward","Elijah","Eric",
            "Frank",
            "George","Gary","Gregory",
            "James","John","Joseph","Joshua","Jason","Jeffrey","Jacob","Jonathan","Justin","Jack","Jerry","Jose",
            "Kenneth","Kevin",
            "Larry",
            "Michael","Mark","Matthew",
            "Nicholas","Nathan",
            "Paul","Patrick",
            "Robert","Richard","Ronald","Ryan","Raymond",
            "Steven","Stephen","Scott","Samuel",
            "Thomas","Timothy","Tyler",
            "William",

            "Henry","Zachary","Peter","Kyle","Noah","Ethan","Jeremy","Walter","Christian","Keith","Roger",
            "Terry","Sean","Gerald","Carl","Harold","Lawrence","Jordan","Jesse","Bryan","Billy","Bruce","Gabriel","Joe",
            "Logan","Juan","Albert","Willie","Wayne","Randy","Vincent","Mason","Roy","Ralph","Russell","Bradley","Philip","Eugene"};

    private static String[] femaleNames = new String[]{
            "Ashley","Amanda","Amber","Alice","Amy","Angela","Anna","Alexis","Abigail",
            "Barbara","Brittany","Betty","Brenda",
            "Cynthia","Carol","Christine","Carolyn","Catherine",
            "Diana","Doris","Donna","Deborah","Dorothy","Debra","Diane",
            "Emily","Elizabeth","Emma","Erin","Evelyn","Faythe",
            "Helen","Heather",
            "Jessica","Jennifer","Janet","Julie","Joyce","Jean","Judy","Isabella","Julia","Jacqueline",
            "Karen","Kimberly","Kathleen","Katherine",
            "Lisa","Linda","Laura",
            "Mary","Margaret","Michelle","Melissa","Maria",
            "Nancy","Nicole","Olivia",
            "Patricia","Pamela","Philine",
            "Rebecca","Rachel",
            "Susan","Sarah","Sandra","Stephanie","Sharon","Shirley","Samantha",
            "Victoria","Virginia",

            "Ruth","Lauren","Kelly","Christina","Joan","Judith","Andrea","Hannah",
            "Megan","Cheryl","Martha","Madison","Teresa","Gloria","Sara","Janice","Ann","Kathryn","Sophia","Frances",
            "Grace","Denise","Danielle","Marilyn","Beverly","Charlotte","Natalie","Theresa",
            "Kayla","Lori","Marie"};

    private static String[] neutralNames = new String[]{"Charlie"};

    private static String[] surnames = new String[]{"Smith","Jones","Williams","Taylor","Brown","Davies","Evans","Wilson","Thomas","Johnson","Roberts",
            "Robinson","Thompson","Wright","Walker","White","Edwards","Hughes","Green","Hall","Lewis","Harris","Clarke","Patel","Jackson","Wood",
            "Turner","Martin","Cooper","Hill","Ward","Morris","Moore","Clark","Lee","King","Baker","Harrison","Morgan","Allen","James","Scott",
            "Phillips","Watson","Davis","Parker","Price","Bennett","Young","Griffiths","Mitchell","Kelly","Cook","Carter","Richardson","Bailey",
            "Collins","Bell","Shaw","Murphy","Miller","Cox","Richards","Khan","Marshall","Anderson","Simpson","Ellis","Adams","Singh","Begum",
            "Wilkinson","Foster","Chapman","Powell","Webb","Rogers","Gray","Mason","Ali","Hunt","Hussain","Campbell","Matthews","Owen","Palmer",
            "Holmes","Mills","Barnes","Knight","Lloyd","Butler","Russell","Barker","Fisher","Stevens","Jenkins","Murray","Dixon","Harvey"};

    public static String getRandomName() {
        Vector<String> names = new Vector<>();
        names.addAll(Arrays.asList(maleNames));
        names.addAll(Arrays.asList(femaleNames));
        names.addAll(Arrays.asList(neutralNames));
        Collections.shuffle(names);
        Vector<String> names2 = new Vector<>(Arrays.asList(surnames));
        Collections.shuffle(names2);
        return names.firstElement()+ " " + names2.firstElement();
    }

}
