INSERT INTO forum_management_system.tags (name)
VALUES ('Sports Cars'),
       ('Classic Cars'),
       ('Electric Vehicles'),
       ('Car Maintenance'),
       ('Luxury Cars'),
       ('Motorcycles'),
       ('Car Modifications'),
       ('Tuning'),
       ('Formula 1'),
       ('Electric Car Industry');

-- Insert dummy data into the 'users' table (car enthusiasts and admins)
INSERT INTO users (first_name, last_name, email, username, password, is_admin, is_blocked, profile_photo, phone_number)
VALUES ('Georgi', 'Benchev', 'GeogriBenchev@gmail.com', 'georgi', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, NULL, '123-456-7890'),
       ('Ivan', 'Ivanov', 'Vankata@gmail.com', 'vankata', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, NULL, '098-765-4321'),
       ('Stoyanka', 'Karashtranova', 'tancheto@gmail.com', 'tancheto', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 1, 0, NULL, '555-555-5555'),
       ('StoyankaUser', 'Karashtranova', 'tanchetoUser@gmail.com', 'tancheto_user', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, NULL, NULL),
       ('GeorgiUser', 'Benchev', 'georgiUser@gmail.com', 'georgi_user', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 0, NULL, NULL),
       ('IvanUser', 'Ivanov', 'vankataUser@gmail.com', 'vankata_user', '$2a$10$ML33hI.7hTPKXMV1s35D/udMXQtjFpOdYIGfu/IQ4GqqITPCP088m', 0, 1, NULL, NULL);

-- Insert dummy data into the 'posts' table (car-related posts)
INSERT INTO posts (user_id, title, content)
VALUES
    (2, 'Top 10 Sports Cars of 2025',
     CONCAT('Sports cars are constantly evolving. Here are the top 10 sports cars of 2025 that every car enthusiast should check out. ',
            'In this post, we cover the most iconic sports cars for 2025 including the Porsche 911 Turbo, Ferrari 488 GTB, and Lamborghini Huracán.')),
    (2, 'Electric Cars vs Gasoline Cars: Which is Better?',
     CONCAT('Electric vehicles are becoming more popular, but how do they compare to traditional gasoline-powered cars? ',
            'We dive deep into the pros and cons of electric cars, covering aspects such as range, charging infrastructure, and environmental impact.')),
    (3, 'How to Maintain Your Classic Car',
     CONCAT('Classic car maintenance can be tricky, but with the right tools and knowledge, you can keep your vintage car in top condition. ',
            'In this article, we explore the best tips and techniques for keeping your classic car running smoothly and ensuring it holds its value.')),
    (3, 'Tuning Your Car: Everything You Need to Know',
     CONCAT('Tuning a car can make a huge difference in performance. Let’s break down the basics of car tuning and how it can improve your ride. ',
            'We’ll discuss everything from engine tuning to suspension modifications, helping you achieve the perfect balance of power and handling.')),
    (2, 'Formula 1: The Future of Racing',
     CONCAT('Formula 1 continues to innovate, with new technologies and rules making the sport more exciting than ever. What does the future hold for F1? ',
            'From hybrid engines to autonomous tech, we explore what the future of Formula 1 racing could look like and how it will change the sport.'));


-- Insert dummy data into the 'comments' table (comments on posts)
INSERT INTO comments (post_id, user_id, content)
VALUES (1, 3, 'I love the Porsche 911 Turbo! Definitely deserves the top spot on this list.'),
       (2, 4, 'Great comparison! I’m leaning towards electric cars for my next purchase.'),
       (3, 5, 'Classic car maintenance is a real challenge, but worth it. I’ve restored a few old Mustangs myself.'),
       (4, 1,
        'Tuning makes all the difference. I’ve done a few mods on my Subaru WRX and it’s a completely different beast now!'),
       (5, 2,
        'F1 is going to be even more exciting in the future. I can’t wait to see the technological advancements!');

-- Insert dummy data into the 'post_likes_dislikes' table (likes and dislikes for posts)
INSERT INTO post_likes_dislikes (post_id, user_id, is_like)
VALUES (1, 2, 1),
       (2, 1, 1),
       (3, 4, 1),
       (4, 3, 1),
       (5, 5, 1),
       (1, 4, 0),
       (2, 5, 0);

-- Insert dummy data into the 'post_tags' table (tagging posts with appropriate topics)
INSERT INTO post_tags (post_id, tag_id)
VALUES (1, 1), -- 'Sports Cars' tag on post 1
       (1, 6), -- 'Motorcycles' tag on post 1 (sports-related content)
       (2, 3), -- 'Electric Vehicles' tag on post 2
       (2, 4), -- 'Car Maintenance' tag on post 2
       (3, 2), -- 'Classic Cars' tag on post 3
       (3, 4), -- 'Car Maintenance' tag on post 3
       (4, 7), -- 'Car Modifications' tag on post 4
       (4, 8), -- 'Tuning' tag on post 4
       (5, 9); -- 'Formula 1' tag on post 5