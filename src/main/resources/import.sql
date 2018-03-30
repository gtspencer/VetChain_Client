INSERT INTO user(user_id,full_name, user_name,password,email,phone,account_type,date_created) VALUES (1,"James Bond","admin","123","admin@gmail.com","800255600","admin","2018-1-2");
INSERT INTO user(user_id,full_name, user_name,password,email,phone,account_type,date_created) VALUES (2,"Morgan Freeman", "client","123","client@gmail.com","4045006019","client","2018-1-3");
INSERT INTO user(user_id,full_name, user_name,password,email,phone,account_type,date_created) VALUES (3,"Donald Trump", "doctor1","123","doctor1@gmail.com","6789331234","doctor","2018-1-4");
INSERT INTO user(user_id,full_name, user_name,password,email,phone,account_type,date_created) VALUES (4,"Bill Gates", "doctor2","123","doctor2@gmail.com","6789331235","doctor","2018-1-5");
INSERT INTO user(user_id,full_name, user_name,password,email,phone,account_type,date_created) VALUES (5,"Taylor Swift","doctor3","123","doctor3@gmail.com","6789331236","doctor","2018-1-6");
INSERT INTO user(user_id,full_name, user_name,password,email,phone,account_type,date_created) VALUES (6,"Keenu Reeves", "doctor4","123","doctor4@gmail.com","6789331237","doctor","2018-1-7");

INSERT INTO doctor(id,user_id) VALUES (1,3);
INSERT INTO doctor(id,user_id) VALUES (2,4);
INSERT INTO doctor(id,user_id) VALUES (3,5);
INSERT INTO doctor(id,user_id) VALUES (4,6);

INSERT INTO pet(pet_id, avatar_url, breed, dob, gender, insrance_carrier, insurance_num, license, microchip_num, pet_name, weight) VALUES (1,"/static/images/animal1.png","turtle","2017-02-04","Female","TurtleCare","T1234","TTL12345","CH12345","Sea Turtle","5 lbs");
INSERT INTO pet(pet_id, avatar_url, breed, dob, gender, insrance_carrier, insurance_num, license, microchip_num, pet_name, weight) VALUES (2,"/static/images/animal2.gif","squirrel","2016-01-20","Male","SquirrelCare","S5678","SQR4346","CH45423","Squirrel","1 lbs");
INSERT INTO pet(pet_id, avatar_url, breed, dob, gender, insrance_carrier, insurance_num, license, microchip_num, pet_name, weight) VALUES (3,"/static/images/animal3.png","chipmunk","2015-05-21","Female","ChipmunkCare","C561278","CHM434632","CH45422343","Chipmunk","1 lbs");

INSERT INTO user_pet(user_id,pet_id) VALUES (2,1);
INSERT INTO user_pet(user_id,pet_id) VALUES (2,2);
INSERT INTO user_pet(user_id,pet_id) VALUES (3,1);

INSERT INTO appointment(id, date, reason, time, doctor_id, patient) VALUES (1,"2018-3-30","Checkup1","10:30:00",1,1);
INSERT INTO appointment(id, date, reason, time, doctor_id, patient) VALUES (2,"2018-3-31","Test","11:30:00",1,2);
INSERT INTO appointment(id, date, reason, time, doctor_id, patient) VALUES (3,"2018-4-1","Checkup2","10:30:00",1,2);
INSERT INTO appointment(id, date, reason, time, doctor_id, patient) VALUES (4,"2018-4-2","Checkup3","13:30:00",1,1);