import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        ArrayList<HashMap<String, Object>> test = readcsv();
        writeToFile(test);
        ArrayList test2 = readBackJSON("src/student_json.txt");
        HashMap<String, Object> test3 = readBackJSONRecord("src/record1_json.txt");
        System.out.println(test2.get(0));
        System.out.println(test3);
        readAndWrite(test);
    }

    public static ArrayList<HashMap<String, Object>> readcsv() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/Student.csv")); //Creates a buffered
        //reader to read in the file
        ArrayList list = new ArrayList();
        String line = reader.readLine(); //Reads first line of file
        String[] headers = line.split(","); //Splits headers into separate strings and stores in array
        String line1 = reader.readLine();
        while(line1 != null) {
            HashMap<String,Object> map = new HashMap<String,Object>(); //Create a map for each line of the file
            String[] nextLine = line1.split(","); //Split line into separate strings
            String street = "";
            String city = "";
            String zip = "";
            String state = "";
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equals("Address.Street"))
                    street = nextLine[i];
                else if (headers[i].equals("Address.City"))
                    city = nextLine[i];
                else if (headers[i].equals("Address.Zip"))
                    zip = nextLine[i];
                else if (headers[i].equals("Address.State"))
                    state = nextLine[i];
                else
                    map.put(headers[i], nextLine[i]); //Add every other type as a String
            }
            map.put("Address", new Address(street, city, state, zip));
            line1 = reader.readLine(); //Read the next line of the file
            list.add(map);
        }
        reader.close();
        return list;
    }

    public static JSONObject addressToJSON(Address ad) {
        JSONObject obj = new JSONObject();
        obj.put("street", ad.street);
        obj.put("city", ad.city);
        obj.put("state", ad.state);
        obj.put("zip", ad.zip);
        return obj;
    }

    public static JSONObject recordToJSON(HashMap<String, Object> record) {
        JSONObject obj = new JSONObject();
        for (String key : record.keySet()) {
            obj.put(key, record.get(key));
        }
        JSONObject obj2 = addressToJSON((Address) record.get("Address"));
        obj.put("Address", obj2);
        return obj;
    }

    public static JSONArray collectionToJSON(ArrayList list ) {
        JSONArray arr = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            arr.add(recordToJSON((HashMap<String, Object>)list.get(i)));
        }
        return arr;
    }

    public static Address reverseAddress(JSONObject obj) {
        String street = (String) obj.get("street");
        String city = (String) obj.get("city");
        String state = (String) obj.get("state");
        String zip = (String) obj.get("zip");
        return new Address(street, city, state, zip);
    }

    public static HashMap<String, Object> reverseRecord (JSONObject obj) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Object key : obj.keySet()) {
            map.put((String) key, obj.get(key));
        }
        Address obj2 = reverseAddress((JSONObject) obj.get("Address"));
        map.put("Address", obj2);
        return map;
    }

    public static ArrayList reverseCollection(JSONArray arr) {
        ArrayList<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
        for(Object record : arr) {
            ret.add(reverseRecord((JSONObject) record));
        }
        return ret;
    }

    public static void writeToFile(ArrayList<HashMap<String, Object>> list) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/record1_json.txt"));
        writer.write(recordToJSON(list.get(0)).toJSONString());
        writer.close();
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("src/student_json.txt"));
        writer2.write(collectionToJSON(list).toJSONString());
        writer2.close();
    }

    public static ArrayList readBackJSON(String f) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        JSONParser parser = new JSONParser();
        JSONArray obj = (JSONArray) parser.parse(reader);
        return reverseCollection(obj);
    }

    public static HashMap<String, Object> readBackJSONRecord(String f) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(reader);
        HashMap<String, Object> map = reverseRecord(obj);
        return map;
    }

    public static void readAndWrite(ArrayList<HashMap<String, Object>> list) throws IOException, ParseException {
        writeToFile(list);
        ArrayList test = readBackJSON("src/student_json.txt");
        System.out.println(test.get(6));
        System.out.println(list.get(6));
    }

}
