package Database;

import External.MonkeyLearnClient;
import Model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class DBOperations_Hibernate {
    private static ApplicationContext context;
    private static SessionFactory sessionFactory;
    private static Session session;
    private static Set<String> set = new HashSet<>(Arrays.asList("data","ai","software","web","ios","android","business","machine learn","natural language","system","ui"));
    public DBOperations_Hibernate(){
        context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        sessionFactory = (SessionFactory) context.getBean("sessionFactory");
        session = sessionFactory.openSession();
    }
    public static void main(String[] args){

    }
    public static boolean register(User user) {
        session.beginTransaction();
        User getUser = (User) session.get(User.class,user.getUser_id());
        if(getUser != null){
            return false;
        }
        session.save(user);
        session.getTransaction().commit();

        return true;
    }

    public static List<List<String>> TFIDF(String job_id){
        session.beginTransaction();
        Job job = (Job) session.get(Job.class,job_id);
        List<List<String>> keywords = new ArrayList<>();
        try {
            String description = job.getJob_description();
            MonkeyLearnClient client = new MonkeyLearnClient();
            keywords = client.extract(description);
        }catch(NullPointerException | IOException e){
            System.out.println(e.getMessage());
        }
        if (keywords.size() == 0){
            return keywords;
        }
        List<String> keyList = keywords.get(0);
        System.out.println(keyList.size());
        for(int i = 0; i < keyList.size(); i++){
            String keyword = keyList.get(i);
            //abbreviate key words
            for (String str : set){
                if (keyword.contains(str)){
                    keyList.set(i,str);
                    break;
                };
            }
        }
        for(String str : keywords.get(0)){
            System.out.println(str);
        }


        return keywords;
    }


    public static void setFavorite(String userid, String job_id){
        //saveJob(job);
        session.beginTransaction();
        History history = new History();
        history.setKey(new HistoryKey(userid, job_id));
        session.saveOrUpdate(history);
        session.getTransaction().commit();

        saveKeyword(TFIDF(job_id),job_id);
    }



    private static void saveKeyword(List<List<String>> keyList,String job_id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (String key : keyList.get(0)){
            KeyWordsKey keykey= new KeyWordsKey(job_id,key);
            Keywords getKeyword = (Keywords) session.get(Keywords.class,keykey);
            if(getKeyword == null){
                Keywords keyword = new Keywords();
                keyword.setKey(keykey);
                session.saveOrUpdate(keyword);
            }
        }
        session.getTransaction().commit();
        session.close();
        return;
    }



    public static void close(){
        if (session != null){
            session.close();
        }
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
