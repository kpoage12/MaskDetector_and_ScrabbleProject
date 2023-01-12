import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Scrabble {
    //Creating an alphabet with corresponding int score values

    //create instance variable for dictionary
    private  Trie dictionary;

    //constructor to initialize trie
    public Scrabble(){
        dictionary=new Trie();
    }

    //Takes in a string and returns its corresponding int value by running a series of if statements
    public int computeScore(String word){
        int sum = 0;
        for (int i=0; i<word.length(); i++){
            if (word.charAt(i)=='a' || word.charAt(i)=='e' || word.charAt(i)=='i' || word.charAt(i)=='l'
            || word.charAt(i)=='n' || word.charAt(i)=='o' || word.charAt(i)=='r' || word.charAt(i)=='s'
            || word.charAt(i)=='t' || word.charAt(i)=='u'){
                sum+=1;
            }
            if (word.charAt(i)=='d' || word.charAt(i)=='g'){
                sum+=2;
            }
            if (word.charAt(i)=='b' || word.charAt(i)=='c' || word.charAt(i)=='m' || word.charAt(i)=='p'){
                sum+=3;
            }
            if (word.charAt(i)=='f' || word.charAt(i)=='h' || word.charAt(i)=='v' || word.charAt(i)=='w'
            || word.charAt(i)=='y'){
                sum+=4;
            }
            if (word.charAt(i)=='k'){
                sum+=5;
            }
            if (word.charAt(i)=='j'||word.charAt(i)=='x'){
                sum+=8;
            }
            if (word.charAt(i)=='q'||word.charAt(i)=='z'){
                sum+=10;
            }
            else{
                sum+=0;
            }
        }
        return sum;
    }

    //Takes in a file name and inserts each value into the trie while it has a following line
    public void load(String file) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(file));
        while(scan.hasNextLine()){
            dictionary.add(scan.nextLine());
        }
    }

    //Takes in a string of letters and returns an string arraylist with all possible combinations of that word of
    //any length.
    public static ArrayList<String> searchWords(String chars){
        ArrayList<String> list1 = new ArrayList<>();
        // Base case: If chars has a length less than two, add chars to the arraylist.
        if (chars.length()<2){
            list1.add(chars);
        }
        else {
            //Initialize second arraylist
            ArrayList<String> list2 = new ArrayList<>();
            for (int i = 0; i < chars.length(); i++) {

                //First, if statement to handle a space which signifies a blank tile
                if (chars.charAt(i) == ' ') {

                    //cycle through alphabet
                    for (char j = 'a'; j <= 'z'; j++) {

                        //Create new string that represents the letter at index i
                        String newChars = chars.substring(0, i) + j + chars.substring(i + 1);

                        //creates an arraylist with all words without i
                        list2 = searchWords(newChars.substring(0, i) + newChars.substring(i + 1));

                        //loop through list2 and add letter i back on
                        for (int k=0; k<list2.size(); k++) {

                            //first add string without to get all values less than the original length
                            list1.add(list2.get(k));

                            //Add letter at i back on each word of list2
                            String x = newChars.charAt(i) + list2.get(k);

                            //set list2 at index k to x
                            list2.set(k, x);

                            //add x to list1
                            list1.add(x);
                        }
                    }
                }
                //Now handle a string without a blank tile
                //recursively call search words to find all words without letter at chars.charAt(i)
                list2 = searchWords(chars.substring(0, i) + chars.substring(i + 1));

                //loop through list2 and add all its values into list1
                for (int j = 0; j < list2.size(); j++) {

                    //add string without letter at charAt(i) to get all strings with a length less than chars.length()
                    list1.add(list2.get(j));

                    //Add letter at i back onto the shorter string
                    String y = chars.charAt(i) + list2.get(j);

                    //set list2.get(j) to y to update it
                    list2.set(j, y);

                    //add all list2 values to list1
                    list1.add(y);
                }
            }
        }
        //For loop to remove all Strings in list1 that have a space in it. If not, the code will not run because dictionary
        //does not have strings with spaces
        for (int i=0; i<list1.size(); i++){
            String current = list1.get(i);
            for (int j=0; j<current.length(); j++){
                if (current.charAt(j)==' '){
                    list1.remove(i);
                }
            }
        }

        //return list1 with all possible combinations of string char
        return list1;
    }


    //Takes an string arraylist as input and returns all values that are found in the dictionary
    public void print(ArrayList<String> combinations){
        //First make a new string arraylist that will be used to print
        ArrayList<String> combos = new ArrayList<>();

        //loop through input and add all values into new arraylist unless the new arraylist already contains it.
        //this removes all duplicates if there are multiple letters
        for (int i=0; i<combinations.size(); i++) {
            if (!combos.contains(combinations.get(i))) {
                combos.add(combinations.get(i));
            }
        }

        //loop through the new arraylist and if the value at index i is found in the dictionary, print out the value.
        //then call compute score to find the int value of the string and print that out as well.
        for (int i=0; i<combos.size(); i++) {
            if(dictionary.contains(combos.get(i))) {
                System.out.print(combos.get(i));
                String score = ": Score is " + computeScore(combos.get(i));
                System.out.println(score);
            }
        }
    }

    //Similar to print but now print out all combinations not found in the dictionary to find all anagrams of the string
    public void printClabbers(ArrayList<String> combinations){
        ArrayList<String> combos = new ArrayList<>();

        //Still remove all duplicates
        for (int i=0; i<combinations.size(); i++) {
            if (!combos.contains(combinations.get(i))){
                combos.add(combinations.get(i));
            }
        }

        //Now print out all combinations not found in the dictionary and their corresponding int score
        for (int i=0; i<combos.size(); i++) {

            if (!dictionary.contains(combos.get(i))){
                System.out.print(combos.get(i));
                String score = ": Score is " + computeScore(combos.get(i));
                System.out.println(score);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //Ask the user what type of game mode
        System.out.println("Is this scrabble or clabbers?");
        Scanner mode = new Scanner(System.in);
        String type = (mode.nextLine()).toLowerCase();

        //Case if scrabble is the game mode
        if (type.equals("scrabble")){
            System.out.println("enter letters:");
            Scanner scan = new Scanner(System.in);
            String letters = (scan.nextLine()).toLowerCase();
            Scrabble scrabble = new Scrabble();

            //load dictionary into the trie
            scrabble.load("dictionaries/usa.txt");
            scrabble.print(scrabble.searchWords(letters));

        }

        //case if clabbers is the game mode
        else if (type.equals("clabbers")){
            System.out.println("enter letters:");
            Scanner scan = new Scanner(System.in);
            String letters = (scan.nextLine()).toLowerCase();
            Scrabble scrabble = new Scrabble();

            //load dictionary into the trie
            scrabble.load("dictionaries/usa.txt");
            scrabble.printClabbers(scrabble.searchWords(letters));
        }

        //no game mode specified
        else{
            System.out.println("Not valid game mode");
        }
    }
}
