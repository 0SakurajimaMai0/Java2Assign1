
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieAnalyzer {

    private List<Movie> movies;

    public static class Movie{
        private String Series_Title;
        private int Released_Year;
        private String Certificate;
        private String Runtime;
        private String Genre;
        private float IMDB_Rating;
        private String Overview;
        private String Meta_score;
        private String Director;
        private String Star1;
        private String Star2;
        private String Star3;
        private String Star4;
        private int Noofvotes;
        private String Gross;

        public Movie(String Series_Title, int Released_Year, String Certificate, String Runtime, String Genre,
                     float IMDB_Rating, String Overview, String Meta_score,
                     String Director, String Star1, String Star2, String Star3, String Star4, int Noofvotes, String Gross){
            this.Series_Title=Series_Title;
            this.Released_Year=Released_Year;
            this.Certificate=Certificate;
            this.Runtime=Runtime;
            this.Genre=Genre;
            this.IMDB_Rating=IMDB_Rating;
            this.Overview=Overview;
            this.Meta_score=Meta_score;
            this.Director=Director;
            this.Star1=Star1;
            this.Star2=Star2;
            this.Star3=Star3;
            this.Star4=Star4;
            this.Noofvotes=Noofvotes;
            this.Gross=Gross;

        }

        public String getSeries_Title() {
            return Series_Title;
        }

        public int getReleased_Year() {
            return Released_Year;
        }

        public String getCertificate() {
            return Certificate;
        }

        public String getRuntime() {
            return Runtime;
        }

        public String getGenre() {
            return Genre;
        }

        public float getIMDB_Rating() {
            return IMDB_Rating;
        }

        public String getOverview() {
            return Overview;
        }

        public String getMeta_score() {
            return Meta_score;
        }

        public String getDirector() {
            return Director;
        }

        public String getStar1() {
            return Star1;
        }

        public String getStar2() {
            return Star2;
        }

        public String getStar3() {
            return Star3;
        }

        public String getStar4() {
            return Star4;
        }

        public int getNoofvotes() {
            return Noofvotes;
        }

        public String getGross() {
            return Gross;
        }
    }

    public MovieAnalyzer(String dataset_path)throws IOException {
        String temp = null;
        File file = new File(dataset_path);
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        ArrayList<String> readList = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(read);
        while ((temp = reader.readLine())!=null&&!"".equals((temp))){
            readList.add(temp);
        }
        read.close();
        this.movies=Files.lines(Paths.get(dataset_path))
                .skip(1)
                .map(l -> l.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"))
                .map(l -> Arrays.stream(l).collect(Collectors.toList()))
                .map(l -> l.stream().map(a->{if(a.contains("\"")){return a.substring(1,a.length()-1);}else {return a;}}).collect(Collectors.toList()))
                .peek(l -> l.add(""))
                .map(a -> new Movie(a.get(1), Integer.parseInt(a.get(2)), a.get(3), a.get(4), a.get(5), Float.parseFloat(a.get(6)), a.get(7), a.get(8), a.get(9), a.get(10), a.get(11), a.get(12), a.get(13), Integer.parseInt(a.get(14)), a.get(15)))
                .collect(Collectors.toList());


    }

    public Map<Integer, Integer> getMovieCountByYear(){
        Map<Integer, Integer> counter = new LinkedHashMap<>();
        movies.stream()
                .sorted((Comparator.comparingInt(Movie::getReleased_Year)).reversed())
                .forEach(a -> {if(counter.containsKey(a.getReleased_Year())){
            int y=counter.get(a.getReleased_Year())+1;
            counter.replace(a.getReleased_Year(),y);
        }else {
            counter.put(a.getReleased_Year(),1);
        }
        });
        return counter;
    }

    public Map<String, Integer> getMovieCountByGenre(){
        Map<String, Integer> counter = new HashMap<>();
        movies.forEach(a -> {
            List<String> list = new ArrayList<>(Arrays.asList(a.getGenre().split(",")));
            list=list.stream().map(b->b.replace(" ","")).collect(Collectors.toList());
            for(String s:list){
                if(counter.containsKey(s)){
                    int y = counter.get(s)+1;
                    counter.replace(s,y);
                }else {
                    counter.put(s,1);
                }
            }

        });

        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(counter.entrySet());
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue())*-1;
            }

        });

        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                if(o1.getValue().equals(o2.getValue())){
                    return o1.getKey().compareTo(o2.getKey());
                }
                else {
                    return 0;
                }
            }

        });

        Map<String,Integer> counter2 = new LinkedHashMap<>();
        for(Map.Entry<String,Integer> e :list){
            counter2.put(e.getKey(),e.getValue());
        }

        return counter2;
    }

    public Map<List<String>, Integer> getCoStarCount(){
        Map<List<String>, Integer> counter = new HashMap<>();
        List<List<String>> stars = new ArrayList<>();
        movies.forEach(a -> {
            List<String> l = new ArrayList<>();
            l.add(a.getStar1());
            l.add(a.getStar2());
            l.add(a.getStar3());
            l.add(a.getStar4());
            l.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            stars.add(l);
        });
        for(List<String> l : stars){
            List<String> coStar1 = new ArrayList<>();
            coStar1.add(l.get(0));
            coStar1.add(l.get(1));
            List<String> coStar2 = new ArrayList<>();
            coStar2.add(l.get(0));
            coStar2.add(l.get(2));
            List<String> coStar3 = new ArrayList<>();
            coStar3.add(l.get(0));
            coStar3.add(l.get(3));
            List<String> coStar4 = new ArrayList<>();
            coStar4.add(l.get(1));
            coStar4.add(l.get(2));
            List<String> coStar5 = new ArrayList<>();
            coStar5.add(l.get(1));
            coStar5.add(l.get(3));
            List<String> coStar6 = new ArrayList<>();
            coStar6.add(l.get(2));
            coStar6.add(l.get(3));
            if(counter.containsKey(coStar1)){
                int y = counter.get(coStar1)+1;
                counter.replace(coStar1,y);
            }else {
                counter.put(coStar1,1);
            }
            if(counter.containsKey(coStar2)){
                int y = counter.get(coStar2)+1;
                counter.replace(coStar2,y);
            }else {
                counter.put(coStar2,1);
            }
            if(counter.containsKey(coStar3)){
                int y = counter.get(coStar3)+1;
                counter.replace(coStar3,y);
            }else {
                counter.put(coStar3,1);
            }
            if(counter.containsKey(coStar4)){
                int y = counter.get(coStar4)+1;
                counter.replace(coStar4,y);
            }else {
                counter.put(coStar4,1);
            }
            if(counter.containsKey(coStar5)){
                int y = counter.get(coStar5)+1;
                counter.replace(coStar5,y);
            }else {
                counter.put(coStar5,1);
            }
            if(counter.containsKey(coStar6)){
                int y = counter.get(coStar6)+1;
                counter.replace(coStar6,y);
            }else {
                counter.put(coStar6,1);
            }
        }


        return counter;

    }

    public List<String> getTopMovies(int top_k, String by){
        List<List<String>> counter4 = new ArrayList<>();
        List<String> TopMovies= new ArrayList<>();
        if(by.length()==7){
            movies.forEach(a ->{
                List<String> b = new ArrayList<>();
                b.add(a.getSeries_Title());
                b.add(a.getRuntime());
                counter4.add(b);
            });
            counter4.sort(new Comparator<List<String>>() {
                @Override
                public int compare(List<String> o1, List<String> o2) {
                    return Integer.parseInt(o2.get(1).split(" ")[0])-Integer.parseInt(o1.get(1).split(" ")[0]);
                }
            });

            counter4.sort(new Comparator<List<String>>() {
                @Override
                public int compare(List<String> o1, List<String> o2) {
                    if(o1.get(1).equals(o2.get(1))){
                        return o1.get(0).compareTo(o2.get(0));
                    }else {
                        return 0;
                    }
                }
            });

        }
        if(by.length()==8){
            movies.forEach(a ->{
                List<String> b = new ArrayList<>();
                b.add(a.getSeries_Title());
                b.add(a.getOverview());
                counter4.add(b);
            });
            counter4.sort(new Comparator<List<String>>() {
                @Override
                public int compare(List<String> o1, List<String> o2) {
                    return o2.get(1).length()-o1.get(1).length();
                }
            });

            counter4.sort(new Comparator<List<String>>() {
                @Override
                public int compare(List<String> o1, List<String> o2) {
                    if(o1.get(1).length()==o2.get(1).length()){
                        return o1.get(0).compareTo(o2.get(0));
                    }else {
                        return 0;
                    }
                }
            });

        }

        for (int i=0;i<top_k;i++){
            TopMovies.add(counter4.get(i).get(0));
        }

        return TopMovies;
    }

    public List<String> getTopStars(int top_k, String by){
        List<String> TopStars = new ArrayList<>();
        if(by.length()==6){
            Map<String,List<Double>> stars = new HashMap<>();
            movies.forEach(a -> {
                List<Double> b = new ArrayList<>();
                if(!stars.containsKey(a.getStar1())){
                    b.add(1.0);
                    b.add((double)a.getIMDB_Rating());
                    stars.put(a.getStar1(),b);
                }else {
                    double b1=stars.get(a.getStar1()).get(0)+1.0;
                    double b2=stars.get(a.getStar1()).get(1)+a.getIMDB_Rating();
                    b.add(b1);
                    b.add(b2);
                    stars.replace(a.getStar1(),b);
                }
                List<Double> c = new ArrayList<>();
                if(!stars.containsKey(a.getStar2())){
                    c.add(1.0);
                    c.add((double)a.getIMDB_Rating());
                    stars.put(a.getStar2(),c);
                }else {
                    double c1=stars.get(a.getStar2()).get(0)+1.0;
                    double c2=stars.get(a.getStar2()).get(1)+a.getIMDB_Rating();
                    c.add(c1);
                    c.add(c2);
                    stars.replace(a.getStar2(),c);
                }
                List<Double> d = new ArrayList<>();
                if(!stars.containsKey(a.getStar3())){
                    d.add(1.0);
                    d.add((double)a.getIMDB_Rating());
                    stars.put(a.getStar3(),d);
                }else {
                    double d1=stars.get(a.getStar3()).get(0)+1.0;
                    double d2=stars.get(a.getStar3()).get(1)+a.getIMDB_Rating();
                    d.add(d1);
                    d.add(d2);
                    stars.replace(a.getStar3(),d);
                }
                List<Double> e = new ArrayList<>();
                if(!stars.containsKey(a.getStar4())){
                    e.add(1.0);
                    e.add((double)a.getIMDB_Rating());
                    stars.put(a.getStar4(),e);
                }else {
                    double e1=stars.get(a.getStar4()).get(0)+1.0;
                    double e2=stars.get(a.getStar4()).get(1)+a.getIMDB_Rating();
                    e.add(e1);
                    e.add(e2);
                    stars.replace(a.getStar4(),e);
                }
            });

            Map<String,Double> stars1 = new HashMap<>();
            for(Map.Entry<String,List<Double>> entry :stars.entrySet()){
                DecimalFormat decimalFormat=new DecimalFormat("#.0000");
                String X = decimalFormat.format(entry.getValue().get(1)/entry.getValue().get(0));
                double x =Double.parseDouble(X);
                stars1.put(entry.getKey(),x);
            }

            List<Map.Entry<String,Double>> list = new ArrayList<Map.Entry<String,Double>>(stars1.entrySet());
            list.sort(new Comparator<Map.Entry<String, Double>>() {

                public int compare(Map.Entry<String, Double> o1,
                                   Map.Entry<String, Double> o2) {
                    return o1.getValue().compareTo(o2.getValue())*-1;
                }

            });

            list.sort(new Comparator<Map.Entry<String, Double>>() {
                public int compare(Map.Entry<String, Double> o1,
                                   Map.Entry<String, Double> o2) {
                    if(o1.getValue().equals(o2.getValue())){
                        return o1.getKey().compareTo(o2.getKey());
                    }
                    else {
                        return 0;
                    }
                }

            });

            for(int i=0;i<top_k;i++){
                TopStars.add(list.get(i).getKey());
            }






        }
        if(by.length()==5){
            Map<String,List<Long>> stars = new HashMap<>();
            movies.stream().filter(a -> !a.getGross().equals("")).forEach(a -> {
                String s = "";
                String[] s1 = a.getGross().split(",");
                for (String value : s1) {
                    s = s + value;
                }
                long gross = Long.parseLong(s);
                List<Long> b = new ArrayList<>();
                if(!stars.containsKey(a.getStar1())){
                    b.add(1L);
                    b.add(gross);
                    stars.put(a.getStar1(),b);
                }else {
                    long b1=stars.get(a.getStar1()).get(0)+1;
                    long b2=stars.get(a.getStar1()).get(1)+gross;
                    b.add(b1);
                    b.add(b2);
                    stars.replace(a.getStar1(),b);
                }
                List<Long> c = new ArrayList<>();
                if(!stars.containsKey(a.getStar2())){
                    c.add(1L);
                    c.add(gross);
                    stars.put(a.getStar2(),c);
                }else {
                    long c1=stars.get(a.getStar2()).get(0)+1;
                    long c2=stars.get(a.getStar2()).get(1)+gross;
                    c.add(c1);
                    c.add(c2);
                    stars.replace(a.getStar2(),c);
                }
                List<Long> d = new ArrayList<>();
                if(!stars.containsKey(a.getStar3())){
                    d.add(1L);
                    d.add(gross);
                    stars.put(a.getStar3(),d);
                }else {
                    long d1=stars.get(a.getStar3()).get(0)+1;
                    long d2=stars.get(a.getStar3()).get(1)+gross;
                    d.add(d1);
                    d.add(d2);
                    stars.replace(a.getStar3(),d);
                }
                List<Long> e = new ArrayList<>();
                if(!stars.containsKey(a.getStar4())){
                    e.add(1L);
                    e.add(gross);
                    stars.put(a.getStar4(),e);
                }else {
                    long e1=stars.get(a.getStar4()).get(0)+1;
                    long e2=stars.get(a.getStar4()).get(1)+gross;
                    e.add(e1);
                    e.add(e2);
                    stars.replace(a.getStar4(),e);
                }
            });

            Map<String,Long> stars1 = new HashMap<>();
            for(Map.Entry<String,List<Long>> entry :stars.entrySet()){
                long x = entry.getValue().get(1)/entry.getValue().get(0);
                stars1.put(entry.getKey(),x);
            }

            List<Map.Entry<String,Long>> list = new ArrayList<Map.Entry<String,Long>>(stars1.entrySet());
            list.sort(new Comparator<Map.Entry<String, Long>>() {

                public int compare(Map.Entry<String, Long> o1,
                                   Map.Entry<String, Long> o2) {
                    return o1.getValue().compareTo(o2.getValue())*-1;
                }

            });

            list.sort(new Comparator<Map.Entry<String, Long>>() {
                public int compare(Map.Entry<String, Long> o1,
                                   Map.Entry<String, Long> o2) {
                    if(o1.getValue().equals(o2.getValue())){
                        return o1.getKey().compareTo(o2.getKey());
                    }
                    else {
                        return 0;
                    }
                }

            });

            for(int i=0;i<top_k;i++){
                TopStars.add(list.get(i).getKey());
            }



        }
        return TopStars;
    }

    public List<String> searchMovies(String genre, float min_rating, int max_runtime){
        List<String> counter6 = new ArrayList<>();
        movies.stream().filter(a -> a.getGenre().contains(genre)&&a.getIMDB_Rating()>=min_rating&&Integer.parseInt(a.getRuntime().split(" ")[0])<=max_runtime)
                .forEach(b -> counter6.add(b.getSeries_Title()));
        counter6.sort(Comparator.naturalOrder());
        return counter6;
    }





}