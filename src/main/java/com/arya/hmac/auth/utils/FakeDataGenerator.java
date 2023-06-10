package com.arya.hmac.auth.utils;

import com.github.javafaker.Faker;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FakeDataGenerator {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static final LocalDate FIRST_DAY_2023 = LocalDate.of(2023, Month.JANUARY, 1);
    public static final Faker FAKER = new Faker(Locale.ENGLISH);

    private static List<String> ACTIONS = Arrays.asList("View", "Back", "Add-To-Cart", "Remove-From-Cart", "Purchase");

    private static List<String> PAYMENTS = Arrays.asList("Credit-Card", "Debit-Card", "UPI", "Net-Banking", "Cash-On-Delivery");

    private static List<String> USERS = Arrays.asList("Gregorio Hammes", "Jess Huel PhD", "Nancey Dietrich", "Michale Gislason", "Kevin Cummings", "Miss Rex Kessler",
            "Morgan Torphy", "Marybelle D'Amore", "Ellamae Bins", "Amiee Hudson", "Sandie Von", "Sandy Reichel", "Dale Hintz", "Trenton Runolfsdottir",
            "Alejandro Oberbrunner", "Quinn Thompson V", "Jazmin McGlynn DDS", "Parker Johnson", "Marcelo Stark", "Miss Leonila Ondricka", "Obdulia Goyette",
            "Kassandra Hand", "Kylee Kirlin", "Paul Sawayn", "Nathanial O'Kon", "Georgina Kassulke", "Myrl Lakin", "Ingrid Denesik", "Miss Jame Robel",
            "Micaela Greenholt", "Winston Abshire", "Kayce Considine", "Frederic Weissnat", "Lyla Stehr", "Hillary Dach DDS", "Dick Reichel I", "Portia Collier",
            "Gabriel Herzog", "Ollie Mraz", "Elicia Corkery", "Roni Kshlerin", "Debroah Kuvalis", "Keneth Wiza", "Sheila Flatley", "Clay Abshire", "Darin Bashirian",
            "Adah Murray", "Lamar Boyle", "Miss Sheba Romaguera", "Pamala Kuhic", "Michell Gutkowski", "Misty Wolf", "Leandro Thompson", "Pedro Halvorson",
            "Edmond Harris", "Vince Lemke", "Miss Santos Mraz", "Miss Randa McKenzie", "Donny Heidenreich", "Sammie Tillman", "Lucien Murray", "Mrs. Golda Veum",
            "Jerrell Gislason", "Flo Flatley Sr.", "Wilma Ritchie", "Hershel Sanford DVM", "Suzette Reilly", "Florence Trantow", "Miss Valarie Lebsack", "Dione Conn",
            "Trang Schroeder", "Miss Andera Dooley", "Dusty Goodwin Jr.", "Chere Goodwin", "Dannette Ratke", "Steven Hoeger", "Argelia Emmerich", "Easter Brekke IV",
            "Roosevelt Macejkovic", "Mrs. Nikki West", "Grant Spinka", "Fabian Kuvalis", "Lavelle Auer", "Theo Frami II", "Shakita Schumm", "Eugenio Windler",
            "Delia Wilkinson", "Rob Connelly V", "Normand Harvey PhD", "Zackary Skiles V", "Emilia Beatty V", "Kristofer Witting", "Jose Cruickshank",
            "Luigi Dietrich", "Mrs. Margery Nikolaus", "Garry Medhurst", "Alex Prosacco", "Willie Block", "James Jaskolski", "Theodore Jacobson",
            "Elfreda Conroy", "Annett Dicki", "Casey Murazik", "Miss Normand Price", "Gigi Johnston I", "Ahmed O'Connell", "Raeann Rosenbaum",
            "Bertram Runolfsson", "Tien Wilderman Jr.", "Tonette Weimann", "Jarod Dach", "Willy Reichel", "Heriberto Kuhlman", "Kristi Brakus", "Lenard Torphy IV",
            "Miss Mohammed Sauer", "Regan Dickinson", "Miss Benito Schmeler", "Kirk Fadel", "Cristobal Crooks", "Wynona Klocko", "Gerald Feest", "Logan Stracke",
            "Delaine Terry II", "Jasper Simonis", "Arline Flatley", "Shonna Hilpert", "Junior Brown", "Elwood Rosenbaum", "Mrs. Brenton Moore", "Delphia Hoeger Sr.",
            "Nan Blick III", "Kip Lynch", "Eddy Moore II", "Mel Schoen", "Mrs. Courtney Carter", "Daniel Schowalter", "Edison Emard", "Ewa Steuber", "Darryl Pacocha",
            "Adalberto Metz", "Archie Littel", "Faye Braun", "Coleman Watsica", "Andres Pfannerstill III", "Dorcas O'Connell", "Britt Hackett", "Ronnie Bartell", "Devin Dibbert",
            "Lala Sipes", "Myrtis Kuhic", "Kyong Sporer", "Jacinto Fisher", "Miss Robbyn Reichel", "Marion Yundt", "Nolan Wisozk", "Curt Hagenes", "Raymond Bradtke",
            "Marcelino Cummerata II", "Leslie Murphy", "Ilse Swaniawski MD", "Phuong Ullrich", "Clark Gusikowski DVM", "Alberta Dietrich", "Beatrice Wyman", "Ramon Lind",
            "Clifford Lockman", "Leroy Ebert", "Alexis Wisozk", "Lionel Rowe", "Val Hermiston", "Bobbie Keeling", "Anibal McDermott", "Preston Murphy", "Rashad Reilly",
            "Ruben Waelchi DVM", "Shanice Wintheiser Sr.", "Dorla Fahey", "Nellie Kozey", "Miss Saturnina Hahn", "Helena Hagenes", "Miss Marshall Berge", "Edgardo Kiehn",
            "Natacha Tromp", "Willis Sipes", "Genoveva Boyer I", "Merrill Leuschke", "Alysa Spencer Sr.", "Sharell Kovacek II", "Bernadine Schultz", "Miss Janessa Luettgen",
            "Kirby Rau", "Marybelle Walsh", "Kurt O'Connell", "Edra Romaguera", "Babette Hammes", "Randy Ziemann", "Shawnna Bechtelar", "Lorrine Stark DDS", "Blake Haag");

    private static List<String> ITEMS = Arrays.asList("chair", "Iphone 14", "Laptop", "shoes", "watches", "T-shirts", "Jeans", "Headphones", "Iphone14", "jackets", "mobile cover",
            "charger", "HomeTheatre", "Blanket", "NoteBook", "Bike Cover", "Garbage Bag", "Pen", "Ball Pen", "Gel Pen", "Ink Pen", "Casual Shoes",
            "Trousers", "Casual Shirts", "Ink", "Table", "Boat Headphones", "Belts", "Tiffin Box", "Hoodies", "Sweat Shirts", "Shorts",
            "Perfumes", "Body Spray", "Brush", "Soap", "Shampoo", "Screen Guard", "School Bags", "Pencil", "Air Conditioner", "Refrigerator",
            "Washing Machines", "Microwaves", "Dishwasher", "Chimneys", "Smart Watch", "Face Wash", "Hand Bags");


    private static List<String> CITIES = Arrays.asList("Renaefort", "Howellville", "Ricehaven", "Lake Tanya", "North Caridad", "Marshallshire",
            "South Val", "Lake Akiko", "New Robertland", "Babarachester", "North Damion", "Macejkovicmouth", "Port Lynnbury", "Yostborough",
            "North Fernande", "Lake Elisha", "West Melvinport", "Gorczanyton", "Port Morganmouth", "Runteland", "Lake Thaddeusmouth", "Juniorchester",
            "Lake Percyfurt", "Marvinport", "East Fosterchester", "Waelchiborough", "Fredrickaport", "Lake Sana", "Edmundofurt", "Littelhaven", "Lake Haydenport",
            "Parkerborough", "South Novellashire", "Port Danyelleland", "South Phuong", "Schimmelshire", "Ernsermouth", "West Alton", "Wadeside", "Alishire",
            "East Isaacmouth", "Carolynhaven", "West Karmen", "Schaeferchester", "Kunzeport", "Weissnatton", "East Wallyfurt", "Port Verenaview", "Lake Lidiahaven", "Tillmanside");


    public static List<UserData> getData(int noOfRecords) {
//        String fullName = FAKER.name().fullName();
//        String title = FAKER.name().title();
//        String suffix = FAKER.name().suffix();
//        String address = FAKER.address().streetAddress();
//        String city = FAKER.address().cityName();
//        String country = FAKER.address().country();

//        System.out.println(fullName);
//        System.out.println(lastName);
//        System.out.println(title);
//        System.out.println(suffix);
//        System.out.println(address);
//        System.out.println(city);
//        System.out.println(country);

        if (noOfRecords == 0) noOfRecords = 1000000;

        List<UserData> userData = new ArrayList<>();
        ;

        for (int i = 0; i < noOfRecords; i++) {

            String paymentMethod = ACTIONS.get(new Random().nextInt(ACTIONS.size()));
            Date date = FAKER.date().between(new Date(FIRST_DAY_2023.atStartOfDay(ZoneId.of("America/New_York")).toEpochSecond() * 1000), new Date());


            UserData user = UserData.builder()
                    .userId(USERS.get(new Random().nextInt(USERS.size())))
                    .location(CITIES.get(new Random().nextInt(CITIES.size())))
                    .location(CITIES.get(new Random().nextInt(CITIES.size())))
                    .sessionId("Session_clickShop" + FAKER.number().digits(10))
                    .url("www.shop.com/purchased/item?" + ITEMS.get(new Random().nextInt(ITEMS.size())))
                    .paymentMethod(paymentMethod.equals("Purchase") ? PAYMENTS.get(new Random().nextInt(PAYMENTS.size())) : "NA")
                    .logDate(SIMPLE_DATE_FORMAT.format(date).split(" ")[0])
                    .logTime(SIMPLE_DATE_FORMAT.format(date))
                    .build();

            userData.add(user);

        }
        return userData;
    }
}

@Data
@Builder
@ToString
class UserData {
    private String userId;
    //    private String username;
    private String location;
    private String sessionId;
    private String url;
    private String paymentMethod;
    private String logDate;
    private String logTime;

}


// {
//
//"userID":"Marvin Eriksen Sr.",
//
//"location":"Virginia",
//
//"sessionId":"Session_clickShop15970860614820",
//
//"url":"http://www.shop.com/purchased/item?Small Bronze Chair",
//
//"logTime":"2020-08-11 00:31:01.485",
//
//"payment_method":"Credit Card",
//
//"logDate":"2020-08-11"
//
//}