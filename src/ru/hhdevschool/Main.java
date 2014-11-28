package ru.hhdevschool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    static Map<Integer,User> users = new HashMap<Integer,User>();
    public static void main(String[] args) throws IOException {
        if(args.length == 0) throw new IllegalArgumentException("No log file was specified");

        //Getting info from file
        String filename = args[0];
        BufferedReader bufferedReader =new BufferedReader(new FileReader(filename));
        String line;

        //Calculating total online time
        while((line = bufferedReader.readLine()) != null){
            String[] elements = line.split(",");
            Date time = new Date(Integer.parseInt(elements[0].trim())*1000);
            int id = Integer.parseInt(elements[1].trim());
            String action = elements[2].trim();
            if(!users.containsKey(id)){
                users.put(id, new User(id));
            }
            User user = users.get(id);
            if(action.equalsIgnoreCase("login")){
                user.setLastLogin(time);
                continue;
            }
            if(action.equalsIgnoreCase("logout")){
                long newTime = time.getTime() - user.getLastLogin().getTime();
                user.setTotalOnline(user.getTotalOnline() + newTime);
            }
        }

        //Sorting and outputting the data
        User[] list = users.values().toArray(new User[users.size()]);
        Arrays.sort(list, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if(o1.getTotalOnline() == o2.getTotalOnline()) return 0;
                return (o1.getTotalOnline() > o2.getTotalOnline())?-1:1;
            }
        });

        for(int i=0; i<list.length; i++){
            System.out.println("User id: "+list[i].getId()+" total online time: "+list[i].getTotalOnline()/1000+" seconds");
        }
    }

    static class User{
        int id;
        Date lastLogin;
        long totalOnline;

        public User(int id){
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public Date getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(Date lastLogin) {
            this.lastLogin = lastLogin;
        }

        public long getTotalOnline() {
            return totalOnline;
        }

        public void setTotalOnline(long totalOnline) {
            this.totalOnline = totalOnline;
        }
    }
}
