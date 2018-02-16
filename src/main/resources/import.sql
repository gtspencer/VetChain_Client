/*Populate all the tables in here*/

/*USER TABLE */
INSERT INTO user(user_id,user_name,password,email,phone,account_type,date_created) VALUES (1,"admin","123","admin@gmail.com","800255600","admin","2018-1-2");
INSERT INTO user(user_id,user_name,password,email,phone,account_type,date_created) VALUES (2,"client","123","client@gmail.com","4045006019","client","2018-1-3");
INSERT INTO user(user_id,user_name,password,email,phone,account_type,date_created) VALUES (3,"doctor1","123","doctor1@gmail.com","6789331234","doctor","2018-1-4");
INSERT INTO user(user_id,user_name,password,email,phone,account_type,date_created) VALUES (4,"doctor2","123","doctor2@gmail.com","6789331235","doctor","2018-1-5");
INSERT INTO user(user_id,user_name,password,email,phone,account_type,date_created) VALUES (5,"doctor3","123","doctor3@gmail.com","6789331236","doctor","2018-1-6");
INSERT INTO user(user_id,user_name,password,email,phone,account_type,date_created) VALUES (6,"doctor4","123","doctor4@gmail.com","6789331237","doctor","2018-1-7");

/*PET TABLE */

INSERT INTO pet(pet_id, avatar_url, breed, dob, gender, insrance_carrier, insurance_num, license, microchip_num, pet_name, weight,user_id) VALUES(1,"/static/images/avatar.png","Chihuahua","2017-3-3","Male","INSCARRIER","1234","1234","1234","Doggo","6 lbs",2);
INSERT INTO pet(pet_id, avatar_url, breed, dob, gender, insrance_carrier, insurance_num, license, microchip_num, pet_name, weight,user_id) VALUES(2,"/static/images/animal3.png","Squirrle","2016-4-3","Female","INSCARRIER2","1234","1234","1234","Nick","100 lbs",2);
INSERT INTO pet(pet_id, avatar_url, breed, dob, gender, insrance_carrier, insurance_num, license, microchip_num, pet_name, weight,user_id) VALUES(3,"/static/images/animal2.gif","Unknown","2015-2-5","Male","INSCARRIER3","1234","1234","1234","Jew","10 lbs",2);
